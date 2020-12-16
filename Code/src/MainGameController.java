import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import java.util.*;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.util.Duration;

/*
    MainGameController - Where all the GUI fun happens. Takes a reference to a GameLogic class to assist with the bookkeeping and rule-making, and then displays the results of
    the game's ongoing moves.

   -Darragh O'Halloran
 */
public class MainGameController {

    //We keep track of the canvases and gridPane node refs since they're made dynamically

    private GridPane[][] gridPaneNodes;         //Individual gridpanes within each of our mainGridPane nodes
    private Node[][] canvases;                  //Individual canvases inside of each gridpane node in mainGridPane
    private GameLogic gameLogic;                //Ref to GameLogic

    //References to our main window objects for easier coding/listeners
    @FXML
    private VBox rootPane;

    @FXML
    //The Borderpane which our main Gridpane window is contained within
    private BorderPane mainPane;

    @FXML
    private GridPane mainGridPane;

    @FXML
    private HBox buttonPane;

    @FXML
    private Label timerLabel;

    @FXML
    //The Left side of the BorderPane which contains the player health and timerLabel
    private VBox labelsPane;

    @FXML
    private Button upButton;

    @FXML
    private Button leftButton;

    @FXML
    private Button rightButton;

    @FXML
    private Button downButton;

    //Game Attributes

    //Colors for each player. (Prevents random colors being bad)
    private static final Color[] playerColors = {Color.VIOLET, Color.ORANGE, Color.BLUE, Color.SIENNA};

    //Padding between edges so you can see gridLines;
    private static final int PADDING = 2;

    //How long games run in seconds
    private static final int GAME_TIME = 150;

    //Array of booleans keeping track of who's alive among the players
    private boolean[] deathStatus;

    //Size is dynamic so if we want a good idea of how big each of our gridpanes is we keep it stored.
    private double individualGridPaneWidth;
    private double individualGridPaneHeight;

    //Placeholder now because it'll always == GAME_TIME, but separate field for easy inclusion of custom game-time
    private int gameTimer;

    //The container for the image of our key
    private ImageView keyImage;

    //Whether or not we've drawn the key already. Used to prevent errors
    private boolean keyDrawn;

    //Flag for whether or not player one should be excluded from randomly moving
    private boolean controlPlayerOne;

    //Really cool property I wish I was able to understand - in theory I bound this (the font of the labels) to the size of the label panel
    private ObjectProperty<Font> fontScaling;               //Spoiler: It didn't work much

    //We use an ArrayList of double properties which are bound to the player's health as a percentage remaining from their max
    private ArrayList<DoubleProperty> healthBarUpdaters;

    //Num of rows and columns to draw
    private int numColumns;
    private int numRows;

    //We use a timeline to control the movement of players and the countdown timer
    private Timeline timeline;



    /**
     * This runs first whenever application tester calls Loader.load() so it acts as the driver code for our JavaFX project
     *
     * Darragh O'Halloran
     */


    public void initialize() {

    }

    //Attempt at a restart method call alongside cleanUp() - Doesn't actually work all that well so it's disabled
    @FXML
    void init(ActionEvent e){
        cleanUp();
        startUp();
    }
    public void cleanUp(){
        labelsPane.getChildren().clear();
        labelsPane.getChildren().add(timerLabel);
    }


    /**
     * Starts the GUI building process, and initializes all of our data structures for use in the game
     *
     * Darragh O'Halloran
     */
    public void startUp(){
        numColumns = 9;
        numRows = 9;
        keyDrawn = false;
        keyImage = new ImageView();
        fontScaling = new SimpleObjectProperty<Font>(Font.getDefault());
        healthBarUpdaters = new ArrayList<>();
        gameLogic = new GameLogic(4);

        gridPaneNodes = new GridPane[numRows][numColumns];
        canvases = new Node[numRows][numColumns];
        setGridPaneUp();
        gameLogic.mapInitializing(-1,-1);
        deathStatus = new boolean[gameLogic.getMaxPlayers()];
        Arrays.fill(deathStatus, Boolean.FALSE);
        gameTimer = GAME_TIME;
        timeline = startTimer();
        setUpPlayerGraphics();

        //We bind a listener to the size of the window to allow things to resize smoothly. resizing calls resizeMap()
        mainGridPane.heightProperty().addListener(evt -> resizeMap());
        mainGridPane.widthProperty().addListener(evt -> resizeMap());



        //This adds the ability for the user to play with the arrow keys as well
        mainPane.setOnKeyPressed(evt ->{
            //System.out.println(evt.getCode());
            switch(evt.getCode()){
                case LEFT:
                    moveLeft(null);
                    break;
                case RIGHT:
                    moveRight(null);
                    break;
                case UP:
                    moveUp(null);
                    break;
                case DOWN:
                    moveDown(null);
                    break;

            }
        });
        //We attempt to bind the listener for font-resizing.
        labelsPane.widthProperty().addListener((observableValue, oldWidth, newWidth) -> fontScaling.set(Font.font(newWidth.doubleValue() / 4)));
    }


    /**
     * Creates the graphics renderings for each player as well as their label and healthbar to display in the game
     *
     * Darragh O'Halloran
     */
    private void setUpPlayerGraphics(){
        int[] xYOffsets = {0, 0};

        //We make the radius whichever is lower, half of the width of the window, or the height
        double radius = Math.min(individualGridPaneHeight/gameLogic.getMaxPlayers(), individualGridPaneWidth/gameLogic.getMaxPlayers())-(PADDING/2.0);

        //For all players in game add a circle
        for(int i = 0; i < gameLogic.getPlayerList().size();i++){
            Player player = gameLogic.getPlayerList().get(i+1);
            Circle c = new Circle(radius, playerColors[i]);

            //Bind the circle's radius to the mainGridPane's width/height changes.
            c.radiusProperty().bind(Bindings.min(mainGridPane.widthProperty(), mainGridPane.heightProperty()).divide(gameLogic.getGridRows()*gameLogic.getMaxPlayers()));

            //Store the ref in the player
            player.setPlayerRender(c);

            //Label + healthbar
            Label playerLabel = new Label("Player" + (i+1));
            ProgressBar playerHealthBar = new ProgressBar();
            healthBarUpdaters.add(new SimpleDoubleProperty((double)player.getHealthPool()/(double)Player.getDefaultHealth()));

            //We bind an updater to the player'shealth bar so that it updates with the player's health changes
            playerHealthBar.progressProperty().bind(healthBarUpdaters.get(healthBarUpdaters.size()-1));

            //Color the font to match the player's color
            playerLabel.setTextFill(playerColors[i]);

            //Attempt at font scaling
            playerLabel.fontProperty().bind(fontScaling);

            //Add labels + healthbar
            labelsPane.getChildren().add(playerLabel);
            labelsPane.getChildren().add(playerHealthBar);
        }

    }

    /**
     * Sets the initial gridPane game window up with the required number of rows/columns
     *
     * Darragh O'Halloran
     */
    private void setGridPaneUp(){
        for (int i = 0 ; i < gameLogic.getGridRows(); i++) {
            RowConstraints row = new RowConstraints();
            //row.setVgrow(Priority.ALWAYS);
            row.setValignment(VPos.CENTER);
            mainGridPane.getRowConstraints().add(row);
        }

        for (int j = 0 ; j < gameLogic.getGridColumns(); j++) {
            ColumnConstraints col = new ColumnConstraints();
            //col.setHgrow(Priority.ALWAYS);
            col.setHalignment(HPos.CENTER);
            mainGridPane.getColumnConstraints().add(col);
        }
    }



    /**
     * Draws everything
     *
     * Darragh O'Halloran
     */
    private void resizeMap() {

        double winHeight = mainGridPane.getHeight();
        double winWidth = mainGridPane.getWidth();
        //First loop through will sometimes return 0 as height/width before the scene is actually displayed - this prevents that
        if (winHeight == 0 || winWidth == 0) {
            winHeight = mainGridPane.getPrefHeight();
            winWidth = mainGridPane.getPrefWidth();
        }

        //We update the references for the size of an individual gridpane Node in mainGridPane
        individualGridPaneWidth = (winWidth / numColumns);
        individualGridPaneHeight = (winHeight / numRows);


        //Another hacky solution that I've found. getChildren().clear() removes the gridlines on our gridpane. However, this information is stored within the very first
        //child so we simply store that through the deletion, and fit it back in to regain our lines.
        Node node = mainGridPane.getChildren().get(0);
        mainGridPane.getChildren().clear();
        mainGridPane.getChildren().add(0, node);

        //gc to draw onto
        GraphicsContext gc;

        //Canvas associated with gc
        Canvas newMapImage;
        boolean finalRoom = false;
        //For each node within the gridpane draw a square representing a room.
        for (int i = 0; i < numRows; i++) {
            for (int k = 0; k < numColumns; k++) {
                //If we're at the far right bottom corner
                if(i == numRows-1 && k == numColumns-1){
                    finalRoom = true;
                }
                Room currRoom = gameLogic.getRoom(i, k);
                //Think there's a better way to do this, but default behavior each gridpane node gets a percent of the screen X based on number of children C (size = X/C)
                newMapImage = new Canvas(winWidth / numColumns, winHeight / numRows);
                canvases[i][k] = newMapImage;

                //Returns a graphics object of the canvas for drawing
                gc = newMapImage.getGraphicsContext2D();

                //We make a rectangle to act as the background and color it. The madness in the constructor is to make the passed X,Y act as the center, rather than top-left corner
                Rectangle newRect = new Rectangle
                        (newMapImage.getWidth() / 2.0, newMapImage.getHeight() / 2.0, newMapImage.getWidth() - PADDING, newMapImage.getHeight() - PADDING);

                //We add this reference to the room incase
                gameLogic.getRoomList().get(new Point2D(i, k)).setRoomRender(newRect);
                drawRectangle(gc, newRect, currRoom.isATrap(), finalRoom);

                //Then we add it to the pane as a background (since it's drawn first everything else will be above)
                Group newGroup = new Group();
                newGroup.getChildren().add(newMapImage);
                mainGridPane.add(newGroup, i, k);


                //xYOffsets[0] = x xYOffsets[1] = y
                int[] xYOffsets = {0, 0};
                //double radius = Math.min(individualGridPaneHeight / gameLogic.getMaxPlayers(), individualGridPaneWidth / gameLogic.getMaxPlayers()) - (PADDING / 2.0);
                GridPane playersInRoom = new GridPane();

                //Adds some space between each circle just for looks
                playersInRoom.setAlignment(Pos.CENTER);
                playersInRoom.setHgap(individualGridPaneHeight / gameLogic.getMaxPlayers());
                playersInRoom.setVgap(PADDING);


                //Now we redraw each room and its contents, so for each room we check the players inside
                for (int j = 0; j < currRoom.playersInside.size(); j++) {
                    Player curPlayer = currRoom.playersInside.get(j);
                    Circle playerCircle = curPlayer.getPlayerRender();


                    //If the player has died, and we haven't played his "death animation" (black coloring) we do so
                    if(curPlayer.getHealthPool() <= 0 && !deathStatus[curPlayer.getPlayerID()-1]) {
                        curPlayer.getPlayerRender().setFill(Color.BLACK);
                        //if(curPlayer.getKey() != null)
                        playersInRoom.add(playerCircle, xYOffsets[0], xYOffsets[1]);
                        deathStatus[curPlayer.getPlayerID()-1] = true;

                    }

                    //If the player is still alive, we add them to the room
                    else if(curPlayer.getHealthPool() > 0){
                        playersInRoom.add(playerCircle, xYOffsets[0], xYOffsets[1]);
                    }

                    //If we make it here without going into the if clause(s), the player's dead and we don't need them anymore, so nothing has to happen.
                    xYOffsets[0]++;
                    if (j == gameLogic.getMaxPlayers() / 2 - 1) {           //I figure that the width of these rects will always be longer than the height
                        xYOffsets[0] = 0;                                   //So we reset xYoffsets[0] and increment xYOffsets[1] at the halfway point of max players to divide them into
                        xYOffsets[1]++;                                     //rows to take advantage of the increased room
                    }
                }
                gridPaneNodes[i][k] = playersInRoom;
                mainGridPane.add(playersInRoom, i, k);
            }
        }
        placeKeyOnMap();
    }


    /**
     * Places the key on the map if needed ( during the first loop or if a player dies with the key)
     *
     * Darragh O'Halloran
     */
    private void placeKeyOnMap() {

        //We don't want to try and draw the key if a player is already carrying it
        if (!gameLogic.getKey().playerCarrying()) {
            Image keyPic = new Image("smallKey.png");
            keyDrawn = true;

            //Resizes the image as necessary to fit the window
            keyImage.fitWidthProperty().bind(mainGridPane.widthProperty().divide(numColumns * gameLogic.getMaxPlayers()));
            keyImage.fitHeightProperty().bind(mainGridPane.heightProperty().divide(numColumns * gameLogic.getMaxPlayers()));
            keyImage.setImage(keyPic);

            //Gridpanes are weird and ignore centerX and centerY of objects, so we translate the object ~ to the center based on size of window
            keyImage.translateXProperty().bind(mainGridPane.widthProperty().divide(numColumns * gameLogic.getMaxPlayers()));

            //This prevents duplication issues within fxml. We don't want the key in two places at once
            if (keyDrawn) {
                mainGridPane.getChildren().remove(keyImage);
                keyDrawn = false;
            }
            mainGridPane.add(keyImage, gameLogic.getKey().getX(), gameLogic.getKey().getY());
        }
    }
    /**
     * Creates a Timeline object and sets it up to countdown from gameTimer. Returns timeLine object for later accessing
     * @return timeline the created timeline object
     *
     * Darragh O'Halloran
     */
    private Timeline startTimer(){
        Timeline timeline = new Timeline();
        timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(1), event -> {

            //Set time left to new value
            timerLabel.setText("Time Left: " + Integer.toString(gameTimer));
            timerLabel.setTextFill(Color.DARKSLATEBLUE);

            //1 sec down
            gameTimer = gameTimer-1;
            if(gameTimer < 15){
                timerLabel.setTextFill(Color.RED);
            }
            //Call the move random() to allow nodes to move
            moveRandom();
        }));
        //How long to run for
        timeline.setCycleCount(GAME_TIME+1);
        timeline.play();
        return timeline;
    }

    /**
     * Stops the timeline object created in startTimer()
     *
     * Darragh O'Halloran
     */
    private void stopTimer(){
        timeline.stop();
    }
    //Darragh O'Halloran
    //Listener Wrapper, we don't care about the MouseEvent, but JavaFX requires it of its controller listener methods. Then we simply call doStuff();  -- Unused Right now
    public void gridClicked(MouseEvent mouseEvent) {
        resizeMap();
    }

    //Helper method to quickly set the fill and X/Y width/height fields for our rectangles, which we then draw
    //Darragh O'Halloran
    private void drawRectangle(GraphicsContext gc,Rectangle rect, boolean isTrap, boolean isFinal){
        gc.setFill(Color.DARKGREY);
        if(isTrap){
            gc.setFill(Color.RED);
        }
        if(isFinal){
            gc.setFill(Color.DARKGREEN);
        }
        gc.fillRect(rect.getX()-rect.getWidth()/2.0,
                rect.getY()-rect.getHeight()/2.0,
                rect.getWidth(),
                rect.getHeight());

    }

    /**
     * Randomly picks and moves a player in a random direction.
     *
     * Darragh O'Halloran
     */
    void moveRandom(){
        int minID = 1;
        //Prevents player one from being randomly selected
        if(controlPlayerOne){
            minID = 2;
        }

        //Random player
        int playerID = (int)(Math.random()*(gameLogic.getPlayerList().size()+1-minID)+minID);

           Player player = gameLogic.getPlayerList().get(playerID);

           int moveChoice = (int)(Math.random()*4);
           int flag =-1;
           int old = -1;

           switch(moveChoice){
               case 0:
                   old = player.moveUp();
                   flag = 1;
                   break;
               case 1:
                   old = player.moveDown();
                   flag = 1;
                   break;
               case 2:
                   old = player.moveLeft();
                   flag = 0;
                   break;
               case 3:
                   old = player.moveRight();
                   flag = 0;
                   break;
           }
            move(player, flag, old);
        //}
    }

    /**
     * Moves a given player if their movement is allowed, and performs the book-keeping required to move them from one room
     * to the next
     * @param player     the player who is moving
     * @param flag       the x/y plane it's moving on 0 = x 1 = y
     * @param oldPoint   the point which the player came from
     *
     * Darragh O'Halloran
     */
    void move(Player player, int flag, int oldPoint){
        boolean trapped;

        //We check if the move was legal before proceeding
        boolean wasAllowed = gameLogic.checkMove(player, flag, oldPoint);
        if (wasAllowed) {

            //If it was we remove the player from its current room
            player.getCurrentRoom().playerExiting(player);

            //Then allow the player to move into their desired room, returning if there was a trap
            trapped = gameLogic.playerMoves(player);

            //If the player walks into a trapped room they take a random amount of damage and we update the healthbar of that player
            if(trapped){
                System.out.println("Cell was trapped! Health left: " + player.getHealthPool());
                healthBarUpdaters.get(player.getPlayerID()-1).set(player.getHealthPool()/(double)Player.getDefaultHealth());
            }
            //Add the player's circle to the gridPane
            gridPaneNodes[player.getX()][player.getY()].add(player.getPlayerRender(), 0, 0);

            //Check if a key was round and reDraw the map
            keyFound();
            resizeMap();

            //If we're in the final room it's game over!!
            if(player.getCurrentRoom().getClass() == FinalRoom.class) {
                stopTimer();
                Alert alert = new Alert(Alert.AlertType.INFORMATION, player.toString() + " wins!", ButtonType.OK);
                alert.showAndWait();
                System.exit(0);
            }
        }
    }

    /**
     * Removing the key from the grid when a player picks it up
     *
     *
     * Darragh O'Halloran
     */
    private void keyFound(){
        if(gameLogic.getKey().playerCarrying() && keyDrawn){
            mainGridPane.getChildren().remove(keyImage);
            keyDrawn = false;
        }
    }
    /*
            Move listener methods. They all pass into move() but since we have 4 buttons we need four smaller listener methods at least
     */
    @FXML
    void moveDown(ActionEvent event) {
        if(controlPlayerOne) {
            Player player = gameLogic.getPlayer(1);
            int old = player.moveDown();
            move(player, 1, old);
        }
    }
    @FXML
    void moveLeft(ActionEvent event) {
        if(controlPlayerOne) {
            Player one = gameLogic.getPlayer(1);
            int old = one.moveLeft();
            move(one, 0, old);
        }
    }

    @FXML
    void moveRight(ActionEvent event) {
        if(controlPlayerOne) {
            Player one = gameLogic.getPlayer(1);
            int old = one.moveRight();
            move(one,0,old);
        }
    }

    @FXML
    void moveUp(ActionEvent event) {
        if(controlPlayerOne) {
            Player one = gameLogic.getPlayer(1);
            int old = one.moveUp();
            move(one, 1, old);
        }
    }
    @FXML
    /**
     * Toggle player control listener
     */
    void togglePlayerControl(ActionEvent event) {
        controlPlayerOne = !controlPlayerOne;
        //System.out.println(controlPlayerOne);
    }

    /**
     * Quit button listener
     * @param actionEvent
     */
    public void quit(ActionEvent actionEvent) {
        System.exit(0);
    }





    /*

                    Graveyard below
     */
    private void populateArray(){
        for (Node child : mainGridPane.getChildren()) {
            Integer column = GridPane.getColumnIndex(child);
            Integer row = GridPane.getRowIndex(child);
            if (column != null && row != null) {
                //gridPaneNodes[column][row] = child;
            }
        }
        int numColumns = mainGridPane.getColumnConstraints().size();
        int numRows  = mainGridPane.getRowConstraints().size();
        double winHeight = mainGridPane.getHeight();
        double winWidth = mainGridPane.getWidth();
        if(winHeight == 0 || winWidth == 0){
            winHeight = mainGridPane.getPrefHeight();
            winWidth = mainGridPane.getPrefWidth();
        }
        for(int i = 0; i < canvases.length; i++){
            for(int k = 0; k < canvases[0].length; k++){
                canvases[i][k] = new Canvas(winWidth/numRows, winHeight/numColumns);
            }
        }
    }
    /*
     * method to be called in order to draw the players
     * elsewhere in the map
     */
    private void erasePlayer(Circle player){
        mainGridPane.getChildren().remove(player);
    }


    //Does nothing right now, was figuring out the syntax for removing within a group env.
    private void movePlayer(Player player){
        if(mainGridPane.getChildren().get(mainGridPane.getChildren().size()-1).getClass() == GridPane.class)
            ((GridPane)(mainGridPane.getChildren().get(mainGridPane.getChildren().size()-1))).getChildren().remove(3);
    }


    //Plan to move this method entirely out of doStuff, but I didn't want to break anything working right now
    private void updateRoom(Room room){
        double winHeight = mainGridPane.getHeight();
        double winWidth = mainGridPane.getWidth();
        //To keep track of when to jump down on the y offset.
        int[] xYOffsets = {0, 0};

        //Approximate width/height of each individual rectangle
        double individualGridPaneWidth = (winWidth/gameLogic.getGridColumns());
        double individualGridPaneHeight = (winHeight/gameLogic.getGridRows());

        //Take the lesser of the two values ( should usually be the height, but..) and sets the radius to half that value (accounting for padding)
        double radius = Math.min(individualGridPaneHeight/gameLogic.getMaxPlayers(), individualGridPaneWidth/gameLogic.getMaxPlayers())-(PADDING/2.0);

        //TODO: Check if FlowPane offers an easier implementation.
        GridPane startingPlayers = new GridPane();

        startingPlayers.setAlignment(Pos.CENTER);
        startingPlayers.setHgap(individualGridPaneHeight/gameLogic.getMaxPlayers());
        startingPlayers.setVgap(PADDING);
        for(int i = 0; i < gameLogic.getMaxPlayers(); i++) {
            startingPlayers.add(new Circle(radius, playerColors[i%4]), xYOffsets[0], xYOffsets[1]);
            xYOffsets[0]++;
            if( i == gameLogic.getMaxPlayers()/2-1){
                xYOffsets[0] = 0;
                xYOffsets[1]++;
            }
        }
        //Pretty sure this will always be the last child, but just in case I'm full of crap, we'll check
        if(mainGridPane.getChildren().get(mainGridPane.getChildren().size()-1).getClass() == GridPane.class){
            mainGridPane.getChildren().remove(mainGridPane.getChildren().size()-1);
        }
        mainGridPane.add(startingPlayers, room.getY(),room.getX());
    }
}



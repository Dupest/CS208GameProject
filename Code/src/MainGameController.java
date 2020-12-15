import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectExpression;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.awt.*;
import java.sql.Time;
import java.util.*;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.util.Duration;

//Edited By Svetozar Draganitchki
public class MainGameController<Vbox> {

    //We keep track of the canvases and gridPane node refs since they're made dynamically
    private GridPane[][] gridPaneNodes;
    private Node[][] canvases;
    private GameLogic gameLogic;

    //References to our main window objects for easier coding/listeners yada-yada
    @FXML
    private VBox rootPane;

    @FXML
    private BorderPane mainPane;

    @FXML
    private GridPane mainGridPane;

    @FXML
    private HBox buttonPane;

    @FXML
    private Label timerLabel;

    @FXML
    private VBox labelsPane;

    @FXML
    private Button upButton;

    @FXML
    private Button leftButton;

    @FXML
    private Button rightButton;

    @FXML
    private Button downButton;


    private double individualGridPaneWidth;
    private double individualGridPaneHeight;
    private static final Color[] playerColors = {Color.VIOLET, Color.ORANGE, Color.BLUE, Color.SIENNA};
    private static final int NUM_COLORS = playerColors.length;

    private static final int PADDING = 2;
    private static final int GAME_TIME = 60;
    private int gameTimer;
    private ImageView keyImage;
    private boolean keyDrawn;

    private ObjectProperty<Font> fontScaling;

    /**
     * This runs first whenever application tester calls Loader.load() so it acts as the driver code for our JavaFX project
     */
    public void initialize() {
        keyImage = new ImageView();
        fontScaling = new SimpleObjectProperty<Font>(Font.getDefault());
        //We can get them number of columns/rows by checking to see how many constraints there are. There will be a specific constraint object for each row/column
        gameLogic = new GameLogic(4);
        setGridPaneUp();
        int numColumns = mainGridPane.getColumnConstraints().size();
        int numRows  = mainGridPane.getRowConstraints().size();
        //setGridPaneUp();
        //Init arrays
        gridPaneNodes = new GridPane[numRows][numColumns];
        canvases = new Node[numRows][numColumns];
        //mapLayout = new Room[numRows][numColumns];
        //populateArray();


        gameLogic.mapInitializing(-1,-1);
        gameTimer = GAME_TIME;
        startTimer();
        setUpPlayerGraphics();
        //We bind a listener to the size of the window to allow things to resize smoothly. resizing calls doStuff()
        mainGridPane.heightProperty().addListener(evt -> doStuff());
        mainGridPane.widthProperty().addListener(evt -> doStuff());
        labelsPane.widthProperty().addListener((observableValue, oldWidth, newWidth) -> fontScaling.set(Font.font(newWidth.doubleValue() / 4)));
    }

    private void setUpPlayerGraphics(){
        int[] xYOffsets = {0, 0};
        double radius = Math.min(individualGridPaneHeight/gameLogic.getMaxPlayers(), individualGridPaneWidth/gameLogic.getMaxPlayers())-(PADDING/2.0);
//        if(radius < 5){
//            radius = 15;
//        }
        for(int i = 0; i < gameLogic.getPlayerList().size();i++){
            Player player = gameLogic.getPlayerList().get(i+1);
            Circle c = new Circle(radius, playerColors[i]);
            c.radiusProperty().bind(Bindings.min(mainGridPane.widthProperty(), mainGridPane.heightProperty()).divide(gameLogic.getGridRows()*gameLogic.getMaxPlayers()));
            c.radiusProperty().addListener((obs, old , nw)-> {
                System.out.println("old = " + old + " new: " + nw);
            });
            player.setPlayerRender(c);
            Label playerLabel = new Label("Player" + (i+1));
            playerLabel.setTextFill(playerColors[i]);
            playerLabel.fontProperty().bind(fontScaling);
            labelsPane.getChildren().add(playerLabel);


        }

    }
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
     * Draw things
     */
    private void doStuff(){
        int numColumns = gameLogic.getGridColumns();
        int numRows  = gameLogic.getGridRows();
        //First call to doStuff() will be in the initialize() method and for do to order of the loader's ops, getHeight() and getWidth() will return 0 at this point.
        // So we call the prefHeight/Width in that case.
        double winHeight = mainGridPane.getHeight();
        double winWidth = mainGridPane.getWidth();
        //System.out.println(winWidth/numRows + " " + mainGridPane.getColumnConstraints().get(1).toString());
        if(winHeight == 0 || winWidth == 0){
            winHeight = mainGridPane.getPrefHeight();
            winWidth = mainGridPane.getPrefWidth();
        }
        individualGridPaneWidth = (winWidth/numColumns);
        individualGridPaneHeight = (winHeight/numRows);
        //Another hacky solution that I've found. getChildren().clear() removes the gridlines on our gridpane. However, this information is stored within the very first
        //child so we simply store that through the deletion, and fit it back in to regain our lines.
        Node node = mainGridPane.getChildren().get(0);
        mainGridPane.getChildren().clear();
        mainGridPane.getChildren().add(0, node);
        GraphicsContext gc;
        Canvas newMapImage = null;
        //For each node within the gridpane draw a square representing a room.
        for(int i = 0; i < numRows; i++){
            for(int k = 0; k < numColumns; k++){
                Room currRoom = gameLogic.getRoom(i, k);
                //Think there's a better way to do this, but default behavior each gridpane node gets a percent of the screen X based on number of children C (size = X/C)
                newMapImage = new Canvas(winWidth/numColumns, winHeight/numRows);
                canvases[i][k] = newMapImage;

                //Returns a graphics object of the canvas for drawing
                gc = newMapImage.getGraphicsContext2D();
                Rectangle newRect = new Rectangle
                        (newMapImage.getWidth()/2.0, newMapImage.getHeight()/2.0, newMapImage.getWidth()-PADDING, newMapImage.getHeight()-PADDING);
                gameLogic.getRoomList().get(new Point2D(i, k)).setRoomRender(newRect);
                drawRectangle(gc, newRect, currRoom.isATrap());
                Group newGroup = new Group();
                newGroup.getChildren().add(newMapImage);
                mainGridPane.add(newGroup, i, k);

                int[] xYOffsets = {0, 0};
                double radius = Math.min(individualGridPaneHeight/gameLogic.getMaxPlayers(), individualGridPaneWidth/gameLogic.getMaxPlayers())-(PADDING/2.0);
                GridPane playersInRoom = new GridPane();
                playersInRoom.setAlignment(Pos.CENTER);
                playersInRoom.setHgap(individualGridPaneHeight/gameLogic.getMaxPlayers());
                playersInRoom.setVgap(PADDING);
                for(int j = 0; j < currRoom.playersInside.size(); j++) {
                    Circle playerCircle = currRoom.playersInside.get(j).getPlayerRender();
                    playersInRoom.add(playerCircle,xYOffsets[0],xYOffsets[1]);
                    xYOffsets[0]++;
                    if( j == gameLogic.getMaxPlayers()/2-1){
                        xYOffsets[0] = 0;
                        xYOffsets[1]++;
                    }
                }
                gridPaneNodes[i][k] = playersInRoom;
                mainGridPane.add(playersInRoom, i, k);
                
            }
        }
        if(!gameLogic.getKey().playerCarrying()) {
            Image keyPic = new Image("smallKey.png");
            keyDrawn = true;
            keyImage.fitWidthProperty().bind(mainGridPane.widthProperty().divide(numColumns * gameLogic.getMaxPlayers()));
            keyImage.fitHeightProperty().bind(mainGridPane.heightProperty().divide(numColumns * gameLogic.getMaxPlayers()));
            keyImage.setImage(keyPic);
            keyImage.translateXProperty().bind(mainGridPane.widthProperty().divide(numColumns * gameLogic.getMaxPlayers()));
            System.out.println(gameLogic.getKey().getX() + " " + gameLogic.getKey().getY());
            mainGridPane.add(keyImage, gameLogic.getKey().getX(), gameLogic.getKey().getY());
        }
        //mainGridPane.add(drawKey(new Rectangle()), gameLogic.getKey().getX(),  gameLogic.getKey().getY());
        //movePlayer(null);
        
        //Groups just add an extra layer of organization. In this case not necessary, but trying to show of some of the syntax too

    }

    private void startTimer(){
        Timeline timeline = new Timeline();
        timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(1), event -> {
            timerLabel.setText("Time Left: " + Integer.toString(gameTimer));
            timerLabel.setTextFill(Color.DARKSLATEBLUE);
            gameTimer = gameTimer-1;
            if(gameTimer < 15){
                timerLabel.setTextFill(Color.RED);
            }
        }));
        //timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(1)));
        timeline.setCycleCount(GAME_TIME+1);
        timeline.play();
//        long endTime = 60;
//        DateFormat timeFormat = new SimpleDateFormat( "HH:mm:ss" );
//        final Timeline timeline = new Timeline(
//                new KeyFrame(
//                        Duration.millis( 500 ),
//                        event -> {
//                            long diff = endTime - System.currentTimeMillis();
//                            if ( diff < 0 ) {
//                                //  timeLabel.setText( "00:00:00" );
//                                timerLabel.setText( timeFormat.format( 0 ) );
//                            } else {
//                                timerLabel.setText( timeFormat.format( diff ) );
//                            }
//                        }
//                )
//        );
//        timeline.setCycleCount( Animation.INDEFINITE );
//        timeline.play();
    }
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
    //Listener Wrapper, we don't care about the MouseEvent, but JavaFX requires it of its controller listener methods. Then we simply call doStuff();  -- Unused Right now
    public void gridClicked(MouseEvent mouseEvent) {
        doStuff();
    }

    private void drawRectangle(GraphicsContext gc,Rectangle rect, boolean isTrap){
        gc.setFill(Color.DARKGREY);
        if(isTrap){
            gc.setFill(Color.RED);
        }
        gc.fillRect(rect.getX()-rect.getWidth()/2.0,
                rect.getY()-rect.getHeight()/2.0,
                rect.getWidth(),
                rect.getHeight());

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
    @FXML
    void moveDown(ActionEvent event) {
        Player one = gameLogic.getPlayer(1);
        one.moveForward();
        boolean wasAllowed = gameLogic.checkMove(one);
        System.out.println(one.getX() + " " +one.getY());
        if(wasAllowed) {
            one.getCurrentRoom().playerExiting(one);
            gameLogic.playerMoves(one, 1, one.getY());
            gridPaneNodes[one.getX()][one.getY()].add(one.getPlayerRender(), 0, 0);
            keyFound();
            doStuff();
        }
    }
    void keyFound(){
        if(gameLogic.getKey().playerCarrying() && keyDrawn){
            mainGridPane.getChildren().remove(mainGridPane.getChildren().indexOf(keyImage));
            keyDrawn = false;
        }
    }
    @FXML
    void moveLeft(ActionEvent event) {
        Player one = gameLogic.getPlayer(1);
        one.moveLeft();
        boolean wasAllowed = gameLogic.checkMove(one);
        System.out.println(one.getX() + " " +one.getY());
        if (wasAllowed) {
            one.getCurrentRoom().playerExiting(one);
            gameLogic.playerMoves(one, 0, one.getX());
            gridPaneNodes[one.getX()][one.getY()].add(one.getPlayerRender(), 0, 0);
            keyFound();
            doStuff();
        }
    }

    @FXML
    void moveRight(ActionEvent event) {

        Player one = gameLogic.getPlayer(1);
        one.moveRight();
        boolean wasAllowed = gameLogic.checkMove(one);
        System.out.println(one.getX() + " " +one.getY());
        if (wasAllowed) {
            one.getCurrentRoom().playerExiting(one);
            gameLogic.playerMoves(one, 0, one.getX());
            gridPaneNodes[one.getX()][one.getY()].add(one.getPlayerRender(), 0, 0);
            keyFound();
            doStuff();
        }
    }

    @FXML
    void moveUp(ActionEvent event) {
        Player one = gameLogic.getPlayer(1);
        one.moveBackward();
        boolean wasAllowed = gameLogic.checkMove(one);
        System.out.println(one.getX() + " " +one.getY());
        if (wasAllowed) {
            one.getCurrentRoom().playerExiting(one);
            gameLogic.playerMoves(one, 1, one.getY());
            gridPaneNodes[one.getX()][one.getY()].add(one.getPlayerRender(), 0, 0);
            keyFound();
            doStuff();
        }
    }


}

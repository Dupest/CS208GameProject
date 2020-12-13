import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.*;
import javafx.scene.shape.Circle;

//Edited By Svetozar Draganitchki
public class MainGameController {

    //We keep track of the canvases and gridPane node refs since they're made dynamically
    private Node[][] gridPaneNodes;
    private Node[][] canvases;
    private GameLogic gameLogic;

    //References to our main window objects for easier coding/listeners yada-yada
    @FXML
    private VBox rootPane;

    @FXML
    private GridPane mainGridPane;

    private Room[][]  mapLayout;
    private HashMap<Integer, Key> keyMap;
    private HashMap<Integer, Player> playerMap;

    private final Color[] playerColors = {Color.VIOLET, Color.ORANGE, Color.RED, Color.BLACK};

    private static final int PADDING = 2;

    /**
     * This runs first whenever application tester calls Loader.load() so it acts as the driver code for our JavaFX project
     */
    public void initialize() {

        //We can get them number of columns/rows by checking to see how many constraints there are. There will be a specific constraint object for each row/column
        gameLogic = new GameLogic(4);
        int numColumns = mainGridPane.getColumnConstraints().size();
        int numRows  = mainGridPane.getRowConstraints().size();
        //setGridPaneUp();
        //Init arrays
        gridPaneNodes = new Node[numRows][numColumns];
        canvases = new Node[numRows][numColumns];
        mapLayout = new Room[numRows][numColumns];
        populateArray();

        //We bind a listener to the size of the window to allow things to resize smoothly. resizing calls doStuff()
        mainGridPane.heightProperty().addListener(evt -> doStuff());
        mainGridPane.widthProperty().addListener(evt -> doStuff());

        //Creates the "map" of rooms, players, and Keys
        keyMap = new HashMap<>();
        playerMap = new HashMap<>();

        mapInitializing();
    }
    /*
    initializes the map depending on the size of the map and
    number of players. By default, it generates a 9 x 9 map
    and 2 players (Justin Lamberson)
     */
    public void mapInitializing(){
        int numColumns = gameLogic.getGridColumns();
        int numRows  = gameLogic.getGridRows();
        System.out.println(numColumns + " " + numRows);
        //assumes that there are 2 players and a 9 x 9 map
        Random rand = new Random();

        //traps is max number of traps
        int traps = 10;
        int roomNumber = 1;

        //loop initializes all rooms
        for(int i = 0; i < numRows; i++){
            for(int k = 0; k < numColumns; k++){
                if(rand.nextInt(101) < 50 || traps >= 0){
                    traps--;
                    mapLayout[i][k] = new Room(false, roomNumber, true);

                //If last room ie (8,8)
                }else if(i == numRows-1 && k == numColumns-1){

                    //Make special constructor for the final room so this is different from else loop
                    mapLayout[i][k] = new Room(true, roomNumber, false);
                } else {
                    mapLayout[i][k] = new Room(false, roomNumber, false);
                }
                roomNumber++;
            }
        }
        System.out.println(roomNumber);
        //generates final key =
        //keyList.put(81, new Key(mapLayout[8][8] ,1));

        //generates the two players in the top 2 rooms
        playerMap.put(1, new Player(mapLayout[0][0]));
        playerMap.put(2, new Player(mapLayout[0][1]));

    }

    private void setGridPaneUp(){
        for (int i = 0 ; i < gameLogic.getGridRows(); i++) {
            RowConstraints row = new RowConstraints();
            row.setVgrow(Priority.ALWAYS);
            mainGridPane.getRowConstraints().add(row);
        }

        for (int j = 0 ; j < gameLogic.getGridColumns(); j++) {
            ColumnConstraints col = new ColumnConstraints();
            col.setHgrow(Priority.ALWAYS);
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
        System.out.println(winWidth/numRows + " " + mainGridPane.getColumnConstraints().get(1).toString());
        if(winHeight == 0 || winWidth == 0){
            winHeight = mainGridPane.getPrefHeight();
            winWidth = mainGridPane.getPrefWidth();
        }

        //Another hacky solution that I've found. getChildren().clear() removes the gridlines on our gridpane. However, this information is stored within the very first
        //child so we simply store that through the deletion, and fit it back in to regain our lines.
        Node node = mainGridPane.getChildren().get(0);
        mainGridPane.getChildren().clear();
        mainGridPane.getChildren().add(0, node);
        GraphicsContext gc;
        Canvas newMapImage = null;
        //For each node within the gridpane draw a circle representing a room.
        for(int i = 0; i < numRows; i++){
            for(int k = 0; k < numColumns; k++){

                //Think there's a better way to do this, but default behavior each gridpane node gets a percent of the screen X based on number of children C (size = X/C)
                newMapImage = new Canvas(winWidth/numColumns, winHeight/numRows);
                canvases[i][k] = newMapImage;

                //Returns a graphics object of the canvas for drawing
                gc = newMapImage.getGraphicsContext2D();
                Rectangle newRect = new Rectangle
                        (newMapImage.getWidth()/2.0, newMapImage.getHeight()/2.0, newMapImage.getWidth()-PADDING, newMapImage.getHeight()-PADDING);
                
                System.out.println("W:" + newMapImage.getWidth());
                System.out.println("H:" + newMapImage.getHeight());

                mapLayout[i][k].setRoomRender(newRect);
                drawRectangle(gc, newRect);
                
                //My custom circle class from the last project we did because I wanted a quick lazy drawing to show this off.
                //CircleWithText mapCircle = new CircleWithText("Map (" + i + ", " + k + ")", new Point2D(newMapImage.getWidth()/2.0,newMapImage.getHeight()/2.0));

//                //Default radius on the circles is 40, but if we start to get smaller screen sizes to where each node only has 80 pixels of room in any direction, we adjust the radius.
//                if(newMapImage.getHeight() < mapCircle.getDefRadius()*2 || newMapImage.getWidth() < mapCircle.getDefRadius()*2){
//
//                    //We take the smaller of the two values
//                    double smallerBound = Math.min(newMapImage.getHeight(), newMapImage.getWidth());
//                    //double smallerBound =(newMapImage.getHeight() > newMapImage.getWidth()) ? newMapImage.getWidth(): newMapImage.getHeight();
//
//                    //Radius becomes half of the smallest distance to an edge.
//                    mapCircle.setRadius((int)(smallerBound/2.0));
//                    mapCircle.drawCusRadius(gc);
//                }
//
//                //Otherwise we draw it normally
//                else
//                    mapCircle.draw(gc);
                //System.out.println("Circle coords: " + mapCircle.getPoint());
                Group newGroup = new Group();
                newGroup.getChildren().add(newMapImage);
                mainGridPane.add(newGroup, i, k);
                
            }
        }
        //Way to draw traps
        for(int i = 1; i < 81; i++){
            if(gameLogic.getRoom(i).isATrap()) {
                Room currRoom = gameLogic.getRoom(i);
                mainGridPane.add(drawTrap(new Rectangle()), currRoom.getX(), currRoom.getY());
            }
        }

        //To keep track of when to jump down on the y offset.
        int[] xYOffsets = {0, 0};

        //Approximate width/height of each individual rectangle
        double individualGridPaneWidth = (winWidth/numColumns);
        double individualGridPaneHeight = (winHeight/numRows);

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
//        //Generates 4 Players.
//        Circle player1Graphic = new Circle
//                        (newMapImage.getWidth()/2.0, newMapImage.getHeight(),15);
//        Circle player2Graphic = new Circle
//                        (newMapImage.getWidth()/2.0, newMapImage.getHeight(),15);
//        Circle player3Graphic = new Circle
//                        (newMapImage.getWidth()/2.0, newMapImage.getHeight(),15);
//        Circle player4Graphic = new Circle
//                        (newMapImage.getWidth()/2.0, newMapImage.getHeight(),15);
//        //sets colors of 4 players
//        drawPlayer1(player1Graphic);
//        drawPlayer2(player2Graphic);
//        drawPlayer3(player3Graphic);
//        drawPlayer4(player4Graphic);
//        //adds 4 players to nodes the commented out code is how to add the players when we have them working.
//        mainGridPane.add(player1Graphic, 0, 0); //mainGridPane.add(player1Graphic, GL.getPlayer(2).getX(), GL.getPlayer(1).getY());
//        mainGridPane.add(player2Graphic, 0, 0); //mainGridPane.add(player2Graphic, GL.getPlayer(2).getX(), GL.getPlayer(1).getY());
//        mainGridPane.add(player3Graphic, 0, 0); //mainGridPane.add(player3Graphic, GL.getPlayer(2).getX(), GL.getPlayer(1).getY());
//        mainGridPane.add(player4Graphic, 0, 0); //mainGridPane.add(player4Graphic, GL.getPlayer(2).getX(), GL.getPlayer(1).getY());
        mainGridPane.add(startingPlayers, 0,0);
        movePlayer(null);
        mainGridPane.add(drawKey(new Rectangle()), 7, 7);
        movePlayer(null);
        
        //Groups just add an extra layer of organization. In this case not necessary, but trying to show of some of the syntax too

    }

    private void populateArray(){
        for (Node child : mainGridPane.getChildren()) {
            Integer column = GridPane.getColumnIndex(child);
            Integer row = GridPane.getRowIndex(child);
            if (column != null && row != null) {
                gridPaneNodes[column][row] = child;
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

    private void drawRectangle(GraphicsContext gc,Rectangle rect){
        gc.setFill(Color.DARKGREY);
        gc.fillRect(rect.getX()-rect.getWidth()/2.0,
                rect.getY()-rect.getHeight()/2.0,
                rect.getWidth(),
                rect.getHeight());
        gc.setFill(Color.GREEN);
        gc.setStroke(Color.ORANGE);
    }

    //TODO make players able to be visable in rooms where they are as well as deleting them from rooms


    //Sets the Colors and Sizes of different players
        
    private void drawPlayer1(Circle Circle){
        Circle.setFill(javafx.scene.paint.Color.DARKBLUE);
        Circle.setTranslateX(26);
        Circle.setTranslateY(0);
    }
    
    private void drawPlayer2(Circle Circle){
        Circle.setFill(javafx.scene.paint.Color.CRIMSON);
        Circle.setTranslateX(56);
        Circle.setTranslateY(0);
    }
    
    private void drawPlayer3(Circle Circle){
        Circle.setFill(javafx.scene.paint.Color.BLUEVIOLET);
        Circle.setTranslateX(86);
        Circle.setTranslateY(0);
    }
    
    private void drawPlayer4(Circle Circle){
        Circle.setFill(javafx.scene.paint.Color.CORAL);
        Circle.setTranslateX(116);
        Circle.setTranslateY(0);
    }
    //GUI to draw traps
    private Rectangle drawTrap(Rectangle Rect){
        Rect.setWidth(50);
        Rect.setHeight(50);
        Rect.setFill(Color.GREENYELLOW);
        Rect.setTranslateX(58);
        return Rect;
    }
    //GUI to draw keyse
    private Rectangle drawKey(Rectangle Rect){
        Rect.setWidth(25);
        Rect.setHeight(12);
        Rect.setFill(Color.GOLD);
        Rect.setTranslateX(130);
        return Rect;
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

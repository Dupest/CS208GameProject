import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.awt.*;
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
    private HashMap<Integer, Key> keyList;
    private HashMap<Integer, Player> playerList;
    
    private GameLogic GL;

    private static final int PADDING = 10;

    /**
     * This runs first whenever application tester calls Loader.load() so it acts as the driver code for our JavaFX project
     */
    public void initialize() {

        //We can get them number of columns/rows by checking to see how many constraints there are. There will be a specific constraint object for each row/column
        int numColumns = mainGridPane.getColumnConstraints().size();
        int numRows  = mainGridPane.getRowConstraints().size();

        //Init arrays
        gridPaneNodes = new Node[numRows][numColumns];
        canvases = new Node[numRows][numColumns];
        mapLayout = new Room[numRows][numColumns];
        populateArray();

        //We bind a listener to the size of the window to allow things to resize smoothly. resizing calls doStuff()
        mainGridPane.heightProperty().addListener(evt -> doStuff());
        mainGridPane.widthProperty().addListener(evt -> doStuff());

        //Creates the "map" of rooms, players, and Keys
        keyList = new HashMap<>();
        playerList = new HashMap<>();
        GL = new GameLogic();
        mapInitializing();
    }
    /*
    initializes the map depending on the size of the map and
    number of players. By default, it generates a 9 x 9 map
    and 2 players (Justin Lamberson)
     */
    public void mapInitializing(){
        int numColumns = mainGridPane.getColumnConstraints().size();
        int numRows  = mainGridPane.getRowConstraints().size();
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
        playerList.put(1, new Player(mapLayout[0][0]));
        playerList.put(2, new Player(mapLayout[0][1]));

    }



    /**
     * Draw things
     */
    private void doStuff(){
        int numColumns = mainGridPane.getColumnConstraints().size();
        int numRows  = mainGridPane.getRowConstraints().size();

        //First call to doStuff() will be in the initialize() method and for do to order of the loader's ops, getHeight() and getWidth() will return 0 at this point.
        // So we call the prefHeight/Width in that case.
        double winHeight = mainGridPane.getHeight();
        double winWidth = mainGridPane.getWidth();
        if(winHeight == 0 || winWidth == 0){
            winHeight = mainGridPane.getPrefHeight();
            winWidth = mainGridPane.getPrefWidth();
        }

        //Another hacky solution that I've found. getChildren().clear() removes the gridlines on our gridpane. However, this information is stored within the very first
        //child so we simply store that through the deletion, and fit it back in to regain our lines.
        Node node = mainGridPane.getChildren().get(0);
        mainGridPane.getChildren().clear();
        mainGridPane.getChildren().add(0, node);
        GraphicsContext gc = null;
        Canvas newMapImage = null;
        //For each node within the gridpane draw a circle representing a room.
        for(int i = 0; i < numRows; i++){
            for(int k = 0; k < numColumns; k++){

                //Think there's a better way to do this, but default behavior each gridpane node gets a percent of the screen X based on number of children C (size = X/C)
                 newMapImage = new Canvas(winWidth/numRows, winHeight/numColumns);
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
            if(GL.getRoom(i).isATrap()) {
                Room currRoom = GL.getRoom(i);
                mainGridPane.add(drawTrap(new Rectangle()), currRoom.getX(), currRoom.getY());
            }
        }
        
        //Generates 4 Players.
        Circle player1Graphic = new Circle
                        (newMapImage.getWidth()/2.0, newMapImage.getHeight(),15);
        Circle player2Graphic = new Circle
                        (newMapImage.getWidth()/2.0, newMapImage.getHeight(),15);
        Circle player3Graphic = new Circle
                        (newMapImage.getWidth()/2.0, newMapImage.getHeight(),15);
        Circle player4Graphic = new Circle
                        (newMapImage.getWidth()/2.0, newMapImage.getHeight(),15);
        //sets colors of 4 players
        drawPlayer1(player1Graphic);
        drawPlayer2(player2Graphic);
        drawPlayer3(player3Graphic);
        drawPlayer4(player4Graphic);
        //adds 4 players to nodes the commented out code is how to add the players when we have them working.
        mainGridPane.add(player1Graphic, 0, 0); //mainGridPane.add(player1Graphic, GL.getPlayer(2).getX(), GL.getPlayer(1).getY());
        mainGridPane.add(player2Graphic, 0, 0); //mainGridPane.add(player2Graphic, GL.getPlayer(2).getX(), GL.getPlayer(1).getY());
        mainGridPane.add(player3Graphic, 0, 0); //mainGridPane.add(player3Graphic, GL.getPlayer(2).getX(), GL.getPlayer(1).getY());
        mainGridPane.add(player4Graphic, 0, 0); //mainGridPane.add(player4Graphic, GL.getPlayer(2).getX(), GL.getPlayer(1).getY());
        mainGridPane.add(drawKey(new Rectangle()), 7, 7);
        
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

}

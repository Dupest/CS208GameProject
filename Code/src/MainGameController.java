import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.awt.*;
import java.util.*;

public class MainGameController {

    //We keep track of the canvases and gridPane node refs since they're made dynamically
    private Node[][] gridPaneNodes;
    private Node[][] canvases;


    //References to our main window objects for easier coding/listeners yada-yada
    @FXML
    private VBox rootPane;

    @FXML
    private GridPane mainGridPane;

    private HashMap<Integer, Room>  mapLayout;

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
        populateArray();

        //We bind a listener to the size of the window to allow things to resize smoothly. resizing calls doStuff()
        mainGridPane.heightProperty().addListener(evt -> doStuff());
        mainGridPane.widthProperty().addListener(evt -> doStuff());

        //Creates the "map" of rooms
        mapLayout = new HashMap<>();
    }

    public void mapInitializing(){
        //assumes that there are 2 players and a 9 x 9 map
        Random rand = new Random();

        int traps = 10;
        int roomNumber = 1;
        for(int i = 0; i < 81; i++){
            if(rand.nextInt(101) < 50 || traps >= 0){
                traps--;
                mapLayout.put(roomNumber, new Room(false, roomNumber, true));
                roomNumber++;
            } else {
                mapLayout.put(roomNumber, new Room(false, roomNumber, false));
                roomNumber++;
            }
        }
    }

    public void mapInitializing(int players, int rooms){
        //TODO scaling traps with map size and player
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

        //For each node within the gridpane draw a circle representing a room.
        for(int i = 0; i < numColumns; i++){
            for(int k = 0; k < numRows; k++){

                //Think there's a better way to do this, but default behavior each gridpane node gets a percent of the screen X based on number of children C (size = X/C)
                Canvas newMapImage = new Canvas(winWidth/numRows, winHeight/numColumns);
                canvases[i][k] = newMapImage;

                //Returns a graphics object of the canvas for drawing
                GraphicsContext gc = newMapImage.getGraphicsContext2D();

                //My custom circle class from the last project we did because I wanted a quick lazy drawing to show this off.
                CircleWithText mapCircle = new CircleWithText("Map (" + i + ", " + k + ")", new Point2D(newMapImage.getWidth()/2.0,newMapImage.getHeight()/2.0));

                //Default radius on the circles is 40, but if we start to get smaller screen sizes to where each node only has 80 pixels of room in any direction, we adjust the radius.
                if(newMapImage.getHeight() < mapCircle.getDefRadius()*2 || newMapImage.getWidth() < mapCircle.getDefRadius()*2){

                    //We take the smaller of the two values
                    double smallerBound = Math.min(newMapImage.getHeight(), newMapImage.getWidth());
                    //double smallerBound =(newMapImage.getHeight() > newMapImage.getWidth()) ? newMapImage.getWidth(): newMapImage.getHeight();

                    //Radius becomes half of the smallest distance to an edge.
                    mapCircle.setRadius((int)(smallerBound/2.0));
                    mapCircle.drawCusRadius(gc);
                }

                //Otherwise we draw it normally
                else
                    mapCircle.draw(gc);
                //System.out.println("Circle coords: " + mapCircle.getPoint());

                //Groups just add an extra layer of organization. In this case not necessary, but trying to show of some of the syntax too
                Group newGroup = new Group();
                newGroup.getChildren().add(newMapImage);
                mainGridPane.add(newGroup, i, k);
            }
        }
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

    //TODO make players able to be visable in rooms where they are as well as deleting them from rooms
}
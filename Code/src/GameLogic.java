import javafx.geometry.Point2D;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Random;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *  By Svetozar Draganitchki
 * 
 */
public class GameLogic implements KeyListener{
    //TODO: Figure out rehashing - how do we actually do it, is it an automatic call?
    //private Room[][] mapLayout;                                          //TODO: Change to 2D array potentially.

    private HashMap<Point2D, Room> roomList;
    private Key key;
    private HashMap<Integer, Player> playerList;
    private int maxPlayers;
    private int gridRows;
    private int gridColumns;
    private static final int trapChance = 10;

    
    public GameLogic(){
        roomList = new HashMap<>();
        playerList = new HashMap<>();
        mapInitializing(-1, -1);
        maxPlayers = 4;
        gridColumns = 9;
        gridRows = 9;
    }

    public GameLogic(int maxPlayers) {
        roomList = new HashMap<>();
        playerList = new HashMap<Integer, Player> ();
        mapInitializing(-1, -1);
        this.maxPlayers = maxPlayers;
        gridColumns = 9;
        gridRows = 9;
    }
    public GameLogic(int maxPlayers, int gridColumns, int gridRows) {
        key = null;
        playerList = new HashMap<Integer, Player> ();
        roomList = new HashMap<>();
        mapInitializing(-1, -1);
        this.maxPlayers = maxPlayers;
        this.gridColumns = gridColumns;
        this.gridRows = gridRows;
    }

    public GameLogic(Room[][] mapLayout, Key key, HashMap<Integer, Player> playerList){
        playerList = new HashMap<Integer, Player> ();
        roomList = new HashMap<>();
        this.key = key;
        this.playerList = playerList;
        mapInitializing(-1, -1);
    }


    /**
     * method allows there to be a set amount of both
     * players and trapped rooms at the initialization of the
     * game when both parameters are -1
     * otherwise both the number of players and trapped rooms may be changed
     * or only one may be changed-Justin Lamberson
     */
    public void mapInitializing(int players, int trappedRooms){
        //method assumes the map generated is a 9 x 9
        Random rand = new Random();

        int roomNumber = 1;

        //traps is max number of traps
        int traps;
        if(trappedRooms >= -1){
            traps = trappedRooms;
        } else {
            traps = 10;
        }

        //loop initializes all rooms
//        for(int i = 0; i < 81; i++){
//            if(rand.nextInt(101) < 50 || traps >= 0){
//                traps--;
//                mapLayout.put(roomNumber, new Room(false, roomNumber, true, new Key(mapLayout.get(i) ,1) ));
//                roomNumber++;
//            }else if(roomNumber == 81){
//                mapLayout.put(roomNumber, new Room(true, roomNumber, false, new Key(mapLayout.get(i) ,1) ));
//            } else {
//                mapLayout.put(roomNumber, new Room(false, roomNumber, false, new Key(mapLayout.get(i) ,1) ));
//                roomNumber++;
//            }
//        }
        //loop initializes all rooms
        for(int y = 0 ; y < 9; y ++)
        {
            for(int x = 0 ; x < 9; x ++)
            {
                if(rand.nextInt(100) <= trapChance && traps >= 0){
                    roomList.put(new Point2D(x, y), new Room(false, roomNumber, true, x, y));
                    traps --;
                    roomNumber++;
                } else if (x == 8 && y == 8){
                    roomList.put(new Point2D(x, y), new Room(true, roomNumber, false, x, y));
                    roomNumber++;
                }
                else
                {
                    roomList.put(new Point2D(x, y), new Room(true, roomNumber, false, x, y));
                    roomNumber++;
                }
            }
        }

        int x = rand.nextInt(8);
        int y = rand.nextInt(7);

        //generates final key =
        key = new Key(roomList.get(new Point2D(8,8)), 1, x, y);




        //generates the number of players specified by the variable players
        //generates the players at the top right of the map
        if(players == -1){
            for(int i = 0; i < 4; i++){
                playerList.put(i+1, new Player(0 ,0));
            }
        }
        else{
            for(int i = 0; i < players; i++){
                playerList.put(i+1, new Player(0 ,0));
            }
        }

    }
    /*
        By Svetozar Draganitchki
        method that checks if player can enter room
    */
    public boolean canEnter(int roomX, int roomY,Player p){
        if((roomList.get(new Point2D(roomX, roomY)).isLocked())){
            return hasKey(roomX, roomY, p);           //Simplified this logic - DO
        }
        return true;
    }

    public void playerMoves(Player player){
    }
    
    /*
        By Svetozar Draganitchki
        checks if a player has the matching key to a room
    */
    public boolean hasKey(int roomX, int roomY, Player p){
        if((roomList.get(new Point2D(roomX, roomY)).getDoorID() == p.getKey().getMyRoom().getDoorID())){
            return true;
        } else {
            return false;
        }
    }

    /**
     * checks that a player has not gone out of bounds
     * @param p is the player referenced
     */
    public void EnteredRoom(Player p){
        int x = p.getX();
        int y = p.getY();
        if(x < 0){
            p.setX(0);
        } else if (x > gridColumns){
            p.setX(gridColumns);
        }

        if(y < 0){
            p.setY(0);
        } else if(y > gridRows){
            p.setY(gridRows);
        }
    }
    
    public Player getPlayer(int playerID){
        return playerList.get(playerID);
    }
    
    public Key getKey(){
        return key;
    }
    
    public Room getRoom(int x, int y){
        return (roomList.get(new Point2D(x, y)));
    }
    
    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT ||
            e.getKeyCode() == KeyEvent.VK_D) {
            System.out.println("Right key Released");
            
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT ||
            e.getKeyCode() == KeyEvent.VK_A) {
            System.out.println("Left key Released");
            
        }
        if (e.getKeyCode() == KeyEvent.VK_KP_UP ||
            e.getKeyCode() == KeyEvent.VK_W) {
            System.out.println("Up key pressed");
            
        }
        if (e.getKeyCode() == KeyEvent.VK_KP_DOWN ||
            e.getKeyCode() == KeyEvent.VK_S) {
            System.out.println("Down key pressed");
            
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT ||
            e.getKeyCode() == KeyEvent.VK_D) {
            System.out.println("Right key Released");
            
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT ||
            e.getKeyCode() == KeyEvent.VK_A) {
            System.out.println("Left key Released");
            
        }
        if (e.getKeyCode() == KeyEvent.VK_KP_UP ||
            e.getKeyCode() == KeyEvent.VK_W) {
            System.out.println("Up key pressed");
            
        }
        if (e.getKeyCode() == KeyEvent.VK_KP_DOWN ||
            e.getKeyCode() == KeyEvent.VK_S) {
            System.out.println("Down key pressed");
            
        }
    }

    public HashMap<Point2D, Room> getRoomList() {
        return roomList;
    }

    public void setRoomList(HashMap<Point2D, Room> roomList) {
        this.roomList = roomList;
    }

    public void setKey(Key newKey){
        key = newKey;
    }


    public HashMap<Integer, Player> getPlayerList() {
        return playerList;
    }

    public void setPlayerList(HashMap<Integer, Player> playerList) {
        this.playerList = playerList;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public static int getTrapChance() {
        return trapChance;
    }

    public int getGridRows() {
        return gridRows;
    }

    public void setGridRows(int gridRows) {
        this.gridRows = gridRows;
    }

    public int getGridColumns() {
        return gridColumns;
    }

    public void setGridColumns(int gridColumns) {
        this.gridColumns = gridColumns;
    }
}

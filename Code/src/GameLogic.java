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
public class GameLogic{
    //TODO: Figure out rehashing - how do we actually do it, is it an automatic call?
    //private Room[][] mapLayout;                                          //TODO: Change to 2D array potentially.

    //private hashMap for roomList
    private HashMap<Point2D, Room> roomList;
    
    //private key
    private Key key;
    
    //private hashMap to keep track of playerList
    private HashMap<Integer, Player> playerList;
    
    //private int variable to keep track of the maximum players
    private int maxPlayers;
    
    //private int all the grid rows
    private int gridRows;
    
    //private int all the grid columns
    private int gridColumns;
    
    //private int the percentage chance of a trap occuring
    private static final int trapChance = 10;

    /*
        Default constructor
    */
    public GameLogic(){
        roomList = new HashMap<>();
        playerList = new HashMap<>();
        mapInitializing(-1, -1);
        maxPlayers = 4;
        gridColumns = 9;
        gridRows = 9;
    }

    /*
        Construcotr that takes the maximum amount of players
    */
    public GameLogic(int maxPlayers) {
        roomList = new HashMap<>();
        playerList = new HashMap<Integer, Player> ();
        mapInitializing(-1, -1);
        this.maxPlayers = maxPlayers;
        gridColumns = 9;
        gridRows = 9;
    }

    /*
        Construcotr that takes the maximum amount of players, the columns and rows as intergere values.
    */
    public GameLogic(int maxPlayers, int gridColumns, int gridRows) {
        key = null;
        playerList = new HashMap<Integer, Player> ();
        roomList = new HashMap<>();
        mapInitializing(-1, -1);
        this.maxPlayers = maxPlayers;
        this.gridColumns = gridColumns;
        this.gridRows = gridRows;
    }


    /*
        Construcotr that takes the maximum amount of players, a key, and the players Hashmap.
    */
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
        // int to intialize the number value
        int roomNumber = 1;

        //traps is max number of traps
        int traps;
        if(trappedRooms > -1){
            traps = trappedRooms;
        } else {
            traps = 10;
        }
        //Nested for loop that generates rooms and stores them into room
        for(int y = 0 ; y < 9; y++)
        {
            for(int x = 0 ; x < 9; x++)
            {
                if(rand.nextInt(100) <= trapChance && traps >= 0){ //generates traps based on trapchance of occuring
                    //puts rooms into a hashmap of roomlist and generates the code key based on room location
                    roomList.put(new Point2D(x, y), new Room(false, roomNumber, true, x, y));
                    traps --;
                    roomNumber++;
                } else if (x == 8 && y == 8){
                    //creates the final room
                    roomList.put(new Point2D(x, y), new Room(true, roomNumber, false, x, y));
                    roomList.get(new Point2D(x, y)).setFinal(true);
                    System.out.println(roomList.get(new Point2D(x, y)).isATrap());
                    roomNumber++;
                }
                else
                {
                    //puts rooms into a room
                    roomList.put(new Point2D(x, y), new Room(false, roomNumber, false, x, y));
                    roomNumber++;
                }
            }
        }
        //methods to help take the burden off of mapInitializing
        generateFinalKey(rand.nextInt(8), rand.nextInt(7));
        generatePlayers(players);  
    }
    /*generates the number of players specified by the variable players
      generates the players at the top right of the map
    */
    private void generatePlayers(int players){
        if(players == -1){
            for(int i = 0; i < 4; i++){
                playerList.put(i+1, new Player(roomList.get(new Point2D(0, 0)), 0 ,0, i+1));
                roomList.get(new Point2D(0, 0)).playerEntry(playerList.get(i+1));
            }
        }
        else{
            for(int i = 0; i < players; i++){
                playerList.put(i+1, new Player(roomList.get(new Point2D(0, 0)), 0 ,0, i+1));
                roomList.get(new Point2D(0, 0)).playerEntry(playerList.get(i+1));
            }
        }
    }
    
    //method to generate finalKey
    private void generateFinalKey(int x, int y){
       key = new Key(roomList.get(new Point2D(8,8)), 1, x, y);
       roomList.get(new Point2D(x,y)).setKey(key);
    }
    
    /*
        method that checks if player can enter room
    */
    public boolean canEnter(int roomX, int roomY,Player p){
        if((roomList.get(new Point2D(roomX, roomY)).isLocked())){
            return hasKey(roomX, roomY, p);           //Simplified this logic - DO
        }
        return true;
    }
    /*
        updates the Room of the player based on player location
        @param player 
    */
    public boolean playerMoves(Player player){             //Point = 1 or -1
        boolean trap;
        
        Room newRoom = roomList.get(new Point2D(player.getX(), player.getY()));

        trap = newRoom.playerEntry(player);
        player.setCurrentRoom(newRoom);
        return trap;
    }
    
    /*
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
     * returns false if player is outside of bounds and true if player is inside
     */
    public boolean checkMove(Player p){
        boolean fairMove = true;
        int x = p.getX();

        int y = p.getY();
        if(x < 0){
            p.setX(0);
            fairMove = false;
        } else if (x >= gridColumns){
            p.setX(gridColumns-1);
            fairMove = false;
        }

        if(y < 0){
            p.setY(0);
            fairMove = false;
        } else if(y >= gridRows){
            p.setY(gridRows-1);
            fairMove = false;
        }
        System.out.println(p.getKey() == null);
        return fairMove;
    }
    
    //Gettters and Setters
    
    //returns key 
     public Key getKey(){
        return key;
    }
    //returns the hashmap of Room object called roomList
    public HashMap<Point2D, Room> getRoomList() {
        return roomList;
    }
    
    //returns the hashmap of Room object called roomList
    public HashMap<Integer, Player> getPlayerList() {
        return playerList;
    }
    
    //returns maximumplayers
    public int getMaxPlayers(){
        return maxPlayers;
    }
    
    //returns gridRows
    public int getGridRows(){
        return gridRows;
    }
    
    //returns gridColumns
    public int getGridColumns(){
        return gridColumns;
    }
    
    //returns the player from the Hashmap of players
    public Player getPlayer(int playerID){
        return playerList.get(playerID);
    }
    
   //returns the room  from the Hashmap of rooms
    public Room getRoom(int x, int y){
        return (roomList.get(new Point2D(x, y)));
    }

    //set key value
    public void setKey(Key newKey){
        key = newKey;
    }
    
    //sets a hashmap of new rooms to roomList
    public void setRoomList(HashMap<Point2D, Room> roomList) {
        this.roomList = roomList;
    }
    
    //sets a hashmap of new players to playerList
    public void setPlayerList(HashMap<Integer, Player> playerList) {
        this.playerList = playerList;
    }

    //sets the maximum amount of players
    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    //sets the gridRows
    public void setGridRows(int gridRows) {
        this.gridRows = gridRows;
    }

    //sets the gridColumns 
    public void setGridColumns(int gridColumns) {
        this.gridColumns = gridColumns;
    }
    
    public String toString() {
        return "\nRooms List:" + roomList +
                "\nPlayer List: " + playerList +
                "\nKey: " + key +
                "\nMaximumPlayers: " + maxPlayers +
                "\nGrid Row Count: " + gridRows +
                "\nGrid Column Count: " + gridColumns;
    }
    
     @Override
    public boolean equals(Object obj){
        if (obj == this) return true;

        if (obj == null) return false;

        if (this.getClass() == obj.getClass()){
            GameLogic a = (GameLogic) obj;

            return roomList == a.roomList
                    && this.playerList== a.playerList
                    && this.key== a.key
                    && this.maxPlayers== a.maxPlayers
                    && this.gridRows== a.gridRows
                    && this.gridColumns== a.gridColumns;
        }
        else
            return false;
    }
}
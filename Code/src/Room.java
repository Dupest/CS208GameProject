import javafx.scene.shape.Rectangle;

import java.util.*;
/*
    Edited By Svetozar Draganitchki
*/
public class Room {

    //field to determine if a room is locked and a player requires a key to enter
    private boolean isLocked;

    //the ID of the door
    private int doorID;

    //field to determine if a room is trapped and a player will take damage
    private boolean isATrap;

    //Object to render the room in the UI
    private Rectangle roomRender;

    //a hashmap to contain all of the players inside a room
    //HashMap<Integer, Player> playersInside = new HashMap<>();
    LinkedList<Player> playersInside;

    //reference to the key that unlocks the room
    private Key roomKey;

    //coordinates of the room in the map starting at (0,0)
    private int x,y;
    
    //used to keep track of last room or game winning room
    private boolean isFinalRoom;


    //Default Constructor
    public Room(){
        this.isLocked = false;
        this.doorID = -1;
        this.isATrap = false;
        this.roomKey = null;
        this.roomRender = null;
        playersInside = new LinkedList<>();
    }
    /*
        Constructor with three parameters
        @boolean isLocked to checkif room is locked
        @int doorID to check if the id of the door
        @boolean isATrap to check if door has a trap
    */
    public Room(boolean isLocked, int doorID, boolean isATrap) {
        this.isLocked = isLocked;
        this.doorID = doorID;
        this.isATrap = isATrap;
        this.roomRender = null;
        playersInside = new LinkedList<>();
    }
    
     /*
        Constructor with four parameters
        @boolean isLocked to checkif room is locked
        @int doorID to check if the id of the door
        @boolean isATrap to check if door has a trap
        @Key key to have a refrence to key value
    */
    public Room(boolean isLocked, int doorID, boolean isATrap, Key key) {
        this.isLocked = isLocked;
        this.doorID = doorID;
        this.isATrap = isATrap;
        this.roomKey = key;
        this.roomRender = null;
        playersInside = new LinkedList<>();
    }
    
    /*
        Constructor with five parameters
        @boolean isLocked to checkif room is locked
        @int doorID to check if the id of the door
        @boolean isATrap to check if door has a trap
        @int x to setup room at grid location
        @int y to setup room at grid location
    */
    public Room(boolean isLocked, int doorID, boolean isATrap, int x, int y) {
        this.isLocked = isLocked;
        this.doorID = doorID;
        this.isATrap = isATrap;
        this.roomKey = null;
        this.x = x;
        this.y = y;
        this.roomRender = null;
        playersInside = new LinkedList<>();
    }
     /*
        Constructor with six parameters
        @boolean isLocked to checkif room is locked
        @int doorID to check if the id of the door
        @boolean isATrap to check if door has a trap
        @Key key to have a refrence to key value
        @int x to setup room at grid location
        @int y to setup room at grid location
    */
    public Room(boolean isLocked, int doorID, boolean isATrap, Key key, int x, int y) {
        this.isLocked = isLocked;
        this.doorID = doorID;
        this.isATrap = isATrap;
        this.roomKey = key;
        this.x = x;
        this.y = y;
        this.roomRender = null;
        playersInside = new LinkedList<>();
    }
    
     /*
        Constructor with seven parameters
        @boolean isLocked to checkif room is locked
        @int doorID to check if the id of the door
        @boolean isATrap to check if door has a trap
        @Key key to have a refrence to key value
        @boolean isFinalRoom used to generate a room that is the last room
        @int x to setup room at grid location
        @int y to setup room at grid location
    */
    public Room(boolean isLocked, int doorID, boolean isATrap, Key key, boolean isFinalRoom, int x, int y) {
        this.isLocked = isLocked;
        this.doorID = doorID;
        this.isATrap = isATrap;
        this.roomKey = key;
        this.x = x;
        this.y = y;
        this.roomRender = null;
        this.isFinalRoom = isFinalRoom;
        playersInside = new LinkedList<>();
    }
    
    /**checks for a trap when a player comes into a room
     *if room does have a trap, the player takes a random amount damage
     * Damage taken can be no less than 1
     * In addition
     * Justin Lamberson
     */
    public boolean playerEntry(Player player){
        boolean wasTrapped = isATrap;
        playersInside.add(player);
        Random rand = new Random();
        //Math.random() * (max-min+1) + min   - Will return in range
        int damageTaken = rand.nextInt(5);
        if(isATrap){
            if(damageTaken == 0){ //check prevents the damage taken not to be less than 1
                player.setHealthPool((player.getHealthPool() - 1));
                isATrap = false;
            } else {
                player.setHealthPool((player.getHealthPool() - damageTaken));
                isATrap = false;
            }
        }
        if(roomKey != null){
            roomKey.playerTakes(player);
            setKey(null);
        }
        return wasTrapped;
    }
    
    
    
    /**
     * method for removing a player from a room and returning that player
     * -Justin Lamberson
     */
    public Player playerExiting(Player player){
        playersInside.remove(player);
        return player;
    }


    //Getter and setter methods for fields
    
    //returns the room locked option
    public boolean getIsLocked() {
        return isLocked;
    }
    //returns the doorID
    public int getDoorID() {
        return doorID;
    }
    
    //returns true or false if room has a trap
    public boolean isATrap() {
        return isATrap;
    }
    
    //returns true or false if room is last room
    public boolean isFinal() {
        return isFinalRoom;
    }
    //returns key
    public Key getKey(){
        return roomKey;
    }
    //returns x
    public int getX(){
        return x;
    }
    //returns y
    public int getY(){
        return y;
    }
    
    //gets the players inside
    public LinkedList<Player> getPlayersInside() {
        return playersInside;
    }
    
    //get room key
    public Key getRoomKey() {
        return roomKey;
    }

    
    //returns the room render
    public Rectangle getRoomRender() {
        return roomRender;
    }
    
    //sets the lock type of room
    public void setLocked(boolean locked) {
        isLocked = locked;
    }

    //sets the door id
    public void setDoorID(int doorID) {
        this.doorID = doorID;
    }

    //sets the room type
    public void setFinal(boolean last){
        isFinalRoom = last;
    }

    //sets the trap type
    public void setATrap(boolean ATrap) {
        isATrap = ATrap;
    }
    //sets the key 
    public void setKey(Key a){
        roomKey = a;
    }
    
    //sets the room renderer
    public void setRoomRender(Rectangle roomRender) {
        this.roomRender = roomRender;
    }
    
//
//    public HashMap<Integer, Player> getPlayersInside() {
//        return playersInside;
//    }
//
//    public void setPlayersInside(HashMap<Integer, Player> playersInside) {
//        this.playersInside = playersInside;
//    }


    //set the players inside
    public void setPlayersInside(LinkedList<Player> playersInside) {
        this.playersInside = playersInside;
    }
    //sets the roomkey
    public void setRoomKey(Key roomKey) {
        this.roomKey = roomKey;
    }
    //sets x
    public void setX(int x) {
        this.x = x;
    }
    //sets y
    public void setY(int y) {
        this.y = y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(doorID, isATrap);
    }

    /**
     * a debug method in order to populate the room with both a player
     * and a key
     */
    public void debugInitialize(){
        for(int i = 1; i < 5; i++){
            playersInside.add(new Player());
        }
        roomKey = new Key();

    }
    
    public String toString() {
        return "\nPlayer Render:" + isLocked +
                "\nKey: " + doorID +
                "\nRoom: " + isATrap +
                "\nRoom: " + roomRender +
                "\nRoom: " + playersInside +
                "\nRoom: " + roomKey +
                "\nGrid Row Location: " + x +
                "\nGrid Column Location: " + y +
                "\nKey ID: " + isFinalRoom;
    }
    
     @Override
    public boolean equals(Object obj){
        if (obj == this) return true;

        if (obj == null) return false;

        if (this.getClass() == obj.getClass()){
            Room a = (Room) obj;

            return isLocked == a.isLocked
                    && this.doorID== a.doorID
                    && this.isATrap== a.isATrap
                    && this.roomRender== a.roomRender
                    && this.playersInside== a.playersInside
                    && this.roomKey== a.roomKey
                    && this.x== a.x
                    && this.y== a.y
                    && this.isFinalRoom== a.isFinalRoom;
        }
        else
            return false;
    }

}
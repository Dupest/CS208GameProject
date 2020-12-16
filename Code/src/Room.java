import javafx.scene.shape.Rectangle;

import java.util.*;
/*
    Room's hashcode utilizes built in Integer.hashcode() and passed its doorID (Of which will always be a unique incrementing int value. It's a disgustingly simple
    hashing setup, because we can guarantee a unique bucket for each room. This makes collisions a non-issue, and outside of potentially going over load-factor,
    we keep rehashing to a minimum.

    - Darragh O'Halloran


    Edited By Svetozar Draganitchki
*/
public class Room {

    //field to determine if a room is locked and a player requires a key to enter
    private boolean isLocked;

    //the ID of the door
    private int doorID;

    //field to determine if a room is trapped
    private boolean isATrap;

    //Object to render the room in the UI
    private Rectangle roomRender;

    //LinkedList of player references within the room
    LinkedList<Player> playersInside;

    //reference to the key that unlocks this room (or null)
    private Key roomKey;

    //coordinates of the room in the map (0,0) is top left
    private int x,y;


    /**
     * No args constructor
     */
    public Room(){
        this.isLocked = false;
        this.doorID = -1;
        this.isATrap = false;
        this.roomKey = null;
        this.roomRender = null;
        playersInside = new LinkedList<>();
    }
    /**
        Generic room creation without x/y coord's involved
        @boolean whether or not room is locked
        @int doorID hashID of the door;
        @boolean isATrap to check if door has a trap
    */
    public Room(boolean isLocked, int doorID, boolean isATrap) {
        this.isLocked = isLocked;
        this.doorID = doorID;
        this.isATrap = isATrap;
        this.roomRender = null;
        playersInside = new LinkedList<>();
    }
    
     /**
      * Generic room creation without x/y coord's involved that contains a key.
        Constructor with four parameters
        @boolean whether or not room is locked
        @int doorID hashID of the door;
        @boolean isATrap to check if door has a trap
        @Key key A key to place in the room for players to rush to
    */
    public Room(boolean isLocked, int doorID, boolean isATrap, Key key) {
        this.isLocked = isLocked;
        this.doorID = doorID;
        this.isATrap = isATrap;
        this.roomKey = key;
        this.roomRender = null;
        playersInside = new LinkedList<>();
    }
    
    /**
        Creates a room object at the specified coordinates.
         @boolean whether or not room is locked
         @int doorID hashID of the door;
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
     /**
      Creates a room object at the specified coordinates with a key
        @boolean whether or not room is locked
        @int doorID hashID of the door;
        @boolean isATrap to check if door has a trap
        @Key key A key to place in the room for players to rush to
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
    
    /**checks for a trap when a player comes into a room
     *if room does have a trap, the player takes a random amount damage
     * Damage taken can be no less than 1
     * @param player the player that is entering this room
     *
     *
     * Justin Lamberson
     * Darragh O'Halloran
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
     * method for removing and returning a player from this room
     * @param player the player that is exiting this room
     *
     *
     * -Justin Lamberson
     * Darragh O'Halloran
     */
    public Player playerExiting(Player player){
        playersInside.remove(player);
        return player;
    }


    //Getter and setter methods for fields
    
    //returns the room locked option
    public boolean isLocked() {
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
    //returns the game winning key or null
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
        return Integer.hashCode(doorID);
    }

    
    public String toString() {
        return "\nPlayer Render:" + isLocked +
                "\nKey: " + doorID +
                "\nRoom: " + isATrap +
                "\nRoom: " + roomRender +
                "\nRoom: " + playersInside +
                "\nRoom: " + roomKey +
                "\nGrid Row Location: " + x +
                "\nGrid Column Location: " + y;
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
                    && this.y== a.y;
        }
        else
            return false;
    }

}
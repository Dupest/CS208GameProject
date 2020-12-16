import javafx.scene.shape.Rectangle;

import java.util.*;

// Edited By Svetozar Draganitchki
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


    //main constructors to the room
    public Room(){
        this.isLocked = false;
        this.doorID = -1;
        this.isATrap = false;
        this.roomKey = null;
        this.roomRender = null;
        playersInside = new LinkedList<>();
    }
    public Room(boolean isLocked, int doorID, boolean isATrap) {
        this.isLocked = isLocked;
        this.doorID = doorID;
        this.isATrap = isATrap;
        this.roomRender = null;
        playersInside = new LinkedList<>();
    }
    
    //By Svetozar Draganitchki
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
    //By Svetozar Draganitchki
    public Room(boolean isLocked, int doorID, boolean isATrap, Key key) {
        this.isLocked = isLocked;
        this.doorID = doorID;
        this.isATrap = isATrap;
        this.roomKey = key;
        this.roomRender = null;
        playersInside = new LinkedList<>();
    }
    //By Svetozar Draganitchki
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


    //Getter and setter methods for fields
    public boolean isLocked() {
        return isLocked;
    }

    public void setLock(boolean locked) {
        isLocked = locked;
    }

    public int getDoorID() {
        return doorID;
    }

    public void setDoorID(int doorID) {
        this.doorID = doorID;
    }

    public boolean isATrap() {
        return isATrap;
    }

    public void setATrap(boolean ATrap) {
        isATrap = ATrap;
    }
    
    public void setKey(Key a){
        roomKey = a;
    }
    
    public Key getKey(){
        return roomKey;
    }
    
    public int getX(){
        return x;
    }
    
    public int getY(){
        return y;
    }

    public void setLocked(boolean locked) {
        isLocked = locked;
    }

    public Rectangle getRoomRender() {
        return roomRender;
    }

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


    public LinkedList<Player> getPlayersInside() {
        return playersInside;
    }

    public void setPlayersInside(LinkedList<Player> playersInside) {
        this.playersInside = playersInside;
    }

    public Key getRoomKey() {
        return roomKey;
    }

    public void setRoomKey(Key roomKey) {
        this.roomKey = roomKey;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }


    /*TODO: Reconsider these two. We probably don't need to check for the booleans because they'll change. We can, but room.doorID should be unique from it's implementation anyway  */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Room room = (Room) o;
        return doorID == room.doorID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(doorID, isATrap);
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
        int damageTaken = rand.nextInt(9);
        if(isATrap){
            if(damageTaken == 0){ //check prevents the damage taken not to be less than 1
                player.setHealthPool((player.getHealthPool() - 1));
                isATrap = false;
            } else {
                player.setHealthPool((player.getHealthPool() - damageTaken));
                isATrap = false;
            }

            if(player.getHealthPool() < 0){
                player.playerDead();
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

    private boolean hasKey(){
        return getKey() != null;
    }


}

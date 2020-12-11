import javafx.scene.shape.Rectangle;

import java.util.*;

// Edited By Svetozar Draganitchki
public class Room {
    private boolean isLocked;
    private int doorID;
    private boolean isATrap;
    private Rectangle roomRender;

    HashMap<Integer, Player> playersInside = new HashMap<>(); //TODO check hashmap implementation
    
    private Key isKey;
    private int x,y;


    public Room(){
        this.isLocked = false;
        this.doorID = -1;
        this.isATrap = false;
        this.isKey = null;
    }
    public Room(boolean isLocked, int doorID, boolean isATrap) {
        this.isLocked = isLocked;
        this.doorID = doorID;
        this.isATrap = isATrap;
    }
    
    //By Svetozar Draganitchki
    public Room(boolean isLocked, int doorID, boolean isATrap, int x, int y) {
        this.isLocked = isLocked;
        this.doorID = doorID;
        this.isATrap = isATrap;
        this.x = x;
        this.y = y;
    }
    //By Svetozar Draganitchki
    public Room(boolean isLocked, int doorID, boolean isATrap, Key isKey) {
        this.isLocked = isLocked;
        this.doorID = doorID;
        this.isATrap = isATrap;
        this.isKey = isKey;
    }
    //By Svetozar Draganitchki
    public Room(boolean isLocked, int doorID, boolean isATrap, Key isKey, int x, int y) {
        this.isLocked = isLocked;
        this.doorID = doorID;
        this.isATrap = isATrap;
        this.isKey = isKey;
        this.x = x;
        this.y = y;
    }

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
    
    public int getX(){
        return x;
    }
    
    public int getY(){
        return y;
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

    /*checks for a trap when a player comes into a room
     *if room does have a trap, the player takes a random amount damage
     * Damage taken can be no less than 1
     * In addition
     * Justin Lamberson
     */
    public void playerEntry(int playerID, Player player){
        playersInside.put(playerID, player);
        Random rand = new Random();
        //Math.random() * (max-min+1) + min   - Will return in range
        int damageTaken = rand.nextInt(5);
        if(isATrap){
            if(damageTaken == 0){ //check prevents the damage taken not to be less than 1
                player.trapTriggered();
            } else {
                player.trapTriggered(damageTaken);
            }
        }
    }

    /*
     * method for removing a player from a room and returning that player
     * -Justin Lamberson
     * TODO should table be rehashed in this method? -JL
     */
    public Player playerExiting(int playerID){
        Player player = playersInside.get(playerID);
        playersInside.remove(playerID);
        return player;
    }


}

import java.util.Objects;

// Edited By Svetozar Draganitchki
public class Room {
    private boolean isLocked;
    private int doorID;
    //TODO: Hash Table Implementation
    private boolean isATrap;
    
    private Key isKey;
//    private int x,y;


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
    
//    //By Svetozar Draganitchki
//    public Room(boolean isLocked, int doorID, boolean isATrap, int x, int y) {
//        this.isLocked = isLocked;
//        this.doorID = doorID;
//        this.isATrap = isATrap;
//        this.x = x;
//        this.y = y;
//    }
    //By Svetozar Draganitchki
    public Room(boolean isLocked, int doorID, boolean isATrap, Key isKey) {
        this.isLocked = isLocked;
        this.doorID = doorID;
        this.isATrap = isATrap;
        this.isKey = isKey;
    }
    //By Svetozar Draganitchki
//    public Room(boolean isLocked, int doorID, boolean isATrap, Key isKey, int x, int y) {
//        this.isLocked = isLocked;
//        this.doorID = doorID;
//        this.isATrap = isATrap;
//        this.isKey = isKey;
//        this.x = x;
//        this.y = y;
//    }

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
    
//    public int getX(){
//        return x;
//    }
//    
//    public int getY(){
//        return y;
//    }

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
    
    public void trapTriggered(){
        //disables the trap for the room and hurts the player crossing into it
    }
}

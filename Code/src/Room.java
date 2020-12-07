import java.util.Objects;

public class Room {
    private boolean isLocked;
    private int doorID;
    //TODO: Hash Table Implementation
    private boolean isATrap;



    public Room(){
        this.isLocked = false;
        this.doorID = -1;
        this.isATrap = false;
    }
    public Room(boolean isLocked, int doorID, boolean isATrap) {
        this.isLocked = isLocked;
        this.doorID = doorID;
        this.isATrap = isATrap;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void setLocked(boolean locked) {
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
}

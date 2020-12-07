import java.util.ArrayList;

public class Player {
    
    private static final int DEFAULT_HEALTH = 15;

    //TODO: Pick one of these two
    private ArrayList<Key> keyList;
    //private int numKeys;
    private Room currentRoom;
    private int healthPool;
    
    public Player(){
        healthPool = DEFAULT_HEALTH;
        keyList = new ArrayList();
        currentRoom = null;
    }
    
    public void trapTriggered(){
        healthPool--;
    }
}

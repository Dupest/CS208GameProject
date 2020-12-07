import java.util.ArrayList;
import java.util.HashMap;

public class Player {
    
    private static final int DEFAULT_HEALTH = 15;
    private HashMap<String, String> playersMap;

    //TODO: Pick one of these two
    private ArrayList<Key> keyList;
    //private int numKeys;
    private Room currentRoom;
    private int healthPool;
    
    public Player(){
        healthPool = DEFAULT_HEALTH;
        keyList = new ArrayList();
        currentRoom = null;
        playersMap = new HashMap<>();
    }
    
    public void trapTriggered(){
        healthPool--;
    }
    }

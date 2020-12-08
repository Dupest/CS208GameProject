import java.util.ArrayList;
import java.util.HashMap;

//Edited By Svetozar Draganitchki
public class Player {
    
    private static final int DEFAULT_HEALTH = 15;
    private HashMap<String, String> playersMap; ///I think this might be useless -Svet

    //TODO: Pick one of these two
    private ArrayList<Key> keyList; 
    //private int numKeys;
    private Room currentRoom;
    private int healthPool;
    //variables to keep track of player location
    private int x,y;
    
    public Player(){
        healthPool = DEFAULT_HEALTH;
        keyList = new ArrayList();
        currentRoom = null;
        playersMap = new HashMap<>();
    }

    public Player(Room initalRoom){
        healthPool = DEFAULT_HEALTH;
        keyList = new ArrayList();
        currentRoom = null;
        playersMap = new HashMap<>();
    }
    
    //By Svetozar Draganitchki
    public Player(int x,int y){
        healthPool = DEFAULT_HEALTH;
        keyList = new ArrayList();
        currentRoom = null;
        playersMap = new HashMap<>();
        this.x = x;
        this.y = y;
    }

    public void playerDead(){
        if(healthPool <= 0){
            currentRoom = null;
        }
    }
    
    public void trapTriggered(){
        healthPool--;
    }

    public int getHealthPool(){
        return healthPool;
    }
    
    //By Svetozar Draganitchki
    public void moveRight(){
        x++;
    }
    
    public void moveLeft(){
        x--;
    }
    
    public void moveForward(){
        y++;
    }
    
    public void moveBackward(){
        y--;
    }
}



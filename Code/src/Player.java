import javafx.scene.shape.Circle;


/*
    Player's hashcode utilizes built in Integer.hashcode() and passed its doorID (Of which will always be a unique incrementing int value. It's a disgustingly simple
    hashing setup, because we can guarantee a unique bucket for each room. This makes collisions a non-issue, and outside of potentially going over load-factor,
    we keep rehashing to a minimum.

    - Darragh O'Halloran
 */

//Edited By Svetozar Draganitchki
public class Player {

    //used to draw the player on the map
    private Circle playerRender;

    //default health of the player if the constructor without the health parameter is used
    private static final int DEFAULT_HEALTH = 15;

    //Space for the game winning key if the player picks it up
    private Key key;

    //Reference to currentRoom, or null in the case of death
    private Room currentRoom;

    //the current health of the player
    private int healthPool;
    
    //variables to keep track of player location
    private int x,y;

    private int playerID;

    //No args Constructor
    public Player(){
        healthPool = DEFAULT_HEALTH;
        key = null;
        currentRoom = null;
    }
    /**
     *
     * @param room  player's current room
     */
    public Player(Room room){
        healthPool = DEFAULT_HEALTH;
        key = null;
        currentRoom = room;
    }


    /**
     *
     * @param x     player's x
     * @param y     player's y
     */
    public Player(int x,int y){
        healthPool = DEFAULT_HEALTH;
        key = null;
        currentRoom = null;
        this.x = x;
        this.y = y;
    }

    /**
     *
     * @param room  player's current room
     * @param x     player's x
     * @param y     player's y
     * @param id   player's ID
     */
    public Player(Room room, int x,int y, int id){
        healthPool = DEFAULT_HEALTH;
        key = null;
        currentRoom = room;
        this.x = x;
        this.y = y;
        this.playerID = id;
        playerRender = new Circle();
    }


    /**
     * Called when the player dies, deals with the organizational points of removing the player, as well as
     * re-placing the key if the player was carrying it.
     */
    public void playerDead(){
        if(key != null){
            currentRoom.setKey(key);
            key.setKeyID(-1);
            key.setX(x);
            key.setY(y);
            setKey(null);
        }
        currentRoom = null;

    }

    /*
        Getters and Setters
     */
    public int getHealthPool(){
        return healthPool;
    }

    public void setHealthPool(int newHealth){
        healthPool = newHealth;
    }


    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    //By Svetozar Draganitchki
    public void moveRight(){ x++; }
    
    public void moveLeft(){
        x--;
    }
    
    public void moveDown(){
        y++;
    }
    
    public void moveUp(){
        y--;
    }
    
    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }

    public Circle getPlayerRender() {
        return playerRender;
    }

    public void setPlayerRender(Circle playerRender) {
        this.playerRender = playerRender;
    }

    public static int getDefaultHealth() {
        return DEFAULT_HEALTH;
    }

    public void setKey(Key newKey){
        key = newKey;
    }

    /**
     *
     * @return this player's key
     */
    public Key getKey(){
        return key;
    }

    /**
     *
     * @return this player's current room
     */
    public Room getCurrentRoom() {
        return currentRoom;
    }

    public void setCurrentRoom(Room currentRoom) {
        this.currentRoom = currentRoom;
    }

    /**
     *
     * @return ID # corresponding to this player
     */
    public int getPlayerID() {
        return playerID;
    }

    @Override
    /*
       By Svetozar Draganitcki
       ToString method to return the health of the player and the key
    */
    public String toString() {
        return "Player Health:" + healthPool + "Key " + key;
    }
    
     @Override
    public boolean equals(Object obj){
        if (obj == this) return true;

        if (obj == null) return false;

        if (this.getClass() == obj.getClass()){
            Player a = (Player) obj;

            return currentRoom == a.currentRoom
                    && this.healthPool== a.healthPool
                    && this.key== a.key && a.getPlayerID() == this.getPlayerID();
        }
        else
            return false;
    }

    /**
     * Darragh O'Halloran
     * @return object's hashcode
     */
    @Override
    public int hashCode() {
        return Integer.hashCode(playerID);
    }
}



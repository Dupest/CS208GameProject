import javafx.scene.shape.Circle;

import java.util.ArrayList;
import java.util.HashMap;

//Edited By Svetozar Draganitchki
public class Player {

    //used to draw the player on the map
    private Circle playerRender;

    //default health of the player if the constructor without the health parameter is used
    private static final int DEFAULT_HEALTH = 15;

    //TODO: Pick one of these two
  //  private ArrayList<Key> keyList;

    //The list of keys a player would have on them after collection
    private Key key;
    //private int numKeys;

    /**reference to the current room a player is in
     * if set to null, the player is out of the game
     * or is not playing the game
     */
    private Room currentRoom;

    //the current health of the player
    private int healthPool;
    
    //variables to keep track of player location
    private int x,y;

    private int hashKey;

    //main constructors
    public Player(){
        healthPool = DEFAULT_HEALTH;
        key = null;
        currentRoom = null;
    }

    public Player(Room room){
        healthPool = DEFAULT_HEALTH;
        key = null;
        currentRoom = room;
    }


    //By Svetozar Draganitchki
    public Player(int x,int y){
        healthPool = DEFAULT_HEALTH;
        key = null;
        currentRoom = null;
        this.x = x;
        this.y = y;
    }

    public Player(Room room, int x,int y, int hashKey){
        healthPool = DEFAULT_HEALTH;
        key = null;
        currentRoom = room;
        this.x = x;
        this.y = y;
        this.hashKey = hashKey;
        playerRender = new Circle();
    }

    public void playerDead(){
        if(healthPool <= 0){
            currentRoom = null;
        }
    }

    //old methods that were moved into the room class
    /*
     *if a trap is triggered, by default, damage taken is one
     */
    /*public void trapTriggered(){
        healthPool--;
    }*/

    /*
     * Overloaded method does the same thing as the default method
     * but, it allows the damage taken to be set
     */
    /*public void trapTriggered(int damage){
        healthPool -= damage;
    }*/


    //getters and setters of the class
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
    
    public void moveForward(){
        y++;
    }
    
    public void moveBackward(){
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

    public Key getKey(){
        return key;
    }

    public Room getCurrentRoom() {
        return currentRoom;
    }

    public void setCurrentRoom(Room currentRoom) {
        this.currentRoom = currentRoom;
    }

    public int getHashKey() {
        return hashKey;
    }

    public void setHashKey(int hashKey) {
        this.hashKey = hashKey;
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
                    && this.key== a.key && a.getHashKey() == this.getHashKey();
        }
        else
            return false;
    }


}



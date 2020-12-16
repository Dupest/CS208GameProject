import javafx.scene.shape.Circle;

import java.util.ArrayList;
import java.util.HashMap;

/*
    By Svetozar Draganitchki
    Player class object that holds render for players, health, key refrence, room refrence values.
    Used to represent players on game board.
*/
public class Player {

    //used to draw the player on the map
    private Circle playerRender;

    //default health of the player if the constructor without the health parameter is used
    private static final int DEFAULT_HEALTH = 15;

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
    
    //hash value of key used to compare to final room hash value to unlock room
    private int hashKey;

    //Default constructor
    public Player(){
        healthPool = DEFAULT_HEALTH;
        key = null;
        currentRoom = null;
    }
    /*Constructor with one parameter
        @Room room to set players current room when making a player
    */
    public Player(Room room){
        healthPool = DEFAULT_HEALTH;
        key = null;
        currentRoom = room;
    }

    /*Constructor with two parameters
        @int x to set x coordinate value
        @int y to set y coordinate value
    */
    public Player(int x,int y){
        healthPool = DEFAULT_HEALTH;
        key = null;
        currentRoom = null;
        this.x = x;
        this.y = y;
    }

     /*Constructor with four parameters
        @Room room to set players current room when making a player
        @int x to set x coordinate value
        @int y to set y coordinate value
        @hashKey to set the hashcode value of the key
    */
    public Player(Room room, int x,int y, int hashKey){
        healthPool = DEFAULT_HEALTH;
        key = null;
        currentRoom = room;
        this.x = x;
        this.y = y;
        this.hashKey = hashKey;
        playerRender = new Circle();
    }
    
    //method that sets the players room to null if they are dead
    public void playerDead(){
        if(healthPool <= 0){
            currentRoom = null;
        }
    }
    /*
    *Methods to move player in on x and y coordinates
    */
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
    //method to set player at a certain location
    public void setLocation(int x, int y){
       this.x = x;
       this.y = y;
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
    
    //get playerRender
    public Circle getPlayerRender() {
        return playerRender;
    }
    //gets the health of player
    public int getHealthPool(){
        return healthPool;
    }
    
    //gets the key of the player
    public Key getKey(){
        return key;
    }
    
    //gets the current room of the player
    public Room getCurrentRoom() {
        return currentRoom;
    }
    
    //gets the hash code value of the key
     public int getHashKey() {
        return hashKey;
    }
     
    //gets default health value
    public static int getDefaultHealth() {
        return DEFAULT_HEALTH;
    } 
    
    //gets the x value 
    public int getX(){
        return x;
    }
    //gets the y value 
    public int getY(){
        return y;
    }
   
     //sets the health pool value of the key
    public void setHealthPool(int newHealth){
        healthPool = newHealth;
    }
    
    //sets the x location
    public void setX(int x) {
        this.x = x;
    }
    //sets the y location
    public void setY(int y) {
        this.y = y;
    }

    //sets the type of Cirle render to player
    public void setPlayerRender(Circle playerRender) {
        this.playerRender = playerRender;
    }

    //sets the key
    public void setKey(Key newKey){
        key = newKey;
    }

    //sets the currentRoom
    public void setCurrentRoom(Room currentRoom) {
        this.currentRoom = currentRoom;
    }
    
    //sets the hash key value
    public void setHashKey(int hashKey) {
        this.hashKey = hashKey;
    }

    public String toString() {
        return "\nPlayer Render:" + playerRender +
                "\nKey: " + key +
                "\nRoom: " + currentRoom +
                "\nGrid Row Location: " + x +
                "\nGrid Column Location: " + y +
                "\nKey ID: " + hashKey;
    }
    
     @Override
    public boolean equals(Object obj){
        if (obj == this) return true;

        if (obj == null) return false;

        if (this.getClass() == obj.getClass()){
            Player a = (Player) obj;

            return playerRender == a.playerRender
                    && this.key== a.key
                    && this.currentRoom== a.currentRoom
                    && this.x== a.x
                    && this.y== a.y
                    && this.hashKey== a.hashKey;
        }
        else
            return false;
    }
}



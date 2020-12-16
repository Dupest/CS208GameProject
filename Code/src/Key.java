//Edited by Svetozar Draganitchki
public class Key {

    //reference to the room the key unlocks
    private Room roomUnlock;

    //To provide the ability to scale up key damage ( not implemented )
    private int damageMagnitude;

    //Default scaling
    private static int magnitudeModifier = 1;
    
    //location of the key on the map
    private int x,y;

    //Key hash;
    private int keyID;

    //Default constructor 
    public Key(){
        this.roomUnlock = null;
        this.damageMagnitude = magnitudeModifier*100;   //Random magic number, mostly placeholder idea
    }
    /**Constructor that takes two parameters
        @Room roomUnlock they room value to unlock the room
        @int damgaeMagnitude the damage a key will do if you don't pick it up
    */
    public Key(Room roomUnlock, int damageMagnitude) {
        this.roomUnlock = roomUnlock; //Room the key opens
        this.damageMagnitude = damageMagnitude*magnitudeModifier;
    }
    
    /**Constructor that takes four parameters
        @Roomt roomUnlock they room value to unlock the room
        @int damgaeMagnitude the damage a key will do if you don't pick it up
        @int x the cordinational direction to keep track of key on map
        @int y the cordinational direction to keep track of key on map
    */
    public Key(Room roomUnlock, int damageMagnitude, int x, int y) {
        this.roomUnlock = roomUnlock; //Room the key opens
        this.damageMagnitude = damageMagnitude*magnitudeModifier;
        this.x = x;
        this.y = y;
        this.keyID = -1;
    }
    
    //method for player to retrieve key
    public void playerTakes(Player player){
        player.setKey(this);
        keyID = player.getPlayerID();
    }
    
    //method for player to keep track of carrying the key
    public boolean playerCarrying(){
        return keyID != -1;
    }


    //Getter and setter methods
    
    //gets Room that is used to keep track of room unlock
    public Room getMyRoom() {
        return roomUnlock;
    }
    
    //gets the damage value of key
     public int getDamageMagnitude() {
        return damageMagnitude;
    }
     
    //gets the damage magnifier of the room
    public static int getMagnitudeModifier() {
        return magnitudeModifier;
    }

    //returns the x value of key
    public int getX() {
        return x;
    }
    
    //returns the y value of key
    public int getY() {
        return y;
    }

    public Room getRoomUnlock() {
        return roomUnlock;
    }

    public int getKeyID() {
        return keyID;
    }

    public void setKeyID(int keyID) {
        this.keyID = keyID;
    }

    //sets the room reference
    public void setMyRoom(Room myRoom) {
        this.roomUnlock = myRoom;
    }

   //sets the damage 
    public void setDamageMagnitude(int damageMagnitude) {
        this.damageMagnitude = damageMagnitude;
    }

    //sets the damage magnifire 
    public static void setMagnitudeModifier(int magnitudeModifier) {
        Key.magnitudeModifier = magnitudeModifier;
    }

    //sets the room to be unlock or locked
    public void setRoomUnlock(Room roomUnlock) {
        this.roomUnlock = roomUnlock;
    }

    //sets x
    public void setX(int x) {
        this.x = x;
    }
    
    //sets y
    public void setY(int y) {
        this.y = y;
    }
    
    public String toString() {
        return "" + keyID;
    }
    
     @Override
    public boolean equals(Object obj){
        if (obj == this) return true;

        if (obj == null) return false;

        if (this.getClass() == obj.getClass()){
            Key a = (Key) obj;

            return roomUnlock == a.roomUnlock
                    && this.damageMagnitude== a.damageMagnitude
                    && this.magnitudeModifier== a.magnitudeModifier
                    && this.x== a.x
                    && this.y== a.y
                    && this.keyID== a.keyID;
        }
        else
            return false;
    }

}
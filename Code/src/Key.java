//Edited by Svetozar Draganitchki
public class Key {

    //reference to the room the key unlocks
    private Room roomUnlock;

    //used if the game master wishes the key to do more damage to other players when aquired
    private int damageMagnitude;
    //auto-scaling damage maybe
    //damage magnitude of the key
    private static int magnitudeModifier = 1;
    
    //location of the key on the map
    private int x,y;

    //constuctors for the class
    public Key(){
        this.roomUnlock = null;
        this.damageMagnitude = magnitudeModifier*100;   //Random magic number, mostly placeholder idea
    }
    public Key(Room roomUnlock, int damageMagnitude) {
        this.roomUnlock = roomUnlock; //Room the key opens
        this.damageMagnitude = damageMagnitude*magnitudeModifier;
    }
    
    //Edited By Svetozar Draganitchki
    public Key(Room roomUnlock, int damageMagnitude, int x, int y) {
        this.roomUnlock = roomUnlock; //Room the key opens
        this.damageMagnitude = damageMagnitude*magnitudeModifier;
        this.x = x;
        this.y = y;
    }


    //Getter and setter methods
    public Room getMyRoom() {
        return roomUnlock;
    }

    public void setMyRoom(Room myRoom) {
        this.roomUnlock = myRoom;
    }

    public int getDamageMagnitude() {
        return damageMagnitude;
    }

    public void setDamageMagnitude(int damageMagnitude) {
        this.damageMagnitude = damageMagnitude;
    }

    public static int getMagnitudeModifier() {
        return magnitudeModifier;
    }

    public static void setMagnitudeModifier(int magnitudeModifier) {
        Key.magnitudeModifier = magnitudeModifier;
    }

}

//Edited by Svetozar Draganitchki
public class Key {
    private Room roomUnlock;
    private int damageMagnitude;
    //auto-scaling damage maybe
    private static int magnitudeModifier = 1;
    
    //location variables
    private int x,y;
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

    public Room getMyRoom() {
        return roomUnlock;
    }
    //Jesus loves you
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

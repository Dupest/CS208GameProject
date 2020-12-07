public class Key {
    private Room myRoom;
    private int damageMagnitude;
    //auto-scaling damage maybe
    private static int magnitudeModifier = 1;
    public Key(){
        this.myRoom = null;
        this.damageMagnitude = magnitudeModifier*100;   //Random magic number, mostly placeholder idea
    }
    public Key(Room myRoom, int damageMagnitude) {
        this.myRoom = myRoom;
        this.damageMagnitude = damageMagnitude*magnitudeModifier;
    }

    public Room getMyRoom() {
        return myRoom;
    }

    public void setMyRoom(Room myRoom) {
        this.myRoom = myRoom;
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

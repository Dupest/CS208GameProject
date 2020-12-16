public class FinalRoom extends Room {
    public FinalRoom() {
        super();
    }

    public FinalRoom(boolean isLocked, int doorID, boolean isATrap) {
        //super(isLocked, doorID, isATrap);
    }

    public FinalRoom(int doorID, int x, int y) {
        super(true, doorID, false, x, y);
    }

    public FinalRoom(boolean isLocked, int doorID, boolean isATrap, Key key) {
        //super(isLocked, doorID, isATrap, key);
    }

    public FinalRoom(boolean isLocked, int doorID, boolean isATrap, Key key, int x, int y) {
        //super(isLocked, doorID, isATrap, key, x, y);
    }

    @Override
    public boolean playerEntry(Player player) {
        boolean hasKey = checkPlayerForKey(player);
        if(hasKey)
            return super.playerEntry(player);
        else
            return false;

    }
    public boolean checkPlayerForKey(Player player){
        if(player.getKey() != null){
            unlockRoom();
            //player.openRoom();
            return true;
        }
        return false;

    }
    public void unlockRoom(){
        this.setLocked(false);
    }

}

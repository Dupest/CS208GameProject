/*
   Very basic  FinalRoom which extends Room to add a few extra methods to assist with keeping players out of it.


   I would have liked to improve upon this more, because the current version is a skeleton, but I ran out of time

   - Darragh O'Halloran

 */


public class FinalRoom extends Room {
    //Barebones Constructors



    public FinalRoom() {
        super();
    }

    public FinalRoom(int doorID, int x, int y) {
        super(true, doorID, false, x, y);
    }


    //We disallow players entering without a key
    @Override
    public boolean playerEntry(Player player) {
        boolean hasKey = checkPlayerForKey(player);
        if(hasKey)
            return super.playerEntry(player);
        else
            return false;

    }

    //Check to see if the player has a key
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

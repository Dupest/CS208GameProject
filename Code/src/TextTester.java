import java.util.HashMap;


/**
 * tester used to determine
 * if methods were working correctly
 */
public class TextTester {
    public static void main(String[] args) {
        GameLogic gamelogic = new GameLogic();

        for (int x = 0; x < gamelogic.getGridRows(); x++) {
            for (int y = 0; y < gamelogic.getGridColumns(); y++) {
                Room room = gamelogic.getRoom(x, y);
                System.out.println("Room Cords: " + room.getX() + ", " + room.getY() + "\nIs locked? " + room.isLocked() + "\nDoor ID: " + room.getDoorID() + "\nTrapped? " + room.isATrap());
                System.out.print("Key? ");
                if (room.getKey() == null) {
                    System.out.println("No");
                } else {
                    System.out.println("Yes. Unlocks roomID: " + room.getKey().getMyRoom().getDoorID());
                }

                if(room.getPlayersInside().containsKey(1)){
                    System.out.println("Player 1 is inside");
                }
                if(room.getPlayersInside().containsKey(2)){
                    System.out.println("Player 2 is inside");
                }
                if(room.getPlayersInside().containsKey(3)){
                    System.out.println("Player 3 is inside");
                }
                if(room.getPlayersInside().containsKey(4)){
                    System.out.println("Player 4 is inside");
                }
                System.out.print("\n");
            }
        }


    }
}

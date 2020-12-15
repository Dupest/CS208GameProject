


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

//                if(room.getPlayersInside().containsKey(1)){
//                    System.out.println("Player 1 is inside");
//                }
//                if(room.getPlayersInside().containsKey(2)){
//                    System.out.println("Player 2 is inside");
//                }
//                if(room.getPlayersInside().containsKey(3)){
//                    System.out.println("Player 3 is inside");
//                }
//                if(room.getPlayersInside().containsKey(4)){
//                    System.out.println("Player 4 is inside");
//                }
                System.out.print("\n");
            }
        }

        gamelogic.playerMoves(gamelogic.getPlayer(1), 0, 1);

        for(int i = 1; i < 5; i++){
            System.out.println("Player " + i + " Cords: " + gamelogic.getPlayer(i).getCurrentRoom().getX() + ", " + gamelogic.getPlayer(i).getCurrentRoom().getY());
        }


        /*for (int x = 0; x < gamelogic.getGridRows(); x++) {
            for (int y = 0; y < gamelogic.getGridColumns(); y++) {
                Room room = gamelogic.getRoom(x, y);


                if(room.getPlayersInside().containsKey(1)){
                    System.out.println("Player 1 is inside");
                    System.out.println("Room: " + room.getX() + ", " + room.getY());
                    System.out.println("Player Cords: " + room.getPlayersInside().get(1).getX() + ", " + room.getPlayersInside().get(1).getY());
                }
                if(room.getPlayersInside().containsKey(2)){
                    System.out.println("Player 2 is inside");
                    System.out.println("Room: " + room.getX() + ", " + room.getY());
                    System.out.println("Player Cords: " + room.getPlayersInside().get(2).getX() + ", " + room.getPlayersInside().get(2).getY());
                }
                if(room.getPlayersInside().containsKey(3)){
                    System.out.println("Player 3 is inside");
                    System.out.println("Room: " + room.getX() + ", " + room.getY());
                    System.out.println("Player Cords: " + room.getPlayersInside().get(3).getX() + ", " + room.getPlayersInside().get(3).getY());
                }
                if(room.getPlayersInside().containsKey(4)){
                    System.out.println("Player 4 is inside");
                    System.out.println("Room: " + room.getX() + ", " + room.getY());
                    System.out.println("Player Cords: " + room.getPlayersInside().get(4).getX() + ", " + room.getPlayersInside().get(4).getY());
                }
                System.out.print("\n");
            }
        }*/


    }
}

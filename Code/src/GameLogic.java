
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Random;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *  By Svetozar Draganitchki
 * 
 */
public class GameLogic implements KeyListener{
    //TODO: Figure out rehashing - how do we actually do it, is it an automatic call?
    private HashMap<Integer, Room>  mapLayout;                                          //TODO: Change to 2D array potentially.
    private HashMap<Integer, Key> keyList;
    private HashMap<Integer, Player> playerList;
    private static final int MAXPLAYERS = 4;

    
    public GameLogic(){
        mapLayout = new HashMap<Integer, Room> ();
        keyList = new HashMap<Integer, Key> ();
        playerList = new HashMap<Integer, Player> ();
        mapInitializing();
    }
    
    public GameLogic( HashMap<Integer, Room>  mapLayout, HashMap<Integer, Key> keyList, HashMap<Integer, Player> playerList){
        this.mapLayout = mapLayout;
        this.keyList = keyList;
        this.playerList = playerList;
        mapInitializing();
    }
  

    
    /*
    initializes the map depending on the size of the map and
    number of players. By default, it generates a 9 x 9 map
    and 2 players
     */
    public void mapInitializing(){
        //assumes that there are 2 players and a 9 x 9 map
        Random rand = new Random();

        //traps is max number of traps
        int traps = 10;
        int roomNumber = 1;

        //loop initializes all rooms
//        for(int i = 0; i < 81; i++){
//            if(rand.nextInt(101) < 50 || traps >= 0){
//                traps--;
//                mapLayout.put(roomNumber, new Room(false, roomNumber, true, new Key(mapLayout.get(i) ,1) ));
//                roomNumber++;
//            }else if(roomNumber == 81){
//                mapLayout.put(roomNumber, new Room(true, roomNumber, false, new Key(mapLayout.get(i) ,1) ));
//            } else {
//                mapLayout.put(roomNumber, new Room(false, roomNumber, false, new Key(mapLayout.get(i) ,1) ));
//                roomNumber++;
//            }
//        }
        //loop initializes all rooms
        for(int y = 0 ; y < 9; y ++)
        {
            for(int x = 0 ; x < 9; x ++)
            {
                if(rand.nextInt(10) <= 5 || traps >= 0){
                    mapLayout.put(roomNumber, new Room(false, roomNumber, true, x, y));
                    traps --;
                    roomNumber ++;
                } else if (x == 8 && y == 0){
                    mapLayout.put(roomNumber, new Room(true, roomNumber, false, x, y));
                    roomNumber++;
                }
                else
                {
                    mapLayout.put(roomNumber, new Room(true, roomNumber, false, x, y));
                    roomNumber++;
                }
            }
        }
        
        //generates final key =
        keyList.put(81, new Key(mapLayout.get(81) ,1));
        
        //generates keys
        for(int i = 1; i <= 10; i++)
        {
            mapLayout.get(i).setLock(true);
            keyList.put(i, new Key(mapLayout.get(i),1));
        }
        

        //generates the two players in the top 2 rooms
        playerList.put(1, new Player(mapLayout.get(rand.nextInt(10)))); // playerList.put(1, new Player(0,8));
        playerList.put(2, new Player(mapLayout.get(rand.nextInt(10)))); // playerList.put(1, new Player(0,7));

    }
    
    //Only to be used after implmenting mapInitializing() not sure about using this, ***** DON'T LIKE THIS ******
    public int[][] to2DArray(){
        int[][] room2d = new int[9][9];
        int x = 0;
        int y = 0;
        for(HashMap.Entry<Integer, Room> entry : mapLayout.entrySet())
        {
            if(x >= 9)
            {
                if(y >= 9)
                {
                    room2d[x][y] = entry.getKey();
                    y = 0;
                    x = 0;
                }
                else
                {
                    room2d[x][y] = entry.getKey();
                    y++;
                    x = 0;
                }
            }
            else
            {
               if(y >= 9)
                {
                    room2d[x][y] = entry.getKey();
                    y = 0;
                    x++;
                }
                else
                {
                    room2d[x][y] = entry.getKey();
                    y++;
                    x++;
                }
            }
        }
        return room2d;
    }

    /*
     * method allows there to be a set amount of both
     * players and trapped rooms at the initialization of the
     * game -Justin Lamberson
     */
    public void mapInitializing(int players, int trappedRooms){
        //method assumes the map generated is a 9 x 9
        Random rand = new Random();

        //traps is max number of traps
        int traps = trappedRooms;
        int roomNumber = 1;

        //loop initializes all rooms
//        for(int i = 0; i < 81; i++){
//            if(rand.nextInt(101) < 50 || traps >= 0){
//                traps--;
//                mapLayout.put(roomNumber, new Room(false, roomNumber, true, new Key(mapLayout.get(i) ,1) ));
//                roomNumber++;
//            }else if(roomNumber == 81){
//                mapLayout.put(roomNumber, new Room(true, roomNumber, false, new Key(mapLayout.get(i) ,1) ));
//            } else {
//                mapLayout.put(roomNumber, new Room(false, roomNumber, false, new Key(mapLayout.get(i) ,1) ));
//                roomNumber++;
//            }
//        }
        //loop initializes all rooms
        for(int y = 0 ; y < 9; y ++)
        {
            for(int x = 0 ; x < 9; x ++)
            {
                if(rand.nextInt(10) <= 5 || traps >= 0){
                    mapLayout.put(roomNumber, new Room(false, roomNumber, true, x, y));
                    traps --;
                    roomNumber ++;
                } else if (x == 8 && y == 0){
                    mapLayout.put(roomNumber, new Room(true, roomNumber, false, x, y));
                    roomNumber++;
                }
                else
                {
                    mapLayout.put(roomNumber, new Room(true, roomNumber, false, x, y));
                    roomNumber++;
                }
            }
        }

        //generates final key =
        keyList.put(81, new Key(mapLayout.get(81) ,1));

        //generates keys
        for(int i = 1; i <= 10; i++)
        {
            mapLayout.get(i).setLock(true);
            keyList.put(i, new Key(mapLayout.get(i),1));
        }


        //generates the number of players specified by the variable players
        //generates the players at the top of the map
        for(int i = 1; i <= players; i++){
            playerList.put(i, new Player(mapLayout.get(rand.nextInt(10))));
        }

    }
    /*
     * method assumes the map is a 9 x 9 and that there are only 2 players
     * -Justin Lamberson
     */
    public void mapInitializing(int trappedRooms){
        Random rand = new Random();

        //traps is max number of traps
        int traps = 10;
        int roomNumber = 1;

        //loop initializes all rooms
//        for(int i = 0; i < 81; i++){
//            if(rand.nextInt(101) < 50 || traps >= 0){
//                traps--;
//                mapLayout.put(roomNumber, new Room(false, roomNumber, true, new Key(mapLayout.get(i) ,1) ));
//                roomNumber++;
//            }else if(roomNumber == 81){
//                mapLayout.put(roomNumber, new Room(true, roomNumber, false, new Key(mapLayout.get(i) ,1) ));
//            } else {
//                mapLayout.put(roomNumber, new Room(false, roomNumber, false, new Key(mapLayout.get(i) ,1) ));
//                roomNumber++;
//            }
//        }
        //loop initializes all rooms
        for(int y = 0 ; y < 9; y ++)
        {
            for(int x = 0 ; x < 9; x ++)
            {
                if(rand.nextInt(10) <= 5 || traps >= 0){
                    mapLayout.put(roomNumber, new Room(false, roomNumber, true, x, y));
                    traps --;
                    roomNumber ++;
                } else if (x == 8 && y == 0){
                    mapLayout.put(roomNumber, new Room(true, roomNumber, false, x, y));
                    roomNumber++;
                }
                else
                {
                    mapLayout.put(roomNumber, new Room(true, roomNumber, false, x, y));
                    roomNumber++;
                }
            }
        }

        //generates final key =
        keyList.put(81, new Key(mapLayout.get(81) ,1));

        //generates keys
        for(int i = 1; i <= 10; i++)
        {
            mapLayout.get(i).setLock(true);
            keyList.put(i, new Key(mapLayout.get(i),1));
        }


        //generates the two players in the top 2 rooms
        playerList.put(1, new Player(mapLayout.get(rand.nextInt(10)))); // playerList.put(1, new Player(0,8));
        playerList.put(2, new Player(mapLayout.get(rand.nextInt(10)))); // playerList.put(1, new Player(0,7));
    }
    
    
    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT ||
            e.getKeyCode() == KeyEvent.VK_D) {
            System.out.println("Right key Released");
            
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT ||
            e.getKeyCode() == KeyEvent.VK_A) {
            System.out.println("Left key Released");
            
        }
        if (e.getKeyCode() == KeyEvent.VK_KP_UP ||
            e.getKeyCode() == KeyEvent.VK_W) {
            System.out.println("Up key pressed");
            
        }
        if (e.getKeyCode() == KeyEvent.VK_KP_DOWN ||
            e.getKeyCode() == KeyEvent.VK_S) {
            System.out.println("Down key pressed");
            
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT ||
            e.getKeyCode() == KeyEvent.VK_D) {
            System.out.println("Right key Released");
            
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT ||
            e.getKeyCode() == KeyEvent.VK_A) {
            System.out.println("Left key Released");
            
        }
        if (e.getKeyCode() == KeyEvent.VK_KP_UP ||
            e.getKeyCode() == KeyEvent.VK_W) {
            System.out.println("Up key pressed");
            
        }
        if (e.getKeyCode() == KeyEvent.VK_KP_DOWN ||
            e.getKeyCode() == KeyEvent.VK_S) {
            System.out.println("Down key pressed");
            
        }
    }
}

/*
    Main Game Logic Controller, does all of the heavy lifting and works alongside the MainGameController to display things in the GUI and handle player movement(s).

    The main features are a special key, and two hashmaps for the players and rooms within this game-like maze.

    It also stores a required width/height (number of columns/rows ) to enable building within a gridpane


 */


import javafx.geometry.Point2D;
import java.util.HashMap;
import java.util.Random;

public class GameLogic{
    //HashMap of all rooms within the game
    private HashMap<Point2D, Room> roomList;

    //The key that opens the game-winning room
    private Key key;

    //HashMap of all the players in the game
    private HashMap<Integer, Player> playerList;

    //Max values for the game
    private int maxPlayers;
    private int gridRows;
    private int gridColumns;

    //Trap Chance and the unused numTraps to assist with spawning of traps
    private static final int trapChance = 35;
    private static final int numTraps = 20;

    /**
     *  NO args constructor
     */
    public GameLogic(){
        roomList = new HashMap<>();
        playerList = new HashMap<>();
        mapInitializing(-1, -1);
        maxPlayers = 4;
        gridColumns = 9;
        gridRows = 9;
    }

    /**
     *
     * @param maxPlayers max players in the game
     */
    public GameLogic(int maxPlayers) {
        roomList = new HashMap<>();
        playerList = new HashMap<Integer, Player> ();
        mapInitializing(-1, -1);
        this.maxPlayers = maxPlayers;
        gridColumns = 9;
        gridRows = 9;
    }

    /**
     *
     * @param maxPlayers max players in the game
     * @param gridColumns max columns in the game
     * @param gridRows max rows in the game
     */
    public GameLogic(int maxPlayers, int gridColumns, int gridRows) {
        key = null;
        playerList = new HashMap<Integer, Player> ();
        roomList = new HashMap<>();
        mapInitializing(-1, -1);
        this.maxPlayers = maxPlayers;
        this.gridColumns = gridColumns;
        this.gridRows = gridRows;
    }

    /**


    /**
     * method allows there to be a set amount of both
     * players and trapped rooms at the initialization of the
     * game when both parameters are -1
     * otherwise both the number of players and trapped rooms may be changed
     * or only one may be changed
     *
     * -Justin Lamberson
     *
     * Darragh O'Halloran
     * @param players number of players to initialize
     * @param trappedRooms unused but would set the number of trapped Rooms to cap it at something
     */
    public void mapInitializing(int players, int trappedRooms){
        //method assumes the map generated is a 9 x 9
        Random rand = new Random();

        int roomNumber = 1;

        int traps = 0;
        for(int y = 0 ; y < 9; y++)
        {
            for(int x = 0 ; x < 9; x++)
            {
                //~ trapChance % to spawn a trap
                if(rand.nextInt(100) <= trapChance){
                    //System.out.println("Set Trap!");
                    roomList.put(new Point2D(x, y), new Room(false, roomNumber, true, x, y));           //We make a new room with a trap
                    traps++;
                    roomNumber++;
                } else if (x == 8 && y == 8){
                    roomList.put(new Point2D(x, y), new FinalRoom(roomNumber,x, y));                                    //The final room is special. It ends the game
                    roomNumber++;
                }
                else
                {
                    roomList.put(new Point2D(x, y), new Room(false, roomNumber, false, x, y));          //Otherwise we leave it harmless
                    roomNumber++;
                }
            }
        }

        int x = rand.nextInt(8);
        int y = rand.nextInt(7);

        //generates final key =
        key = new Key(roomList.get(new Point2D(8,8)), 1, x, y);
        roomList.get(new Point2D(x,y)).setKey(key);




        //generates the number of players specified by the variable players
        //generates the players at the top left of the map (0,0)

        //-1 is default value, so we check to a void issues
        if(players == -1){
            for(int i = 0; i < 4; i++){
                playerList.put(i+1, new Player(roomList.get(new Point2D(0, 0)), 0 ,0, i+1));
                roomList.get(new Point2D(0, 0)).playerEntry(playerList.get(i+1));
            }
        }
        else{
            for(int i = 0; i < players; i++){
                playerList.put(i+1, new Player(roomList.get(new Point2D(0, 0)), 0 ,0, i+1));
                roomList.get(new Point2D(0, 0)).playerEntry(playerList.get(i+1));
            }
        }

    }
    /**
        By Svetozar Draganitchki
        method that checks if player can enter room
    */
    public boolean canEnter(int roomX, int roomY,Player p){
        if((roomList.get(new Point2D(roomX, roomY)).isLocked())){
            return hasKey(roomX, roomY, p);           //Simplified this logic - DO
        }
        return true;
    }

    /**
     * Darragh O'Halloran
     * Allows a player to move into a room, or whisks them away to the underworld if they died due to their move
     *
     * @param player    The player which is moving
     * @return  whether or not the player encountered a trap
     */
    public boolean playerMoves(Player player){
        boolean trap;
        Room newRoom = roomList.get(new Point2D(player.getX(), player.getY()));
        trap = false;

        //If the trap kills the player we send it to the underworld
        if(player.getHealthPool() < 0)
            player.playerDead();

        //Otherwise we let the move onto the next room
        else{
            trap = newRoom.playerEntry(player);
            player.setCurrentRoom(newRoom);
        }
        return trap;
    }
    
    /**
        By Svetozar Draganitchki
        checks if a player has the matching key to a room
    */
    public boolean hasKey(int roomX, int roomY, Player p){
        if((roomList.get(new Point2D(roomX, roomY)).getDoorID() == p.getKey().getMyRoom().getDoorID())){
            return true;
        } else {
            return false;
        }
    }

    /**
     * This checks to see if the move the player made is allowed or not. If it's not, it reverts their coordinates back to their
     * previous position
     *
     * Darragh O'Halloran
     * checks that a player has not gone out of bounds
     * @param p is the player referenced
     * @param flag which coordinate plane we're moving on 1 = y 0 = x
     * @param oldPoint the oldPoint, X or Y based on flag's value
     */
    public boolean checkMove(Player p, int flag, int oldPoint){
        boolean fairMove = true;
        int x = p.getX();

        //Checking for out of bounds
        int y = p.getY();
        if(x < 0){                          //X Bounds
            p.setX(0);
            fairMove = false;
        } else if (x >= gridColumns){
            p.setX(gridColumns-1);
            fairMove = false;
        }

        if(y < 0){                          //Y bounds
            p.setY(0);
            fairMove = false;
        } else if(y >= gridRows){
            p.setY(gridRows-1);
            fairMove = false;
        }


        //Checking to see player tried moving before we notice they're dead, or trying to move into the last room without a key
        Room roomToMove = (roomList.get(new Point2D(p.getX(), p.getY())));
        if(p.getHealthPool() < 0 || roomToMove.getClass() == FinalRoom.class && p.getKey() == null){
            if(flag == 0){
                p.setX(oldPoint);
            }
            if(flag == 1){
                p.setY(oldPoint);
            }
            return false;
        }

        //System.out.println(p.getKey() == null);
        return fairMove;
    }

    /*
        Getters and Setters
     */
    public Player getPlayer(int playerID){
        return playerList.get(playerID);
    }
    
    public Key getKey(){
        return key;
    }

    public Room getRoom(int x, int y){
        return (roomList.get(new Point2D(x, y)));
    }

    public HashMap<Point2D, Room> getRoomList() {
        return roomList;
    }

    public void setRoomList(HashMap<Point2D, Room> roomList) {
        this.roomList = roomList;
    }

    public void setKey(Key newKey){
        key = newKey;
    }


    public HashMap<Integer, Player> getPlayerList() {
        return playerList;
    }

    public void setPlayerList(HashMap<Integer, Player> playerList) {
        this.playerList = playerList;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public static int getTrapChance() {
        return trapChance;
    }

    public int getGridRows() {
        return gridRows;
    }

    public void setGridRows(int gridRows) {
        this.gridRows = gridRows;
    }

    public int getGridColumns() {
        return gridColumns;
    }

    public void setGridColumns(int gridColumns) {
        this.gridColumns = gridColumns;
    }
}

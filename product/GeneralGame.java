package sprint3.product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

//import sprint3.product.Board.Cell;
//import sprint3.product.Board.GameState;

public class GeneralGame extends Board {
    private List<SOSEvent> foundSOSEvents;  // Tracks all SOS events found during game
    private Map<SOSEvent, Character> sosEventPlayers = new HashMap<>();  // Associates each SOS event w/ player that made it
    private Cell[][] grid;   
    private GameState currentGameState;  
    private int boardSize;       
    private int blueScore;        
    private int redScore;        
    private char currentPlayer;   // 'B' for Blue player, 'R' for Red player
    
    // Constructor for default 3x3 game board
    public GeneralGame() {
    	this.grid = new Cell[3][3];
    	this.boardSize = 3;
    	this.foundSOSEvents = new ArrayList<>();
    	foundSOSEvents.clear();
        initializeBoard(); 
    }

    // Constructor accepting custom game board size
    public GeneralGame(int size) {
    	this.grid = new Cell[size][size];
    	if (size >= 3 && size <= 20) { // sets upper bound to 20x20 to match spinner
    		this.boardSize = size;
    	}else
    		this.boardSize = 3; //invalid size resets to default
    	this.foundSOSEvents = new ArrayList<>();
    	foundSOSEvents.clear();
        initializeBoard(); 
    }

    // Setup game board for new game
    public void initializeBoard() {
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                grid[i][j] = Cell.EMPTY;
            }
        }
        currentGameState = GameState.PLAYING; // Reset game state & scores
        currentPlayer = 'B';	// Blue starts game
        blueScore = redScore = 0;    // Reset both player's scores
    }    
    
    public Cell[][] getBoard() {
    	return grid;
    }

    // Set new size for game board and reinitialize
    public void setBoardSize(int size) {
    	this.boardSize = size;
    	this.grid = new Cell[size][size];
    	foundSOSEvents.clear();   // Reset list of found SOS's for new game
        initializeBoard();
    }   
    
    public int getBoardSize() {
    	return boardSize;
    }
     
    public void setCurrentPlayer(char player) {
        this.currentPlayer = player;
    }

    public char getCurrentPlayer() {
        return currentPlayer;
    }
    
    public GameState getCurrentGameState() {
	return currentGameState;
    }
    
    public Cell getCell(int row, int column) {
        if (row >= 0 && row < boardSize && column >= 0 && column < boardSize)
            return grid[row][column];
        else
            return Cell.EMPTY;
    }

    public Cell getSymbol(int row, int column) {
        return grid[row][column];
    }
    
    public int getBlueScore() {
    	return blueScore;
    }
    
    public int getRedScore() {
    	return redScore;
    }
    
    // Resets board to start a new game
    @Override
    public void newGame() {
    	initializeBoard();
    }
    
    @Override
    public boolean makeMove(int row, int column, Cell cell) {
        // No move if game is already over
        if (currentGameState != GameState.PLAYING) {
            System.out.println("Game over");
            return false;
        }    
        // Check for valid move
        if (row < 0 || row >= boardSize || column < 0 || column >= boardSize || grid[row][column] != Cell.EMPTY) {
            System.out.println("Invalid move");
            return false; // Invalid move
        }
        grid[row][column] = cell;  // Place the cell

        if (hasSOS()) {
            System.out.println(currentPlayer + " made an SOS");            
        } 
        else {            
            changeTurn();  // If no SOS is made, switch the player
        }

        printScores();  
        printList();    

        updateGameState(currentPlayer);        
        return true;
    }
    
    @Override
    public void changeTurn() {
        currentPlayer = (currentPlayer == 'B') ? 'R' : 'B';
        System.out.println("Switching players... Current Player is " + currentPlayer);
    }

    private void printScores() {  // Print current scores for both players
        System.out.println("Blue Score: " + blueScore);
        System.out.println("Red Score: " + redScore);
    }

    public void printList() {  // Print list of SOS events found
        foundSOSEvents.forEach(event -> System.out.println(event));
    }
       
    public void countSOS() {
    	if (currentPlayer == 'B') {
    		blueScore++;
    	}
    	else {
    		redScore++;
    	}
    }   
    
    public void updateGameState(char turn) {
        boolean boardFull = isFull();

        if (boardFull) {
            if (blueScore > redScore) {  // Blue wins
                currentGameState = GameState.BLUE_WON;
                logWinner("Blue");
            } 
            else if (redScore > blueScore) {   // Red wins
                currentGameState = GameState.RED_WON;
                logWinner("Red");
            } 
            else {                            // Tie game
                currentGameState = GameState.DRAW;
                logDraw();
            }
        }
    }

    private void logWinner(String winner) {  
        System.out.println(winner + " WINS");
    }

    private void logDraw() {               
        System.out.println("Tie Game");
    }
	
    public boolean isFull() {
    	for (int row = 0; row < boardSize; row++) {
            for (int column = 0; column < boardSize; column++) {
                if (grid[row][column] == Cell.EMPTY) {   
                    return false;	// Board is not full because an empty cell was found
                }
            }
    	}
    	return true;  // No empty cells are found, board is full
    }
    
    private boolean eventInList(SOSEvent event, List<SOSEvent> eventList) {
        for (SOSEvent existingEvent : eventList) {
            if (existingEvent.equals(event)) {
                return true; 
            }
        }
        return false; 
    }
    
    public boolean hasSOS() {   // Checks board for any SOS events in all directions
        
    	Cell[] symbols = {Cell.S, Cell.O, Cell.S}; 
    	int[][] directions = {{0, 1}, {1, 0}, {1, 1}, {1, -1}}; // Directions to check
        boolean sosFound = false;     

        for (int row = 0; row < boardSize; row++) {  
            for (int column = 0; column < boardSize; column++) {
                for (int[] dir : directions) {   // Check each direction from current cell
                    if (checkAndScoreSOS(row, column, dir[0], dir[1], symbols)) {
                        sosFound = true;   // SOS event found
                    }
                }
            }
        }
        return sosFound;   // True if at least one SOS event is found
    }

    private boolean checkAndScoreSOS(int rowStart, int columnStart, int rowDirection, int columnDirection, Cell[] symbols) {
        int endRow = rowStart + 2 * rowDirection;  // Calculate end row of event
        int endColumn = columnStart + 2 * columnDirection;  // Calculate end column of event

        if (endRow < 0 || endRow >= boardSize || endColumn < 0 || endColumn >= boardSize) {
            return false;  // Checks the SOS stays in board limits
        }
        if (grid[rowStart][columnStart] == symbols[0] &&
            grid[rowStart + rowDirection][columnStart + columnDirection] == symbols[1] &&
            grid[endRow][endColumn] == symbols[2]) {  // Check for SOS starting from specific cell
            
        	// If SOS found, create a new event that includes starting cell, symbol ('S') and direction
            SOSEvent newEvent = new SOSEvent(symbols[0], rowStart, columnStart, getDirectionString(rowDirection, columnDirection));

            if (!eventInList(newEvent, foundSOSEvents)) { 
                foundSOSEvents.add(newEvent);  
                sosEventPlayers.put(newEvent, currentPlayer);    
                countSOS();
                return true;  
            }
        }
        return false;  // No SOS found
    }
    
    // Convert numerical directions into strings describing the event's path for easier tracking
    private String getDirectionString(int rowDirection, int columnDirection) {
        if (rowDirection == 0) return "row";  // Horizontal event
        if (columnDirection == 0) return "column";  // Vertical event
        return rowDirection == 1 ? (columnDirection == 1 ? "diagTlBr" : "diagTrBl") : "";  // Diagonal events
    }
    
    public class SOSEvent {  // Event details are set when created and can't be changed later
        private final Cell cell;
        private final int row;
        private final int column;
        private final String direction;
     
        public SOSEvent(Cell cell, int row, int column, String direction) {
            this.cell = cell;  // The type of cell (S or O) that starts SOS event
            this.row = row;    // The row index where SOS starts
            this.column = column;    // The column index where SOS starts
            this.direction = direction;  // The direction of SOS event (horizontal, vertical, diagonal Tlbr & Trbl
        }

        public Cell getCell() {
            return cell;
        }

        public int getRow() {
            return row;
        }

        public int getColumn() {
            return column;
        }

        public String getDirection() {
            return direction;
        }

        @Override
        public String toString() { // Change to string for testing
            return String.format("Cell: %s, Row: %d, Column: %d, Direction: %s", cell, row, column, direction);
        }

        @Override
        public boolean equals(Object event) {  // Checks if event is equal to another event
            if (this == event) return true;
            if (event == null || getClass() != event.getClass()) return false;
            SOSEvent otherEvent = (SOSEvent) event;
            return cell == otherEvent.cell && row == otherEvent.row && column == otherEvent.column && Objects.equals(direction, otherEvent.direction);
        }

        @Override
        public int hashCode() {   // Create hash code for this SOS event
            return Objects.hash(cell, row, column, direction);
        }
    }
}

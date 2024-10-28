package sprint3.test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sprint3.product.Board;
import sprint3.product.SimpleGame;

class SimpleGameTest {

    private SimpleGame game;

    @BeforeEach
    void setUp() {     // Initialized before each test
        game = new SimpleGame(3);
    }
    
    @Test
    void testInvalidBoardSize() {
    	game = new SimpleGame(100); //invalid size
    	assertFalse(game.getBoardSize() == 100);
    	assertEquals(game.getBoardSize(), 3);
    	game = new SimpleGame(8);
    	assertEquals(game.getBoardSize(), 8);
    }
    
    @Test
    public void testSetAndGetGameMode() {
        // Default game mode is simple (true)
        assertTrue(game.getGameMode());

        // Set game mode to general (false)
        game.setGameMode(false);
        assertFalse(game.getGameMode());

        // Set game mode back to simple (true)
        game.setGameMode(true);
        assertTrue(game.getGameMode());
    }

    @Test
    void testMakeValidMove() {
        assertTrue(game.makeMove(0, 0, Board.Cell.S));  // Return true when valid move made
        assertEquals(Board.Cell.S, game.getSymbol(0, 0));   // Cell has the symbol placed by the move
        assertEquals('R', game.getCurrentPlayer()); // Player changes from 'B' to 'R'
    }

    @Test
    void testMakeInvalidMove() {
        game.makeMove(0, 0, Board.Cell.S); // First player makes a valid move
        assertFalse(game.makeMove(0, 0, Board.Cell.O));   // Invalid move on full cell
    }

    @Test
    void testBoardIsFullWithoutSOS() {
    	simulateNoSOSEvents();   // Fill the board without forming an SOS
        assertTrue(game.isFull());  // Board should be full
        
    }

    @Test
    void testDrawGame() {
    	simulateNoSOSEvents();        
        assertEquals(Board.GameState.DRAW, game.getCurrentGameState()); // Game should end in tie
    }

    @Test
    void testBlueWins() {
        simulateBlueWinSOS();
        assertEquals(Board.GameState.BLUE_WON, game.getCurrentGameState());
    }
    
    @Test
    void testRedWins() {
        simulateRedWinSOS();
        assertEquals(Board.GameState.RED_WON, game.getCurrentGameState());
    }

    // Helper methods for scenarios
    private void simulateNoSOSEvents() {
        game = new SimpleGame(3);     // Reset to a 3x3 board for this specific test
        game.makeMove(0, 0, Board.Cell.S);
        game.makeMove(0, 1, Board.Cell.S);
        game.makeMove(0, 2, Board.Cell.S);
        game.makeMove(1, 0, Board.Cell.S);
        game.makeMove(1, 1, Board.Cell.S);
        game.makeMove(1, 2, Board.Cell.S);
        game.makeMove(2, 0, Board.Cell.S);
        game.makeMove(2, 1, Board.Cell.S);
        game.makeMove(2, 2, Board.Cell.S);
    }

    private void simulateBlueWinSOS() {
        game.makeMove(0, 0, Board.Cell.S); // B
        game.makeMove(1, 0, Board.Cell.O); // R
        game.makeMove(2, 0, Board.Cell.S); // B SOS WIN
        assertEquals(Board.GameState.BLUE_WON, game.getCurrentGameState());
    }
    
    private void simulateRedWinSOS() {
        game.makeMove(0, 0, Board.Cell.S); // B
        game.makeMove(1, 0, Board.Cell.O); // R
        game.makeMove(1, 2, Board.Cell.S); // B
        game.makeMove(2, 0, Board.Cell.S); // R SOS WIN 
        assertEquals(Board.GameState.RED_WON, game.getCurrentGameState());
    }
    
    // CHATGPT TEST
    @Test
    void testMakeMoveOutsideBoardBounds() {
        // Attempt to make a move outside the board dimensions
        assertFalse(game.makeMove(-1, 0, Board.Cell.S)); // Negative row index
        assertFalse(game.makeMove(0, 3, Board.Cell.S)); // Row index exceeds board size
        assertFalse(game.makeMove(3, 0, Board.Cell.S)); // Row index exceeds board size
        assertFalse(game.makeMove(0, -1, Board.Cell.S)); // Negative column index
        assertFalse(game.makeMove(0, 3, Board.Cell.S)); // Column index exceeds board size
    }
}
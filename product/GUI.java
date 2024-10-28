package sprint3.product;

import sprint3.product.Board.Cell;

import sprint3.product.Board.GameState;
import sprint3.product.GeneralGame.SOSEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import javax.swing.JTextField;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.JButton;
import java.awt.Cursor;

@SuppressWarnings("serial")
public class GUI extends JFrame {
	
	public int CELL_SIZE;  
    public int CELL_PADDING;
    public int SYMBOL_SIZE;
    public static final int SYMBOL_STROKE_WIDTH = 8;
    public char currentTurn;
    Cell currentPlayerSymbol;
    
    private int CANVAS_WIDTH;
    private int CANVAS_HEIGHT;
    
    private GameBoardCanvas gameBoardCanvas; 
    private JTextField currentTurnText;
    private JTextField scoreText;
	
    private JRadioButton rdbtnBlueS;
    private JRadioButton rdbtnBlueO;
    private ButtonGroup B; // For Blue Player’s radio buttons 

    private JRadioButton rdbtnRedS;
    private JRadioButton rdbtnRedO;
    private ButtonGroup R; // For Red Player’s radio buttons
    
    private ButtonGroup G; // For Gamemode
    private JSpinner spinBoardSize;
    
    private SimpleGame simpleGame; 
    private GeneralGame generalGame; 
    private SOSEvent sos;
    private Board board; 
    private int boardSize;
    
    public GUI() {
    	getContentPane().setBackground(Color.WHITE);
        setBackground(Color.WHITE);
        //this.board = board;
        setContentPane();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setTitle("SOS GAME");
        setVisible(true);
    	
    	// Initialize game objects
    	generalGame = new GeneralGame();
    	simpleGame = new SimpleGame();
    	board = simpleGame;	
    }
	
    public Board getBoard() {
    	return board;
    }
    
    public SOSEvent getEvent() {
    	return sos;
    }
	
    // Setup content pane with game components
    protected void setContentPane(){	
		
    	gameBoardCanvas = new GameBoardCanvas();
        gameBoardCanvas.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
        Border borderGrid = BorderFactory.createLineBorder(Color.BLACK, 2);
        gameBoardCanvas.setBorder(borderGrid);
        
        JPanel topPanel = new JPanel();
    	topPanel.setBackground(Color.WHITE);
    	JPanel rightPanel = new JPanel();
    	rightPanel.setBackground(Color.WHITE);
    	JPanel leftPanel = new JPanel();
    	leftPanel.setBackground(Color.WHITE);
    	JPanel bottomPanel = new JPanel();
    	bottomPanel.setBackground(Color.WHITE);
        
        topPanel.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        
        //top panel attributes
    	JLabel lblTitle = new JLabel("SOS");
    	lblTitle.setBackground(Color.WHITE);
    	JLabel lblBoardInput = new JLabel("Board Size: ");
    	lblBoardInput.setBackground(Color.WHITE);
    	
    	//BoardSize init:3 min:3 max:20 step:1
        spinBoardSize = new JSpinner(new SpinnerNumberModel(3, 3, 20, 1));
        spinBoardSize.addChangeListener(e -> {
        	int sizeSelected = (int) spinBoardSize.getValue();
        	board.setBoardSize(sizeSelected); // update board size
        	board.newGame();
            gameBoardCanvas.repaint();
        });
    	
    	// simple or general game mode option
        JLabel lblGameMode = new JLabel("Game Mode: ");
    	JRadioButton rdbtnSimpleGame = new JRadioButton("Simple Game", true);
    	rdbtnSimpleGame.setBackground(Color.WHITE);
    	JRadioButton rdbtnGeneralGame = new JRadioButton("General Game");
    	rdbtnGeneralGame.setBackground(Color.WHITE);
    	rdbtnSimpleGame.addActionListener(e -> {
			//gameMode = true;
			board = simpleGame;
			});
		rdbtnGeneralGame.addActionListener(e -> {
			//gameMode = false;
			board = generalGame;
			});
		
		//ButtonGroup for game mode
		G = new ButtonGroup();
		G.add(rdbtnSimpleGame);
		G.add(rdbtnGeneralGame);
		
		topPanel.add(rdbtnSimpleGame);
		topPanel.add(rdbtnGeneralGame);
		topPanel.add(lblTitle);
		topPanel.add(lblGameMode);
		topPanel.add(lblBoardInput);
		topPanel.add(spinBoardSize);
        
		//Right Panel Attributes - Red Player
		//RED PLAYER'S LABEL AND BUTTONS
		JLabel lblRed = new JLabel("Red Player");
		rdbtnRedS = new JRadioButton("S");
		rdbtnRedS.setActionCommand("S");
		rdbtnRedS.setBackground(Color.WHITE);
		
		rdbtnRedO = new JRadioButton("O");
		rdbtnRedO.setActionCommand("O");
		rdbtnRedO.setBackground(Color.WHITE);
		
		//ButtonGroup for Red
		R = new ButtonGroup();
		R.add(rdbtnRedS);
		R.add(rdbtnRedO);
		
		/*
		rdbtnRedS.addActionListener(e -> {
			redSymbol = 'S';
			});
		rdbtnRedO.addActionListener(e -> {
			redSymbol = 'O';
			});
		*/
		
		rightPanel.add(lblRed);
		rightPanel.add(rdbtnRedS);
		rightPanel.add(rdbtnRedO);
			    
		//Left Panel Attributes - Blue Player
		//Blue Player's label and buttons
		JLabel lblBlue = new JLabel("Blue Player");
		rdbtnBlueS = new JRadioButton("S");
		rdbtnBlueS.setActionCommand("S");
		rdbtnBlueS.setBackground(Color.WHITE);
		rdbtnBlueO = new JRadioButton("O");
		rdbtnBlueO.setActionCommand("O");
		rdbtnBlueO.setBackground(Color.WHITE);
		
		//ButtonGroup for Blue
		B = new ButtonGroup();
		B.add(rdbtnBlueS);
		B.add(rdbtnBlueO);
		
		/*
		rdbtnBlueS.addActionListener(e -> {
			blueSymbol = 'S';
			});
		rdbtnBlueO.addActionListener(e -> {
			blueSymbol = 'O';
			});
		*/
		leftPanel.add(lblBlue);
		leftPanel.add(rdbtnBlueS);
		leftPanel.add(rdbtnBlueO);
		
		//Bottom Panel attributes
		currentTurnText = new JTextField();
		currentTurnText.setBorder(null);
		currentTurnText.setBackground(Color.WHITE);
		currentTurnText.setColumns(16);
        
        scoreText = new JTextField();
        scoreText.setBorder(null);
        scoreText.setBackground(Color.WHITE);
        scoreText.setColumns(16);
        
        // NEW GAME BUTTON
        JButton btnNewGame = new JButton("New Game");
        btnNewGame.setToolTipText("Start New Game");
        btnNewGame.addActionListener(e -> {
            board.newGame();
            rdbtnBlueS.setSelected(true);
            rdbtnRedS.setSelected(true);
            rdbtnSimpleGame.setSelected(true);
            currentTurnText.setText(""); 
            scoreText.setText(""); 
            gameBoardCanvas.repaint(); 
        });
        
        bottomPanel.add(currentTurnText);
        bottomPanel.add(scoreText);
        bottomPanel.add(btnNewGame);
        
        //set up contentPane
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(topPanel, BorderLayout.NORTH);
        contentPane.add(bottomPanel, BorderLayout.SOUTH);
        contentPane.add(leftPanel, BorderLayout.WEST);
        contentPane.add(rightPanel, BorderLayout.EAST);
        contentPane.add(gameBoardCanvas, BorderLayout.CENTER);
        topPanel.setPreferredSize(new Dimension(100, 100));
        bottomPanel.setPreferredSize(new Dimension(100, 100));
        leftPanel.setPreferredSize(new Dimension(100, 100));
        rightPanel.setPreferredSize(new Dimension(100, 100));                 				
    }
	
    class GameBoardCanvas extends JPanel {
	    
	GameBoardCanvas() {	   
	    // Listen for mouse clicks to handle player moves
	    addMouseListener(new MouseAdapter() {
	        @Override
	        public void mouseClicked(MouseEvent e) {
	            handleMouseClick(e);
	        }
	    });
	}
	    
	// Process mouse click events on the game board
	private void handleMouseClick(MouseEvent e) {
	        
	    if (board.getCurrentGameState() == GameState.PLAYING) {
	        int rowSelected = e.getY() / CELL_SIZE;
	        int columnSelected = e.getX() / CELL_SIZE;
	            
	        if (board.getCell(rowSelected, columnSelected) == Cell.EMPTY) {
	                makeMove(rowSelected, columnSelected);
	        }
	    }
	}
	    
	// Make move based on the selected cell & current player
	private void makeMove(int row, int col) {
	        
	    char currentPlayer = board.getCurrentPlayer();
	    Cell playerSymbol = currentSymbol(currentPlayer);
	        
	    if (playerSymbol != null) {
	        board.makeMove(row, col, playerSymbol);
	        repaint();
	        handleGameResult();
	    }
	}
	    
	// Determines proper symbol based on current player's choice
	private Cell currentSymbol(char currentPlayer) {
	    ButtonModel selection = (currentPlayer == 'B') ? B.getSelection() : R.getSelection();
	    return (selection != null && selection.getActionCommand() != null) ? Cell.valueOf(selection.getActionCommand()) : null;
	}
	    
	@Override
	protected void paintComponent(Graphics g) {
	    super.paintComponent(g);
	    setBackground(Color.WHITE);
	        
	    int canvasSize = Math.min(getWidth(), getHeight());
	    CELL_SIZE = canvasSize / board.getBoardSize();
	    CELL_PADDING = CELL_SIZE / 6;
	    SYMBOL_SIZE = CELL_SIZE - CELL_PADDING * 2;
	        
	    drawGridLines(g);
	    drawBoard(g);
	    
	}
	    
	// Draws grid lines according to board size
	private void drawGridLines(Graphics g) {
	    g.setColor(Color.BLACK);
	    int gridSize = getSize().width / board.getBoardSize();
	        
	    for (int i = 1; i < board.getBoardSize(); i++) {
	        int pos = i * gridSize;
	        g.drawLine(pos, 0, pos, getSize().height);
	        g.drawLine(0, pos, getSize().width, pos);
	    }
	}
	    
	// Draws S and O symbols on the board
	private void drawBoard(Graphics g) {
	    Graphics2D g2d = (Graphics2D) g;
	    g2d.setStroke(new BasicStroke(SYMBOL_STROKE_WIDTH, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
	        
	    for (int row = 0; row < board.getBoardSize(); row++) {
	        for (int col = 0; col < board.getBoardSize(); col++) {
	            int x1 = col * CELL_SIZE + CELL_PADDING;
	            int y1 = row * CELL_SIZE + CELL_PADDING;
	            Cell cellValue = board.getCell(row, col);
	            if (cellValue == Cell.S || cellValue == Cell.O) {
	                drawSymbol(g2d, cellValue.name().charAt(0), x1, y1);
	                /*
	                if(board.hasSOS()) {
	                	board.getDirection();
	                	int x2 = x1 * 3;
	                	int y2 =  y1 * 3;
	                	g.drawLine(x1, y1, x2, y2);
	                	//drawWinLine(g2d,)
	                	*/
	                }
	            }
	        }
	    }
	}
	    
	// Draw a symbol at specific cell coordinates
	private void drawSymbol(Graphics2D g2d, char symbol, int x, int y) {
	    String letter = String.valueOf(symbol);
	        
	    Font font = new Font("Arial", Font.BOLD, CELL_SIZE > 30 ? SYMBOL_SIZE : 28);
	        
	    // Center the symbol within the cell.
	    int centerX = x + (CELL_SIZE - g2d.getFontMetrics(font).stringWidth(letter)) / 2;
	    int centerY = y + (CELL_SIZE + g2d.getFontMetrics(font).getHeight()) / 2 - g2d.getFontMetrics(font).getDescent();
	        
	    g2d.setFont(font);
	    g2d.drawString(letter, centerX, centerY);
	}
	/*
	// Draw winning lines
	private void drawWinLine(Graphics2D g2d, ) {	
		x1 = col
		y1 =
		x2 = 
		y2 =
		g.setColor((currentPlayer == 'B') ? Color.BLUE : Color.RED);
		g.drawLine(x1, y1, x2, y2);
	}*/
	    
	//Update GUI based on game's current state and scores
	public void handleGameResult() {
	    // Update the status message based on the game state
	    switch (board.getCurrentGameState()) {
	        case BLUE_WON:
	            currentTurnText.setText("Blue Wins!");
	            break;
	        case RED_WON:
	            currentTurnText.setText("Red Wins!");
	            break;
	        case DRAW:
	            currentTurnText.setText("Tie Game!");
	            break;
	        default:
	            // Show which player's turn it is during an ongoing game.
	            currentTurnText.setText("Play: " + (board.getCurrentPlayer() == 'B' ? "Blue" : "Red"));
	            break;
	        }	        
	        // Update scores for both players.
	        scoreText.setText(String.format("Blue Score: %d, Red Score: %d", board.getBlueScore(), board.getRedScore()));
	        repaint();
	}
	
	public static void main(String[] args) {
	    SwingUtilities.invokeLater(new Runnable() {
            	public void run() {
                    GUI gui = new GUI();
                    gui.setVisible(true);
                    gui.board.initializeBoard();
                }
	    });
	}
}
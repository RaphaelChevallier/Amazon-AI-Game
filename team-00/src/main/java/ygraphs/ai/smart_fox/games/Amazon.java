package ygraphs.ai.smart_fox.games; //to access boardgame since not public

import ygraphs.ai.smart_fox.GameMessage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Map;

/**
 * For testing and demo purposes only. An GUI Amazon client for human players
 * @author yong.gao@ubc.ca
 */
public class Amazon extends GamePlayer{
	private MinMaxTree MinMax;
	private GameClient gameClient;
	private JFrame guiFrame = null;
	private GameBoard board = null;
	private boolean gameStarted = false;
	public String userName = null;
	private GConstraints ourBoard = null;
	String self;
	String other;
	int turn;
	private GConstraints personalBoard = null;

	/**
	 * Constructor
	 * @param name
	 * @param passwd
	 */
	public Amazon(String name, String passwd){

		this.userName = name;
		setupGUI();

		COSC322Test(name, passwd);
		System.out.println(name + " just connected");
	}

	private void COSC322Test(String name, String passwd){
		gameClient = new GameClient(name, passwd, this);
	}

	@Override
	public void onLogin() {

		//once logged in, the gameClient will have  the names of available game rooms
		ArrayList<String> rooms = gameClient.getRoomList();
		this.gameClient.joinRoom(rooms.get(0));//this can be changed for different rooms
	}


	public boolean handleGameMessage(String messageType, Map<String, Object> msgDetails){

		if(messageType.equals(GameMessage.GAME_ACTION_START)){
			if(( msgDetails.get("player-white")).equals(this.userName())){
				System.out.println("Game State: " +  msgDetails.get("player-white"));
				self = "White Player: " + this.userName();
				other = "Black Player: " + msgDetails.get("player-black");
				turn++;
				guiFrame.setTitle("Turn: " + turn + " | Move: " + userName() + " | " + self + " | " + other);
				ourBoard = new GConstraints(true);
				System.out.println("Initial Board");
				ourBoard.printBoard();
				ourBoard.enemyPotentialAction();
				ourBoard.updateLegalQueenMoves();
				MinMax = new MinMaxTree(new TreeNodes(ourBoard));
				TreeNodes ourBestMove = MinMax.Move();
				Queen ourMove = ourBestMove.getQueen();
				Arrow ourArrow = ourBestMove.getArrow();
				ourBoard.enemyPotentialAction();
				ourBoard.updateLegalQueenMoves();
				System.out.println("\nOur Move: [" + translateRow(ourMove.row) + ", " + translateCol(ourMove.col) + "]");
				System.out.println("Our Arrow Shot: [" + translateRow(ourArrow.row) + ", " + translateCol(ourArrow.col) + "]\n");
				board.markPosition(translateRow(ourMove.row), translateCol(ourMove.col), translateRow(ourArrow.getRowPos()), translateCol(ourArrow.getColPos()),
						translateRow(ourMove.previousRow), translateCol(ourMove.previousCol), false);

				gameClient.sendMoveMessage(ourMove.combinedMove(translateRow(ourMove.previousRow), translateCol(ourMove.previousCol)),
						ourMove.combinedMove(translateRow(ourMove.row), translateCol(ourMove.col)),
						ourArrow.combinedMove(translateRow(ourArrow.getRowPos()), translateCol(ourArrow.getColPos())));
				ourBoard.printBoard();

			}
			else {
				self = "Black Player: " + this.userName();
				other = "White Player: " + msgDetails.get("player-white");
				ourBoard = new GConstraints(false);
				MinMax = new MinMaxTree(new TreeNodes(ourBoard));

			}

		}
		else if(messageType.equals(GameMessage.GAME_ACTION_MOVE)){

			try {
				handleOpponentMove(msgDetails);
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
		}
		return true;
	}

	//handle the event that the opponent makes a move.
	private void handleOpponentMove(Map<String, Object> msgDetails) throws CloneNotSupportedException{
		boolean gameOver = false;
		turn++;
		guiFrame.setTitle("Turn: " + turn + " | Move: " + userName() + " | " + self + " | " + other);
		System.out.println("\nOpponentMove: " + msgDetails.get(AmazonsGameMessage.Queen_POS_NEXT));
		System.out.println("Opponent Arrow Shot: " + msgDetails.get(AmazonsGameMessage.ARROW_POS) + "\n");
		ArrayList<Integer> qcurr = (ArrayList<Integer>) msgDetails.get(AmazonsGameMessage.QUEEN_POS_CURR);
		ArrayList<Integer> qnew = (ArrayList<Integer>) msgDetails.get(AmazonsGameMessage.Queen_POS_NEXT);
		ArrayList<Integer> arrow = (ArrayList<Integer>) msgDetails.get(AmazonsGameMessage.ARROW_POS);
		// Enemy move
		Queen enemyQueen = new Queen(convertRow(qnew.get(0)), convertCol(qnew.get(1)), true);
		enemyQueen.previousRow = convertRow(qcurr.get(0));
		enemyQueen.previousCol = convertCol(qcurr.get(1));
		Arrow enemyArrow = new Arrow(convertRow(arrow.get(0)), convertCol(arrow.get((1))));
		MinMax.rootMove(enemyQueen, enemyArrow);
		board.markPosition(qnew.get(0), qnew.get(1), arrow.get(0), arrow.get(1),
				qcurr.get(0), qcurr.get(1), true);
		ourBoard.enemyPotentialAction();
		ourBoard.updateLegalQueenMoves();
		ourBoard.printBoard();

		// Check if we're at a goal node
		gameOver = ourBoard.goalTest();

		if(gameOver) {
			System.out.println("\n THE GAME IS NOW OVER \n");
		}

		// Our move
		turn++;
		guiFrame.setTitle("Turn: " + turn + " | Move: " + userName() + " | " + self + " | " + other);
		TreeNodes ourBestMove = MinMax.Move();
		Queen ourMove = ourBestMove.getQueen();
		Arrow ourArrow = ourBestMove.getArrow();
		ourBoard.enemyPotentialAction();
		ourBoard.updateLegalQueenMoves();
		System.out.println("\nOur Move: [" + translateRow(ourMove.row) + ", " + translateCol(ourMove.col) + "]");
		board.markPosition(translateRow(ourMove.row), translateCol(ourMove.col), translateRow(ourArrow.getRowPos()), translateCol(ourArrow.getColPos()),
				translateRow(ourMove.previousRow), translateCol(ourMove.previousCol), false);
		System.out.println("Our Arrow Shot: [" + translateRow(ourArrow.row) + ", " + translateCol(ourArrow.col) + "]\n");
		gameClient.sendMoveMessage(ourMove.combinedMove(translateRow(ourMove.previousRow), translateCol(ourMove.previousCol)),
				ourMove.combinedMove(translateRow(ourMove.row), translateCol(ourMove.col)),
				ourArrow.combinedMove(translateRow(ourArrow.getRowPos()), translateCol(ourArrow.getColPos())));
		ourBoard.printBoard();
		gameOver = ourBoard.goalTest();

		if(gameOver) {
			System.out.println("\n THE GAME IS NOW OVER \n");
		}
	}

	private int convertRow(int row){
		return Math.abs(row - 10);	// formula to convert server's row coordinate system to our Board's coordinate system
	}

	private int convertCol(int col){
		return (col - 1);	      // formula to convert server's column coordinate system to our Board's coordinate system
	}

	private int translateCol(int col){
		return (col + 1);	      // formula to translate our Board's column coordinate system to the server's coordinate system
	}

	private int translateRow(int row){
		return Math.abs(10 - row);	      // formula to convert our Board's row coordinate system to the server's coordinate system
	}


	/**
	 * handle a move made by this player --- send the info to the server.
	 * @param x queen row index
	 * @param y queen col index
	 * @param arow arrow row index
	 * @param acol arrow col index
	 * @param qfr queen original row
	 * @param qfc queen original col
	 */
	public void playerMove(int x, int y, int arow, int acol, int qfr, int qfc){

		int[] qf = new int[2];
		qf[0] = qfr;
		qf[1] = qfc;

		int[] qn = new int[2];
		qn[0] = x;
		qn[1] = y;

		int[] ar = new int[2];
		ar[0] = arow;
		ar[1] = acol;

		//To send a move message, call this method with the required data
		this.gameClient.sendMoveMessage(qf, qn, ar);

	}


	//set up the game board
	private void setupGUI(){
		guiFrame = new JFrame();

		guiFrame.setSize(800, 600);
		guiFrame.setTitle("Turn: " + turn + " | Move: | " + self + " | " + other);

		guiFrame.setLocation(200, 200);
		guiFrame.setVisible(true);
		guiFrame.repaint();
		guiFrame.setLayout(null);

		Container contentPane = guiFrame.getContentPane();
		contentPane.setLayout(new  BorderLayout());

		contentPane.add(Box.createVerticalGlue());

		board = createGameBoard();
		contentPane.add(board,  BorderLayout.CENTER);
	}

	private GameBoard createGameBoard(){
		return new GameBoard(this);
	}

	public boolean handleMessage(String msg) {
		System.out.println("Time Out ------ " + msg);
		return true;
	}


	@Override
	public String userName() {
		return userName;
	}


	/**
	 * The game board
	 *
	 * @author yongg
	 *
	 */
	public class GameBoard extends JPanel{

		private static final long serialVersionUID = 1L;
		private  int rows = 10;
		private  int cols = 10;

		int width = 500;
		int height = 500;
		int cellDim = width / 10;
		int offset = width / 20;

		int posX = -1;
		int posY = -1;

		int r = 0;
		int c = 0;


		Amazon game = null;
		private BoardGameModel gameModel = null;


		public GameBoard(Amazon game){
			this.game = game;
			gameModel = new BoardGameModel(this.rows + 1, this.cols + 1);

			//if(!game.isGamebot){
			addMouseListener(new  GameEventHandler());
			//}
			init();
		}


		public void init() {
			String tagB = BoardGameModel.POS_MARKED_BLACK;
			String tagW = BoardGameModel.POS_MARKED_WHITE;
			gameModel.gameBoard[7][1] = tagB;
			gameModel.gameBoard[7][10] = tagB;
			gameModel.gameBoard[10][4] = tagB;
			gameModel.gameBoard[10][7] = tagB;
			gameModel.gameBoard[1][4] = tagW;
			gameModel.gameBoard[1][7] = tagW;
			gameModel.gameBoard[4][1] = tagW;
			gameModel.gameBoard[4][10] = tagW;

		}


		/**
		 * repaint the part of the board
		 * @param qrow queen row index
		 * @param qcol queen col index
		 * @param arow arrow row index
		 * @param acol arrow col index
		 * @param qfr queen original row
		 * @param qfc queen original col
		 */
		public boolean markPosition(int qrow, int qcol, int arow, int acol,
									int qfr, int qfc, boolean  opponentMove){

			boolean valid = gameModel.positionMarked(qrow, qcol, arow, acol, qfr, qfc, opponentMove);
			if(valid) {
				repaint();
			}
			else {
				gameModel.positionMarked(qrow, qcol, arow, acol, qfr, qfc, opponentMove);
			}
			return valid;
		}

		// JCmoponent method
		protected void paintComponent(Graphics gg){
			Graphics g = (Graphics2D) gg;

			for(int i = 0; i < rows + 1; i++){
				g.drawLine(i * cellDim + offset, offset, i * cellDim + offset, rows * cellDim + offset);
				g.drawLine(offset, i*cellDim + offset, cols * cellDim + offset, i*cellDim + offset);
			}

			for(int r = 0; r < rows; r++){
				for(int c = 0; c < cols; c++){

					posX = c * cellDim + offset;
					posY = r * cellDim + offset;

					posY = (9 - r) * cellDim + offset;

					if(gameModel.gameBoard[r + 1][c + 1].equalsIgnoreCase(BoardGameModel.POS_AVAILABLE)){
						g.clearRect(posX + 1, posY + 1, 49, 49);
					}

					if(gameModel.gameBoard[r + 1][c + 1].equalsIgnoreCase(
							BoardGameModel.POS_MARKED_BLACK)){
						g.fillOval(posX, posY, 50, 50);
					}
					else if(gameModel.gameBoard[r + 1][c + 1].equalsIgnoreCase(
							BoardGameModel.POS_MARKED_ARROW)) {
						g.clearRect(posX + 1, posY + 1, 49, 49);
						g.drawLine(posX, posY, posX + 50, posY + 50);
						g.drawLine(posX, posY + 50, posX + 50, posY);
					}
					else if(gameModel.gameBoard[r + 1][c + 1].equalsIgnoreCase(BoardGameModel.POS_MARKED_WHITE)){
						g.drawOval(posX, posY, 50, 50);
					}
				}
			}

		}//method

		//JComponent method
		public Dimension getPreferredSize() {
			return new Dimension(600,800);
		}

		/**
		 * Handle mouse events
		 *
		 * @author yongg
		 */
		public class GameEventHandler extends MouseAdapter{

			int counter = 0;

			int qrow = 0;
			int qcol = 0;

			int qfr = 0;
			int qfc = 0;

			int arow = 0;
			int acol = 0;

			public void mousePressed(MouseEvent e) {

				if(!gameStarted){
					//return;
				}

				int x = e.getX();
				int y = e.getY();


				if(((x - offset) < -5) || ((y - offset) < -5)){
					return;
				}

				int row = (y - offset) / cellDim + 1;
				int col = (x - offset) / cellDim + 1;

				if(counter == 0){
					qfr = row;
					qfc = col;

					qfr = 11 - qfr;
					counter++;
				}
				else if(counter ==1){
					qrow = row;
					qcol = col;

					qrow = 11 - qrow;
					counter++;
				}
				else if (counter == 2){
					arow = row;
					acol = col;

					arow = 11 - arow;
					counter++;
				}

				if(counter == 3){
					counter = 0;
					boolean validMove = markPosition(qrow, qcol, arow, acol, qfr, qfc, false); // update itself
					if(validMove){
						game.playerMove(qrow, qcol, arow, acol, qfr, qfc); //to server
					}

					qrow = 0;
					qcol = 0;
					arow = 0;
					acol = 0;

				}
			}
		}//end of GameEventHandler

	}//end of GameBoard

	/**
	 * Constructor
	 * @param args
	 */
    public static void main(String[] args) {
		Amazon game01 = new Amazon("Group 6", "01");


    }
}

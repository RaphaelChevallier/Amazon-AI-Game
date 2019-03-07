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
	//this.gameClient.sendMoveMessage(qf, qn, ar);
	
	//Task: Replace the above with a timed task to wait for 10 seconds befor sending the move
	
    }
	  
    //set up the game board
    private void setupGUI(){
	    guiFrame = new JFrame();
		   
		guiFrame.setSize(800, 600);
		guiFrame.setTitle("Game of the Amazons (COSC 322, )" + this.userName());	
		
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
	return usrName;
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


	    Amazons game = null; 
	private BoardGameModel gameModel = null;

	    boolean playerAMove;

	    public GameBoard(Amazons game){
	    this.game = game;	       
	    gameModel = new BoardGameModel(this.rows + 1, this.cols + 1);

	    //if(!game.isGamebot){
		    addMouseListener(new  GameEventHandler());
	    //}
	    init(true);	
	    }


	    public void init(boolean isPlayerA){
	    String tagB = null;
	    String tagW = null;

	    tagB = BoardGameModel.POS_MARKED_BLACK;
	    tagW = BoardGameModel.POS_MARKED_WHITE;

	    gameModel.gameBoard[1][4] = tagW;
	    gameModel.gameBoard[1][7] = tagW;
	    gameModel.gameBoard[3][1] = tagW;
	    gameModel.gameBoard[3][10] = tagW;

	    gameModel.gameBoard[8][1] = tagB;
	    gameModel.gameBoard[8][10] = tagB;
	    gameModel.gameBoard[10][4] = tagB;
	    gameModel.gameBoard[10][7] = tagB;		
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

		    System.out.println(qrow + ", " + qcol + ", " + arow + ", " + acol 
				    + ", " + qfr + ", " + qfc);

		    boolean valid = gameModel.positionMarked(qrow, qcol, arow, acol, qfr, qfc, opponentMove);
		    repaint();						
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
		    return new Dimension(500,500);
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

    class BoardGameModel extends GameModel {

	public static final String POS_MARKED_BLACK = "black";
	public static final String POS_MARKED_WHITE = "white";
	public static final String POS_MARKED_ARROW = "arrow";
	public static final String POS_AVAILABLE = "available";
	
	String[][] gameBoard = null; 
	//int[][] posScores = null;
	
	
	/**
	 * @param rows
	 * @param columns
	 */
	public BoardGameModel(int rows, int columns){
		
		gameBoard = new String[rows + 1][columns + 1];
		//posScores = new int[rows][columns];
		for(int i = 1; i < rows + 1; i++){
			for(int j = 1; j < columns + 1; j++){
				gameBoard[i][j] = BoardGameModel.POS_AVAILABLE;
				//posScores[i][j] = 0;
			}
		}
	}
	
	
	public boolean positionMarked(int row, int column, int arow, int acol,
			 int qfr, int qfc, boolean opponentMove){
		boolean valid = true;
		
 
		
		if(row >= gameBoard.length | column >= gameBoard.length 
				 || row <= 0 || column <= 0){
			valid = false;
		}
		else if (!gameBoard[row][column].equalsIgnoreCase(BoardGameModel.POS_AVAILABLE)){
			valid = false;
		}
        
		if(valid){
			gameBoard[row][column] = gameBoard[qfr][qfc];		
			gameBoard[qfr][qfc] = BoardGameModel.POS_AVAILABLE;		
			gameBoard[arow][acol] = BoardGameModel.POS_MARKED_ARROW;
		}
		
		//System.out.println(this.toString());
		
		return valid;
	}	
	
	public String toString(){
      String b = null;

      for(int i = 1; i < 11; i++){
	      for(int j = 1; j< 11; j++){
		b = b + gameBoard[i][j] + " ";
	      }
	      b = b + "\n";
      }  	  
      return b;
    }	
    }
	
    class MyTimer extends TimerTask{
	GameClient gameClient = null;
	int[] qf;
	int[] qn;
	int[] ar;
	
	public MyTimer(GameClient gameClient, int[] qf, int[] qn, int[] ar){	
	    this.gameClient = gameClient;
	    this.qf = qf;
	    this.qn = qn;
	    this.ar = ar;
	}
		
	/**
	 * send the move 
	 */
	public void run() {
		gameClient.sendMoveMessage(qf, qn, ar);
	}
    }
    
    /**
     * Constructor 
     * @param args
     */
    public static void main(String[] args) { 
	Amazons game01 = new Amazons("player-01", "01");
	Amazons game02 = new Amazons("player-02", "02");
	//Amazons game = new Amazons(args[0], args[1]);		
    }
}//end of Amazon

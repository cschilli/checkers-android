package com.example.dylan.checkers;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.Toast;
import java.util.ArrayList;
import android.widget.TextView;

/*
 * ButtonBoard.java - Handles the graphical user interface for the game board
 *                  - Stores button ids for game board layout and maps them to the correct cell (x, y)
 *                  - Creates array of buttons that map each square on the game board
 *                  - Initializes the game piece images on the board (12 dark pieces and 12 light pieces)
 */
public class ButtonBoard extends AppCompatActivity {

    // Game board layout of the black squares by square ID
    // 0-63  --> black button squares, used for indexing      _ -->  red button squares, are not used in indexing
    // 32-34 --> initially empty black squares with no pieces
    //
    //	      0   1  2   3  4   5  6   7
    //
    //	 0    0   _  2   _  4   _  6   _
    //	 1    _   9  _  11  _  13  _  15
    //	 2    16  _  18  _  20  _  22 _
    //	 3    _  25  _  27  _  29  _  31
    //	 4    32  _  34  _  36  _  38  _
    //	 5    _  41  _  43  _  45  _  47
    //	 6    48  _  50  _  52  _  54
    //	 7    _  57  _  59  _  61  _  63

    // Add the buttons into an array by their specific id in integer form
    private final int[] buttons_id = {R.id.button0,  R.id.button2,  R.id.button4,  R.id.button6,
            R.id.button9,  R.id.button11, R.id.button13, R.id.button15,
            R.id.button16, R.id.button18, R.id.button20, R.id.button22,
            R.id.button25, R.id.button27, R.id.button29, R.id.button31,
            R.id.button32, R.id.button34, R.id.button36, R.id.button38,
            R.id.button41, R.id.button43, R.id.button45, R.id.button47,
            R.id.button48, R.id.button50, R.id.button52, R.id.button54,
            R.id.button57, R.id.button59, R.id.button61, R.id.button63};


    private static int BOARD_SIZE = 8;                                                  // board is 8x8, 64 spaces (buttons)
    private final Button[][] buttonIndexes = new Button[BOARD_SIZE][BOARD_SIZE];        // stores the Button objects with their indexes
    private Board board = new Board();
    private ArrayList<Cell> moves = new ArrayList<>();                                          // stores the possible moves for a piece
    int xCord2 = 0;
    int yCord2 = 0;
    int xCordCapturedPiece = 0;
    int yCordCapturedPiece = 0;
    int counter = 0;

    private enum GameStatus {DRAW, OVER, RUNNING};
    private GameStatus gameStatus;
    private Player player1 = new PlayerTUI(Piece.LIGHT);
    private Player player2 = new PlayerTUI(Piece.DARK);
    private Player currentPlayer;
    private boolean hasAnotherTurn = false;
    Cell possMoves;     // stores all of the possible moves for a piece


    /*
    * Creates the activity for the game board, then sets up game piece images on game board
    * @param Bundle savedInstanceState
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.board);
        fillButtonIndexArray(listener);             // fill the board with the correct indexes

        // If the load message was loaded, we load the game, otherwise a new game is created
        if(getIntent().getExtras() != null ) {
            board.LoadGameState(getApplicationContext());
        }

        this.currentPlayer = player1;
        updateTurnTracker();
        updateBoard(buttonIndexes, board);
        gameStatus = GameStatus.RUNNING;

    }


    public Player getCurrentPlayer(){
        return this.currentPlayer;
    }


    public void changeTurn(){
        if(this.currentPlayer.equals(player1)){
            this.currentPlayer = player2;
            updateTurnTracker();
        }
        else{
            this.currentPlayer = player1;
            updateTurnTracker();
        }
    }

    public void onFirstClick(int xCord, int yCord){
        // Draw the possible moves on the board
        int xPossMoves;  // stores X coord of possible moves
        int yPossMoves; // stores Y coord of possible moves

        // For all of the moves that a piece has, get the X and Y coord and color piece in
        for(int i = 0; i < moves.size(); i++){
            possMoves = moves.get(i);       // get first set of moves
            xPossMoves = possMoves.getX();  // get x coord of possible move
            yPossMoves = possMoves.getY();  // get y coord of possible move
            buttonIndexes[xPossMoves][yPossMoves].setBackgroundResource(R.drawable.possible_moves_image);   // color possible moves square
        }

        // If current player is light AND the piece selected is a light piece, player can ONLY move light pieces and can jump ONLY dark pieces
        if(currentPlayer.getColor().equals(Piece.LIGHT) && board.getCell(xCord, yCord).getPiece().getColor().equals(Piece.LIGHT)){
            buttonIndexes[xCord][yCord].setBackgroundResource(R.drawable.light_piece_pressed);  // fill selected light piece as pressed piece image
        }
        // If current player is dark AND the piece selected is a dark piece, player can ONLY move dark pieces and can jump ONLY light pieces
        if(currentPlayer.getColor().equals(Piece.DARK) && board.getCell(xCord, yCord).getPiece().getColor().equals(Piece.DARK)){
            buttonIndexes[xCord][yCord].setBackgroundResource(R.drawable.dark_piece_pressed);   // fill selected dark piece as pressed piece image
        }
        xCord2 = xCord; // stores coordinates of first click
        yCord2 = yCord; // stores coordinates or second click
        counter++;      // increment counter so user can click a target cell

    }

    /*
     * Creates listener to perform action when user clicks a game piece
     * When user clicks on a piece, gets the coordinates and stores into x and y variable
     */
    private View.OnClickListener listener = new OnClickListener() {
        @Override
        public void onClick(View v) {

                int tag = (Integer) v.getTag();
                int xCord = tag / 10;
                int yCord = tag % 10;


                currentPlayer = getCurrentPlayer();     // gets the current player

                // If piece exists AND color of piece matches players piece AND counter == 0, let the player take a turn
                if (board.getCell(xCord, yCord).containsPiece() && board.getCell(xCord, yCord).getPiece().getColor().equals(currentPlayer.getColor()) && counter == 0 && !hasAnotherTurn) {
                    moves = board.possibleMoves(xCord, yCord);  // stores possible moves for a cell

                    // If player has no possible moves AND is NOT on their second turn, cannot move piece so player must choose new piece
                    if (moves.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "No possible moves!", Toast.LENGTH_SHORT).show();
                        updateBoard(buttonIndexes, board);
                    }

                    // Else, we can move piece because we have possible moves
                    else {
                        onFirstClick(xCord, yCord);
                    }
                }

                // If after a player captures a move, they can ONLY move the piece that performed the capture
                else if (board.getCell(xCord, yCord).containsPiece() && board.getCell(xCord, yCord).getPiece().getColor().equals(currentPlayer.getColor()) && counter == 0 && hasAnotherTurn &&
                        board.getCell(xCordCapturedPiece, yCordCapturedPiece) == board.getCell(xCord, yCord)) {

                    moves = board.getCaptureMoves(xCord, yCord);  // stores possible capture moves for a cell

                    // If player has no possible moves AND is NOT on their second turn, cannot move piece so player must choose new piece
                    if (moves.isEmpty()) {
                        changeTurn();
                        System.out.println("Cannot capture, switch turns");
                    }

                    // Else, we can move piece because we have possible moves
                    else if (!moves.isEmpty()) {
                        onFirstClick(xCord, yCord);
                    }
                }

                // If the clicked destination cell IS empty AND player has possible moves AND if counter == 1, then user can move piece
                else if (!(board.getCell(xCord, yCord).containsPiece()) && moves.contains(board.getCell(xCord, yCord)) && counter == 1) {

                    // If user does a capture move, we want to allow them to click another piece
                    if (board.isCaptureMove(board.getCell(xCord2, yCord2), board.getCell(xCord, yCord))) {
                        System.out.println("User must make another move using the same piece!");
                        xCordCapturedPiece = xCord;
                        yCordCapturedPiece = yCord;
                        hasAnotherTurn = true;
                    }
                    // Capture move was not made, they moved to empty spot
                    else {
                        hasAnotherTurn = false;
                        gameStatus = getGameStatus();
                        changeTurn();
                    }

                    board.movePiece(xCord2, yCord2, xCord, yCord);
                    counter--;
                    updateBoard(buttonIndexes, board);

                    // If player has another turn, check to see if they have any future captures
                    if (hasAnotherTurn) {
                        moves = board.getCaptureMoves(xCord, yCord);    // stores the future capture moves of the cell

                        // If the piece that captured opponents piece has no capture moves, end turn
                        if (moves.isEmpty()) {
                            System.out.println("Can NOT make another move!");
                            hasAnotherTurn = false;
                            gameStatus = getGameStatus();
                            changeTurn();
                        }
                        // Else, we can go forward and let them capture another piece
                        else {
                            System.out.println("Can make another move!");
                            hasAnotherTurn = true;
                        }
                    }
                }

                // If player clicks on the same piece twice, we simply want to de-select it since they have not made a move yet
                else if (board.getCell(xCord, yCord) == board.getCell(xCord2, yCord2)) {
                    counter--;
                    updateBoard(buttonIndexes, board);
                }


        }
    };


    public void updateTurnTracker() {
        TextView p1 = (TextView) findViewById(R.id.playerOneTurn);
        TextView p2 = (TextView) findViewById(R.id.playerTwoTurn);

        if(currentPlayer.getColor().equals(Piece.LIGHT)){
            p1.setText("Player 1 Turn");
            p2.setText("");

        }
        else{
            p2.setText("Player 2 Turn");
            p1.setText("");
        }

    }


    /*
     * Fills the Button indexes array with each button object and asigns index using button tag
     * @param View.OnClickListener listener
     */
    public void fillButtonIndexArray(View.OnClickListener listener ){
        int index = 0;
        for(int i=0; i< 8; i++){
            for(int j=0; j<8; j++){
                if((i+j)%2 == 0){
                    buttonIndexes[i][j] = (Button) findViewById(buttons_id[index]);
                    index++;
                    buttonIndexes[i][j].setTag(i*10 +j);
                    buttonIndexes[i][j].setOnClickListener(listener);
                }
            }
        }
    }


    /*
     * Updates the game pieces on the UI Board according to Game.java Cell[][] array
     * @param Button[][] buttonIndexes, Board board
     */
    // TODO: Update game piece images when swapping instead of refreshing entire board
    public void updateBoard(Button[][] buttonIndexes, Board board){
        // Initially set all black squares to blank_square spaces, and is used to remove a piece (sets button to blank_square)
        for(int column = 0; column < 8; column+= 2){
            buttonIndexes[0][column].setBackgroundResource(R.drawable.blank_square);
            buttonIndexes[2][column].setBackgroundResource(R.drawable.blank_square);
            buttonIndexes[4][column].setBackgroundResource(R.drawable.blank_square);
            buttonIndexes[6][column].setBackgroundResource(R.drawable.blank_square);
        }
        // Initially set all black squares to blank_square spaces, and is used to remove a piece (sets button to blank_square)
        for(int column = 1; column < 8; column+=2){
            buttonIndexes[1][column].setBackgroundResource(R.drawable.blank_square);
            buttonIndexes[3][column].setBackgroundResource(R.drawable.blank_square);
            buttonIndexes[5][column].setBackgroundResource(R.drawable.blank_square);
            buttonIndexes[7][column].setBackgroundResource(R.drawable.blank_square);
        }

        // Places the pieces on the black squares according to location
        for(int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {

                // Fills the light pieces in on the board
                if ((board.getCell(i, j).containsPiece()) && (board.getCell(i, j).getPiece().getColor().equals(Piece.LIGHT))) {

                    //King light piece
                    if(board.getCell(i, j).getPiece().isKing()){
                        buttonIndexes[i][j].setBackgroundResource(R.drawable.king_red);
                    }
                    // No king
                    else {
                        buttonIndexes[i][j].setBackgroundResource(R.drawable.light_piece);
                    }
                    //System.out.println("CONTAINS LIGHT PIECE");
                }
                // Fills the dark pieces in on the board
                if ((board.getCell(i, j).containsPiece()) && (board.getCell(i, j).getPiece().getColor().equals(Piece.DARK))) {

                    // King dark piece
                    if(board.getCell(i, j).getPiece().isKing()){
                        buttonIndexes[i][j].setBackgroundResource(R.drawable.king_black);
                    }
                    // No king
                    else {
                        buttonIndexes[i][j].setBackgroundResource(R.drawable.dark_piece);
                    }

                    // System.out.println("CONTAINS DARK PIECE");
                }
            }
        }
    }


    //TODO: complete this method
    public void run(){
        while(this.gameStatus == GameStatus.RUNNING){

            boolean validMove = false;
            int[] givenMove= null;

            while(!validMove){
                givenMove = this.currentPlayer.getMove(this.board);
                validMove = checkIfValidMove(this.currentPlayer, givenMove);
                if(!validMove){
                    System.out.print("The move is not valid. Please try again. ");
                }
            }
            boolean capturingMove = this.board.isCaptureMove(givenMove);
            this.board.movePiece(givenMove);

            if(capturingMove){
                boolean hopAgain = (this.board.getCaptureMoves(givenMove[2], givenMove[3]).size() > 0);

                int[] srcCell = {givenMove[0], givenMove[1]};;
                int[] dstCell  = {givenMove[2], givenMove[2]};;
                //TODO make sure that the next move will have the same srccells
                while(hopAgain){
                    System.out.println("You just captured opponent piece and have 1 or more capturing moves. Please select one of the capturing move.");
                    srcCell = dstCell;
                    validMove = false;
                    while(!validMove){
                        dstCell = this.currentPlayer.getMoveDstCell(this.board);
                        validMove = checkIfValidMove(this.currentPlayer, srcCell, dstCell);
                        if(!validMove || !this.board.isCaptureMove(givenMove)){
                            validMove = false;
                            System.out.print("The move is not valid. Please select again. ");
                        }
                        else{
                            int[] move = {srcCell[0], srcCell[1], dstCell[0], dstCell[1]};
                            this.board.movePiece(move);
                            hopAgain = (this.board.getCaptureMoves(dstCell[0], dstCell[1]).size() > 0);
                        }
                    }
                }
            }
            this.gameStatus = getGameStatus();
            changeTurn();
        }


        if(this.gameStatus == GameStatus.DRAW){
            System.out.println("The game is draw.");
        }
        else if(hasMoves(this.player1)){
            System.out.println(this.player1.getColor() + " has won!");
        }
        else{
            System.out.println(this.player2.getColor() + "has won! ");
        }
    }


    public boolean checkIfValidMove(Player givenPlayer, int[] srcCell, int[] dstCell){
        int[] move = {srcCell[0], srcCell[1], dstCell[0], dstCell[1]};
        return checkIfValidMove(givenPlayer, move );
    }

    public boolean checkIfValidMove(Player givenPlayer, int[] givenMove){
        Cell srcCell = this.board.getCell(givenMove[0], givenMove[1]);
        Cell dstCell = this.board.getCell(givenMove[2], givenMove[3]);

        if(srcCell == null || dstCell == null || (srcCell.getPiece() == null)){
            return false;
        }

        else if(!srcCell.getPiece().getColor().equals(givenPlayer.getColor())){
            return false;
        }

        ArrayList<Cell> moves = this.board.possibleMoves(srcCell);
        for(Cell temp: moves){
            if(temp.getX() == dstCell.getX() && temp.getY() == dstCell.getY()){
                return true;
            }
        }
        return false;
    }


    public GameStatus getGameStatus(){
        boolean player1HasMove = hasMoves(this.player1);
        boolean player2HasMove = hasMoves(this.player2);

        if(!player1HasMove && !player2HasMove ){
            return GameStatus.DRAW;
        }

        else if((player1HasMove && !player2HasMove) || (player2HasMove && !player1HasMove)){
            return GameStatus.OVER;
        }

        return GameStatus.RUNNING;
    }





    public boolean hasMoves(Player givenPlayer){
        ArrayList<Piece> pieces = this.board.getPieces(givenPlayer.getColor());
        if(pieces.size() > 0){
            for(Piece piece: pieces){
                if(this.board.possibleMoves(piece).size() > 0){
                    return true;
                }
            }
        }
        return false;
    }






    /*
    * Adds Quick Menu at top-right corner with following options: Save, Load, Restart, Quit
    * @param Menu menu
    * @ret boolean
    */
    @Override
    public boolean onCreateOptionsMenu (Menu menu){
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.play_game_drop_down_menu, menu);//Menu Resource, Menu
        return true;
    }


    /*
     * Adds the following options: Save, Load, Restart, Quit to the Quick Menu with case on clicked
     * @param MenuItem menu
     * @ret boolean
     */
    @Override
    public boolean onOptionsItemSelected (MenuItem item){
        switch (item.getItemId()) {
            case R.id.saveGame:
                board.SaveGameState(getApplicationContext());
                Toast.makeText(getApplicationContext(), "Game Saved!", Toast.LENGTH_LONG).show();
                return true;
            case R.id.loadGame:
                board.LoadGameState(getApplicationContext());
                updateBoard(buttonIndexes, board);
                Toast.makeText(getApplicationContext(), "Game Loaded!", Toast.LENGTH_LONG).show();
                return true;
            case R.id.restartMatch:
                Intent intent = new Intent(this, ButtonBoard.class);
                startActivity(intent);
                Toast.makeText(getApplicationContext(), "Match Restarted!", Toast.LENGTH_LONG).show();
                return true;
            case R.id.quitMatch:
                Toast.makeText(getApplicationContext(), "Quitting Match!", Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}







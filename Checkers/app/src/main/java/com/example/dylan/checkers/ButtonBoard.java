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
import android.app.Dialog;
import android.widget.RadioButton;

/*
 * ButtonBoard.java - Handles the graphical user interface for the game board
 *                  - Stores button ids for game board layout and maps them to the correct cell (x, y)
 *                  - Creates array of buttons that map each square on the game board
 *                  - Initializes the game piece images on the board (12 dark pieces and 12 light pieces)
 */
public class ButtonBoard extends AppCompatActivity {

    private ArrayList<Cell> moves;
    private Player player1;
    private Player player2;
    private Player currentPlayer;
    private boolean hasAnotherTurn;
    private int xCordSrcPiece;          // stores the source x-coordinate of first piece clicked
    private int yCordSrcPiece;          // stores the source y-coordinates of first piece clicked
    private int xCordCapturingPiece;    // stores the new source x-coordinate of the first piece that captured an opponent piece
    private int yCordCapturingPiece;    // stores the new source y-coordinate of the first piece that captured an opponent piece
    Cell possMoves;                                 // stores all of the possible moves for a piece

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

    private final Button[][] buttonIndexes = new Button[8][8];        // stores the Button objects with their indexes
    private Board board = new Board();
<<<<<<< HEAD
    private ArrayList<Cell> moves = new ArrayList<>();                                          // stores the possible moves for a piece
    int xCord2 = 0;
    int yCord2 = 0;
    int xCordCapturedPiece = 0;
    int yCordCapturedPiece = 0;
    int counter = 0;
    int roundCounter = 0;

    private enum GameStatus {DRAW, OVER, RUNNING};
    private GameStatus gameStatus;
    private Player player1 = new PlayerTUI(Piece.LIGHT);
    private Player player2 = new PlayerTUI(Piece.DARK);
    private Player currentPlayer;
    private boolean hasAnotherTurn = false;
    Cell possMoves;     // stores all of the possible moves for a piece
=======
    private int counter = 0;
    int roundCounter = 0;

>>>>>>> 5a2e4f4c72d74a2133daf0df52c133d8f4d128dd


    /*
    * Creates the activity for the game board, then sets up game piece images on game board
    * @param Bundle savedInstanceState
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        Dialog dialog = new Dialog(this);
//        dialog.setContentView(R.layout.initial_playgame_menu);
//        dialog.setTitle("This is my custom dialog box");
//        dialog.setCancelable(true);
//        // there are a lot of settings, for dialog, check them all out!
//        // set up radiobutton
//        RadioButton rd1 = (RadioButton) dialog.findViewById(R.id.light_option);
//        RadioButton rd2 = (RadioButton) dialog.findViewById(R.id.dark_option);
//
//        // now that the dialog is set up, it's time to show it
//        dialog.show();

        setContentView(R.layout.board);

        // If user chooses to exit the game
        if (getIntent().getBooleanExtra("EXIT", false)) {
            this.finish();
        }
        // If the load message was loaded, we load the game, otherwise a new game is created
        if(getIntent().getBooleanExtra("LOAD", false)) {
            board.LoadGameState(getApplicationContext());
        }

        moves = new ArrayList<>();                  // init moves arraylist
        player1 = new PlayerTUI(Piece.LIGHT);       // init player 1
        player2 = new PlayerTUI(Piece.DARK);        // init player 2
        this.currentPlayer = player1;               // init current player
        fillButtonIndexArray(listener);
        updateTurnTracker();
        updateBoard(buttonIndexes, board);
<<<<<<< HEAD
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

            // If both players have pieces and game is currently running
            if(!board.getPieces(Piece.LIGHT).isEmpty() && !board.getPieces(Piece.DARK).isEmpty()){

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

        // If player who is light runs out of pieces, they lose
        if(board.getPieces(Piece.LIGHT).isEmpty() && !board.getPieces(Piece.DARK).isEmpty()) {
            System.out.println("Dark player wins! \nPlay Again or Quit");
        }
        // If player who is dark runs out of pieces, they lose
        else if(!board.getPieces(Piece.LIGHT).isEmpty() && board.getPieces(Piece.DARK).isEmpty()) {
            System.out.println("Light player wins! Play Again or Quit");
        }
        // If BOTH players each have 1 piece left AND the round is over 30, call it a draw
        else if(board.getPieces(Piece.LIGHT).size() == 1 && board.getPieces(Piece.DARK).size() == 1 && roundCounter > 40){
            System.out.println("Draw! Play Again or Quit");
        }

        }
    };


    public void updateTurnTracker() {
        TextView p1 = (TextView) findViewById(R.id.playerOneTurn);
        TextView p2 = (TextView) findViewById(R.id.playerTwoTurn);

        if(currentPlayer.getColor().equals(Piece.LIGHT)){
            p1.setText(player1.getColor() + "'s Turn");
            p2.setText("");

        }
        else{
            p2.setText(player2.getColor() + "'s Turn");
            p1.setText("");
        }

=======
>>>>>>> 5a2e4f4c72d74a2133daf0df52c133d8f4d128dd
    }


    /*
     * Fills the Button indexes array with each button object and asigns index using button tag
     * @param View.OnClickListener listener
     */
    public void fillButtonIndexArray(View.OnClickListener listener ){
        int index = 0;
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                if((i+j)%2 == 0){
                    buttonIndexes[i][j] = (Button) findViewById(buttons_id[index]);
                    index++;
                    buttonIndexes[i][j].setTag(i*10 + j);
                    buttonIndexes[i][j].setOnClickListener(listener);

                }
            }
        }
    }

<<<<<<< HEAD


=======
>>>>>>> 5a2e4f4c72d74a2133daf0df52c133d8f4d128dd
    /*
     * Updates the game pieces on the UI Board according to Game.java Cell[][] array
     * @param Button[][] buttonIndexes, Board board
     */
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
                        buttonIndexes[i][j].setBackgroundResource(R.drawable.light_king_piece);
                    }
                    // No king
                    else {
                        buttonIndexes[i][j].setBackgroundResource(R.drawable.light_piece);
                    }
                }
                // Fills the dark pieces in on the board
                if ((board.getCell(i, j).containsPiece()) && (board.getCell(i, j).getPiece().getColor().equals(Piece.DARK))) {
                    // King dark piece
                    if(board.getCell(i, j).getPiece().isKing()){
                        buttonIndexes[i][j].setBackgroundResource(R.drawable.dark_king_piece);
                    }
                    // No king
                    else {
                        buttonIndexes[i][j].setBackgroundResource(R.drawable.dark_piece);
                    }
                }
            }
        }
    }

    /*
     * When a piece moves to an empty cell, we want to update the pieces affected
     * @param int xCord - The x-coordinate of a piece after it has moved to an empty cell
     * @param int yCord - The y-coordinate of a piece after it has moved to an empty cell
     */
    public void updatePieces(int xCord, int yCord){

<<<<<<< HEAD
=======
        // For all of the possible moves colored in on the board, after a piece moves we want to remove them
        for (int i = 0; i < moves.size(); i++) {
            possMoves = moves.get(i);
            buttonIndexes[possMoves.getX()][possMoves.getY()].setBackgroundResource(R.drawable.blank_square);   // color possible moves blank
        }

        // If the piece is light
        if(board.getCell(xCord, yCord).getPiece().getColor() == Piece.LIGHT) {
            // If piece is light AND is king
            if (board.getCell(xCord, yCord).getPiece().isKing()) {
                buttonIndexes[xCord][yCord].setBackgroundResource(R.drawable.light_king_piece);
            }
            // If piece is light AND is not king
            else {
                buttonIndexes[xCord][yCord].setBackgroundResource(R.drawable.light_piece);
            }
        }
        // If the piece is dark
        else{
            // // If piece is dark AND is king
            if (board.getCell(xCord, yCord).getPiece().isKing()) {
                buttonIndexes[xCord][yCord].setBackgroundResource(R.drawable.dark_king_piece);
            }
            // If piece is dark AND is not king
            else {
                buttonIndexes[xCord][yCord].setBackgroundResource(R.drawable.dark_piece);
            }
        }
    }

    /*
     * When a piece jumps an opponent piece, we want to remove the piece jumped and update new piece graphic at its destination
     * @param int xCordSrc - The x-coordinate of a piece that will jump opponent piece
     * @param int yCordSrc - The y-coordinate of a piece that will jump opponent piece
     * @param int xCordDst - The new x-coordinate of a piece after it jumped an opponent piece
     * @param int yCordDst - The new y-coordinate of a piece after it jumped an opponent piece
     * @param Cell pieceCaptured - The piece that was captured
     */
    public void updatePieces(int xCordSrc, int yCordSrc, int xCordDst, int yCordDst, Cell pieceCaptured){

        // For all of the possible moves colored in on the board, after a piece jumps we want to remove them
        for (int i = 0; i < moves.size(); i++) {
            possMoves = moves.get(i);
            buttonIndexes[possMoves.getX()][possMoves.getY()].setBackgroundResource(R.drawable.blank_square);   // color possible moves blank
        }

        buttonIndexes[pieceCaptured.getX()][pieceCaptured.getY()].setBackgroundResource(R.drawable.blank_square);   // removes the captured piece by coloring it blank
        buttonIndexes[xCordSrc][yCordSrc].setBackgroundResource(R.drawable.blank_square);                           // color the old pieces cell blank

        // If the piece is light
        if(board.getCell(xCordDst, yCordDst).getPiece().getColor() == Piece.LIGHT) {
            // If piece is light AND is king
            if (board.getCell(xCordDst, yCordDst).getPiece().isKing()) {
                buttonIndexes[xCordDst][yCordDst].setBackgroundResource(R.drawable.light_king_piece);
            }
            // If piece is light AND is not king
            else {
                buttonIndexes[xCordDst][yCordDst].setBackgroundResource(R.drawable.light_piece);
            }
        }
        // If the piece is dark
        else{
            // If piece is dark AND is king
            if (board.getCell(xCordDst, yCordDst).getPiece().isKing()) {
                buttonIndexes[xCordDst][yCordDst].setBackgroundResource(R.drawable.dark_king_piece);
            }
            // If piece is dark AND is not king
            else {
                buttonIndexes[xCordDst][yCordDst].setBackgroundResource(R.drawable.dark_piece);
            }
        }
    }

    /*
     * Gets the player whos turn it is
     * @ret Player currentPlayer - Returns the current player
     */
    public Player getCurrentPlayer(){
        return this.currentPlayer;
    }

    /*
     * Switches currentPlayer to the other player
     */
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

    /*
     * Updates the player turn tracker
     */
    public void updateTurnTracker() {
        TextView p1 = (TextView) findViewById(R.id.playerOneTurn);
        TextView p2 = (TextView) findViewById(R.id.playerTwoTurn);

        // If current player is light
        if(this.currentPlayer.getColor().equals(Piece.LIGHT)){
            p1.setText(String.format("%s's Turn", this.player1.getColor()));
            p2.setText("");
        }
        // Else if current player is dark
        else{
            p2.setText(String.format("%s's Turn", this.player2.getColor()));
            p1.setText("");
        }

    }

    /*
     * When player clicks a piece, stores all of the possible moves and colors possible moves on board
     * @param int xCord - Gets the possible moves of a piece using this x-coordinate
     * @param int yCord - Gets the possible moves of a piece using this y-coordinate
     */
    public void getPossibleMoves(int xCord, int yCord){

        this.moves = board.possibleMoves(xCord, yCord);  // stores possible capture moves for a cell

        // For all of the moves that a piece has, get the X and Y coord and color piece in
        for (int i = 0; i < this.moves.size(); i++) {
            possMoves = this.moves.get(i);       // get first set of moves
            buttonIndexes[possMoves.getX()][possMoves.getY()].setBackgroundResource(R.drawable.possible_moves_image);   // color possible moves square
        }
    }

    /*
    * Store the possible capture moves of a piece and colors on the board
    * @param int xCord - Gets the capture moves of a piece using this x-coordinate
    * @param int yCord - Gets the capture moves of a piece using this y-coordinate
    */
    public void getCaptureMoves(int xCord, int yCord){
        Cell captureMoves;                                 // stores all of the possible moves for a piece
        this.moves = board.getCaptureMoves(xCord, yCord);  // stores possible capture moves for a cell

        // For all of the moves that a piece has, get the X and Y coord and color piece in
        for (int i = 0; i < this.moves.size(); i++) {
            captureMoves = this.moves.get(i);       // get first set of moves
            buttonIndexes[captureMoves.getX()][captureMoves.getY()].setBackgroundResource(R.drawable.possible_moves_image);   // color possible moves square
        }
    }

    /*
     * When the player clicks a game piece on the board to perform a move
     * Colors the piece/cell that the user presses, stores the coordinates of source cell clicked
     * @param int xCord - Stores x-coordinate of the source cell the user clicks
     * @param int yCord - Stores the y-coordinate of the source cell the user clicks
     */
    public void onFirstClick(int xCord, int yCord){

        // If current player is light AND the piece selected is a light piece, player can ONLY move light pieces and can jump ONLY dark pieces
        if(currentPlayer.getColor().equals(Piece.LIGHT) && board.getCell(xCord, yCord).getPiece().getColor().equals(Piece.LIGHT)){

            // If light AND king
            if (board.getCell(xCord, yCord).getPiece().isKing()) {
                buttonIndexes[xCord][yCord].setBackgroundResource(R.drawable.light_king_piece_pressed);
            }
            // If only light
            else {
                buttonIndexes[xCord][yCord].setBackgroundResource(R.drawable.light_piece_pressed);  // fill selected light piece as pressed piece image
            }
        }
        // If current player is dark AND the piece selected is a dark piece, player can ONLY move dark pieces and can jump ONLY light pieces
        if(currentPlayer.getColor().equals(Piece.DARK) && board.getCell(xCord, yCord).getPiece().getColor().equals(Piece.DARK)){

            // If dark AND king
            if (board.getCell(xCord, yCord).getPiece().isKing()) {
                buttonIndexes[xCord][yCord].setBackgroundResource(R.drawable.dark_king_piece_pressed);
            }
            // If only dark
            else {
                buttonIndexes[xCord][yCord].setBackgroundResource(R.drawable.dark_piece_pressed);   // fill selected dark piece as pressed piece image
            }
        }
        xCordSrcPiece = xCord; // stores coordinates of first click x coordinate
        yCordSrcPiece = yCord; // stores coordinates or first click y coordinate
        counter++;            // increment counter so user can click a destination cell
    }

    /*
     * When the player clicks an empty cell on the board to move source piece to, move the piece
     * If the players move captures a piece, we want to check if THAT piece has any more capture moves
     * Stores the new coordinates of the piece that made a capture (coordinates of the piece after capture)
     * @param int xCord - Stores x-coordinate of the destination cell the user clicks
     * @param int yCord - Stores the y-coordinate of the destination cell the user clicks
     */
    public void onSecondClick(int xCord, int yCord){
        // If user does a capture move, we want to allow them to click another piece
        if (board.isCaptureMove(board.getCell(xCordSrcPiece, yCordSrcPiece), board.getCell(xCord, yCord))) {
            xCordCapturingPiece = xCord;
            yCordCapturingPiece = yCord;
            hasAnotherTurn = true;
        }
        // Capture move was not made, they moved to empty spot
        else {
            hasAnotherTurn = false;
            changeTurn();
        }

        ArrayList<Cell> pieceCaptured = board.movePiece(xCordSrcPiece, yCordSrcPiece, xCord, yCord);    // moves piece, store captured piece into array list
        Cell piece = pieceCaptured.get(0);                                                              // get the piece captured
        updatePieces(xCordSrcPiece, yCordSrcPiece, xCord, yCord, piece);                                // updates the graphical cells
        counter--;
        //updateBoard(buttonIndexes, board);

        // If player has another turn, check to see if they have any future captures
        if (hasAnotherTurn) {
            moves = board.getCaptureMoves(xCord, yCord);    // stores the future capture moves of the cell

            // If the piece that captured opponents piece has no capture moves, end turn
            if (moves.isEmpty()) {
                hasAnotherTurn = false;
                changeTurn();
            }
            // Else, we can go forward and let them capture another piece
            else {
                hasAnotherTurn = true;
            }
        }
    }

    /*
     * Creates listener to perform action when player clicks a game piece
     * Handles when player wants to move a piece
     */
    private View.OnClickListener listener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            int tag = (Integer) v.getTag();
            int xCord = tag / 10;
            int yCord = tag % 10;

            // If both players have pieces, game IS RUNNING
            if(!board.getPieces(Piece.LIGHT).isEmpty() && !board.getPieces(Piece.DARK).isEmpty()){
                currentPlayer = getCurrentPlayer();     // gets the current player, stores in currentPlayer variable

                // If piece exists AND color of piece matches players piece AND counter == 0, let the player take a turn
                if (board.getCell(xCord, yCord).containsPiece() && board.getCell(xCord, yCord).getPiece().getColor().equals(currentPlayer.getColor()) && counter == 0 && !hasAnotherTurn) {
                    getPossibleMoves(xCord, yCord);

                    // If player has no possible moves AND is not on their second turn, cannot move piece so player must choose new piece
                    if (moves.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "No possible moves!", Toast.LENGTH_SHORT).show();
                        updatePieces(xCord, yCord);     // updates the pieces
                        //updateBoard(buttonIndexes, board);
                    }
                    // Else, if player has possible moves THEN we can move piece
                    else {
                        onFirstClick(xCord, yCord);
                    }
                }

                // If after a player captures a move, they can ONLY move the piece that performed the capture
                else if (board.getCell(xCord, yCord).containsPiece() && board.getCell(xCord, yCord).getPiece().getColor().equals(currentPlayer.getColor()) && counter == 0 && hasAnotherTurn &&
                        board.getCell(xCordCapturingPiece, yCordCapturingPiece) == board.getCell(xCord, yCord)) {

                    getCaptureMoves(xCord, yCord);  // get all the possible capture moves

                    // If player has no possible capture moves AND is NOT on their second turn, cannot move piece so player switches turns
                    if (moves.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "No possible capture moves!", Toast.LENGTH_SHORT).show();
                        changeTurn();
                    }
                    // Else, we can move piece because we have possible moves
                    else {
                        onFirstClick(xCord, yCord);
                    }
                }

                // If the clicked destination cell IS empty AND player has possible moves AND if counter == 1, then user can move piece
                else if (!(board.getCell(xCord, yCord).containsPiece()) && moves.contains(board.getCell(xCord, yCord)) && counter == 1) {
                    onSecondClick(xCord, yCord);
                }

                // If player clicks on the same piece twice, we simply want to de-select it since they have not made a move yet
                else if (board.getCell(xCordSrcPiece, yCordSrcPiece) == board.getCell(xCord, yCord)) {
                    counter--;
                    updatePieces(xCordSrcPiece, yCordSrcPiece); // updates the graphical pieces
                    //updateBoard(buttonIndexes, board);
                }
            }

            // If player who is light runs out of pieces, they lose
            //TODO: When player wins, what should we do next?
            if(board.getPieces(Piece.LIGHT).isEmpty() && !board.getPieces(Piece.DARK).isEmpty()) {
                Toast.makeText(getApplicationContext(), "DARK PLAYER WINS!", Toast.LENGTH_LONG).show();
//                Intent exitGame = new Intent(ButtonBoard.this, MainActivity.class);
//                exitGame.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                exitGame.putExtra("EXIT", true);
//                startActivity(exitGame);
            }
            // If player who is dark runs out of pieces, they lose
            //TODO: When player wins, what should we do next?
            else if(!board.getPieces(Piece.LIGHT).isEmpty() && board.getPieces(Piece.DARK).isEmpty()) {
                Toast.makeText(getApplicationContext(), "LIGHT PLAYER WINS!", Toast.LENGTH_LONG).show();
//                Intent exitGame = new Intent(ButtonBoard.this, MainActivity.class);
//                exitGame.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                exitGame.putExtra("EXIT", true);
//                startActivity(exitGame);
            }
            // If BOTH players each have 1 piece left AND the round is over 40, call it a draw
            //TODO: When draw occurs, what should we do next?
            else if(board.getPieces(Piece.LIGHT).size() == 1 && board.getPieces(Piece.DARK).size() == 1 && roundCounter > 40){
                Toast.makeText(getApplicationContext(), "DRAW, NO WINNERS!", Toast.LENGTH_LONG).show();
            }
        }
    };


>>>>>>> 5a2e4f4c72d74a2133daf0df52c133d8f4d128dd
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
        Intent restartGame = new Intent(ButtonBoard.this, ButtonBoard.class);
        Intent exitGame = new Intent(ButtonBoard.this, MainActivity.class);

        switch (item.getItemId()) {
            case R.id.saveGame:
                board.SaveGameState(getApplicationContext());
<<<<<<< HEAD
                Toast.makeText(getApplicationContext(), "Game Saved!", Toast.LENGTH_LONG).show();
=======
                Toast.makeText(getApplicationContext(), "Match Saved!", Toast.LENGTH_SHORT).show();
>>>>>>> 5a2e4f4c72d74a2133daf0df52c133d8f4d128dd
                return true;
            case R.id.restartMatch:
                startActivity(restartGame);
                Toast.makeText(getApplicationContext(), "Match Restarted!", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.quitMatch:
                Toast.makeText(getApplicationContext(), "Quitting Match!", Toast.LENGTH_SHORT).show();
                exitGame.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                exitGame.putExtra("EXIT", true);
                startActivity(exitGame);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}







package com.example.dylan.checkers;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import static com.example.dylan.checkers.R.layout.board;

/*
 * ButtonBoard.java - Handles the graphical user interface for the game cellBoard
 *                  - Stores button ids for game cellBoard layout and maps them to the correct cell (x, y)
 *                  - Creates array of buttons that map each square on the game cellBoard
 *                  - Initializes the game piece images on the cellBoard (12 dark pieces and 12 light pieces)
 */
public class ButtonBoard extends AppCompatActivity {

    // Add the buttons into an array by their specific id in integer form
    private final int[] buttons_id = {R.id.button0, R.id.button2, R.id.button4, R.id.button6,
            R.id.button9, R.id.button11, R.id.button13, R.id.button15,
            R.id.button16, R.id.button18, R.id.button20, R.id.button22,
            R.id.button25, R.id.button27, R.id.button29, R.id.button31,
            R.id.button32, R.id.button34, R.id.button36, R.id.button38,
            R.id.button41, R.id.button43, R.id.button45, R.id.button47,
            R.id.button48, R.id.button50, R.id.button52, R.id.button54,
            R.id.button57, R.id.button59, R.id.button61, R.id.button63};
    private final Button[][] buttonBoard = new Button[8][8];        // stores the Button objects with their indexes
    Cell possMoves;                                 // stores all of the possible moves for a piece
    Cell highlightedCell;
    int roundCounter = 0;
    private ArrayList<Cell> moves;
    private ArrayList<Cell> highlightedCells = new ArrayList<>();
    private Player player1;
    private Player player2;
    private Player currentPlayer;

    // Game cellBoard layout of the black squares by square ID
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
    private Board cellBoard = new Board();

    private Cell srcCell = null;
    private Cell dstCell = null;
    /*
     * Creates listener to perform action when player clicks a game piece
     * Handles when player wants to move a piece
     */
    private View.OnClickListener listener = new OnClickListener() {
        @Override
        public void onClick(View v) {
//            Log.d("***", "onClick: srcCell == null: " + (srcCell== null));
//            Log.d("***", "onClick: dstCell == null: " + (dstCell== null));

            int tag = (Integer) v.getTag();
            int xCord = tag / 10;
            int yCord = tag % 10;

            // If both players have pieces, game IS RUNNING
            if (!cellBoard.getPieces(Piece.LIGHT).isEmpty() && !cellBoard.getPieces(Piece.DARK).isEmpty()) {

                // If piece exists AND color of piece matches players piece AND counter == 0, let the player take a turn
                if (cellBoard.getCell(xCord, yCord).containsPiece() && cellBoard.getCell(xCord, yCord).getPiece().getColor().equals(currentPlayer.getColor()) && srcCell == null) {
                    srcCell = cellBoard.getCell(xCord, yCord);
                    moves = cellBoard.possibleMoves(srcCell);
                    // If player has no possible moves AND is not on their second turn, cannot move piece so player must choose new piece
                    if (moves.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "No possible moves!", Toast.LENGTH_SHORT).show();
                        srcCell = null;
                    }
                    // Else, if player has possible moves THEN we can move piece
                    else {
                        showPossibleMoves(moves);
                        srcCell = cellBoard.getCell(xCord, yCord);
                        updatePiecePressed(srcCell);                       }
                }

                //If the user taps same cell twice then deselect the cell
                else if(srcCell != null && srcCell.equals(cellBoard.getCell(xCord, yCord))){
                    srcCell = null;
                    updatePieces(xCord, yCord); // updates the graphical pieces
                    updateTurnTracker();
                }

                // If after a player captures a move, they can ONLY move the piece that performed the capture
                else if (!cellBoard.getCell(xCord, yCord).containsPiece() && moves.contains(cellBoard.getCell(xCord, yCord)) && srcCell != null) {
                    dstCell = cellBoard.getCell(xCord, yCord);
                    onSecondClick(srcCell, dstCell);
                }

            }

            // If player who is light runs out of pieces, they lose
            if (cellBoard.getPieces(Piece.LIGHT).isEmpty() && !cellBoard.getPieces(Piece.DARK).isEmpty()) {
                gameOverDialog();
            }
            // If player who is dark runs out of pieces, they lose
            else if (!cellBoard.getPieces(Piece.LIGHT).isEmpty() && cellBoard.getPieces(Piece.DARK).isEmpty()) {
                gameOverDialog();
            }
            // If BOTH players each have 1 piece left AND the round is over 40, call it a draw
            //TODO: When draw occurs, what should we do next?
            else if (cellBoard.getPieces(Piece.LIGHT).size() == 1 && cellBoard.getPieces(Piece.DARK).size() == 1 && roundCounter > 40) {
                Toast.makeText(getApplicationContext(), "DRAW, NO WINNERS!", Toast.LENGTH_LONG).show();
            }
        }
    };

    /*
    * Creates the activity for the game cellBoard, then sets up game piece images on game cellBoard
    * @param Bundle savedInstanceState
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // If orientation i
        setContentView(board);

        // If the load message was loaded, we load the game, otherwise a new game is created
        if (getIntent().getBooleanExtra("LOAD", false)) {
            cellBoard.LoadGameState(getApplicationContext());
        }

        this.moves = new ArrayList<>();                  // init moves arraylist
        player1 = new PlayerTUI(Piece.LIGHT);       // init player 1
        player2 = new PlayerTUI(Piece.DARK);        // init player 2
        this.currentPlayer = player1;               // init current player
        fillButtonIndexArray(listener);
        updateBoard(buttonBoard, cellBoard);
        updateTurnTracker();
    }

    /*
     * Fills the Button indexes array with each button object and asigns index using button tag
     * @param View.OnClickListener listener
     */
    public void fillButtonIndexArray(View.OnClickListener listener) {
        int index = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if ((i + j) % 2 == 0) {
                    buttonBoard[i][j] = (Button) findViewById(buttons_id[index]);
                    index++;
                    buttonBoard[i][j].setTag(i * 10 + j);
                    buttonBoard[i][j].setOnClickListener(listener);

                }
            }
        }
    }

    /*
     * Updates the game pieces on the UI Board according to Game.java Cell[][] array
     * @param Button[][] buttonBoard, Board cellBoard
     */
    public void updateBoard(Button[][] buttonIndexes, Board board) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if ((i + j) % 2 == 0) {
                    if (!board.getCell(i, j).containsPiece()) {
                        buttonIndexes[i][j].setBackgroundResource(R.drawable.blank_square);
                    }
                    // Fills the light pieces in on the cellBoard
                    else if (board.getCell(i, j).getPiece().getColor().equals(Piece.LIGHT)) {
                        //King light piece
                        if (board.getCell(i, j).getPiece().isKing()) {
                            buttonIndexes[i][j].setBackgroundResource(R.drawable.light_king_piece);
                        }
                        // No king
                        else {
                            buttonIndexes[i][j].setBackgroundResource(R.drawable.light_piece);
                        }
                    }
                    // Fills the dark pieces in on the cellBoard
                    else if (board.getCell(i, j).getPiece().getColor().equals(Piece.DARK)) {
                        // King dark piece
                        if (board.getCell(i, j).getPiece().isKing()) {
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
    }

    /*
     * When a piece moves to an empty cell, we want to update the pieces affected
     * @param int xCord - The x-coordinate of a piece after it has moved to an empty cell
     * @param int yCord - The y-coordinate of a piece after it has moved to an empty cell
     */
    public void updatePieces(int xCord, int yCord) {

        // For all of the possible moves colored in on the cellBoard, after a piece moves we want to remove them
        for (int i = 0; i < moves.size(); i++) {
            possMoves = moves.get(i);
            buttonBoard[possMoves.getX()][possMoves.getY()].setBackgroundResource(R.drawable.blank_square);   // color possible moves blank
        }

        // If the piece is light
        if (cellBoard.getCell(xCord, yCord).getPiece().getColor().equals(Piece.LIGHT) && cellBoard.getCell(xCord, yCord).containsPiece()) {
            // If piece is light AND is king
            if (cellBoard.getCell(xCord, yCord).getPiece().isKing()) {
                buttonBoard[xCord][yCord].setBackgroundResource(R.drawable.light_king_piece);
            }
            // If piece is light AND is not king
            else {
                buttonBoard[xCord][yCord].setBackgroundResource(R.drawable.light_piece);
            }
        }
        // If the piece is dark
        else {
            // // If piece is dark AND is king
            if (cellBoard.getCell(xCord, yCord).getPiece().isKing()) {
                buttonBoard[xCord][yCord].setBackgroundResource(R.drawable.dark_king_piece);
            }
            // If piece is dark AND is not king
            else {
                buttonBoard[xCord][yCord].setBackgroundResource(R.drawable.dark_piece);
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
    public void updatePieces(ArrayList<Cell> changedCells) {

        // For all of the possible moves colored in on the cellBoard, after a piece jumps we want to remove them
        for (int i = 0; i < moves.size(); i++) {
            possMoves = moves.get(i);
            buttonBoard[possMoves.getX()][possMoves.getY()].setBackgroundResource(R.drawable.blank_square);   // color possible moves blank
        }

        for(Cell cell: changedCells){
            if(!cell.containsPiece()){
                buttonBoard[cell.getX()][cell.getY()].setBackgroundResource(R.drawable.blank_square);
            }
            else if(cell.getPiece().getColor().equals(Piece.LIGHT)){
                if(cell.getPiece().isKing()){
                    buttonBoard[cell.getX()][cell.getY()].setBackgroundResource(R.drawable.light_king_piece);
                }
                else{
                    buttonBoard[cell.getX()][cell.getY()].setBackgroundResource(R.drawable.light_piece);
                }
            }
            else if(cell.getPiece().getColor().equals(Piece.DARK)){
                if(cell.getPiece().isKing()){
                    buttonBoard[cell.getX()][cell.getY()].setBackgroundResource(R.drawable.dark_king_piece);
                }
                else{
                    buttonBoard[cell.getX()][cell.getY()].setBackgroundResource(R.drawable.dark_piece);
                }
            }
        }
    }

    /*
     * When the player clicks a game piece on the cellBoard we want to color in that piece
     * Colors the piece/cell that the user presses
     * @param int xCord - The x-coordinate of the source cell that we want to change to pressed piece graphic
     * @param int yCord - The y-coordinate of the source cell that we want to change to pressed piece graphic
     */
    public void updatePiecePressed(Cell givenCell) {
        // If current player is light AND the piece selected is a light piece, player can ONLY move light pieces and can jump ONLY dark pieces
        if (currentPlayer.getColor().equals(Piece.LIGHT) && givenCell.getPiece().getColor().equals(Piece.LIGHT)) {

            // If light AND king
            if (givenCell.getPiece().isKing()) {
                buttonBoard[givenCell.getX()][givenCell.getY()].setBackgroundResource(R.drawable.light_king_piece_pressed);
            }
            // If only light
            else {
                buttonBoard[givenCell.getX()][givenCell.getY()].setBackgroundResource(R.drawable.light_piece_pressed);  // fill selected light piece as pressed piece image
            }
        }
        // If current player is dark AND the piece selected is a dark piece, player can ONLY move dark pieces and can jump ONLY light pieces
        if (currentPlayer.getColor().equals(Piece.DARK) && givenCell.getPiece().getColor().equals(Piece.DARK)) {

            // If dark AND king
            if (cellBoard.getCell(givenCell.getX(), givenCell.getY()).getPiece().isKing()) {
                buttonBoard[givenCell.getX()][givenCell.getY()].setBackgroundResource(R.drawable.dark_king_piece_pressed);
            }
            // If only dark
            else {
                buttonBoard[givenCell.getX()][givenCell.getY()].setBackgroundResource(R.drawable.dark_piece_pressed);   // fill selected dark piece as pressed piece image
            }
        }
    }

     /*
     * Switches currentPlayer to the other player
     */
    public void changeTurn() {
        // If both players have pieces, we can switch turns
        if (!cellBoard.getPieces(Piece.LIGHT).isEmpty() && !cellBoard.getPieces(Piece.DARK).isEmpty()) {
            if (this.currentPlayer.equals(player1)) {
                this.currentPlayer = player2;
                updateTurnTracker();
            } else {
                this.currentPlayer = player1;
                updateTurnTracker();
            }
        }
    }

    public void unHighlightPieces() {
        while(!highlightedCells.isEmpty()){
            highlightedCell = highlightedCells.remove(0);
            if (highlightedCell.getPiece().getColor().equals(Piece.LIGHT)) {
                if(highlightedCell.getPiece().isKing()){
                    buttonBoard[highlightedCell.getX()][highlightedCell.getY()].setBackgroundResource(R.drawable.light_king_piece);
                }
                else {
                    buttonBoard[highlightedCell.getX()][highlightedCell.getY()].setBackgroundResource(R.drawable.light_piece);
                }
            } else{
                if(highlightedCell.getPiece().isKing()){
                    buttonBoard[highlightedCell.getX()][highlightedCell.getY()].setBackgroundResource(R.drawable.dark_king_piece);
                }
                else{
                    buttonBoard[highlightedCell.getX()][highlightedCell.getY()].setBackgroundResource(R.drawable.dark_piece);
                }
            }
        }
    }

    /*
     * Updates the player turn tracker
     */
    public void updateTurnTracker() {
        // Get all the pieces of the current player that can move & highlight them
        ArrayList<Piece> currentPlayerPieces = cellBoard.getPieces(this.currentPlayer.getColor());
        //TODO add the resource for king piece
        int resourceId;
        if(this.currentPlayer.getColor().equals(Piece.DARK)){
            resourceId = R.drawable.dark_piece_highlighted;
        }
        else{
            resourceId = R.drawable.light_piece_highlighted;
        }

        ArrayList<Cell> moves;
        for(Piece piece: currentPlayerPieces){
            moves = cellBoard.possibleMoves(piece);
            if(!moves.isEmpty()){
                buttonBoard[piece.getCell().getX()][piece.getCell().getY()].setBackgroundResource(resourceId);
                highlightedCells.add(piece.getCell());
            }
        }
    }

    /*
     * When player clicks a piece, stores all of the possible moves and colors possible moves on cellBoard
     * @param int xCord - Gets the possible moves of a piece using this x-coordinate
     * @param int yCord - Gets the possible moves of a piece using this y-coordinate
     */
    public void showPossibleMoves(ArrayList<Cell> moves) {
        for (Cell cell: moves) {
            buttonBoard[cell.getX()][cell.getY()].setBackgroundResource(R.drawable.possible_moves_image);   // color possible moves square
        }
    }

    /*
    * Store the possible capture moves of a piece and colors on the cellBoard
    * @param int xCord - Gets the capture moves of a piece using this x-coordinate
    * @param int yCord - Gets the capture moves of a piece using this y-coordinate
    */



    /*
     * When the player clicks an empty cell on the cellBoard to move source piece to, move the piece
     * If the players move captures a piece, we want to check if THAT piece has any more capture moves
     * Stores the new coordinates of the piece that made a capture (coordinates of the piece after capture)
     * @param int xCord - Stores x-coordinate of the destination cell the user clicks
     * @param int yCord - Stores the y-coordinate of the destination cell the user clicks
     */
    public void onSecondClick(Cell givenSrcCell, Cell givenDstCell) {
        unHighlightPieces();
        boolean captureMove = cellBoard.isCaptureMove(givenSrcCell, givenDstCell);
        ArrayList<Cell> changedCells = cellBoard.movePiece(givenSrcCell.getCoords(), givenDstCell.getCoords());    // moves piece, store captured piece into array list
        updatePieces(changedCells);
        if (captureMove) {
            moves = cellBoard.getCaptureMoves(givenDstCell);    // stores the future capture moves of the cell

            // If the piece that captured opponents piece has no capture moves, end turn
            if (moves.isEmpty()) {
                this.srcCell = null;
                this.dstCell = null;
                changeTurn();
            }
            // Else, we can go forward and let them capture another piece
            else {
                this.srcCell = this.dstCell;
                updatePiecePressed(this.srcCell);
                showPossibleMoves(moves);
            }
        }
        // If player does not have another turn, change turns
        else {
            srcCell= null;
            dstCell = null;
            changeTurn();
        }
    }

    /*
     * The dialog menu that pops up after a game has ended
     */
    public void gameOverDialog(){
        updateTurnTracker();
        final CharSequence choices[] = new CharSequence[]{"Play Again", "Return to Main Menu"};
        AlertDialog.Builder builder = new AlertDialog.Builder(ButtonBoard.this);
        builder.setCancelable(false);
        builder.setTitle(this.currentPlayer.getColor() + " Player Wins!");
        builder.setItems(choices, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int clickValue) {

                // If user clicks New Match, create a new match
                if (clickValue == 0) {
                    restartMatch();
                }
                // If user chooses to Return to Main Menu
                else if (clickValue == 1) {
                    quitMatch();
                }
            }
        });
        builder.show();
    }

    public void saveGameFound() {
        final CharSequence choices[] = new CharSequence[]{"Overwrite", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(ButtonBoard.this);
        builder.setCancelable(true);
        builder.setTitle("A previously saved game was found. Overwrite?");
        builder.setItems(choices, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int clickValue) {

                if(clickValue == 0) {
                    File file = getApplicationContext().getFileStreamPath("savedGame.dat");
                    if (file != null || file.exists()) {
                        file.delete();
                    }
                    cellBoard.SaveGameState(getApplicationContext());
                    Toast.makeText(getApplicationContext(), "Match Saved!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.show();
    }


    public void restartMatchDialog(){
        final CharSequence choices[] = new CharSequence[] {"Restart", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(ButtonBoard.this);
        builder.setCancelable(true);
        builder.setTitle("Are you sure you want to restart?");
        builder.setItems(choices, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int clickValue) {
                if (clickValue == 0) {
                if(clickValue == 0){
                    restartMatch();
                }
            }
        }
    });
        builder.show();
    }

    /*
     * Restarts the match
     */
    public void quitMatchDialog(){
        final CharSequence choices[] = new CharSequence[] {"Quit", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(ButtonBoard.this);
        builder.setCancelable(true);
        builder.setTitle("Are you sure you want to quit?");
        builder.setItems(choices, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int clickValue) {
                if(clickValue == 0){
                    quitMatch();
                }
            }
        });
        builder.show();
    }



    /*
     * Restarts the match
     */
    public void restartMatch(){
        Toast.makeText(getApplicationContext(), "Match Restarted!", Toast.LENGTH_SHORT).show();
        Intent restartMatch = new Intent(ButtonBoard.this, ButtonBoard.class);
        startActivity(restartMatch);
    }

    /*
     * Quits the match, returns to MainMenu.java activity
     */
    public void quitMatch() {
        Intent exitGame = new Intent(ButtonBoard.this, MainActivity.class);
        exitGame.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        exitGame.putExtra("EXIT", true);
        startActivity(exitGame);
    }

    /*
    * Adds Quick Menu at top-right corner with following options: Save, Load, Restart, Quit
    * @param Menu menu
    * @ret boolean
    */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.saveGame:
                File file = getApplicationContext().getFileStreamPath("savedGame.dat");
                if (file == null || !file.exists()) {
                    cellBoard.SaveGameState(getApplicationContext());
                } else {
                    saveGameFound();
                }
                return true;
            case R.id.restartMatch:
                restartMatchDialog();
                return true;
            case R.id.quitMatch:
                quitMatchDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}






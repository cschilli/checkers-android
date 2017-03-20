package com.example.dylan.checkers;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.Toast;
import java.util.ArrayList;

/*
 * ButtonBoard.java - Handles the graphical user interface for the game board
 *                  - Stores button ids for game board layout and maps them to the correct cell (x, y)
 *                  - Creates array of buttons that map each square on the game board
 *                  - Initializes the game piece images on the board (12 dark pieces and 12 light pieces)
 */
public class ButtonBoard extends AppCompatActivity {

    private static int BOARD_SIZE = 8;      // board is 8x8, 64 spaces (buttons)
    private int TOTAL_SQUARES = 32;         // total amount of squares on game board

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

    private final Button[][] buttonIndexes = new Button[BOARD_SIZE][BOARD_SIZE];        // stores the Button objects with their indexes
    private final Board board = new Board();
    ArrayList<Cell> moves = new ArrayList<>();  // stores the possible moves for a piece
    int xCord2 = 0;
    int yCord2 = 0;
    int counter = 0;

    /*
    * Creates the activity for the game board, then sets up game piece images on game board
    * @param Bundle savedInstanceState
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.board);
        fillButtonIndexArray(listener);             // fill the board with the correct indexes
        updateBoard(buttonIndexes, board);          // Initial board setup
    }


    /*
     * Creates listener to perform action when user clicks a game piece
     * When user clicks on a piece, gets the coordinates and stores into x and y variable
     */
    private View.OnClickListener listener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            int tag = (Integer)v.getTag();
            int xCord = tag/10;
            int yCord = tag%10;
            Cell possMoves;     // stores all of the possible moves for a piece

            // If piece exists, highlight piece next choose a piece to swap it with
            if (board.getCell(xCord, yCord).containsPiece() && counter == 0) {
                moves = board.possibleMoves(xCord, yCord);  // stores possible moves for a cell

                // If no possible moves, cannot move piece must choose new piece
                if(moves.isEmpty()){
                    Toast.makeText(getApplicationContext(), "No possible moves!", Toast.LENGTH_SHORT).show();
                    updateBoard(buttonIndexes, board);
                }
                // Else, we can move piece because we have possible moves
                else {
                    // Draw the possible moves on the board
                    int xPossMoves;  // stores X coord of possible moves
                    int yPossMoves; // stores Y coord of possible moves

                    // For all of the moves that a piece has, get the X and Y coord and color piece
                    for(int i = 0; i < moves.size(); i++){
                        possMoves = moves.get(i);       // get first set of moves
                        xPossMoves = possMoves.getX();  // get x coord of possible move
                        yPossMoves = possMoves.getY();  // get y coord of possible move
                        buttonIndexes[xPossMoves][yPossMoves].setBackgroundResource(R.drawable.possible_moves_image);   // color possible moves square
                    }

                    // If pressed piece is light, color it with light pressed piece
                    if(board.getCell(xCord, yCord).getPiece().getColor().equals(Piece.LIGHT)){
                        buttonIndexes[xCord][yCord].setBackgroundResource(R.drawable.light_piece_pressed);
                    }
                    // Else, pressed piece is dark, so we color is dark
                    else{
                        buttonIndexes[xCord][yCord].setBackgroundResource(R.drawable.dark_piece_pressed);
                    }

                    Toast.makeText(getApplicationContext(), "" + board.getCell(xCord, yCord), Toast.LENGTH_SHORT).show();
                    xCord2 = xCord; // stores coordinates of first click
                    yCord2 = yCord; // stores coordinates or second click
                    counter++;
                }
            }

            // If piece doesn't exist, refresh the board and unhighlight piece
            else{
                // If user is on second click, check if space is empty, and if user selected a possible move
                if(!(board.getCell(xCord, yCord).containsPiece()) && counter == 1 && moves.contains(board.getCell(xCord, yCord))){
                    board.movePiece(xCord2, yCord2, xCord, yCord);
                    counter--;
                    //System.out.println(board.getPieces(Piece.DARK).size());   // Test pieces remaining
                    //System.out.println(board.getPieces(Piece.LIGHT).size());  // Test pieces remaining
                    updateBoard(buttonIndexes, board);
                }
                if(board.getCell(xCord, yCord) == board.getCell(xCord2, yCord2)){
                    counter--;
                    updateBoard(buttonIndexes, board);

                }
            }
        }
    };


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
                //System.out.println("Index (" + i + "," + j + ") " + buttonIndexes[i][j]);   // Print contents of the 2d array with button ids at the indexes (Testing)

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
                Toast.makeText(getApplicationContext(), "Game Saved!", Toast.LENGTH_LONG).show();
                return true;
            case R.id.loadGame:
                Toast.makeText(getApplicationContext(), "Game Loaded!", Toast.LENGTH_LONG).show();
                return true;
            case R.id.restartMatch:
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







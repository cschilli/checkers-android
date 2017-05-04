package com.uno.dbbc.checkers;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * A class representing a Board in Checker.
 */

public class Board implements Serializable{

    private Cell[][] board;
    private ArrayList<Piece> lightPieces, darkPieces;
    private static int BOARD_SIZE = 8;

    /**
     * Creates an instance of Board. Sets up the pieces as in freshly started game.
     */
    public Board(){
        this.lightPieces = new ArrayList<Piece>();
        this.darkPieces = new ArrayList<Piece>();
        board = new Cell[Board.BOARD_SIZE][Board.BOARD_SIZE];
    }



    /**
     * Sets up the board in initial configuration i.e puts the pieces in places where they should be when starting the game
     */

    // The configuration is as follows:
    //L -> Light Colored Piece  	D-> Dark Colored Piece  	 _-> a blank cell
    //	    0  1  2  3  4  5  6  7
    //	 0  L  _  L  _  L  _  L  _
    //	 1  _  L  _  L  _  L  _  L
    //	 2  L  _  L  _  L  _  L  _
    //	 3  _  _  _  _  _  _  _  _
    //	 4  _  _  _  _  _  _  _  _
    //	 5  _  D  _  D  _  D  _  D
    //	 6  D  _  D  _  D  _  D  _
    //	 7  _  D  _  D  _  D  _  D
    public void initialBoardSetup(){
        for(int i=0; i < Board.BOARD_SIZE; i++) {
            for (int j = 0; j < Board.BOARD_SIZE; j++) {
                this.board[i][j] = new Cell(i, j);
            }
        }

        for(int column = 0; column < Board.BOARD_SIZE; column+= 2){
            this.board[0][column].placePiece(new Piece(Piece.LIGHT));
            this.board[2][column].placePiece(new Piece(Piece.LIGHT));
            this.board[6][column].placePiece(new Piece(Piece.DARK));

            lightPieces.add(this.board[0][column].getPiece());
            lightPieces.add(this.board[2][column].getPiece());
            darkPieces.add(this.board[6][column].getPiece());
        }

        for(int column = 1; column< Board.BOARD_SIZE; column+=2){
            this.board[1][column].placePiece(new Piece(Piece.LIGHT));
            this.board[5][column].placePiece(new Piece(Piece.DARK));
            this.board[7][column].placePiece(new Piece(Piece.DARK));

            lightPieces.add(this.board[1][column].getPiece());
            darkPieces.add(this.board[5][column].getPiece());
            darkPieces.add(this.board[7][column].getPiece());
        }
    }// end of initialBoardSetup


    /**
     * Gets the Cell in the specified position
     * @param x x-coordinate of the cell
     * @param y y-coordinate of the cell
     * @return the instance of Cell in the position specified by given x-coordinate and y-coordinate
     * @throws IllegalArgumentException if the given x-coordinate and y-coordinate is out of bound i.e not in range 0 <= x, y <= 7
     */
    public Cell getCell(int x, int y) throws IllegalArgumentException{
        if((x<0 || x > 7) || (y<0 || y >7)){
            throw new IllegalArgumentException("The coordinates provided are outside of the board");
        }

        return this.board[x][y];
    }


    /**
     * Returns an ArrayList of pieces of the specified color
     * @return ArrayList of the pieces of the specified color that are in the board currently
     * @param givenColor The color of the pieces that is to be retrieved.
     * @throws IllegalArgumentException if the specified color is not a valid color
     *                                      i.e if the specified color is not one of Piece.LIGHT or Piece.DARK
     */
    public ArrayList<Piece> getPieces(String givenColor) throws IllegalArgumentException{
        if(givenColor.equals(Piece.LIGHT)){
            return this.lightPieces;
        }
        else if(givenColor.equals(Piece.DARK)){
            return this.darkPieces;
        }
        throw new IllegalArgumentException("Given color is not the color of the pieces in board. Given color: " + givenColor);
    }



    /**
     * Moves the piece from one cell to another cell in the board. This method does not checks if the given move is valid or not.
     *
     * @param fromX The x-coordinate of the source cell i.e the cell from where the piece is to be moved.
     * @param fromY The y-coordinate of the source cell.
     * @param toX The x-coordinate of the destination cell i.e. the cell where the piece is to be placed.
     * @param toY The y-coordinate of the destination cell.
     * @return Returns an ArrayList of Cell that were changed by the move. If the move did not involve any capture then the
     *          returned ArrayList will contain the source and destination Cells only. However, if the move comprised of
     *          a capture, then the returned ArrayList will contain the source Cell, destination Cell and the Cell in which
     *          the captured Piece was located.
     * @throws NullPointerException if the source Cell does not contains any Piece.
     * @throws IllegalArgumentException if the given x and y coordinates are out of bound (i.e 0 <= x, y <= 7) or if the destination cell contains a Piece
     */
    public ArrayList<Cell> movePiece(int fromX, int fromY, int toX, int toY) throws NullPointerException, IllegalArgumentException{
        Cell srcCell = this.getCell(fromX, fromY);
        Cell dstCell = this.getCell(toX, toY);
        ArrayList<Cell> changedCells = new ArrayList<Cell>();
        if(srcCell.getPiece() == null){
            throw new NullPointerException("The source cell does not contains piece to move.");
        }
        if(dstCell.getPiece() != null){
            throw new IllegalArgumentException("The destination cell already contains a piece. Cannot move to occupied cell.");
        }


        if(isCaptureMove(srcCell, dstCell)){
            int capturedCellX = (fromX + toX)/ 2;
            int capturedCellY= (fromY + toY)/2;
            Piece capturedPiece = this.board[capturedCellX][capturedCellY].getPiece();
            removePiece(capturedPiece);
            changedCells.add(capturedPiece.getCell());  // here capturedPiece might cause null pointer exception. Not sure yet.
        }
        srcCell.movePiece(dstCell);
        changedCells.add(srcCell);
        changedCells.add(dstCell);
        return changedCells;
    }// End of move


    /**
     * Moves the piece from one cell to another cell in the board. This method does not checks if the given move is valid or not.
     * This method calls movePiece(int fromX, int fromY, int toX, int toY) with arguments src[0], src[1], dst[0] and dst[1] respectively.
     * @param src An integer array of length two representing the coordinate of source Cell i.e. src[0] = x-coordinate of source Cell and src[1] = y-coordiante of source Cell
     * @param dst An integer array of length two representing the coordinate of destination Cell i.e. dst[0] = x-coordinate of destination Cell and dst[1] = y-coordiante of destination Cell
     * @return Returns an ArrayList of Cell that were changed by the move. If the move did not involve any capture then the
     *          returned ArrayList will contain the source and destination Cells only. However, if the move comprised of
     *          a capture, then the returned ArrayList will contain the source Cell, destination Cell and the Cell in which
     *          the captured Piece was located.
     * @throws IllegalArgumentException if the length of the parameters is not equal to two i.e. if src.length != 2 || dst.length != 2;
     */
    public ArrayList<Cell> movePiece(int[] src, int[] dst) throws IllegalArgumentException{
        if(src.length != 2 || dst.length != 2){
            throw new IllegalArgumentException("The given dimension of the points does not match.");
        }
        return movePiece(src[0], src[1], dst[0], dst[1]);
    }

    /**
     * Moves the piece from one cell to another cell in the board. This method does not checks if the given move is valid or not.
     * This method calls movePiece(int fromX, int fromY, int toX, int toY) with arguments move[0], move[1], move[2] and move[3] respectively.
     * @param move An integer array of length four representing the move i.e.
     *             <br>move[0] = x-coordinate of the source Cell,
     *             <br>move[1] = y-coordiante of the source Cell,
     *             <br>move[2] = x-coordinate of the destination Cell,
     *             <br>move[3] = y-coordinate of the destination Cell
     * @return Returns an ArrayList of Cell that were changed by the move. If the move did not involve any capture then the
     *          returned ArrayList will contain the source and destination Cells only. However, if the move comprised of
     *          a capture, then the returned ArrayList will contain the source Cell, destination Cell and the Cell in which
     *          the captured Piece was located.
     * @throws IllegalArgumentException if the length of the parameters is not equal to two i.e. if src.length != 2 || dst.length != 2;
     */
    public ArrayList<Cell> movePiece(int[] move) throws IllegalArgumentException{
        if(move.length != 4){
            throw new IllegalArgumentException("The given dimension of the points does not match.");
        }
        return movePiece(move[0], move[1], move[2], move[3]);
    }


    /**
     * Removes the given Piece from the board.
     * @param capturedPiece The Piece to be removed
     * @throws IllegalStateException if the piece was not successfully removed
     */
    public void removePiece(Piece capturedPiece) throws IllegalStateException, IllegalArgumentException{
        if(capturedPiece.getColor().equals(Piece.LIGHT)){
            if(!lightPieces.remove(capturedPiece)){
                throw new IllegalStateException("Error removing the piece");
            }
            capturedPiece.getCell().placePiece(null);
        }
        else if(capturedPiece.getColor().equals(Piece.DARK)){
            if(!darkPieces.remove(capturedPiece)){
                throw new IllegalStateException("Error removing the piece");
            }
            capturedPiece.getCell().placePiece(null);
        }
    }


    /**
     * Returns the possible moves that the Piece located in the given coordinate can have.
     * This method calls possibleMoves(Cell givenCell) with the Cell returned by the method getCell(x, y) of the Board class as the parameter.
     * @param x The x-coordinate of the Cell in which the required Piece is located.
     * @param y The x-coordinate of the Cell in which the required Piece is located.
     * @return An ArrayList of Cell where the Piece in the given location can move. If there is no Piece in the given location then the returned ArrayList is empty.
     * @throws IllegalArgumentException if the coordinates are out of bound i.e. not in range 0<= x, y <=7
     */
    public ArrayList<Cell> possibleMoves(int x, int y) throws IllegalArgumentException{
        if((x<0 || x>7) || (y<0 || y>7)){
            throw new IllegalArgumentException("Invalid value of x or y provided. (x, y) = (" + x +", " + ")");
        }
        return possibleMoves(this.board[x][y]);
    }




    /**
     * Returns the possible moves that the Piece in given Cell can have.
     * @param givenCell The Cell associated with the Piece whose possible moves is to be determined
     * @return An ArrayList of Cell where the Piece in the given Cell can move. If there is no Piece in the given Cell then the returned ArrayList is empty.
     * @throws NullPointerException if the given Cell is null i.e. givenCell == null.
     */
    public ArrayList<Cell> possibleMoves(Cell givenCell) throws NullPointerException{

        if(givenCell == null){
            throw new NullPointerException("Given Cell is null. Cannot find the possible moves of null Cell");
        }

        ArrayList<Cell> nextMoves = new ArrayList<Cell>();
        Piece givenPiece = givenCell.getPiece();

        if(givenPiece == null){
            return nextMoves;
        }

        String playerColor = givenPiece.getColor();
        String opponentColor = Piece.getOpponentColor(playerColor);


        // if the piece is light-colored
        if(playerColor.equals(Piece.LIGHT)){
            //the next move will be one row ahead i.e in row number X+1
            int nextX = givenCell.getX()+1;

            if(nextX < 8){
                //next move = (currentRow +1, currentColumn +1)
                int nextY = givenCell.getY()+1;
                //if the cell is not out of bound further checking is required
                if(nextY < 8){
                    //if the cell is empty then add the cell to next move
                    if(!this.board[nextX][nextY].containsPiece()){
                        nextMoves.add(this.board[nextX][nextY]);
                    }

                    else if(this.board[nextX][nextY].getPiece().getColor().equals(opponentColor)){
                        int xCoordAfterHoping = nextX + 1;
                        int yCoordAfterHoping = nextY + 1;
                        if(xCoordAfterHoping < 8 && yCoordAfterHoping < 8 && !this.board[xCoordAfterHoping][yCoordAfterHoping].containsPiece()){
                            nextMoves.add(this.board[xCoordAfterHoping][yCoordAfterHoping]);
                        }
                    }
                }


                //next move = (currentRow+1, currentColumn -1)
                nextY = givenCell.getY() -1;
                // if the cell is within bound and does not contains a piece then add it to nextMoves
                if(nextY >=0){
                    if(!this.board[nextX][nextY].containsPiece()){
                        nextMoves.add(this.board[nextX][nextY]);
                    }

                    else if(this.board[nextX][nextY].getPiece().getColor().equals(opponentColor)){
                        int xCoordAfterHoping = nextX + 1;
                        int yCoordAfterHoping = nextY - 1;
                        if(xCoordAfterHoping < 8 && yCoordAfterHoping >= 0 && !this.board[xCoordAfterHoping][yCoordAfterHoping].containsPiece()){
                            nextMoves.add(this.board[xCoordAfterHoping][yCoordAfterHoping]);
                        }
                    }
                }
            }

            //if the given piece is king then have to look to the row behind
            if(givenPiece.isKing()){
                nextX = givenCell.getX() -1;
                if(nextX >=0){
                    //nextMove = (currentRow -1, currentColumn+1)
                    //add this cell if it is within bound and doesnot contain piece
                    int nextY = givenCell.getY()+1;
                    if(nextY < 8 && !this.board[nextX][nextY].containsPiece()){
                        nextMoves.add(this.board[nextX][nextY]);
                    }

                    else if(nextY < 8 && this.board[nextX][nextY].getPiece().getColor().equals(opponentColor)){
                        int xCoordAfterHoping = nextX - 1;
                        int yCoordAfterHoping = nextY + 1;
                        if(xCoordAfterHoping >=0 && yCoordAfterHoping < 8 && !this.board[xCoordAfterHoping][yCoordAfterHoping].containsPiece()){
                            nextMoves.add(this.board[xCoordAfterHoping][yCoordAfterHoping]);
                        }
                    }
                    //nextMove = (currentRow-1, currentColumn-1)
                    //add this cell if it is within bound and does not contains piece
                    nextY = givenCell.getY() -1;
                    if(nextY >=0 && !this.board[nextX][nextY].containsPiece()){
                        nextMoves.add(this.board[nextX][nextY]);
                    }

                    else if(nextY >=0 && this.board[nextX][nextY].getPiece().getColor().equals(opponentColor)){
                        int xCoordAfterHoping = nextX - 1;
                        int yCoordAfterHoping = nextY - 1;
                        if(xCoordAfterHoping >=0 && yCoordAfterHoping >= 0 && !this.board[xCoordAfterHoping][yCoordAfterHoping].containsPiece()){
                            nextMoves.add(this.board[xCoordAfterHoping][yCoordAfterHoping]);
                        }
                    }
                }
            }
        }
        
        //if the piece is dark-colored
        else if(givenPiece.getColor().equals(Piece.DARK)){
            //dark pieces are on the higher rows and to move it forward we have to move them to rows with lower row number.
            //So by assigning currentRow = currentRow -1, we are actually advancing the pieces

            //next move will be on the next row of current row. Rember that currentRow -= 1 will advance the row for darker pieces
            int nextX = givenCell.getX()-1;
            if(nextX >= 0){
                //next move = (currentRow -1, currentColumn +1) which is a row ahead and a column to right
                int nextY = givenCell.getY()+1;
                if(nextY < 8 && !this.board[nextX][nextY].containsPiece()){
                    nextMoves.add(this.board[nextX][nextY]);
                }

                else if(nextY < 8 && this.board[nextX][nextY].getPiece().getColor().equals(opponentColor)){
                    int xCoordAfterHoping = nextX -1;
                    int yCoordAfterHoping = nextY +1;
                    if(xCoordAfterHoping >=0 && yCoordAfterHoping < 8 && !this.board[xCoordAfterHoping][yCoordAfterHoping].containsPiece()){
                        nextMoves.add(this.board[xCoordAfterHoping][yCoordAfterHoping]);
                    }
                }
                //next move = (currentRow -1, currentColumn+1) which is a row ahead and a column to left
                nextY = givenCell.getY()-1;
                if(nextY >=0 && !this.board[nextX][nextY].containsPiece()){
                    nextMoves.add(this.board[nextX][nextY]);
                }
                else if(nextY >=0 && this.board[nextX][nextY].getPiece().getColor().equals(opponentColor)){
                    int xCoordAfterHoping = nextX -1;
                    int yCoordAfterHoping = nextY - 1;
                    if(xCoordAfterHoping >=0 && yCoordAfterHoping >=0 && !this.board[xCoordAfterHoping][yCoordAfterHoping].containsPiece()){
                        nextMoves.add(this.board[xCoordAfterHoping][yCoordAfterHoping]);
                    }
                }
            }

            //if the piece is king we have to look back; Remember in Dark pieces back row = currentRow +1
            if(givenPiece.isKing()){
                //getting to row behind current row
                nextX = givenCell.getX()+1;
                if(nextX < 8){
                    //next move = (currentRow +1, currentColumn+1) which is a row behind and a column right
                    int nextY = givenCell.getY()+1;
                    if(nextY < 8 && !this.board[nextX][nextY].containsPiece()){
                        nextMoves.add(this.board[nextX][nextY]);
                    }

                    else if(nextY < 8 && this.board[nextX][nextY].getPiece().getColor().equals(opponentColor)){
                        int xCoordAfterHoping = nextX + 1;
                        int yCoordAfterHoping = nextY +  1;
                        if(xCoordAfterHoping < 8 && yCoordAfterHoping < 8 && !this.board[xCoordAfterHoping][yCoordAfterHoping].containsPiece()){
                            nextMoves.add(this.board[xCoordAfterHoping][yCoordAfterHoping]);
                        }
                    }

                    //next move = (currentRow +1, currentColumn-1) which is a row behind and a column left
                    nextY = givenCell.getY() -1;
                    if(nextY >=0 && !this.board[nextX][nextY].containsPiece()){
                        nextMoves.add(this.board[nextX][nextY]);
                    }
                    else if(nextY >=0 && this.board[nextX][nextY].getPiece().getColor().equals(opponentColor)){
                        int xCoordAfterHoping = nextX + 1;
                        int yCoordAfterHoping = nextY -  1;
                        if(xCoordAfterHoping < 8 && yCoordAfterHoping >= 0 && !this.board[xCoordAfterHoping][yCoordAfterHoping].containsPiece()){
                            nextMoves.add(this.board[xCoordAfterHoping][yCoordAfterHoping]);
                        }
                    }
                }
            }
        }// end of else if dark piece

        return nextMoves;
    }// end of possibleMoves method



    /**
     * Returns the possible moves that the given Piece can have. This method calls possibleMoves(Cell givenCell) with the Cell object returned by givenPiece.getCell() as a parameter.
     * @param givenPiece The Piece whose possible move is to be determined
     * @return An ArrayList of Cell where the given Piece can move.
     * @throws NullPointerException if the given Piece is null i.e. givenPiece == null.
     */
    public ArrayList<Cell> possibleMoves(Piece givenPiece) throws  NullPointerException{
        if(givenPiece == null){
            throw new NullPointerException("The Piece provided is null. Cannot find possible moves of a null Piece");
        }
        return possibleMoves(givenPiece.getCell());
    }



    /**
     * Returns the capturing moves of the Piece located in the provided Cell.
     * @param givenCell The Cell where the Piece, whose capturing moves is to be determined, is located.
     * @return An ArrayList of Cell where the Piece in given Cell can move and also captures the opponent's piece when performing those moves.
     *          If there is no Piece in the given Cell or if the Piece in given Cell does not have any capturing moves then the returned ArrayList is empty.
     * @throws NullPointerException if the given Cell is null.
     */
    public ArrayList<Cell> getCaptureMoves(Cell givenCell) throws NullPointerException{
        if(givenCell == null){
            throw new NullPointerException("The Cell provided is null.");
        }
        ArrayList<Cell> possibleMovesOfCell = possibleMoves(givenCell);

        ArrayList<Cell> capturingMoves = new ArrayList<Cell>();

        for(Cell dstCell: possibleMovesOfCell){
            if(isCaptureMove(givenCell, dstCell)){
                capturingMoves.add(dstCell);
            }
        }
        return capturingMoves;
    }




    /**
     * Returns the capture moves of the Piece located in the Cell with given x and y coordinates.
     * This method calls the method getCaptureMoves(Cell givenCell) with the Cell object returned by getCell(x, y) (method of the Board class) as argument.
     * @param x The x-coordinate of the Cell where the Piece, whose capture moves is to be determined, is located.
     * @param y The y-coordinate of the Cell where the Piece, whose capture moves is to be determined, is located.
     * @return An ArrayList of Cell where the Piece located in given coordinates can move and also captures the opponent's piece when performing those moves.
     *          If there is no Piece in the Cell with given coordinates or if the Piece in the Cell with given coordinates does not have any capturing moves, then the returned ArrayList is empty.
     * @throws IllegalArgumentException if the coordinates are out of bound i.e. not in range 0<= x, y <=7
     */
    public ArrayList<Cell> getCaptureMoves(int x, int y) throws  IllegalArgumentException{
        if((x<0 || x>7) || (y<0 || y>7)){
            throw new IllegalArgumentException("Invalid value of x or y provided. (x, y) = (" + x +", " + ")");
        }
        return getCaptureMoves(this.board[x][y]);
    }



    /**
     * Returns whether the given pair of Cell can form a capture move. This method does not check if the destination Cell is a valid move for Piece in source Cell.
     * Therefore, make sure that the destination Cell is from the list given by possibleMoves(...) method applied to source Cell.
     * @param srcCell: The source Cell of the move.
     * @param dstCell: The destination Cell of the move.
     * @retun Returns true if the given pair of Cell can form the capture moves. Returns false otherwise.
     * @throws NullPointerException if source Cell and/or destination Cell is null.
     * @throws IllegalArgumentException if source Cell does not contain any piece.
     *
     */
    public boolean isCaptureMove(Cell srcCell, Cell dstCell) throws NullPointerException, IllegalArgumentException {
        if (srcCell == null) {
            throw new NullPointerException("The source cell is null. Cannot tell if the move is capture move or not if source cell is null.");
        }
        if (dstCell == null) {
            throw new NullPointerException("The destination cell is null. Cannot tell if the move is capture move or not if destination cell is null.");
        }
        if (srcCell.getPiece() == null) {
            throw new IllegalArgumentException("The source cell does not contain a piece. Cannot be capture move if source cell does not have a piece. SrcCell: (" + srcCell.getX() + ", " + srcCell.getY() + ")");
        }

        if ((Math.abs(srcCell.getX() - dstCell.getX()) == 2) && (Math.abs(srcCell.getY() - dstCell.getY()) == 2)) {
            return true;
        }
        return false;
    }





    /**
     * Returns if the given coordinates form a capture move.
     * This method does not check if the given move is a valid move or not.
     * This method calls isCaptureMove(Cell srcCell, Cell dstCell) as isCaptureMove(getCell(givenMove[0], givenMove[1]) , getCell(givenMove[2], givenMove[3]))
     * @param givenMove An integer array of size 4 such that:
     *                  <br> givenMove[0] = x-coordinate of the source Cell
     *                  <br> givenMove[1] = y-coordinate of the source Cell
     *                  <br> givenMove[2] = x-coordinate of the destination Cell
     *                  <br> givenMove[3] = y-coordinate of the destination Cell
     * @return Returns if the given Cell is the capturing move of the given Piece.
     * @throws IllegalArgumentException if the coordinates are out of bound i.e. not in range [0, 7] inclusive
     */
    public boolean isCaptureMove(int[] givenMove) throws IllegalArgumentException{
        if(givenMove.length != 4){
            throw new IllegalArgumentException("The dimension of the array that represents the move does not matches");
        }

        return isCaptureMove(this.board[givenMove[0]][givenMove[1]], this.board[givenMove[2]][givenMove[3]]);
	}


}// End of class

package com.example.dylan.checkers;

import android.content.Context;
import java.util.ArrayList;
import java.io.OutputStreamWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileNotFoundException;

public class Board{
	private Cell[][] board;
	private ArrayList<Piece> lightPieces;
	private ArrayList<Piece> darkPieces;


	private static int BOARD_SIZE = 8;

	public Board(){
		this.lightPieces = new ArrayList<Piece>();
		this.darkPieces = new ArrayList<Piece>();
		board = new Cell[Board.BOARD_SIZE][Board.BOARD_SIZE];
		initialBoardSetup();
		//winScenarioTest();
	}

	//sets up the board in intial configuration which is as follows.
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

	public void winScenarioTest(){
		for(int i=0; i < Board.BOARD_SIZE; i++) {
			for (int j = 0; j < Board.BOARD_SIZE; j++) {
				this.board[i][j] = new Cell(i, j);
			}
		}

		this.board[1][1].placePiece(new Piece(Piece.LIGHT));
		this.board[1][3].placePiece(new Piece(Piece.LIGHT));
		this.board[1][1].getPiece().makeKing();
		this.board[1][3].getPiece().makeKing();
		lightPieces.add(this.board[1][1].getPiece());
		lightPieces.add(this.board[1][3].getPiece());

		this.board[2][2].placePiece(new Piece(Piece.DARK));
		this.board[2][4].placePiece(new Piece(Piece.DARK));
		this.board[2][2].getPiece().makeKing();
		this.board[2][4].getPiece().makeKing();
		darkPieces.add(this.board[2][2].getPiece());
		darkPieces.add(this.board[2][4].getPiece());


	}// end of initialBoardSetup


	//returns the cell in specified indices
	//if the indices are invalid returns null
	public Cell getCell(int x, int y){
		if((x<0 || x > 7) || (y<0 || y >7)){
			throw new IllegalArgumentException("The coordinates provided are outside of the board");
		}

		return this.board[x][y];
	}

	public ArrayList<Piece> getPieces(String givenColor){
		if(givenColor.equals(Piece.LIGHT)){
			return this.lightPieces;
		}
		else if(givenColor.equals(Piece.DARK)){
			return this.darkPieces;
		}
		throw new IllegalArgumentException("Given color is not the color of the pieces in board. Given color: " + givenColor);
	}



	//moves the piece from coordinate (fromX, fromY) to coordinate (toX, toY)
	//Does not
	public ArrayList<Cell> movePiece(int fromX, int fromY, int toX, int toY) throws NullPointerException, IllegalArgumentException{
		Cell srcCell = this.getCell(fromX, fromY);
		Cell dstCell = this.getCell(toX, toY);
		ArrayList<Cell> changedCells = new ArrayList<Cell>();
		if(srcCell == null){
			throw new NullPointerException("The source cell is null. Cannot move from null cell");
		}
		if(dstCell == null){
			throw new NullPointerException("The destination cell is null. Cannot move to null cell");
		}
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





	//overloaded method
	public ArrayList<Cell> movePiece(int[] src, int[] dst){
		if(src.length != 2 || dst.length != 2){
			throw new IllegalArgumentException("The given dimension of the points does not match.");
		}
		return movePiece(src[0], src[1], dst[0], dst[1]);
	}

	//overloaded method
	public ArrayList<Cell> movePiece(int[] move){
		if(move.length != 4){
			throw new IllegalArgumentException("The given dimension of the points does not match.");
		}
		return movePiece(move[0], move[1], move[2], move[3]);
	}


	public void removePiece(Piece capturedPiece) throws IllegalStateException, IllegalArgumentException{
		if(capturedPiece.getColor().equals(Piece.LIGHT)){
			if(!lightPieces.remove(capturedPiece)){
				throw new IllegalStateException("The captured piece was not removed");
			}
			capturedPiece.getCell().placePiece(null);
		}
		else if(capturedPiece.getColor().equals(Piece.DARK)){
			if(!darkPieces.remove(capturedPiece)){
				throw new IllegalStateException("The captured piece was not removed");
			}
			capturedPiece.getCell().placePiece(null);
		}
		else{
			throw new IllegalArgumentException("The captured piece's color does not match the color of the pieces on board");
		}
	}


	//overloaded method for possibleMoves
	//returns ArrayList<Cell> of possible moves for given cell located at given coordinate
	//If the coordinates are out of bound it throws NullPointerException
	public ArrayList<Cell> possibleMoves(int x, int y) throws NullPointerException{
		if((x<0 || x>7) || (y<0 || y>7)){
			System.out.println("Board::possibleMoves(int x, int y): Invalid value of x or y provided.");
			throw new NullPointerException();
		}

		return possibleMoves(this.board[x][y]);
	}




	//retrurns ArrayList<Cell> of possible moves for given cell
	//If the given cell does not contain any piece then the arrayList will be empty i.e returnedArrayList.size() == 0
	//Will throw NullPointerException when null is passed as parameter
	public ArrayList<Cell> possibleMoves(Cell givenCell) throws NullPointerException{

		if(givenCell == null){
			System.out.println("Board::possibleMoves(Cell givenCell): Called with null argument");
			throw new NullPointerException();
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



	public ArrayList<Cell> possibleMoves(Piece givenPiece) {
		return possibleMoves(givenPiece.getCell());
	}

	public ArrayList<Cell> getCaptureMoves(Cell givenCell){
		int srcX = givenCell.getX();
		int srcY = givenCell.getY();

		ArrayList<Cell> possibleMovesOfCell = possibleMoves(givenCell);

		ArrayList<Cell> capturingMoves = new ArrayList<Cell>();

		for(Cell dstCell: possibleMovesOfCell){
			int dstX = dstCell.getX();
			int dstY = dstCell.getY();

			if(Math.abs(dstX - srcX) == 2 && Math.abs(dstY - srcY)==2){
				capturingMoves.add(dstCell);
			}
		}
		return capturingMoves;
	}


	public ArrayList<Cell> getCaptureMoves(int x, int y){
		return getCaptureMoves(this.board[x][y]);
	}

	public ArrayList<Cell> getCaptureMoves(Piece givenPiece ){
		return getCaptureMoves(givenPiece.getCell());
	}



	/**
	 * @param srcCell: The source cell of the move.
	 * @param dstCell: The destination cell of the move.
	 *
	 * @retun Returns true if the given pair of cells can form the capture moves. Returns false otherwise
	 * 		  This method does not check if the dstCell is a valid move for piece in srcCell.
	 * 		  Therefore, for proper functioning, dstCell must be from the list given by possbileMoves(...) method
	 *
	 * @throws NullPointerException if srcCell or dstCell is null.
	 * @throws IllegalArgumentException if srcCell does not contain any piece.
	 *
	 */
	public boolean isCaptureMove(Cell srcCell, Cell dstCell) throws NullPointerException, IllegalArgumentException{
		if(srcCell == null){
			throw new NullPointerException("The source cell is null. Cannot tell if the move is capture move or not if source cell is null.");
		}
		if(dstCell == null){
			throw new NullPointerException("The destination cell is null. Cannot tell if the move is capture move or not if destination cell is null.");
		}
		if(srcCell.getPiece() == null){
			throw new IllegalArgumentException("The source cell does not contain a piece. Cannot be capture move if source cell does not have a piece. SrcCell: (" + srcCell.getX() + ", "+ srcCell.getY() + ")" );
		}

		if((Math.abs(srcCell.getX()-dstCell.getX()) == 2) && (Math.abs(srcCell.getY()- dstCell.getY()) == 2)){
			return true;
		}
		return false;
	}


	/**
	 * @param givenPiece: The piece whose move is to be checked.
	 * @param dstCell: The destination cell of the move.
	 *
	 * @retun Returns if the given dstCell is the capturing move of the given piece.
	 * 		  This method does not check if dstCell is a valid move for given piece.
	 * 		  Therefore, for proper functioning, dstCell must be from the list given by possbileMoves(...) method
	 *
	 * @throws NullPointerException if dstCell is null
	 *
	 */
	public boolean isCaptureMove(Piece givenPiece, Cell dstCell) throws NullPointerException{
		return isCaptureMove(givenPiece.getCell(), dstCell);
	}

	public boolean isCaptureMove(int[] givenMove){
		if(givenMove.length != 4){
			throw new IllegalArgumentException("The dimension of the array that represents the move does not matches");
		}

		return isCaptureMove(this.board[givenMove[0]][givenMove[1]], this.board[givenMove[2]][givenMove[3]]);
	}




	// Methods for debugging purposes

	public String toString(){
		StringBuilder string = new StringBuilder();
		string.append("  1 2 3 4 5 6 7 8\n");
		for(int i=0; i< Board.BOARD_SIZE; i++){
			string.append((i+1)+ " ");
			for(int j=0; j <Board.BOARD_SIZE; j++){
				Cell cell = this.board[i][j];
				if(cell.getPiece() == null){
					string.append("_ ");
				}
				else if (cell.getPiece().getColor().equals(Piece.LIGHT)){
					string.append("L ");
				}
				else if(cell.getPiece().getColor().equals(Piece.DARK)){
					string.append("D ");
				}
			}
			string.append("\n");
		}
		return string.toString();

	}

	public String possibleMovesAsString(int x, int y){
		StringBuilder builder = new StringBuilder();
		Cell givenCell = this.board[x][y];
		builder.append(givenCell.toString());
		ArrayList<Cell> possibleCells = possibleMoves(x, y);
		builder.append("Possible Next Moves are: \n");
		for(Cell cell: possibleCells){
			builder.append(cell.toString());
		}

		possibleCells = getCaptureMoves(givenCell);
		builder.append("Capture Moves are: \n");
		for(Cell cell: possibleCells){
			builder.append(cell.toString());
		}

		return builder.toString();
	}

	public void LoadGameState(Context context) {

		String saveData = "";

		try {
			InputStream inputStream = context.openFileInput("savedFile.dat");

			if ( inputStream != null ) {
				InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
				BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
				String receiveString = "";
				StringBuilder stringBuilder = new StringBuilder();

				while ( (receiveString = bufferedReader.readLine()) != null ) {
					stringBuilder.append(receiveString);
				}

				inputStream.close();
				saveData = stringBuilder.toString();
			}
		}
		catch (FileNotFoundException e) {
			System.out.println("File not found: " + e.getMessage());
		} catch (IOException e) {
			System.out.println("Can not read file: " + e.getMessage());
		}

		// If there is a save file
		if(saveData != "") {
			String[] boardRows = saveData.split(";");

			for (int i = 0; i < Board.BOARD_SIZE; i++) {
				for (int j = 0; j < Board.BOARD_SIZE; j++) {
					this.board[i][j] = new Cell(i, j);
				}
			}

			System.out.println("**** BEGIN LOADING ****");
			for (int i = 0; i < Board.BOARD_SIZE; i++) {
				String thisRow[] = boardRows[i].split(",");
				for (int j = 0; j < thisRow.length; j++) {
					if (thisRow[j].equalsIgnoreCase("L")) {
						this.board[i][j].placePiece(new Piece(Piece.LIGHT));
						lightPieces.add(this.board[i][j].getPiece());
						//place a red piece at i, j
					} else if (thisRow[j].equalsIgnoreCase("D")) {
						this.board[i][j].placePiece(new Piece(Piece.DARK));
						darkPieces.add(this.board[i][j].getPiece());
						//place a black piece at i, j
					} else if (thisRow[j].equalsIgnoreCase("_")) {
						//do nothing
					}
				}
			}
			System.out.println("**** END LOADING ****");
		}
		// If there is no save file
		else if(saveData == ""){
			System.out.println("No file to load!");
		}
	}

	public void SaveGameState(Context context) {
		StringBuilder string = new StringBuilder();
		String newString = null;
		for(int i=0; i< Board.BOARD_SIZE; i++){
			for(int j=0; j <Board.BOARD_SIZE; j++){
				Cell cell = this.board[i][j];
				if(cell.getPiece() == null){
					string.append("_,");
				}
				else if (cell.getPiece().getColor().equals(Piece.LIGHT)){
					string.append("L,");
				}
				else if(cell.getPiece().getColor().equals(Piece.DARK)){
					string.append("D,");
				}
			}
			string.append(";");
			newString = string.toString().replace(",;", ";");
		}

		try {
			OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("savedFile.dat", Context.MODE_PRIVATE));
			outputStreamWriter.write(newString);
			outputStreamWriter.close();
			System.out.println("Saved! Location: " + context.getFilesDir() + "/savedFile.dat");
		}
		catch (IOException e) {
			System.out.println("Error writing to file! " + e.getMessage());
		}
	}

	public static void main(String[] args){
		Board board = new Board();
		System.out.println(board.toString());
		System.out.println(board.possibleMovesAsString(5, 3));
	}// End of main
}// End of class

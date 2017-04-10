package com.uno.dbbc.checkers;

import java.io.Serializable;

public class Cell implements Serializable{


	private int x;
	private int y;
	private Piece placedPiece;

	/**
	 * Creates an instance of Cell with given x-coordinate and y-coordinate with no piece placed in the cell i.e. piece placed in the cell is null.
	 * @param x The x-coordinate of the cell.
	 * @param y The y-coordinate of the cell.
	 */
	public Cell(int x, int y){
		if((x<0 || x>7) || (y<0 || y>7)){
			System.out.println("The provided coordinates for the cell are out of range.");
			return;
		}
		this.x = x;
		this.y = y;
		this.placedPiece = null;
	}

	/**
	 * Creates an instance of Cell with given x-coordinate and y-coordinate and given piece placed in the cell.
	 * @param x The x-coordinate of the cell.
	 * @param y The y-coordinate of the cell.
	 * @param givenPiece The piece to be placed in the Cell.
	 * @throws IllegalArgumentException Throws IllegalArgumentException if the given x-coordinate or y-coordinate are not in the range [0,7] inclusive.
	 */
	public Cell(int x, int y, Piece givenPiece) throws IllegalArgumentException{
		if((x<0 || x>7) || (y<0 || y>7)){
			throw new IllegalArgumentException("The provided coordinates for the cell are out of range.");
		}
		this.x = x;
		this.y = y;
		this.placedPiece = givenPiece;
	}


	/**
	 * @return Returns the piece placed (null if no piece is placed) in this cell.
	 */
	public Piece getPiece(){
		return this.placedPiece;
	}

	/**
	 * @return Returns the x-coordinate of this cell.
	 */
	public int getX(){
		return this.x;
	}

	/**
	 * @return Returns the y-coordinate of this cell.
	 */
	public int getY(){
		return this.y;
	}


	/**
	 * @return Returns the coordinate of this cell as an integer array of length two,
	 * 			in which the first element is the x-coordinate of the cell and the second value is the y-coordinate of the cell.
	 */
	public int[] getCoords(){
		int[] coords = {x, y};
		return coords;
	}



	/**
	 * @param givenPiece The piece to place in this cell. If the piece are to their opposite end then the piece is made King.
	 */
	public void placePiece(Piece givenPiece){
		this.placedPiece = givenPiece;
		if(givenPiece != null){
			givenPiece.setCell(this);
			if(this.x == 0 && givenPiece.getColor().equals(Piece.DARK)){
				this.placedPiece.makeKing();
			}
			else if(this.x == 7 && givenPiece.getColor().equals(Piece.LIGHT)){
				this.placedPiece.makeKing();
			}
		}
	}

	/**
	 * @return Returns if the cell contains any piece i.e returns true if this cell contains piece and false if the placed piece of this cell is null.
	 */
	public boolean containsPiece(){
		if(this.placedPiece == null){
			return false;
		}

		return true;
	}


	/**
	 * @param anotherCell Cell where the piece in this cell is to be moved.
	 * @throws IllegalArgumentException Throws IllegalArgumentException if the Cell provided is null.
	 */
	public void movePiece(Cell anotherCell) throws IllegalArgumentException{
		if(anotherCell == null){
			throw new IllegalArgumentException("Provided cell is null. Cannot move to a null Cell.");
		}
		anotherCell.placePiece(this.placedPiece);
		this.placedPiece.setCell(anotherCell);
		this.placedPiece = null;
	}


	/**
	 * @return String representation of the Cell.
	 */
	public String toString(){
		String str = "";
		str += "Cell Loc: ("+ this.x + ", " + this.y + ") \t Placed piece: ";
		if(this.placedPiece == null){
			str += "nothing\n";
		}
		else{
			str += this.placedPiece.getColor() + "  isKing: " + placedPiece.isKing()+ "\n";
		}
		return str;
	}
}// End of class





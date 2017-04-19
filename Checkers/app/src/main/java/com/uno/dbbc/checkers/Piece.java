
package com.uno.dbbc.checkers;

import java.io.Serializable;

/**
 * Class representing a piece in Checker. A piece can have one of the two colors: Light (given by Piece.LIGHT) and Dark (given by Piece.DARK).
 * A piece can become king if it reaches the opposite end.
 */

public class Piece implements Serializable{

	private String color;
	private boolean isKing;
	private Cell placedCell;


	public static final String DARK = "Dark";
	public static final String LIGHT = "Light";

	/**
	 * Creates an instance of Piece with given color.
	 * @param color The color of the piece (has to be Piece.LIGHT or Piece.DARK).
	 * @throws IllegalArgumentException Throws IllegalArgumentException if the given color is not equal to Piece.LIGHT or Piece.DARK
	 */
	public Piece(String color) throws IllegalArgumentException{
		if(!(color.equals(Piece.DARK) || color.equals(Piece.LIGHT))){
			throw new IllegalArgumentException("The provided color for piece is not valid. Provided color: " + color);
		}
		this.color = color;
		this.isKing = false;
		this.placedCell = null;
	}

	/**
	 * Sets the placed piece of the given Cell to be this Piece.
	 * @param givenCell The Cell in which this Piece is to be placed.
	 */
	public void setCell(Cell givenCell){
		this.placedCell = givenCell;
	}

	/**
	 * @return Returns the cell on which this Piece is placed.
	 */
	public Cell getCell(){
		return this.placedCell;
	}


	/**
	 * @return Returns the color of this Piece (either Piece.LIGHT or Piece.DARK).
	 */
	public String getColor(){
		return this.color;
	}


	/**
	 * @return Returns if this Piece is a King. Returns true if this Piece is King, false otherwise.
	 */
	public boolean isKing(){
		return this.isKing;
	}


	/**
	 * Makes this piece a King.
	 */
	public void makeKing(){
		this.isKing = true;
	}



	/**
	 * Returns the color of the opponent player i.e. returns the color opposite of this Piece
	 * @param: Color of the player
	 * @return: opponent's color
	 */
	public static String getOpponentColor(String givenColor){
		if(givenColor.equals(Piece.DARK)){
			return Piece.LIGHT;
		}
		else if(givenColor.equals(Piece.LIGHT)){
			return Piece.DARK;
		}
		else{
			System.out.println("Given color is not valid. Given color: " + givenColor);
			return null;
		}
	}

	/**
	 * Checks if the given Object is equal to this Piece.
	 * @param obj Object to be compared
	 * @return Returns true if the given object is equal to this Piece, false otherwise.
	 *          The given object is equal to this Piece if the given object is an instance of Piece, has the same color as this Piece and is located in the same Cell location as this Piece.
	 */
	@Override
	public boolean equals(Object obj){
		if(!(obj instanceof Piece)){
			return false;
		}

		Piece givenPiece =  (Piece) obj;

		if(givenPiece.getColor().equals(this.color) && givenPiece.isKing() == this.isKing &&
				givenPiece.getCell().getX() == this.placedCell.getX() && givenPiece.getCell().getY() == this.placedCell.getY()){
			return true;
		}
		return false;
	}
}//End of class
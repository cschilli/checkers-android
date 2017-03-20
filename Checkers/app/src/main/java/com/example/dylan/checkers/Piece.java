
package com.example.dylan.checkers;

import java.util.ArrayList;


public class Piece{
	
	private String color;
	private boolean isKing;
	private Cell placedCell;
	
	
	public static final String DARK = "Dark";
	public static final String LIGHT = "Light";
	
	
	/**
	 * @param color
	 */
	public Piece(String color){
		if(!(color.equals(Piece.DARK) || color.equals(Piece.LIGHT))){
			System.out.println("The provided color for piece is not valid: " + color);
			return;
		}
		this.color = color;
		this.isKing = false;
		this.placedCell = null;
	}
	
	/**
	 * @param givenCell
	 */
	public void setCell(Cell givenCell){
		this.placedCell = givenCell;
	}
	
	/**
	 * @return
	 */
	public Cell getCell(){
		return this.placedCell;
	}
	
	/**
	 * @return
	 */
	public String getColor(){
		return this.color;
	}
	
	/**
	 * @return
	 */
	public boolean isKing(){
		return this.isKing;
	}

	//sets this piece as king
	/**
	 * 
	 */
	public void makeKing(){
		this.isKing = true;
	}
	
	
	/**
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
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
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
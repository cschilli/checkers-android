package com.example.dylan.checkers;

public class Cell{
	
	
	
	private int x;
	
	private int y;
	
	private Piece placedPiece;
	
	
	/**
	 * @param x
	 * @param y
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
	
	//overloaded constructor
	/**
	 * @param x
	 * @param y
	 * @param givenPiece
	 */
	public Cell(int x, int y, Piece givenPiece){
		if((x<0 || x>7) || (y<0 || y>7)){
			System.out.println("The provided coordinates for the cell are out of range.");
			return;
		}
		this.x = x;
		this.y = y;
		this.placedPiece = givenPiece;
	}
	
	
	
	/**
	 * @return Piece after placement
	 */
	public Piece getPiece(){
		return this.placedPiece;
	}
	
	/**
	 * @return X coordinate
	 */
	public int getX(){
		return this.x;
	}
	
	/**
	 * @return Y coordinate
	 */
	public int getY(){
		return this.y;
	}
	
	
	/**
	 * @return array of X,Y coords
	 */
	public int[] getCoords(){
		int[] coords = {x, y};
		return coords;
	}
	
	
	
	//places the given piece to this cell
	//if the pieces are to their opposite end then makes the piece as king
	/**
	 * @param givenPiece
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
	
	
	//checks if this cell has any piece
	/**
	 * @return boolean 
	 */
	public boolean containsPiece(){
		if(this.placedPiece == null){
			return false;
		}
		
		return true;
	}
	
	
	//moves the piece from this cell to given cell
	/**
	 * @param anotherCell
	 */
	public void movePiece(Cell anotherCell){
		anotherCell.placePiece(this.placedPiece);
		this.placedPiece.setCell(anotherCell);
		this.placedPiece = null;
	}
	
	
	
	//Created for debugging purpose
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
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





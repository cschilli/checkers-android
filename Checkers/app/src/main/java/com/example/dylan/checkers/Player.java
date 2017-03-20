
package com.example.dylan.checkers;

public abstract class Player{
	private String color;



	/**
	 * @param Color of the piece that the player is associated to
	 * @throws IllegalArgumentException if the given color does not matches with one of the two colors of the pieces in the board
	 */
	public Player(String givenColor) throws IllegalArgumentException{
		if((givenColor == Piece.LIGHT) || (givenColor == Piece.DARK)){
			this.color = givenColor;
		}
		else{
			throw new IllegalArgumentException("Given color for the player is not valid. Given color: " + givenColor);
		}
	}

	public String getColor(){
		return this.color;
	}


	//TODO: make this more suitable as number of pieces might not be used in the model.
	public int getNumOfPieces(Board givenBoard){
		return givenBoard.getPieces(this.color).size();
	}


	public abstract int[] getMove(Board givenState);

	public abstract int[] getMoveSrcCell(Board givenState);

	public abstract int[] getMoveDstCell(Board givenState);




	@Override
	public boolean equals(Object obj){
		if(! (obj instanceof Player)){
			return false;
		}

		Player givenPlayer = (Player) obj;

		if(givenPlayer.getColor().equals(this.color)){
			return true;
		}

		return false;
	}
}
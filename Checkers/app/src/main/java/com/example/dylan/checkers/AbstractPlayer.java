
package com.example.dylan.checkers;

public abstract class AbstractPlayer{
	private String color;
	
	
	
	/**
	 * @param Color of the piece that the player is associated to
	 * @throws IllegalArgumentException if the given color does not matches with one of the two colors of the pieces in the board
	 */
	public AbstractPlayer(String givenColor) throws IllegalArgumentException{
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
//		System.out.println(givenState);
//		System.out.println(this.color +  "'s turn");
//		String srcCellString = "";
//		while(srcCellString.length() != 2){
//			System.out.print("Please select the source cell (For cell at (0,2) enter 02): ");
//			srcCellString = input.next();
//			if(srcCellString.length() != 2){
//				System.out.println("\nInvalid format for source cell");
//			}
//		}
//		
//		String dstCellString = "";
//		while(dstCellString.length != 2){
//			System.out.print("Please select the destination cell (For cell at (3,2) enter 32): ");
//			dstCellString = input.next();
//			if(srcCellString.length() != 2){
//				System.out.println("\nInvalid format for destination cell");
//			}
//		}
//		
//		int[] move = {Integer.valueOf(srcCellString.substring(0,1)), Integer.valueOf(srcCellString.substring(1)),
//					   Integer.valueof(dstCellString.substring(0,1)), Integer.valueOf(dstCellString.substring(1))};
//		
//		return move;
	
	
	
	
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

package com.uno.dbbc.checkers;

import java.io.Serializable;
import java.util.ArrayList;

public class Player implements Serializable{

	private String color;

	/**
	 * @param givenColor of the piece that the player is associated to
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


	public boolean hasMoves(Board board){
        ArrayList<Piece> pieces = board.getPieces(this.color);
        if(pieces.size() > 0){
            for(Piece piece: pieces){
                if(board.possibleMoves(piece).size() > 0){
                    return true;
                }
            }
        }
        return false;
    }

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
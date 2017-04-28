package com.uno.dbbc.checkers;

import java.util.ArrayList;
import java.util.Scanner;

public class Game{

	private enum GameStatus {DRAW, OVER, RUNNING};

	private GameStatus gameStatus;
	private Board board;
	private PlayerTUI player1;
	private PlayerTUI player2;
	private PlayerTUI currentPlayer;



	public Game(PlayerTUI player1, PlayerTUI player2){
		this.board = new Board();
		this.player1 = player1;
		this.player2 = player2;
		this.currentPlayer = this.player1;
		this.gameStatus = GameStatus.RUNNING;

	}



	public void run(){
		while(this.gameStatus == GameStatus.RUNNING){
			displayBoard();
			boolean validMove = false;
			int[] givenMove= null;

			while(!validMove){
				givenMove = this.currentPlayer.getMove(this.board);
				validMove = checkIfValidMove(this.currentPlayer, givenMove);
				if(!validMove){
					System.out.print("The move is not valid. Please try again. ");
				}
			}
			boolean capturingMove = this.board.isCaptureMove(givenMove);
			this.board.movePiece(givenMove);

			if(capturingMove){
				boolean hopAgain = (this.board.getCaptureMoves(givenMove[2], givenMove[3]).size() > 0);

				int[] srcCell = {givenMove[0], givenMove[1]};
				int[] dstCell  = {givenMove[2], givenMove[2]};
				while(hopAgain){
					displayBoard();
					System.out.println("You just captured opponent piece and have 1 or more capturing moves. Please select one of the capturing move.");
					srcCell = dstCell;
					validMove = false;
					while(!validMove){
						dstCell = this.currentPlayer.getMoveDstCell(this.board);
						validMove = checkIfValidMove(this.currentPlayer, srcCell, dstCell);
						if(!validMove || !this.board.isCaptureMove(givenMove)){
							validMove = false;
							System.out.print("The move is not valid. Please select again. ");
						}
						else{
							int[] move = {srcCell[0], srcCell[1], dstCell[0], dstCell[1]};
							this.board.movePiece(move);
							hopAgain = (this.board.getCaptureMoves(dstCell[0], dstCell[1]).size() > 0);
						}
					}
				}
			}
			this.gameStatus = getGameStatus();
			changeTurn();
		}


		if(this.gameStatus == GameStatus.DRAW){
			System.out.println("The game is draw.");
		}
		else if(hasMoves(this.player1)){
			System.out.println(this.player1.getColor() + " has won!");
		}
		else{
			System.out.println(this.player2.getColor() + "has won! ");
		}
	}


	public boolean checkIfValidMove(Player givenPlayer, int[] srcCell, int[] dstCell){
		int[] move = {srcCell[0], srcCell[1], dstCell[0], dstCell[1]};
		return checkIfValidMove(givenPlayer, move );
	}

	public boolean checkIfValidMove(Player givenPlayer, int[] givenMove){
		Cell srcCell = this.board.getCell(givenMove[0], givenMove[1]);
		Cell dstCell = this.board.getCell(givenMove[2], givenMove[3]);

		if(srcCell == null || dstCell == null || (srcCell.getPiece() == null)){
			return false;
		}

		else if(!srcCell.getPiece().getColor().equals(givenPlayer.getColor())){
			return false;
		}

		ArrayList<Cell> moves = this.board.possibleMoves(srcCell);
		for(Cell temp: moves){
			if(temp.getX() == dstCell.getX() && temp.getY() == dstCell.getY()){
				return true;
			}
		}
		return false;
	}


	public GameStatus getGameStatus(){
		boolean player1HasMove = hasMoves(this.player1);
		boolean player2HasMove = hasMoves(this.player2);

		if(!player1HasMove && !player2HasMove ){
			return GameStatus.DRAW;
		}

		else if((player1HasMove && !player2HasMove) || (player2HasMove && !player1HasMove)){
			return GameStatus.OVER;
		}

		return GameStatus.RUNNING;
	}





	public boolean hasMoves(Player givenPlayer){
		ArrayList<Piece> pieces = this.board.getPieces(givenPlayer.getColor());
		if(pieces.size() > 0){
			for(Piece piece: pieces){
				if(this.board.possibleMoves(piece).size() > 0){
					return true;
				}
			}
		}
		return false;
	}



	public void changeTurn(){
		if(this.currentPlayer.equals(player1)){
			this.currentPlayer = player2;
		}
		else{
			this.currentPlayer = player1;
		}
	}

	public void displayBoard(){
		System.out.println(this.board.toString());
		//stub method
	}


	public static void main(String[] args){
		Scanner input = new Scanner(System.in);

		int color = 0;
		while(!(color ==1 || color == 2)){
			System.out.println("Please select color for Player 1: \n 1) " + Piece.LIGHT + "\n 2) " + Piece.DARK);
			while (!input.hasNextInt()) {
				System.out.println("Not a valid input type. Please enter 1 or 2");
				input.next();
			}
			color = input.nextInt();
			if(color != 1 && color != 2){
				System.out.println("Invalid input. Please enter 1 or 2.");
			}
		}

		PlayerTUI player1 = null;
		PlayerTUI player2 = null;

		if(color == 1){
			player1 = new PlayerTUI(Piece.LIGHT);
			player2 = new PlayerTUI(Piece.DARK);
		}
		else if(color == 2){
			player1 = new PlayerTUI(Piece.DARK);
			player2 = new PlayerTUI(Piece.LIGHT);
		}

		Game game = new Game(player1, player2);
		game.run();




	}




}
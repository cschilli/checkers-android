package com.uno.dbbc.checkers;

import java.io.Serializable;
import java.util.Scanner;

public class PlayerTUI extends Player implements Serializable{
	private Scanner input;

	public PlayerTUI(String color){
		super(color);
		input = new Scanner(System.in);
	}



	public int[] getMove(Board givenState){
		System.out.println(super.getColor() + "'s turn. ");
		int[] srcCell = getMoveSrcCell(givenState);
		int[] dstCell = getMoveDstCell(givenState);

		int[] move = {srcCell[0], srcCell[1], dstCell[0], dstCell[1]};

		return move;
	}


	public int[] getMoveSrcCell(Board givenState){
		String srcCellString = "";
		while(srcCellString.length() != 2){
			System.out.print("Please select the source cell (For cell at (1, 2) enter 12. Row # first and then column #): ");
			srcCellString = input.next();
			if(srcCellString.length() != 2){
				System.out.println("\n Invalid input format. Please try again");
			}
		}
		int[] srcCell = {Integer.valueOf(srcCellString.substring(0,1)) -1, Integer.valueOf(srcCellString.substring(1)) -1};
		return srcCell;
	}



	public int[] getMoveDstCell(Board givenState){
		String dstCellString = "";
		while(dstCellString.length() != 2){
			System.out.print("Please select the destination cell (For cell at (3,2) enter 32. Row # first and then column #): ");
			dstCellString = input.next();
			if(dstCellString.length() != 2){
				System.out.println("\nInvalid input format. Please try again.");
			}
		}
		int[] dstCell = {Integer.valueOf(dstCellString.substring(0,1)) -1, Integer.valueOf(dstCellString.substring(1)) -1};
		return dstCell;
	}
}
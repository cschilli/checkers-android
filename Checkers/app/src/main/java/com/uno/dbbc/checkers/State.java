package com.uno.dbbc.checkers;

import java.io.Serializable;

/**
 * Created by bregmi1 on 3/31/2017.
 */

public class State implements Serializable{

    private Board board;
    private Player currentPlayer;
    private Player player1;
    private Player player2;
    private boolean singlePlayerMode;


    public State(Board givenBoard, Player player1, Player player2, Player currentPlayer, boolean singlePlayerMode){
        this.board = givenBoard;
        this.player1 = player1;
        this.player2 = player2;
        this.currentPlayer = currentPlayer;
        this.singlePlayerMode = singlePlayerMode;
    }

    public Board getBoard(){
        return board;
    }

    public Player getPlayer1(){
        return player1;
    }

    public Player getPlayer2(){ return player2;}

    public Player getCurrentPlayer() { return currentPlayer;}

    public boolean isSinglePlayerMode(){ return  this.singlePlayerMode; }

}

package com.example.dylan.checkers;

import java.io.Serializable;

/**
 * Created by bregmi1 on 3/31/2017.
 */

public class State implements Serializable{

    private Board board;
    private Player player;

    public State(Board givenBoard, Player givenPlayer){
        this.board = givenBoard;
        this.player = givenPlayer;
    }

    public Board getBoard(){
        return board;
    }

    public Player getPlayer(){
        return player;
    }
}

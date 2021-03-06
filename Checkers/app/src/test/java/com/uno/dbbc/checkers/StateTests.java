package com.uno.dbbc.checkers;

import org.junit.Test;

import static org.junit.Assert.*;
/**
 * Created by Chris on 4/28/2017.
 */

public class StateTests {

    @Test
    public void testState() throws Exception{

        Board board = new Board();
        board.initialBoardSetup();
        Player player1 = new Player("Dark");
        Player player2 = new Player("Light");

        State state = new State(board, player1, player2, player1, false, null, null, false);

        assertEquals("Dark", state.getPlayer1().getColor());
        assertEquals("Light", state.getPlayer2().getColor());
        assertNotEquals("Dark", state.getPlayer2().getColor());
        assertNotEquals("Light", state.getPlayer1().getColor());
        assertEquals(false, state.isSinglePlayerMode());
        assertEquals(player1, state.getCurrentPlayer());
        assertEquals(null, state.getSrcCell());
        assertEquals(null, state.getDstCell());
        assertEquals(false, state.isSrcCellFixed());
    }

    @Test
    public void testGetBoard() throws Exception{

        Board board = new Board();
        board.initialBoardSetup();
        Player player1 = new Player("Dark");
        Player player2 = new Player("Light");

        State state = new State(board, player1, player2, player1, true, null, null, false);

        assertEquals(board, state.getBoard());
    }

    @Test
    public void testGetPlayers() throws Exception{

        Board board = new Board();
        board.initialBoardSetup();
        Player player1 = new Player("Dark");
        Player player2 = new Player("Light");

        State state = new State(board, player1, player2, player1, true, null, null, false);

        assertEquals(player1, state.getPlayer1());
        assertNotEquals(player2, state.getPlayer1());
        assertEquals(player2, state.getPlayer2());
        assertNotEquals(player1, state.getPlayer2());
        assertEquals(player1, state.getCurrentPlayer());
        assertNotEquals(player2, state.getCurrentPlayer());
    }

}

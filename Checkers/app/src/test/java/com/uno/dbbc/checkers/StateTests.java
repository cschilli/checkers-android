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
        Player player1 = new Player("Dark");
        Player player2 = new Player("Light");

        State state = new State(board, player1, player2, player1);

        assertEquals("Dark", state.getPlayer1().getColor());
        assertEquals("Light", state.getPlayer2().getColor());
        assertNotEquals("Dark", state.getPlayer2().getColor());
        assertNotEquals("Light", state.getPlayer1().getColor());
        assertEquals(player1, state.getCurrentPlayer());
    }

    @Test
    public void testGetBoard() throws Exception{

        Board board = new Board();
        Player player1 = new Player("Dark");
        Player player2 = new Player("Light");

        State state = new State(board, player1, player2, player1);

        assertEquals(board, state.getBoard());
    }

    @Test
    public void testGetPlayers() throws Exception{

        Board board = new Board();
        Player player1 = new Player("Dark");
        Player player2 = new Player("Light");

        State state = new State(board, player1, player2, player1);

        assertEquals(player1, state.getPlayer1());
        assertNotEquals(player2, state.getPlayer1());
        assertEquals(player2, state.getPlayer2());
        assertNotEquals(player1, state.getPlayer2());
        assertEquals(player1, state.getCurrentPlayer());
        assertNotEquals(player2, state.getCurrentPlayer());
    }

}

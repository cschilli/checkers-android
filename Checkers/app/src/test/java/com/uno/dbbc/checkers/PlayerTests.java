package com.uno.dbbc.checkers;

import org.junit.Test;

import static org.junit.Assert.*;
/**
 * Created by Chris on 4/28/2017.
 */

public class PlayerTests {


    @Test
    public void testPlayer() throws Exception{
        Player player = new Player("Dark");

        assertEquals("Dark", player.getColor());
        assertNotEquals("Light", player.getColor());
    }

    @Test
    public void testHasMoves() throws Exception{
        Player player = new Player("Dark");
        Board board = new Board();
        assertFalse(player.hasMoves(board));
        board.initialBoardSetup();
        assertTrue(player.hasMoves(board));

    }


}

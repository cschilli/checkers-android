package com.example.dylan.checkers;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void boardTest() throws Exception{
        //test correct instantiation of board
        Board myBoard = new Board();
        assertTrue(myBoard != null);
    }

    @Test
    public void movePieceTest() throws Exception{
        //test correct piece moved to correct location

    }

    @Test
    public void getCellTest() throws Exception{
        //test correct cell is returned

    }
}
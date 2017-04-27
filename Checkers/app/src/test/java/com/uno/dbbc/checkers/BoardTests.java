package com.uno.dbbc.checkers;

import org.junit.Test;

import static org.junit.Assert.*;


public class BoardTests {

    @Test
    public void boardInstanceTest() throws Exception{
        //test correct instantiation of board
        Board myBoard = new Board();
        assertTrue(myBoard != null);
    }
}
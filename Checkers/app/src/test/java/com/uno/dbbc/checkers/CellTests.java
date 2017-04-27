package com.uno.dbbc.checkers;

import org.junit.Test;

import static org.junit.Assert.*;

public class CellTests {

    @Test
    public void testMovePiece() throws Exception{
        //test correct piece moved to correct location
        Cell cellSource = new Cell(3,3);
        Cell cellTarget = new Cell(3,2);
        Piece piece = new Piece("Dark");
        cellSource.placePiece(piece);

        cellSource.movePiece(cellTarget);
        assertEquals(piece, cellTarget.getPiece());
    }

    @Test
    public void testGetCoords() throws Exception{
        //test correct cell is returned
        Cell cell = new Cell(3,3);
        assertEquals(cell.getX(), 3);
        assertEquals(cell.getY(), 3);

        int[] testArray = {3,3};
        assertArrayEquals(testArray, cell.getCoords());
    }

    @Test
    public void testGetAndPlacePiece() throws Exception{
        Cell cell = new Cell(3,3);
        Piece piece = new Piece("Dark");
        cell.placePiece(piece);
        assertEquals(piece, cell.getPiece());
    }

    @Test
    public void testContainsPiece() throws Exception{
        Cell cell5 = new Cell(3,3);
        Cell cell6 = new Cell(4,2);
        Piece piece = new Piece("Dark");

        cell5.placePiece(piece);
        assertEquals(true, cell5.containsPiece());
        assertEquals(false, cell6.containsPiece());
    }
}
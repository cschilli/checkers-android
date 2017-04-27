package com.uno.dbbc.checkers;

import org.junit.Test;

import static org.junit.Assert.*;

public class CellTests {

    @Test
    public void testMovePiece() throws Exception{
        //test correct piece moved to correct location
        Cell cellSource = new Cell(5,5);
        Cell cellTarget = new Cell(5,6);
        Piece piece = new Piece("DARK");
        cellSource.placePiece(piece);

        cellSource.movePiece(cellTarget);
        assertEquals(piece, cellTarget.getPiece());
    }

    @Test
    public void testGetCell() throws Exception{
        //test correct cell is returned
        Cell cell = new Cell(10,5);
        assertEquals(cell.getX(), 10);
        assertEquals(cell.getY(), 5);

        int[] testArray = {10,5};
        assertArrayEquals(testArray, cell.getCoords());
    }

    @Test
    public void testGetAndPlacePiece() throws Exception{
        Cell cell = new Cell(10,5);
        Piece piece = new Piece("DARK");
        cell.placePiece(piece);
        assertEquals(cell.getPiece(), piece);
        assertEquals(piece.getColor(), "DARK");
    }

    @Test
    public void testContainsPiece() throws Exception{
        Cell cell5 = new Cell(10,5);
        Cell cell6 = new Cell(4,2);
        Piece piece = new Piece("DARK");

        cell5.placePiece(piece);
        assertEquals(cell5.containsPiece(), true);
        assertEquals(cell6.containsPiece(), false);
    }
}
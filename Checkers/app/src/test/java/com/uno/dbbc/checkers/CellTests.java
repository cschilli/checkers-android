package com.uno.dbbc.checkers;

import org.junit.Test;

import static org.junit.Assert.*;

public class CellTests {

    @Test
    public void testGetPiece() throws Exception{
        Cell cell = new Cell(3,3);
        Piece piece = new Piece(Piece.DARK);
        cell.placePiece(piece);
        assertEquals(piece, cell.getPiece());
    }

    @Test
    public void testGetPieceTest2() throws Exception{
        Cell cell = new Cell(0,2);
        Piece piece = new Piece(Piece.LIGHT);
        cell.placePiece(piece);
        assertNotEquals(null, cell.getPiece());
    }

    @Test
    public void testGetX() throws Exception{
        Cell cell = new Cell(1,3);
        assertEquals(1, cell.getX());
        assertNotEquals(3, cell.getX());
    }

    @Test
    public void testGetY() throws Exception{
        Cell cell = new Cell(1,3);
        assertEquals(3, cell.getY());
        assertNotEquals(1, cell.getY());
    }

    @Test
    public void testGetCoords() throws Exception{
        Cell cell = new Cell(1,3);
        Cell cell2 = new Cell(1,5);
        Cell cell3 = new Cell(2,2);

        int[] coords1 = new int[]{1,3};
        int[] coords2 = new int[]{1,5};
        int[] coords3 = new int[]{2,2};
        assertArrayEquals(coords1, cell.getCoords());
        assertArrayEquals(coords2, cell2.getCoords());
        assertArrayEquals(coords3, cell3.getCoords());
    }

    @Test
    public void testGetCoords2() throws Exception{
        //test correct cell is returned
        Cell cell = new Cell(3,3);
        assertEquals(cell.getX(), 3);
        assertEquals(cell.getY(), 3);

        int[] testArray = {3,3};
        assertArrayEquals(testArray, cell.getCoords());
    }

    @Test
    public void testPlacePiece() throws Exception{
        Cell cell = new Cell(3,3);
        Piece piece = new Piece(Piece.LIGHT);
        cell.placePiece(piece);

        assertEquals(piece, cell.getPiece());
    }

    @Test
    public void testMovePiece() throws Exception{
        //test correct piece moved to correct location
        Cell cellSource = new Cell(3,3);
        Cell cellTarget = new Cell(3,2);
        Piece piece = new Piece(Piece.DARK);
        cellSource.placePiece(piece);

        cellSource.movePiece(cellTarget);
        assertEquals(piece, cellTarget.getPiece());
    }

    @Test
    public void testGetAndPlacePiece() throws Exception{
        Cell cell = new Cell(3,3);
        Piece piece = new Piece(Piece.DARK);
        cell.placePiece(piece);
        assertEquals(piece, cell.getPiece());
    }

    @Test
    public void testContainsPiece() throws Exception{
        Cell cell5 = new Cell(3,3);
        Cell cell6 = new Cell(4,2);
        Piece piece = new Piece(Piece.DARK);

        cell5.placePiece(piece);
        assertEquals(true, cell5.containsPiece());
        assertEquals(false, cell6.containsPiece());
    }


}
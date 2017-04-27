package com.uno.dbbc.checkers;

import org.junit.Test;

import static org.junit.Assert.*;

public class PieceTests {

    @Test
    public void testPiece() throws Exception{
        Piece pieceX = new Piece("DARK");
        Piece pieceY = new Piece("LIGHT");
        assertNotEquals(pieceX, pieceY);
    }

    @Test
    public void testGetColor() throws Exception{
        Piece piece = new Piece("DARK");
        assertEquals("DARK", piece.getColor());
        assertNotEquals("LIGHT", piece.getColor());
    }

    @Test
    public void testGetCell() throws Exception{
        Cell cell = new Cell(3,3);
        Piece piece = new Piece("DARK");
        cell.placePiece(piece);
        assertEquals(cell, piece.getCell());
    }


}
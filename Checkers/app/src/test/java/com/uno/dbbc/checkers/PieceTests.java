package com.uno.dbbc.checkers;

import org.junit.Test;

import static org.junit.Assert.*;

public class PieceTests {

    @Test
    public void testPiece() throws Exception{
        Piece pieceX = new Piece("Dark");
        Piece pieceY = new Piece("Light");
        assertNotEquals(pieceX, pieceY);
    }

    @Test
    public void testGetColor() throws Exception{
        Piece piece = new Piece("Dark");
        assertEquals("Dark", piece.getColor());
        assertNotEquals("Light", piece.getColor());
    }

    @Test
    public void testGetCell() throws Exception{
        Cell cell = new Cell(3,3);
        Piece piece = new Piece("Dark");
        cell.placePiece(piece);
        assertEquals(cell, piece.getCell());
    }

    @Test
    public void testSetCell() throws Exception{
        Cell cell = new Cell(3,3);
        Piece piece = new Piece("Dark");

        piece.setCell(cell);
        assertEquals(cell, piece.getCell());
    }

    @Test
    public void testIsAndMakeKing() throws Exception{
        Piece pieceKing = new Piece("Dark");
        Piece pieceNotKing = new Piece("Light");

        pieceKing.makeKing();
        assertTrue(pieceKing.isKing());
        assertFalse(pieceNotKing.isKing());
    }

    @Test
    public void testGetOpponentColor() throws Exception{
        Piece piece = new Piece("Dark");

        assertEquals("Light", piece.getOpponentColor("Dark"));
        assertNotEquals("Dark", piece.getOpponentColor("Dark"));
    }
}
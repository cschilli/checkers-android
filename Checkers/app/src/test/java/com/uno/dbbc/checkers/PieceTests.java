package com.uno.dbbc.checkers;

import org.junit.Test;

import static org.junit.Assert.*;

public class PieceTests {

    @Test
    public void testPiece() throws Exception{
        Piece pieceX = new Piece(Piece.DARK);
        Piece pieceY = new Piece(Piece.LIGHT);
        assertNotEquals(pieceX, pieceY);
    }

    @Test
    public void testSetCell() throws Exception{
        Cell cell = new Cell(3,3);
        Piece piece = new Piece(Piece.DARK);
        cell.placePiece(piece);
        assertEquals(piece, cell.getPiece());
        assertEquals(cell, piece.getCell());
    }

    @Test
    public void testGetCell() throws Exception{
        Cell cell = new Cell(3,3);
        Piece piece = new Piece(Piece.DARK);
        cell.placePiece(piece);
        assertEquals(cell, piece.getCell());
    }

    @Test
    public void testGetColor() throws Exception{
        Piece piece = new Piece(Piece.DARK);
        assertEquals(Piece.DARK, piece.getColor());
        assertNotEquals(Piece.LIGHT, piece.getColor());
    }

    @Test
    public void testIsAndMakeKing() throws Exception{
        Piece pieceKing = new Piece(Piece.DARK);
        Piece pieceNotKing = new Piece(Piece.LIGHT);

        pieceKing.makeKing();
        assertTrue(pieceKing.isKing());
        assertFalse(pieceNotKing.isKing());
    }

    @Test
    public void testGetOpponentColor() throws Exception{
        Piece piece = new Piece(Piece.DARK);

        assertEquals(Piece.LIGHT, piece.getOpponentColor(Piece.DARK));
        assertNotEquals(Piece.DARK, piece.getOpponentColor(Piece.DARK));
    }
}
package com.uno.dbbc.checkers;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;


public class BoardTests {

    @Test
    public void boardInstanceTest() throws Exception{
        //test correct instantiation of board
        Board board = new Board();
        assertTrue(board != null);
    }

    @Test
    public void testInitialBoardSetup() throws Exception{
        Board board = new Board();

        Cell[][] cells = new Cell[8][8];
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                cells[i][j] = new Cell(i,j);
            }
        }
        for(int column = 0; column < 8; column+= 2){
            cells[0][column].placePiece(new Piece(Piece.LIGHT));
            cells[2][column].placePiece(new Piece(Piece.LIGHT));
            cells[6][column].placePiece(new Piece(Piece.DARK));

        }
        for(int column = 1; column< 8; column+=2){
            cells[1][column].placePiece(new Piece(Piece.LIGHT));
            cells[5][column].placePiece(new Piece(Piece.DARK));
            cells[7][column].placePiece(new Piece(Piece.DARK));

        }

        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                int[] cellsCoords = {cells[i][j].getX(), cells[i][j].getY()};
                int[] boardCoords = {board.getCell(i,j).getX(), board.getCell(i,j).getY()};
                assertArrayEquals(cellsCoords, boardCoords);
            }
        }
    }

    @Test
    public void testGetCell() throws Exception{
        Board board = new Board();

        Cell cell = new Cell(0,2);
        cell.placePiece(new Piece(Piece.LIGHT));
        assertEquals(cell.getX(), board.getCell(0,2).getX());
        assertEquals(cell.getY(), board.getCell(0,2).getY());

        Cell cell2 = new Cell(5,3);
        cell2.placePiece(new Piece(Piece.DARK));
        assertEquals(cell2.getX(), board.getCell(5,3).getX());
        assertEquals(cell2.getY(), board.getCell(5,3).getY());
        assertNotEquals(cell.getX(), board.getCell(5,3).getX());
        assertNotEquals(cell.getY(), board.getCell(5,3).getY());

    }

    @Test
    public void testGetPieces() throws Exception{
        Board board = new Board();

        assertNotEquals(board.getPieces(Piece.LIGHT), board.getPieces(Piece.DARK));
        assertEquals(12, board.getPieces(Piece.LIGHT).size());    // should have 12 light pieces
        assertEquals(12, board.getPieces(Piece.DARK).size());     // should have 12 dark pieces
    }

    @Test
    public void testMovePiece() throws Exception {
        Board board = new Board();
        int[] coords = {3, 1};

        ArrayList<Cell> movedPiece = board.movePiece(2, 0, 3, 1);       // movePiece(int fromX, int fromY, int toX, int toY)
        assertArrayEquals(coords, board.getCell(3,1).getCoords());
        assertFalse(board.getCell(movedPiece.get(0).getX(), movedPiece.get(0).getY()).containsPiece()); // should be false since we moved that original piece
    }

    @Test
    public void testMovePiece2() throws Exception {
        Board board = new Board();
        int[] src = {2,0};
        int[] dst = {3,1};

        ArrayList<Cell> movedPiece = board.movePiece(src, dst);       // movePiece(int[] source, int[] destination)
        assertArrayEquals(dst, board.getCell(3,1).getCoords());
        assertFalse(board.getCell(movedPiece.get(0).getX(), movedPiece.get(0).getY()).containsPiece()); // should be false since we moved that original piece
    }


    @Test
    public void testMovePiece3() throws Exception {
        Board board = new Board();
        int[] dst = {3,1};
        int[] move = {2,0,3,1};

        ArrayList<Cell> movedPiece = board.movePiece(move);       // movePiece(int[] move)
        assertArrayEquals(dst, board.getCell(3,1).getCoords());
        assertFalse(board.getCell(movedPiece.get(0).getX(), movedPiece.get(0).getY()).containsPiece()); // should be false since we moved that original piece
    }

    @Test
    public void testRemovePiece() throws Exception {
        Board board = new Board();

        assertEquals(12, board.getPieces(Piece.LIGHT).size());        // start with 12 light pieces
        Piece pieceToRemove = board.getCell(2,2).getPiece();
        board.removePiece(pieceToRemove);                               // remove piece at 2,2
        assertFalse(board.getCell(2,2).containsPiece());                // should not contain piece at 2,2
        assertEquals(11, board.getPieces(Piece.LIGHT).size());        // should now have 11 pieces


        pieceToRemove = board.getCell(6,2).getPiece();                  // remove dark piece
        board.removePiece(pieceToRemove);                               // remove piece at 6,2
        assertFalse(board.getCell(6,2).containsPiece());                // should not contain piece at 6,2
        assertEquals(11, board.getPieces(Piece.LIGHT).size());        // should now have 11 dark pieces
    }

    @Test
    public void testPossibleMoves() throws Exception {
        Board board = new Board();
        board.initialBoardSetup();
        ArrayList<Cell> possMoves = board.possibleMoves(2,2);
        int[] moves1 = {3,3};
        int[] moves2 = {3,1};

        assertArrayEquals(moves1, possMoves.get(0).getCoords());
        assertArrayEquals(moves2, possMoves.get(1).getCoords());
    }

    @Test
    public void testPossibleMoves2() throws Exception {
        Board board = new Board();
        board.initialBoardSetup();
        Cell cell = board.getCell(5,1);
        ArrayList<Cell> possMoves = board.possibleMoves(cell);
        int[] moves1 = {4,2};
        int[] moves2 = {4,0};

        assertArrayEquals(moves1, possMoves.get(0).getCoords());
        assertArrayEquals(moves2, possMoves.get(1).getCoords());

    }

    @Test
    public void testPossibleMovesWithEmptyMoves() throws Exception {
        Board board = new Board();
        Cell cell = board.getCell(6,0);
        ArrayList<Cell> possMoves = board.possibleMoves(cell);
        assertTrue(possMoves.isEmpty());    // should be empty since there is no moves for piece at 6,0
    }

    @Test
    public void testPossibleMovesWithKings() throws Exception {
        Board board = new Board();

        // Make empty game board by removing pieces iteratively
        for (int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++) {
                if(board.getCell(i,j).containsPiece())
                    board.removePiece(board.getCell(i, j).getPiece());
            }
        }

        board.getCell(1,1).placePiece(new Piece(Piece.LIGHT));
        board.getCell(1,3).placePiece(new Piece(Piece.LIGHT));
        board.getCell(1,1).getPiece().makeKing();
        board.getCell(1,3).getPiece().makeKing();

        board.getCell(4,2).placePiece(new Piece(Piece.DARK));
        board.getCell(4,6).placePiece(new Piece(Piece.DARK));
        board.getCell(4,2).getPiece().makeKing();
        board.getCell(4,6).getPiece().makeKing();

        int[] moves1 = {3,3};
        int[] moves2 = {3,1};
        int[] moves3 = {5,3};
        int[] moves4 = {5,1};

        Cell cell = board.getCell(4,2);
        ArrayList<Cell> possMoves = board.possibleMoves(cell);
        assertArrayEquals(moves1, possMoves.get(0).getCoords());
        assertArrayEquals(moves2, possMoves.get(1).getCoords());
        assertArrayEquals(moves3, possMoves.get(2).getCoords());
        assertArrayEquals(moves4, possMoves.get(3).getCoords());
    }


    @Test
    public void testPossibleMovesWithKings2() throws Exception {
        Board board = new Board();

        // Make empty game board by removing pieces iteratively
        for (int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++) {
                if(board.getCell(i,j).containsPiece())
                    board.removePiece(board.getCell(i, j).getPiece());
            }
        }

        board.getCell(0,0).placePiece(new Piece(Piece.LIGHT));
        board.getCell(1,3).placePiece(new Piece(Piece.LIGHT));
        board.getCell(0,0).getPiece().makeKing();
        board.getCell(1,3).getPiece().makeKing();

        board.getCell(4,2).placePiece(new Piece(Piece.DARK));
        board.getCell(4,6).placePiece(new Piece(Piece.DARK));
        board.getCell(4,2).getPiece().makeKing();
        board.getCell(4,6).getPiece().makeKing();

        int[] moves1 = {1,1};

        Cell cell = board.getCell(0,0);
        ArrayList<Cell> possMoves = board.possibleMoves(cell);
        assertArrayEquals(moves1, possMoves.get(0).getCoords());
        assertEquals(1, possMoves.size());  // should only have 1 move
    }

    @Test
    public void testGetCaptureMoves() throws Exception {
        Board board = new Board();


        Cell cell = board.getCell(6,0);
        ArrayList<Cell> possMoves = board.possibleMoves(cell);
        assertTrue(possMoves.isEmpty());    // should be empty since there is no moves for piece at 6,0
    }



}

// for(int i = 0; i < possMoves.size(); i++)
//       System.out.println(possMoves.get(i).toString());
//System.out.println(board.toString());
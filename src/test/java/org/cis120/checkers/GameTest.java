package org.cis120.checkers;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

public class GameTest {

    @Test
    public void testSetPieceToMoveCorrectlyChangesPossibleMovesForRedEdgeTest() {
        Checkers c = new Checkers();
        c.selectPieceToMove(0, 2);
        assertEquals(5, c.getCell(1, 3));
    }

    @Test
    public void testSetPieceToMoveCorrectlyChangesPossibleMovesForRedMiddlePiece() {
        Checkers c = new Checkers();
        c.selectPieceToMove(2, 2);
        assertEquals(5, c.getCell(1, 3));
        assertEquals(5, c.getCell(3, 3));
    }

    @Test
    public void testSetPieceToMoveDoesNotChangesBoardForWhiteDuringRedTurn() {
        Checkers c = new Checkers();
        c.selectPieceToMove(3, 5);
        assertEquals(0, c.getCell(4, 4));
        assertEquals(0, c.getCell(2, 4));
    }

    @Test
    public void testSetPieceToMoveDoesNotChangesBoardForRedDuringWhiteTurn() {
        Checkers c = new Checkers();
        c.switchPlayers();
        c.selectPieceToMove(2, 2);
        assertEquals(0, c.getCell(3, 3));
        assertEquals(0, c.getCell(1, 3));
    }

    @Test
    public void testSetPieceToMoveCorrectlyChangesPossibleMovesForWhiteEdge() {
        Checkers c = new Checkers();
        c.switchPlayers();
        c.selectPieceToMove(7, 5);
        assertEquals(5, c.getCell(6, 4));
    }

    @Test
    public void testSetPossibleMovesCorrectlyChangesPossibleMovesForWhiteMiddlePiece() {
        Checkers c = new Checkers();
        c.switchPlayers();
        c.selectPieceToMove(1, 5);
        assertEquals(5, c.getCell(0, 4));
        assertEquals(5, c.getCell(2, 4));
    }

    @Test
    public void testCannotMoveNonDiagonally() {
        Checkers c = new Checkers();
        c.selectPieceToMove(0, 2);
        c.selectPieceToMoveTo(0, 3);
        assertEquals(-1, c.getCell(0, 3));
        assertEquals(1, c.getCell(0, 2));
    }

    @Test
    public void testCannotMoveToSpotWithOtherPlayer() {
        Checkers c = new Checkers();
        c.selectPieceToMove(0, 2);
        c.selectPieceToMoveTo(1, 3);
        c.selectPieceToMove(1, 3);
        c.selectPieceToMoveTo(2, 4);
        c.selectPieceToMove(2, 4);
        c.selectPieceToMoveTo(3, 5);
        assertEquals(1, c.getCell(2, 4));
        assertEquals(2, c.getCell(3, 5));
    }

    @Test
    public void testSetPossibleMovesCorrectlyChangesPossibleMovesForRedJump() {
        Checkers c = new Checkers();
        c.selectPieceToMove(0, 2);
        c.selectPieceToMoveTo(1, 3);
        c.switchPlayers();

        c.selectPieceToMove(3, 5);
        c.selectPieceToMoveTo(2, 4);
        c.switchPlayers();

        c.selectPieceToMove(1, 3);
        assertEquals(5, c.getCell(0, 4));
        assertEquals(5, c.getCell(3, 5));
        c.selectPieceToMoveTo(3, 5);
        assertEquals(1, c.getCell(3, 5));
    }

    @Test
    public void testUndoRedMoveModifiesBoardCorrectly() {
        Checkers c = new Checkers();
        c.selectPieceToMove(0, 2);
        c.selectPieceToMoveTo(1, 3);
        assertEquals(12, c.getPlayerRedPieces());
        assertEquals(2, c.getPlayerRedMoves().size());
        c.undo();
        assertEquals(1, c.getCell(0, 2));
        assertEquals(0, c.getCell(1, 3));
        assertEquals(12, c.getPlayerRedPieces());
        assertEquals(1, c.getPlayerRedMoves().size());
    }

    @Test
    public void testRedKingWhenReachEndOfBoard() {
        Checkers c = new Checkers();
        c.selectPieceToMove(0, 2);
        c.selectPieceToMoveTo(1, 3);
        c.switchPlayers();

        c.selectPieceToMove(3, 5);
        c.selectPieceToMoveTo(2, 4);
        c.switchPlayers();

        c.selectPieceToMove(1, 3);
        c.selectPieceToMoveTo(3, 5);
        c.switchPlayers();

        c.selectPieceToMove(1, 5);
        c.selectPieceToMoveTo(2, 4);
        c.selectPieceToMove(2, 6);
        c.selectPieceToMoveTo(1, 5);
        c.selectPieceToMove(1, 5);
        c.selectPieceToMoveTo(0, 4);
        c.selectPieceToMove(3, 7);
        c.selectPieceToMoveTo(2, 6);
        c.selectPieceToMove(2, 6);
        c.selectPieceToMoveTo(1, 5);
        c.switchPlayers();

        c.selectPieceToMove(3, 5);
        c.selectPieceToMoveTo(2, 6);
        c.selectPieceToMove(2, 6);
        c.selectPieceToMoveTo(3, 7);
        assertEquals(3, c.getCell(3, 7));
    }

    @Test
    public void testRedKingCanJumpWhite() {
        Checkers c = new Checkers();
        c.selectPieceToMove(0, 2);
        c.selectPieceToMoveTo(1, 3);
        c.switchPlayers();

        c.selectPieceToMove(3, 5);
        c.selectPieceToMoveTo(2, 4);
        c.switchPlayers();

        c.selectPieceToMove(1, 3);
        c.selectPieceToMoveTo(3, 5);
        c.switchPlayers();

        c.selectPieceToMove(1, 5);
        c.selectPieceToMoveTo(2, 4);
        c.selectPieceToMove(2, 6);
        c.selectPieceToMoveTo(1, 5);
        c.selectPieceToMove(1, 5);
        c.selectPieceToMoveTo(0, 4);
        c.selectPieceToMove(3, 7);
        c.selectPieceToMoveTo(2, 6);
        c.selectPieceToMove(2, 6);
        c.selectPieceToMoveTo(1, 5);
        c.switchPlayers();

        c.selectPieceToMove(3, 5);
        c.selectPieceToMoveTo(2, 6);
        c.selectPieceToMove(2, 6);
        c.selectPieceToMoveTo(3, 7);
        c.switchPlayers();

        c.selectPieceToMove(5, 5);
        c.selectPieceToMoveTo(6, 4);
        c.switchPlayers();

        c.selectPieceToMove(3, 7);
        c.selectPieceToMoveTo(5, 5);
        assertEquals(3, c.getCell(5, 5));
        assertEquals(10, c.getPlayerWhitePieces());
        assertEquals(12, c.getPlayerRedPieces());
        assertEquals(2, c.getSkippedPieces().size());
    }

    @Test
    public void testUndoRedKingJumpWhite() {
        Checkers c = new Checkers();
        c.selectPieceToMove(0, 2);
        c.selectPieceToMoveTo(1, 3);
        c.switchPlayers();

        c.selectPieceToMove(3, 5);
        c.selectPieceToMoveTo(2, 4);
        c.switchPlayers();

        c.selectPieceToMove(1, 3);
        c.selectPieceToMoveTo(3, 5);
        c.switchPlayers();

        c.selectPieceToMove(1, 5);
        c.selectPieceToMoveTo(2, 4);
        c.selectPieceToMove(2, 6);
        c.selectPieceToMoveTo(1, 5);
        c.selectPieceToMove(1, 5);
        c.selectPieceToMoveTo(0, 4);
        c.selectPieceToMove(3, 7);
        c.selectPieceToMoveTo(2, 6);
        c.selectPieceToMove(2, 6);
        c.selectPieceToMoveTo(1, 5);
        c.switchPlayers();

        c.selectPieceToMove(3, 5);
        c.selectPieceToMoveTo(2, 6);
        c.selectPieceToMove(2, 6);
        c.selectPieceToMoveTo(3, 7);
        c.switchPlayers();

        c.selectPieceToMove(5, 5);
        c.selectPieceToMoveTo(6, 4);
        c.switchPlayers();

        c.selectPieceToMove(3, 7);
        c.selectPieceToMoveTo(5, 5);
        assertEquals(3, c.getCell(5, 5));
        assertEquals(2, c.getSkippedPieces().size());
        assertEquals(10, c.getPlayerWhitePieces());
        assertEquals(12, c.getPlayerRedPieces());
        c.undo();
        assertEquals(3, c.getCell(3, 7));
        assertEquals(0, c.getCell(5, 5));
        assertEquals(2, c.getCell(4, 6));
        assertEquals(11, c.getPlayerWhitePieces());
        assertEquals(12, c.getPlayerRedPieces());
        assertEquals(1, c.getSkippedPieces().size());
    }

    @Test
    public void testWriteGameToFile() {
        Checkers c = new Checkers();
        c.selectPieceToMove(0, 2);
        c.selectPieceToMoveTo(1, 3);
        c.changeTurnOver();
        c.switchPlayers();

        ArrayList<String> thingsToSave = new ArrayList<>();
        int[][] savedBoard = c.getBoard();
        for (int r = 0; r < 8; r++) {
            for (int col = 0; col < 8; col++) {
                thingsToSave.add("" + savedBoard[r][col]);
            }
        }
        thingsToSave.add("" + c.getCurrentPlayer());
        thingsToSave.add("" + c.getGameOver());
        thingsToSave.add("" + c.getTurnOver());
        thingsToSave.add("" + c.getPlayerRedPieces());
        thingsToSave.add("" + c.getPlayerWhitePieces());

        LinkedList<Move> redPlayerMoves = c.getPlayerRedMoves();
        thingsToSave.add("" + redPlayerMoves.get(redPlayerMoves.size() - 2).getC());
        thingsToSave.add("" + redPlayerMoves.get(redPlayerMoves.size() - 2).getR());
        thingsToSave.add("" + redPlayerMoves.get(redPlayerMoves.size() - 2).getIdentity());
        thingsToSave.add("" + redPlayerMoves.getLast().getC());
        thingsToSave.add("" + redPlayerMoves.getLast().getR());
        thingsToSave.add("" + redPlayerMoves.getLast().getWasJump());
        thingsToSave.add("" + redPlayerMoves.getLast().getIdentity());
        thingsToSave.add("null");
        thingsToSave.add("null");
        c.save(thingsToSave);

        c.load();
        int[][] board = { { 1, -1, 1, -1, 1, -1, 1, -1 }, { -1, 1, -1, 1, -1, 1, -1, 1 },
            { 0, -1, 1, -1, 1, -1, 1, -1 },
            { -1, 1, -1, 0, -1, 0, -1, 0 }, { 0, -1, 0, -1, 0, -1, 0, -1 },
            { -1, 2, -1, 2, -1, 2, -1, 2 },
            { 2, -1, 2, -1, 2, -1, 2, -1 }, { -1, 2, -1, 2, -1, 2, -1, 2 } };

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                assertEquals(c.getCell(j, i), board[i][j]);
            }
        }
        assertFalse(c.getCurrentPlayer());
        assertFalse(c.getGameOver());
        assertFalse(c.getTurnOver());
        assertEquals(12, c.getPlayerRedPieces());
        assertEquals(12, c.getPlayerWhitePieces());
        assertEquals(2, c.getPlayerRedMoves().size());
        assertEquals(0, c.getPlayerWhiteMoves().size());
        assertEquals(0, c.getSkippedPieces().size());
    }

}

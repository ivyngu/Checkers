package org.cis120.checkers;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GameTest {

    @Test
    public void ShowPossibleMovesCorrectlyChangesPossibleMovesForRedEdge() {
        Checkers c = new Checkers();
        c.findPossibleMoves(0, 2);
        assertEquals(5, c.getCell(1, 3));
    }

    @Test
    public void ShowPossibleMovesCorrectlyChangesPossibleMovesForRedMiddlePiece() {
        Checkers c = new Checkers();
        c.findPossibleMoves(2, 2);
        assertEquals(5, c.getCell(1, 3));
        assertEquals(5, c.getCell(3, 3));
    }

    @Test
    public void ShowPossibleMovesCorrectlyChangesPossibleMovesForWhiteEdge() {
        Checkers c = new Checkers();
        c.switchPlayers();
        c.findPossibleMoves(7, 5);
        assertEquals(5, c.getCell(6, 4));
    }

    @Test
    public void SetPossibleMovesCorrectlyChangesPossibleMovesForWhiteMiddlePiece() {
        Checkers c = new Checkers();
        c.switchPlayers();
        c.findPossibleMoves(1, 5);
        assertEquals(5, c.getCell(0, 4));
        assertEquals(5, c.getCell(2, 4));
    }

    @Test
    public void ShowPossibleMovesCorrectlyChangesPossibleMovesForWhiteMiddlePiece() {
        Checkers c = new Checkers();
        c.switchPlayers();
        c.findPossibleMoves(1, 5);
        assertEquals(5, c.getCell(0, 4));
        assertEquals(5, c.getCell(2, 4));
    }

}

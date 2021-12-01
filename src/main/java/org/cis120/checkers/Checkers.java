package org.cis120.checkers;

import java.awt.*;
import java.util.LinkedList;

/**
 * This class is a model for Checkers.
 *
 */
public class Checkers {

    private int[][] board;
    private boolean player1;
    private boolean gameOver;
    private int player1Pieces;
    private int player2Pieces;
    private LinkedList<Point> player1Moves;
    private LinkedList<Point> player2Moves;

    /**
     * Constructor sets up game state.
     */
    public Checkers() {
        reset();
    }

    public void undo() {

    }

    public void switchPlayers() {

    }

    /**
     * playTurn allows players to play a turn. Returns true if the move is
     * successful and false if a player tries to play in a location that is
     * taken or after the game has ended. If the turn is successful and the game
     * has not ended, the player is changed. If the turn is unsuccessful or the
     * game has ended, the player is not changed.
     *
     * @param c column to play in
     * @param r row to play in
     * @return whether the turn was successful
     */
    public boolean playTurn(int c, int r) {
        if (board[r][c] != 0 || gameOver) {
            return false;
        }

        if (player1) {
            board[r][c] = 1;
        } else {
            board[r][c] = 2;
        }

        if (checkWinner() == 0) {
            player1 = !player1;
        }
        return true;
    }

    /**
     * checkWinner checks whether the game has reached a win condition (one player loses all their pieces).
     *
     * @return 0 if nobody has won yet, 1 if player 1 has won, and 2 if player 2
     *         has won, 3 if the game hits stalemate
     */
    public int checkWinner() {
        if (player1Pieces == 0) {
            return 2;
        } else if (player2Pieces == 0) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * printGameState prints the current game state
     * for debugging.
     */
    public void printGameState() {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                System.out.print(board[i][j]);
                if (j < 2) {
                    System.out.print(" | ");
                }
            }
            if (i < 2) {
                System.out.println("\n---------");
            }
        }
    }

    /**
     * reset (re-)sets the game state to start a new game.
     */
    public void reset() {
        board = new int[8][8];
        player1 = true;
        gameOver = false;
        player1Pieces = 12;
        player2Pieces = 12;
    }

    /**
     * getCurrentPlayer is a getter for the player
     * whose turn it is in the game.
     * 
     * @return true if it's Player 1's turn,
     *         false if it's Player 2's turn.
     */
    public boolean getCurrentPlayer() {
        return player1;
    }

    /**
     * getCell is a getter for the contents of the cell specified by the method
     * arguments.
     *
     * @param c column to retrieve
     * @param r row to retrieve
     * @return an integer denoting the contents of the corresponding cell on the
     *         game board. 0 = empty, 1 = Player 1, 2 = Player 2
     */
    public int getCell(int c, int r) {
        return board[r][c];
    }

}

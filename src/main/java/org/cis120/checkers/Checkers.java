package org.cis120.checkers;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;


/**
 * This class is a model for Checkers.
 *
 */
public class Checkers {

    private int[][] board;
    private boolean playerRedTurn;
    private boolean gameOver;
    private int playerRedPieces;
    private int playerWhitePieces;
    private LinkedList<Move> playerRedMoves;
    private LinkedList<Move> playerWhiteMoves;

    /**
     * Constructor sets up game state.
     */
    public Checkers() {
        reset();
    }

    /**
     * Moves onto the next player's turn once the current player confirms they are done with their turn.
     */
    public void switchPlayers() {
        playerRedTurn = !playerRedTurn;
    }

    /**
     * reset (re-)sets the game state to start a new game.
     */
    public void reset() {
        board = new int[8][8];
        fillInBoardWithRedPieces();
        fillInBoardWithWhitePieces();
        fillInBoardWithNonDiagonals();
        playerRedTurn = true;
        gameOver = false;
        playerRedPieces = 12;
        playerWhitePieces = 12;
        playerRedMoves = new LinkedList<Move>();
        playerWhiteMoves = new LinkedList<Move>();
    }

    public void fillInBoardWithRedPieces() {
        for (int r = 0; r < 3; r=r+2) {
            for (int c = 0; c < 8; c=c+2) {
                board[r][c] = 1;
            }
        }
        for (int r = 1; r < 3; r=r+2) {
            for (int c = 1; c < 8; c=c+2) {
                board[r][c] = 1;
            }
        }
    }

    public void fillInBoardWithWhitePieces() {
        for (int r = 5; r < 8; r=r+2) {
            for (int c = 1; c < 8; c=c+2) {
                board[r][c] = 2;
            }
        }
        for (int r = 6; r < 8; r=r+2) {
            for (int c = 0; c < 8; c=c+2) {
                board[r][c] = 2;
            }
        }
    }

    public void fillInBoardWithNonDiagonals() {
        for (int r = 1; r < 8; r=r+2) {
            for (int c = 0; c < 8; c=c+2) {
                board[r][c] = -1;
            }
        }
        for (int r = 0; r < 8; r=r+2) {
            for (int c = 1; c < 8; c=c+2) {
                board[r][c] = -1;
            }
        }
    }

    public void undo() {
        if (playerRedTurn) {
            Move lastMove = playerRedMoves.getLast();
            board[lastMove.getY()][lastMove.getX()] = 0;
        } else if (!playerRedTurn) {
            Move lastMove = playerWhiteMoves.getLast();
            board[lastMove.getY()][lastMove.getX()] = 0;
        }
    }


    /**
     *
     */
    public boolean selectPieceToMove(int c, int r) {
        if (playerRedTurn && board[r][c] != 1) {
            return false;
        } else if (!playerRedTurn && board[r][c] != 2) {
            return false;
        }
        if (playerRedTurn) {
            playerRedMoves.add(new Move(c, r));
        } else if (!playerRedTurn) {
            playerWhiteMoves.add(new Move(c, r));
        }
        showPossibleMoves(c, r);
        return true;
    }

    public void showPossibleMoves(int c, int r) {
        if (playerRedTurn) {
            ArrayList<Move> totalMoves = new ArrayList<Move>();
            ArrayList<Move> movesForward = findBlankSpacesForward(c, r);
            totalMoves.addAll(movesForward);
            if (jumpPossible(c, r)) {
                ArrayList<Move> jumpForward = findJumpsForward(c, r);
                totalMoves.addAll(jumpForward);
            }
            for (Move oneMove : totalMoves) {
                board[oneMove.getY()][oneMove.getX()] = 5;
            }
        }
        if (!playerRedTurn) {
            ArrayList<Move> totalMoves = new ArrayList<Move>();
            ArrayList<Move> movesBackward = findBlankSpacesBackward(c, r);
            totalMoves.addAll(movesBackward);
            if (jumpPossible(c, r)) {
                ArrayList<Move> jumpBackward = findJumpsBackward(c, r);
                totalMoves.addAll(jumpBackward);
            }
            for (Move oneMove : totalMoves) {
                board[oneMove.getY()][oneMove.getX()] = 5;
            }
        }
    }

    public ArrayList<Move> findBlankSpacesBackward(int c, int r) {
        ArrayList<Move> moves = new ArrayList();
        if ((r + 1 < 8) && (c - 1 > -1)) {
            if (board[r - 1][c - 1] == 0) {
                moves.add(new Move(c-1,r-1));
            }
        }
        if ((r + 1 < 8) && (c + 1 < 8)) {
            if (board[r - 1][c + 1] == 0) {
                moves.add(new Move(c+1,r-1));
            }
        }
        return moves;
    }


    public ArrayList<Move> findBlankSpacesForward(int c, int r) {
        ArrayList<Move> moves = new ArrayList();
        // for singly moving forward
        if ((r + 1 < 8) && (c - 1 > -1)) {
            if (board[r + 1][c - 1] == 0) {
                moves.add(new Move(c-1,r+1));
            }
        }
        if ((r + 1 < 8) && (c + 1 < 8)) {
            if (board[r + 1][c + 1] == 0) {
                moves.add(new Move(c+1,r+1));
            }
        }
        return moves;
    }

    public ArrayList<Move> findJumpsForward(int c, int r) {
        ArrayList<Move> moves = new ArrayList();
        if ((r + 1 < 8) && (c - 1 > -1)) {
            if (board[r + 1][c - 1] == 2 && playerRedTurn) {
                moves.add(new Move(c-2,r+2));
            }
            if (board[r + 1][c - 1] == 1 && !playerRedTurn) {
                moves.add(new Move(c-2,r+2));
            }
        }
        if ((r + 1 < 8) && (c + 1 < 8)) {
            if (board[r + 1][c + 1] == 2 && playerRedTurn) {
                moves.add(new Move(c+2,r+2));
            }
            if (board[r + 1][c + 1] == 1 && !playerRedTurn) {
                moves.add(new Move(c+2,r+2));
            }
        }
        return moves;
    }

    public ArrayList<Move> findJumpsBackward(int c, int r) {
        ArrayList<Move> moves = new ArrayList();
        if ((r + 1 < 8) && (c - 1 > -1)) {
            if (board[r - 1][c - 1] == 2 && playerRedTurn) {
                moves.add(new Move(c-2,r-2));
            }
            if (board[r - 1][c - 1] == 1 && !playerRedTurn) {
                moves.add(new Move(c-2,r-2));
            }
        }
        if ((r + 1 < 8) && (c + 1 < 8)) {
            if (board[r - 1][c + 1] == 2 && playerRedTurn) {
                moves.add(new Move(c+2,r-2));
            }
            if (board[r - 1][c + 1] == 1 && !playerRedTurn) {
                moves.add(new Move(c+2,r-2));
            }
        }
        return moves;
    }


    public boolean jumpPossible(int c, int r) {
        if ((r + 1 < 8) && (c - 1 > -1)) {
            if (board[r + 1][c - 1] == 2 && playerRedTurn) {
                return true;
            }
            if (board[r + 1][c - 1] == 1 && !playerRedTurn) {
                return true;
            }
        }
        if ((r + 1 < 8) && (c + 1 < 8)) {
            if (board[r + 1][c + 1] == 2 && playerRedTurn) {
               return true;
            }
            if (board[r + 1][c + 1] == 1 && !playerRedTurn) {
                return true;
            }
        }
        return false;
    }

    public boolean selectPieceToMoveTo(int c, int r) {
        if (board[r][c] != 5) {
            return false;
        }
        if (playerRedTurn) {
            Move lastMove = playerRedMoves.getLast();
            board[lastMove.getY()][lastMove.getX()] = 0;
            board[r][c] = 1;
            playerRedMoves.add(new Move(c,r));
            if (jumpPossible(lastMove.getX(), lastMove.getY())) {
                Move skipped = findSkippedPiece(c, r, lastMove);
                board[skipped.getY()][skipped.getX()] = 0;
                playerWhitePieces--;
            }
        } else if (!playerRedTurn) {
            Move lastMove = playerWhiteMoves.getLast();
            board[lastMove.getY()][lastMove.getX()] = 0;
            board[r][c] = 2;
            playerWhiteMoves.add(new Move(c,r));
            if (jumpPossible(lastMove.getX(), lastMove.getY())) {
                Move skipped = findSkippedPiece(c, r, lastMove);
                board[skipped.getY()][skipped.getX()] = 0;
                playerRedPieces--;
            }
        }
        return true;
    }

    public Move findSkippedPiece(int c, int r, Move lastMove) {
        if (c > lastMove.getX() && r > lastMove.getY()) {
            return new Move(c-1, r-1);
        } else if (c > lastMove.getX() && r < lastMove.getY()) {
            return new Move(c-1, r+1);
        } else if (c < lastMove.getX() && r < lastMove.getY()) {
            return new Move(c+1, r+1);
        } else if (c < lastMove.getX() && r > lastMove.getY()) {
            return new Move(c+1, r-1);
        }
        return null;
    }

    /**
     * checkWinner checks whether the game has reached a win condition (one player loses all their pieces).
     *
     * @return 0 if nobody has won yet, 1 if player 1 has won, and 2 if player 2
     *         has won, 3 if the game hits stalemate
     */
    public int checkWinner() {
        if (playerRedPieces == 0) {
            return 2;
        } else if (playerWhitePieces == 0) {
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
                if (j < 8) {
                    System.out.print(" | ");
                }
            }
            if (i < 8) {
                System.out.println("\n---------");
            }
        }
    }

    /**
     * getCurrentPlayer is a getter for the player
     * whose turn it is in the game.
     * 
     * @return true if it's Player 1's turn,
     *         false if it's Player 2's turn.
     */
    public boolean getCurrentPlayer() {
        return playerRedTurn;
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

package org.cis120.checkers;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;


/**
 * This class is a model for Checkers.
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

    /**
     * Helper function for initializing game board by filling in with red player's pieces denoted by
     * a 1 in the 2D array board.
     */
    private void fillInBoardWithRedPieces() {
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
    /**
     * Helper function for initializing game board by filling in with white player's pieces denoted by
     * a 2 in the 2D array board.
     */
    private void fillInBoardWithWhitePieces() {
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

    /**
     * Helper function for initializing game board by filling in with the 2D array board with checker pieces that
     * cannot be used by the checker pieces denoted by -1.
     */
    private void fillInBoardWithNonDiagonals() {
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

    /**
     * Selects a checker piece to move and see if it is a valid piece to move. If so, calls helper function to find
     * possible move coordinates.
     * @param c - column of selected piece to be moved
     * @param r - row of selected piece to be moved
     * @return a boolean denoting if selected piece to be moved can actually be moved
     */
    public boolean selectPieceToMove(int c, int r) {
        // if a selected piece is something you can move, then move it
        if ((playerRedTurn && (board[r][c] == 1)) || (playerRedTurn && (board[r][c] == 3))) {
            playerRedMoves.add(new Move(c, r));
            adjustBoardToPossibleMoves(findPossibleMoves(c, r));
            return true;
        } else if ((!playerRedTurn && (board[r][c] == 2)) || (!playerRedTurn && (board[r][c] == 4))) {
            playerWhiteMoves.add(new Move(c, r));
            adjustBoardToPossibleMoves(findPossibleMoves(c, r));
            return true;
        }
        // if a selected piece is not something you can move, then void the move
        return false;
    }

    /**
     * Helper function to adjust board accordingly to account for possible moves for a checker piece.
     * @param moves - a list of possible moves that a checker piece can make
     */
    private void adjustBoardToPossibleMoves(ArrayList<Move> moves) {
        for (Move oneMove : moves) {
            board[oneMove.getY()][oneMove.getX()] = 5;
        }
    }

    /**
     * Finds possible moves that a checker piece can move to & adjusts the board so that move can be made.
     * @param c - column of selected piece to be moved
     * @param r - row of selected piece to be moved
     * @return a list of possible moves that a checker piece can make
     */
    public ArrayList<Move> findPossibleMoves(int c, int r) {
        // if it's the red player, find where they can move/jump to
        ArrayList<Move> totalMoves = new ArrayList<Move>();
        if (playerRedTurn && !gameOver) {
            // if it's not a king, can only move in one direction
            ArrayList<Move> movesForward = findBlankSpacesForward(c, r);
            totalMoves.addAll(movesForward);
            if (jumpPossible(c, r)) {
                ArrayList<Move> jumpForward = findJumpsForward(c, r);
                totalMoves.addAll(jumpForward);
            }
            // if it's a king, then it can move all ways
            if (kingStatus(r) || previousKingStatus(c, r)) {
                ArrayList<Move> movesBackward = findBlankSpacesBackward(c, r);
                totalMoves.addAll(movesBackward);
                if (jumpPossible(c, r)) {
                    ArrayList<Move> jumpBackward = findJumpsBackward(c, r);
                    totalMoves.addAll(jumpBackward);
                }
            }
        } else if (!playerRedTurn && !gameOver) {
            ArrayList<Move> movesBackward = findBlankSpacesBackward(c, r);
            totalMoves.addAll(movesBackward);
            if (jumpPossible(c, r)) {
                ArrayList<Move> jumpBackward = findJumpsBackward(c, r);
                totalMoves.addAll(jumpBackward);
            }
            if (kingStatus(r) || previousKingStatus(c, r)) {
                ArrayList<Move> movesForward = findBlankSpacesForward(c, r);
                totalMoves.addAll(movesForward);
                if (jumpPossible(c, r)) {
                    ArrayList<Move> jumpForward = findJumpsForward(c, r);
                    totalMoves.addAll(jumpForward);
                }
            }
        }
        return totalMoves;
    }

    /**
     * Finds possible spaces to move to in the forward direction of the checkerboard (towards bottom).
     * @param c - column of selected piece to be moved
     * @param r - row of selected piece to be moved
     * @return an ArrayList of type Moves that includes all possible moves forward for a checkerpiece
     */
    private ArrayList<Move> findBlankSpacesForward(int c, int r) {
        ArrayList<Move> moves = new ArrayList();
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

    /**
     * Finds possible spaces to move to in the backward direction of the checkerboard (towards top).
     * @param c - column of selected piece to be moved
     * @param r - row of selected piece to be moved
     * @return an ArrayList of type Moves that includes all possible moves backward for a checkerpiece
     */
    private ArrayList<Move> findBlankSpacesBackward(int c, int r) {
        ArrayList<Move> moves = new ArrayList();
        if ((r - 1 > -1) && (c - 1 > -1)) {
            if (board[r - 1][c - 1] == 0) {
                moves.add(new Move(c - 1,r - 1));
            }
        }
        if ((r - 1 > -1) && (c + 1 < 8)) {
            if (board[r - 1][c + 1] == 0) {
                moves.add(new Move(c + 1,r - 1));
            }
        }
        return moves;
    }

    /**
     * Sees if a jump is possible for a selected checkerpiece.
     * @param c - column of selected checker piece
     * @param r - row of selected checker piece
     * @return boolean that is true if there is a jump possible for the selected checkerpiece, false otherwise
     */
    private boolean jumpPossible(int c, int r) {
        // forward, left jump
        if ((r + 1 < 8) && (c - 1 > -1)) {
            if ((board[r + 1][c - 1] == 2 && playerRedTurn) || (board[r + 1][c - 1] == 4 && playerRedTurn)) {
                return true;
            }
            if ((board[r + 1][c - 1] == 1 && !playerRedTurn) || (board[r + 1][c - 1] == 3 && !playerRedTurn)) {
                return true;
            }
        }
        // forward, right jump
        if ((r + 1 < 8) && (c + 1 < 8)) {
            if ((board[r + 1][c + 1] == 2 && playerRedTurn) || (board[r + 1][c + 1] == 4 && playerRedTurn)) {
                return true;
            }
            if ((board[r + 1][c + 1] == 1 && !playerRedTurn) || (board[r + 1][c + 1] == 3 && !playerRedTurn)) {
                return true;
            }
        }
        // backward, left jump
        if ((r -1 > -1) && (c - 1 > -1)) {
            if ((board[r - 1][c - 1] == 2 && playerRedTurn) || (board[r - 1][c - 1] == 4 && playerRedTurn)) {
                return true;
            }
            if ((board[r - 1][c - 1] == 1 && !playerRedTurn) || (board[r - 1][c - 1] == 3 && !playerRedTurn)) {
                return true;
            }
        }
        // backward, right jump
        if ((r - 1 > -1) && (c + 1 < 8)) {
            if ((board[r - 1][c + 1] == 2 && playerRedTurn) || (board[r - 1][c + 1] == 4 && playerRedTurn)) {
                return true;
            }
            if ((board[r - 1][c + 1] == 1 && !playerRedTurn) || (board[r - 1][c + 1] == 3 && !playerRedTurn)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Finds possible spaces to jump to in the forward direction of the checkerboard (towards bottom).
     * @param c - column of selected piece to be moved
     * @param r - row of selected piece to be moved
     * @return an ArrayList of type Moves that includes all possible jumps forward for a checkerpiece
     */
    private ArrayList<Move> findJumpsForward(int c, int r) {
        ArrayList<Move> moves = new ArrayList();
        // forward, left
        if ((r + 1 < 8) && (c - 1 > -1)) {
            if ((board[r + 1][c - 1] == 2 && playerRedTurn) || (board[r + 1][c - 1] == 4 && playerRedTurn)) {
                if ((r+2 < 8) && (c-2>-1)) {
                    moves.add(new Move(c-2,r+2));
                }
            }
            if ((board[r + 1][c - 1] == 1 && !playerRedTurn) || (board[r + 1][c - 1] == 3 && !playerRedTurn)) {
                if ((r+2 < 8) && (c-2>-1)) {
                    moves.add(new Move(c-2,r+2));
                }
            }
        }
        // forward, right
        if ((r + 1 < 8) && (c + 1 < 8)) {
            if ((board[r + 1][c + 1] == 2 && playerRedTurn) || (board[r + 1][c + 1] == 4 && playerRedTurn)) {
                if ((r+2 < 8) && (c+2<8)) {
                    moves.add(new Move(c+2,r+2));
                }
            }
            if ((board[r + 1][c + 1] == 1 && !playerRedTurn) || (board[r + 1][c + 1] == 3 && !playerRedTurn)) {
                if ((r+2 < 8) && (c+2<8)) {
                    moves.add(new Move(c+2,r+2));
                }
            }
        }
        return moves;
    }

    /**
     * Finds possible spaces to jump to in the backward direction of the checkerboard (towards top).
     * @param c - column of selected piece to be moved
     * @param r - row of selected piece to be moved
     * @return an ArrayList of type Moves that includes all possible jumps backward for a checkerpiece
     */
    private ArrayList<Move> findJumpsBackward(int c, int r) {
        ArrayList<Move> moves = new ArrayList();
        // backward, left
        if ((r - 1 > -1) && (c - 1 > -1)) {
            if ((board[r - 1][c - 1] == 2 && playerRedTurn) || (board[r - 1][c - 1] == 4 && playerRedTurn)) {
                if ((r-2 > -1) && (c-2>-1)) {
                    moves.add(new Move(c-2,r-2));
                }
            }
            if ((board[r - 1][c - 1] == 1 && !playerRedTurn) || (board[r - 1][c - 1] == 3 && !playerRedTurn)) {
                if ((r-2 > -1) && (c-2>-1)) {
                    moves.add(new Move(c-2,r-2));
                }
            }
        }
        // backward, right
        if ((r - 1 > -1) && (c + 1 < 8)) {
            if ((board[r - 1][c + 1] == 2 && playerRedTurn) || (board[r - 1][c + 1] == 4 && playerRedTurn)) {
                if ((r-2 > -1) && (c+2<8)) {
                    moves.add(new Move(c+2,r-2));
                }
            }
            if ((board[r - 1][c + 1] == 1 && !playerRedTurn) || (board[r - 1][c + 1] == 3 && !playerRedTurn)) {
                if ((r-2 > -1) && (c+2<8)) {
                    moves.add(new Move(c+2,r-2));
                }
            }
        }
        return moves;
    }

    private void resetOldPossibleMoves() {
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                if (board[r][c] == 5) {
                    board[r][c] = 0;
                }
            }
        }
    }

    /**
     * Changes board depending on where player wants to move the selected checker piece to.
     * @param c - column of location of where to move chosen checker piece to
     * @param r - row of location of where to move chosen checker piece to
     * @return boolean that is true if the selected location for the chosen checker piece to be moved to is valid,
     * false otherwise
     */
    public boolean selectPieceToMoveTo(int c, int r) {
        // if the location is not a valid place for the checker piece to be moved to, don't
        if ((board[r][c] != 5) || gameOver) {
            return false;
        }
        // otherwise, modify board to move checker piece to new location
        if (playerRedTurn) {
            Move lastMove = playerRedMoves.getLast();
            int lastC = lastMove.getX();
            int lastR = lastMove.getY();
            // set new location to new assigned number
            if (kingStatus(r) || previousKingStatus(lastMove.getX(), lastMove.getY())) {
                board[r][c] = 3;
            } else if (!kingStatus(r) || !previousKingStatus(lastMove.getX(), lastMove.getY())) {
                board[r][c] = 1;
            }
            // if a checker piece can jump, modify board too
            if (jumpPossible(lastMove.getX(), lastMove.getY())) {
                if (successfulSkipForPiece(c, r, lastMove)) {
                    Move skipped = findSkippedPiece(c, r, lastMove);
                    board[skipped.getY()][skipped.getX()] = 0;
                    playerWhitePieces--;
                }
            }
            resetOldPossibleMoves();
            // clear the last location
            board[lastMove.getY()][lastMove.getX()] = 0;
            // finally add this move to the list of moves made by player
            playerRedMoves.add(new Move(c,r));
        } else if (!playerRedTurn) {
            Move lastMove = playerWhiteMoves.getLast();
            if (kingStatus(r) || previousKingStatus(lastMove.getX(), lastMove.getY())) {
                board[r][c] = 4;
            } else if (!kingStatus(r) || !previousKingStatus(lastMove.getX(), lastMove.getY())) {
                board[r][c] = 2;
            }
            if (jumpPossible(lastMove.getX(), lastMove.getY())) {
                if (successfulSkipForPiece(c, r, lastMove)) {
                    Move skipped = findSkippedPiece(c, r, lastMove);
                    board[skipped.getY()][skipped.getX()] = 0;
                    playerRedPieces--;
                }
            }
           resetOldPossibleMoves();
            board[lastMove.getY()][lastMove.getX()] = 0;
            playerWhiteMoves.add(new Move(c,r));
        }
        return true;
    }

    /**
     * Helper function to check whether a skip over a checker piece was successful or not.
     * @param c - column of piece to be checked
     * @param r - row of piece to be checked
     * @param lastMove - the coordinates of the last move made within the sequence of moves of the player
     * @return boolean that is true if the skip was successful, false otherwise
     */
    private boolean successfulSkip(int c, int r, Move lastMove) {
        if ((c == lastMove.getX() - 2) && (r == lastMove.getY() + 2)) {
            return true;
        }
        if ((c == lastMove.getX() - 2) && (r == lastMove.getY() - 2)) {
            return true;
        }
        if ((c == lastMove.getX() + 2) && (r == lastMove.getY() - 2)) {
            return true;
        }
        if ((c == lastMove.getX() + 2) && (r == lastMove.getY() + 2)) {
            return true;
        }
        return false;
    }

    /**
     * Checks whether the player made a move that was a successful skip.
     * @param c - column of current location of piece
     * @param r - row of current location of piece
     * @param lastMove - coordinates of the last move made within the sequence of moves of the player
     * @return boolean that is true if the player made a successful skip, false otherwise
     */
    private boolean successfulSkipForPiece(int c, int r, Move lastMove) {
        // forward, left jump
        if ((r + 1 < 8) && (c - 1 > -1)) {
            if ((board[r + 1][c - 1] == 2 && playerRedTurn) || (board[r + 1][c - 1] == 4 && playerRedTurn)) {
                return successfulSkip(c, r, lastMove);
            }
            if ((board[r + 1][c - 1] == 1 && !playerRedTurn) || (board[r + 1][c - 1] == 3 && !playerRedTurn)) {
                return successfulSkip(c, r, lastMove);
            }
        }
        // forward, right jump
        if ((r + 1 < 8) && (c + 1 < 8)) {
            if ((board[r + 1][c + 1] == 2 && playerRedTurn) || (board[r + 1][c + 1] == 4 && playerRedTurn)) {
                return successfulSkip(c, r, lastMove);
            }
            if ((board[r + 1][c + 1] == 1 && !playerRedTurn) || (board[r + 1][c + 1] == 3 && !playerRedTurn)) {
                return successfulSkip(c, r, lastMove);
            }
        }
        // backward, left jump
        if ((r -1 > -1) && (c - 1 > -1)) {
            if ((board[r - 1][c - 1] == 2 && playerRedTurn) || (board[r - 1][c - 1] == 4 && playerRedTurn)) {
                return successfulSkip(c, r, lastMove);
            }
            if ((board[r - 1][c - 1] == 1 && !playerRedTurn) || (board[r - 1][c - 1] == 3 && !playerRedTurn)) {
                return successfulSkip(c, r, lastMove);
            }
        }
        // backward, right jump
        if ((r - 1 > -1) && (c + 1 < 8)) {
            if ((board[r - 1][c + 1] == 2 && playerRedTurn) || (board[r - 1][c + 1] == 4 && playerRedTurn)) {
                return successfulSkip(c, r, lastMove);
            }
            if ((board[r - 1][c + 1] == 1 && !playerRedTurn) || (board[r - 1][c + 1] == 3 && !playerRedTurn)) {
                return successfulSkip(c, r, lastMove);
            }
        }
        return false;
    }

    /**
     * Finds the piece in which the chosen checker piece skipped over.
     * @param c - column of checker piece that skipped over another piece
     * @param r - row of checker piece that skipped over another piece
     * @param lastMove - location of checker piece before skipping over the other piece
     * @return Move with coordinates of checker piece that was skipped over
     */
    private Move findSkippedPiece(int c, int r, Move lastMove) {
        if (c > lastMove.getX() && r > lastMove.getY()) {
            // skip forward, right
            return new Move(c - 1, r - 1);
        } else if (c > lastMove.getX() && r < lastMove.getY()) {
            // skip backward, right
            return new Move(c - 1, r + 1);
        } else if (c < lastMove.getX() && r < lastMove.getY()) {
            // skip backward, left
            return new Move(c + 1, r + 1);
        } else if (c < lastMove.getX() && r > lastMove.getY()) {
            // skip forward, left
            return new Move(c + 1, r - 1);
        }
        return null;
    }

    /**
     * Adjusts the board so that a player can restart their turn.
     */
    public void undo() {
        if (playerRedTurn) {
            Move lastMove = playerRedMoves.getLast();
            int identity = getCell(lastMove.getX(), lastMove.getY());

            board[lastMove.getY()][lastMove.getX()] = 0;
            playerRedMoves.removeLast();
            Move secondLastMove = playerRedMoves.getLast();
            if (identity == 3) {
                board[secondLastMove.getY()][secondLastMove.getX()] = 3;
            } else {
                board[secondLastMove.getY()][secondLastMove.getX()] = 1;
            }
        } else if (!playerRedTurn) {
            Move lastMove = playerWhiteMoves.getLast();
            int identity = getCell(lastMove.getX(), lastMove.getY());
            board[lastMove.getY()][lastMove.getX()] = 0;
            playerRedMoves.removeLast();
            Move secondLastMove = playerRedMoves.getLast();
            if (identity == 4) {
                board[secondLastMove.getY()][secondLastMove.getX()] = 4;
            } else {
                board[secondLastMove.getY()][secondLastMove.getX()] = 2;
            }
        }
        printGameState();
    }

    /**
     * Checks if a current checker piece is turned a King or not.
     * @param r - row of current checker piece to be checked
     * @return boolean that is true if the checker piece is a King, false otherwise
     */
    private boolean kingStatus(int r) {
        if (playerRedTurn) {
            if (r == 7) {
                return true;
            }
        } else {
            if (r == 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if a current checker piece was a King previously or not.
     * @param c - column of current checker piece to be checked
     * @param r - row of current checker piece to be checked
     * @return boolean that is true if the checker piece is a King, false otherwise
     */
    private boolean previousKingStatus(int c, int r) {
        return board[r][c] == 3 || board[r][c] == 4;
    }

    /**
     * checkWinner checks whether the game has reached a win condition (one player loses all their pieces).
     *
     * @return 0 if nobody has won yet, 1 if player red has won, and 2 if player white
     *         has won, 3 if the game hits stalemate
     */
    public int checkWinner() {
        if (playerRedPieces == 0) {
            gameOver = true;
            return 2;
        } else if (playerWhitePieces == 0) {
            gameOver = true;
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
     * @return true if it's Red Player's turn,
     *         false if it's White Player's turn.
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
     *         game board. 0 = empty, 1 or 3 = Red Player, 2 or 4 = White Player, -1 = Non-diagonal, 5 = Possible Move
     */
    public int getCell(int c, int r) {
        return board[r][c];
    }

    /**
     * Getter function for how many pieces Red Player has left.
     * @return integer denoting how many pieces Red Player has
     */
    public int getPlayerRedPieces() {
        return playerRedPieces;
    }

    /**
     * Getter function for how many pieces White Player has left.
     * @return integer denoting how many pieces White Player has
     */
    public int getPlayerWhitePieces() {
        return playerWhitePieces;
    }
}

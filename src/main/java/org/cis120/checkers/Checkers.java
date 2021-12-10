package org.cis120.checkers;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * This class is a model for Checkers.
 */
public class Checkers {

    private int[][] board;
    private boolean playerRedTurn;
    private boolean gameOver;
    private boolean turnOver;
    private int playerRedPieces;
    private int playerWhitePieces;
    private LinkedList<Move> playerRedMoves;
    private LinkedList<Move> playerWhiteMoves;
    private LinkedList<Move> skippedPieces;

    /**
     * Constructor sets up game state.
     */
    public Checkers() {
        reset();
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
        turnOver = false;
        playerRedPieces = 12;
        playerWhitePieces = 12;
        playerRedMoves = new LinkedList<>();
        playerWhiteMoves = new LinkedList<>();
        skippedPieces = new LinkedList<>();
    }

    /**
     * Creates a Buffered Writer to write into a file current game state so users
     * can save the current game.
     * 
     * @param stringsToWrite information on game state that should be saved within
     *                       the file
     */
    public void save(ArrayList<String> stringsToWrite) {
        File gameProgress = new File("checkersProgress");
        BufferedWriter bw;
        try {
            bw = new BufferedWriter(new FileWriter(gameProgress, false));
            for (String string : stringsToWrite) {
                bw.write(string);
                bw.write("\n");
            }
            bw.close();
        } catch (IOException e) {
            System.out.println("Done Writing");
        }
    }

    /**
     * Creates Buffered Reader to read through file that stores last played game's
     * game state so that user can reload
     * their last played game into the game board.
     */
    public void load() {
        try {
            BufferedReader br = new BufferedReader(new FileReader("checkersProgress"));
            board = new int[8][8];
            for (int r = 0; r < 8; r++) {
                for (int c = 0; c < 8; c++) {
                    board[r][c] = Integer.parseInt(br.readLine());
                }
            }
            playerRedTurn = Boolean.parseBoolean(br.readLine());
            gameOver = Boolean.parseBoolean(br.readLine());
            turnOver = Boolean.parseBoolean(br.readLine());
            playerRedPieces = Integer.parseInt(br.readLine());
            playerWhitePieces = Integer.parseInt(br.readLine());
            playerRedMoves = new LinkedList<>();
            String redPlay = br.readLine();
            if (!redPlay.equals("null")) {
                Move secondToLastMove = new Move(
                        Integer.parseInt(redPlay), Integer.parseInt(br.readLine())
                );
                secondToLastMove.setIdentity(Integer.parseInt(br.readLine()));
                playerRedMoves.add(secondToLastMove);
                Move lastMove = new Move(
                        Integer.parseInt(br.readLine()), Integer.parseInt(br.readLine())
                );
                lastMove.setWasJump(Boolean.parseBoolean(br.readLine()));
                lastMove.setIdentity(Integer.parseInt(br.readLine()));
                playerRedMoves.add(lastMove);
            }
            playerWhiteMoves = new LinkedList<>();
            String whitePlay = br.readLine();
            if (!whitePlay.equals("null")) {
                Move secondToLastMove = new Move(
                        Integer.parseInt(whitePlay), Integer.parseInt(br.readLine())
                );
                secondToLastMove.setIdentity(Integer.parseInt(br.readLine()));
                playerWhiteMoves.add(secondToLastMove);
                Move lastMove = new Move(
                        Integer.parseInt(br.readLine()), Integer.parseInt(br.readLine())
                );
                lastMove.setWasJump(Boolean.parseBoolean(br.readLine()));
                lastMove.setIdentity(Integer.parseInt(br.readLine()));
                playerWhiteMoves.add(lastMove);
            }
            skippedPieces = new LinkedList<>();
            String skipped = br.readLine();
            if (!skipped.equals("null")) {
                Move skippedPiece = new Move(
                        Integer.parseInt(skipped), Integer.parseInt(br.readLine())
                );
                skippedPiece.setIdentity(Integer.parseInt(br.readLine()));
                skippedPieces.add(skippedPiece);
            }
            br.close();
        } catch (FileNotFoundException e) {
            System.out.println("File Not Found");
            throw new IllegalArgumentException();
        } catch (IOException e) {
            System.out.println("Reader cannot read file.");
            e.printStackTrace();
        }
    }

    /**
     * Moves onto the next player's turn once the current player confirms they are
     * done with their turn.
     */
    public void switchPlayers() {
        playerRedTurn = !playerRedTurn;
    }

    /**
     * Helper function for initializing game board by filling in with red player's
     * pieces denoted by
     * a 1 in the 2D array board.
     */
    private void fillInBoardWithRedPieces() {
        for (int r = 0; r < 3; r = r + 2) {
            for (int c = 0; c < 8; c = c + 2) {
                board[r][c] = 1;
            }
        }
        for (int r = 1; r < 3; r = r + 2) {
            for (int c = 1; c < 8; c = c + 2) {
                board[r][c] = 1;
            }
        }
    }

    /**
     * Helper function for initializing game board by filling in with white player's
     * pieces denoted by
     * a 2 in the 2D array board.
     */
    private void fillInBoardWithWhitePieces() {
        for (int r = 5; r < 8; r = r + 2) {
            for (int c = 1; c < 8; c = c + 2) {
                board[r][c] = 2;
            }
        }
        for (int r = 6; r < 8; r = r + 2) {
            for (int c = 0; c < 8; c = c + 2) {
                board[r][c] = 2;
            }
        }
    }

    /**
     * Helper function for initializing game board by filling in with the 2D array
     * board with checker pieces that
     * cannot be used by the checker pieces denoted by -1.
     */
    private void fillInBoardWithNonDiagonals() {
        for (int r = 1; r < 8; r = r + 2) {
            for (int c = 0; c < 8; c = c + 2) {
                board[r][c] = -1;
            }
        }
        for (int r = 0; r < 8; r = r + 2) {
            for (int c = 1; c < 8; c = c + 2) {
                board[r][c] = -1;
            }
        }
    }

    /**
     * Selects a checker piece to move and see if it is a valid piece to move. If
     * so, calls helper function to find
     * possible move coordinates.
     *
     * @param c - column of selected piece to be moved
     * @param r - row of selected piece to be moved
     */
    public void selectPieceToMove(int c, int r) {
        // if a selected piece is something you can move, then move it or else do
        // nothing
        Move thisMove = new Move(c, r);
        thisMove.setIdentity(board[r][c]);
        if (isRedPlayerPiece(c, r)) {
            playerRedMoves.add(thisMove);
            adjustBoardToPossibleMoves(findPossibleMoves(thisMove));
        } else if (isWhitePlayerPiece(c, r)) {
            playerWhiteMoves.add(thisMove);
            adjustBoardToPossibleMoves(findPossibleMoves(thisMove));
        }
    }

    /**
     * Helper function for determining if player pressed red checker piece.
     * 
     * @param c - column of checker piece pressed
     * @param r - row of checker piece pressed
     * @return boolean that is true if red checker piece at that location in board,
     *         false otherwise
     */
    private boolean isRedPlayerPiece(int c, int r) {
        return (playerRedTurn && (board[r][c] == 1)) || (playerRedTurn && (board[r][c] == 3));
    }

    /**
     * Helper function for determining if player pressed white checker piece.
     * 
     * @param c - column of checker piece pressed
     * @param r - row of checker piece pressed
     * @return boolean that is true if white checker piece at that location in
     *         board, false otherwise
     */
    private boolean isWhitePlayerPiece(int c, int r) {
        return (!playerRedTurn && (board[r][c] == 2)) || (!playerRedTurn && (board[r][c] == 4));
    }

    /**
     * Helper function to adjust board accordingly to account for possible moves for
     * a checker piece.
     *
     * @param moves - a list of possible moves that a checker piece can make
     */
    private void adjustBoardToPossibleMoves(ArrayList<Move> moves) {
        for (Move oneMove : moves) {
            board[oneMove.getR()][oneMove.getC()] = 5;
        }
    }

    /**
     * Finds possible moves that a checker piece can move to & adjusts the board so
     * that move can be made.
     *
     * @param thisMove - Move of current checker piece that user is selecting to
     *                 move
     * @return a list of possible moves that a checker piece can make
     */
    private ArrayList<Move> findPossibleMoves(Move thisMove) {
        ArrayList<Move> totalMoves = new ArrayList<>();
        int c = thisMove.getC();
        int r = thisMove.getR();
        int identity = thisMove.getIdentity();
        // if it's the red player, find where they can move/jump to
        if (playerRedTurn && !gameOver) {
            // if it's not a king, can only move in one direction
            ArrayList<Move> movesForward = findBlankSpacesForward(c, r);
            totalMoves.addAll(movesForward);
            if (jumpPossible(c, r)) {
                ArrayList<Move> jumpForward = findJumpsForward(c, r);
                totalMoves.addAll(jumpForward);
            }
            // if it's a king, then it can move all ways
            if (kingStatus(r) || identity == 3) {
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
            if (kingStatus(r) || identity == 4) {
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
     * Helper function for determining if given position is a 0 in the board or not.
     *
     * @param c - column value that is an int
     * @param r - row value that is an int
     * @return boolean that is true if at that position in the board there is a 0,
     *         false otherwise
     */
    private boolean isBlankSpace(int c, int r) {
        return board[r][c] == 0;
    }

    /**
     * Helper function for determining if given position in board is a red player's
     * piece or not for determining a jump.
     *
     * @param c - column value of piece that is an int
     * @param r - row value of piece that is an int
     * @return boolean that is true if the position on the board is a red player's
     *         piece, false otherwise
     */
    private boolean isRedPlayerPieceJump(int c, int r) {
        return ((board[r][c] == 2 && playerRedTurn) || (board[r][c] == 4 && playerRedTurn));
    }

    /**
     * Helper function for determining if given position in board is a white
     * player's piece or not for determining a jump.
     *
     * @param c - column value of piece that is an int
     * @param r - row value of piece that is an int
     * @return boolean that is true if the position on the board is a white player's
     *         piece, false otherwise
     */
    private boolean isWhitePlayerPieceJump(int c, int r) {
        return ((board[r][c] == 1 && !playerRedTurn) || (board[r][c] == 3 && !playerRedTurn));
    }

    /**
     * Finds possible spaces to move to in the forward direction of the checkerboard
     * (towards bottom).
     *
     * @param c - column of selected piece to be moved
     * @param r - row of selected piece to be moved
     * @return an ArrayList of type Moves that includes all possible moves forward
     *         for a checker piece
     */
    private ArrayList<Move> findBlankSpacesForward(int c, int r) {
        ArrayList<Move> moves = new ArrayList();
        if ((r + 1 < 8) && (c - 1 > -1)) {
            if (isBlankSpace(c - 1, r + 1)) {
                moves.add(new Move(c - 1, r + 1));
            }
        }
        if ((r + 1 < 8) && (c + 1 < 8)) {
            if (isBlankSpace(c + 1, r + 1)) {
                moves.add(new Move(c + 1, r + 1));
            }
        }
        return moves;
    }

    /**
     * Finds possible spaces to move to in the backward direction of the
     * checkerboard (towards top).
     *
     * @param c - column of selected piece to be moved
     * @param r - row of selected piece to be moved
     * @return an ArrayList of type Moves that includes all possible moves backward
     *         for a checker piece
     */
    private ArrayList<Move> findBlankSpacesBackward(int c, int r) {
        ArrayList<Move> moves = new ArrayList();
        if ((r - 1 > -1) && (c - 1 > -1)) {
            if (isBlankSpace(c - 1, r - 1)) {
                moves.add(new Move(c - 1, r - 1));
            }
        }
        if ((r - 1 > -1) && (c + 1 < 8)) {
            if (isBlankSpace(c + 1, r - 1)) {
                moves.add(new Move(c + 1, r - 1));
            }
        }
        return moves;
    }

    /**
     * Sees if a jump is possible for a selected checker piece.
     *
     * @param c - column of selected checker piece
     * @param r - row of selected checker piece
     * @return boolean that is true if there is a jump possible for the selected
     *         checker piece, false otherwise
     */
    private boolean jumpPossible(int c, int r) {
        // forward, left jump
        if ((r + 1 < 8) && (c - 1 > -1)) {
            if (isRedPlayerPieceJump(c - 1, r + 1) || isWhitePlayerPieceJump(c - 1, r + 1)) {
                return ((c - 2 > -1) && (r + 2 < 8));
            }
        }
        // forward, right jump
        if ((r + 1 < 8) && (c + 1 < 8)) {
            if (isRedPlayerPieceJump(c + 1, r + 1) || isWhitePlayerPieceJump(c + 1, r + 1)) {
                return ((c + 2 < 8) && (r + 2 < 8));
            }
        }
        // backward, left jump
        if ((r - 1 > -1) && (c - 1 > -1)) {
            if (isRedPlayerPieceJump(c - 1, r - 1) || isWhitePlayerPieceJump(c - 1, r - 1)) {
                return ((c - 2 > -1) && (r - 2 > -1));
            }
        }
        // backward, right jump
        if ((r - 1 > -1) && (c + 1 < 8)) {
            if (isRedPlayerPieceJump(c + 1, r - 1) || isWhitePlayerPieceJump(c + 1, r - 1)) {
                return ((c + 2 < 8) && (r - 2 > -1));
            }
        }
        return false;
    }

    /**
     * Finds possible spaces to jump to in the forward direction of the checkerboard
     * (towards bottom).
     * 
     * @param c - column of selected piece to be moved
     * @param r - row of selected piece to be moved
     * @return an ArrayList of type Moves that includes all possible jumps forward
     *         for a checker piece
     */
    private ArrayList<Move> findJumpsForward(int c, int r) {
        ArrayList<Move> moves = new ArrayList<>();
        // forward, left
        if ((r + 1 < 8) && (c - 1 > -1)) {
            if (isRedPlayerPieceJump(c - 1, r + 1) || isWhitePlayerPieceJump(c - 1, r + 1)) {
                if ((r + 2 < 8) && (c - 2 > -1)) {
                    if (isBlankSpace(c - 2, r + 2)) {
                        moves.add(new Move(c - 2, r + 2));
                    }
                }
            }
        }
        // forward, right
        if ((r + 1 < 8) && (c + 1 < 8)) {
            if (isRedPlayerPieceJump(c + 1, r + 1) || isWhitePlayerPieceJump(c + 1, r + 1)) {
                if ((r + 2 < 8) && (c + 2 < 8)) {
                    if (isBlankSpace(c + 2, r + 2)) {
                        moves.add(new Move(c + 2, r + 2));
                    }
                }
            }
        }
        return moves;
    }

    /**
     * Finds possible spaces to jump to in the backward direction of the
     * checkerboard (towards top).
     * 
     * @param c - column of selected piece to be moved
     * @param r - row of selected piece to be moved
     * @return an ArrayList of type Moves that includes all possible jumps backward
     *         for a checker piece
     */
    private ArrayList<Move> findJumpsBackward(int c, int r) {
        ArrayList<Move> moves = new ArrayList();
        // backward, left
        if ((r - 1 > -1) && (c - 1 > -1)) {
            if (isRedPlayerPieceJump(c - 1, r - 1) || isWhitePlayerPieceJump(c - 1, r - 1)) {
                if ((r - 2 > -1) && (c - 2 > -1)) {
                    if (isBlankSpace(c - 2, r - 2)) {
                        moves.add(new Move(c - 2, r - 2));
                    }
                }
            }
        }
        // backward, right
        if ((r - 1 > -1) && (c + 1 < 8)) {
            if (isRedPlayerPieceJump(c + 1, r - 1) || isWhitePlayerPieceJump(c + 1, r - 1)) {
                if ((r - 2 > -1) && (c + 2 < 8)) {
                    if (isBlankSpace(c + 2, r - 2)) {
                        moves.add(new Move(c + 2, r - 2));
                    }
                }
            }
        }
        return moves;
    }

    /**
     * Helper function for resetting the board after changing the state to reflect
     * possible moves for a
     * given checker piece.
     */
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
     * Changes board depending on where player wants to move the selected checker
     * piece to.
     * 
     * @param c - column of location of where to move chosen checker piece to
     * @param r - row of location of where to move chosen checker piece to
     */
    public void selectPieceToMoveTo(int c, int r) {
        // if the location is not a valid place for the checker piece to be moved to,
        // don't
        if ((board[r][c] != 5) || gameOver) {
            return;
        }
        // otherwise, modify board to move checker piece to new location
        Move thisMove = new Move(c, r);
        if (playerRedTurn) {
            Move lastMove = playerRedMoves.getLast();
            int lastC = lastMove.getC();
            int lastR = lastMove.getR();
            int identity = lastMove.getIdentity();
            // set new location to new assigned number
            if (kingStatus(r) || identity == 3) {
                thisMove.setIdentity(3);
                board[r][c] = 3;
            } else if (!kingStatus(r) || identity != 3) {
                board[r][c] = 1;
            }
            // if a checker piece can jump
            if (jumpPossible(lastC, lastR)) {
                // and the player made a successful jump, modify the board
                if (successfulSkipForPiece(c, r, lastMove)) {
                    Move skipped = findSkippedPiece(c, r, lastMove);
                    thisMove.setWasJump(true);
                    skipped.setIdentity(getCell(skipped.getC(), skipped.getR()));
                    board[skipped.getR()][skipped.getC()] = 0;
                    playerWhitePieces--;
                    skippedPieces.add(skipped);
                }
            }
            // set old location to blank
            board[lastR][lastC] = 0;
            // add this move to the list of moves made by player
            playerRedMoves.add(thisMove);
        } else {
            Move lastMove = playerWhiteMoves.getLast();
            int lastC = lastMove.getC();
            int lastR = lastMove.getR();
            int identity = lastMove.getIdentity();
            if (kingStatus(r) || identity == 4) {
                thisMove.setIdentity(4);
                board[r][c] = 4;
            } else if (!kingStatus(r) || identity == 2) {
                board[r][c] = 2;
            }
            if (jumpPossible(lastC, lastR)) {
                if (successfulSkipForPiece(c, r, lastMove)) {
                    Move skipped = findSkippedPiece(c, r, lastMove);
                    thisMove.setWasJump(true);
                    skipped.setIdentity(getCell(skipped.getC(), skipped.getR()));
                    board[skipped.getR()][skipped.getC()] = 0;
                    playerRedPieces--;
                    skippedPieces.add(skipped);
                }
            }
            board[lastR][lastC] = 0;
            playerWhiteMoves.add(thisMove);
        }
        // reset the board to before it showed possible moves & set the turn to be the
        // next player's
        resetOldPossibleMoves();
        turnOver = true;
    }

    /**
     * Helper function to check whether a skip over a checker piece was successful
     * or not.
     * 
     * @param c        - column of piece to be checked
     * @param r        - row of piece to be checked
     * @param lastMove - the coordinates of the last move made within the sequence
     *                 of moves of the player
     * @return boolean that is true if the skip was successful, false otherwise
     */
    private boolean successfulSkip(int c, int r, Move lastMove) {
        int lastC = lastMove.getC();
        int lastR = lastMove.getR();
        return ((c == lastC - 2) && (r == lastR + 2)) ||
                ((c == lastC - 2) && (r == lastR - 2)) ||
                ((c == lastC + 2) && (r == lastR - 2)) ||
                ((c == lastC + 2) && (r == lastR + 2));
    }

    /**
     * Checks whether the player made a move that was a successful skip.
     * 
     * @param c        - column of current location of piece
     * @param r        - row of current location of piece
     * @param lastMove - coordinates of the last move made within the sequence of
     *                 moves of the player
     * @return boolean that is true if the player made a successful skip, false
     *         otherwise
     */
    private boolean successfulSkipForPiece(int c, int r, Move lastMove) {
        // backward, right jump
        if ((r + 1 < 8) && (c - 1 > -1)) {
            if ((isRedPlayerPieceJump(c - 1, r + 1)) || (isWhitePlayerPieceJump(c - 1, r + 1))) {
                return successfulSkip(c, r, lastMove);
            }
        }
        // backward, left jump
        if ((r + 1 < 8) && (c + 1 < 8)) {
            if ((isRedPlayerPieceJump(c + 1, r + 1)) || (isWhitePlayerPieceJump(c + 1, r + 1))) {
                return successfulSkip(c, r, lastMove);
            }
        }
        // forward, left jump
        if ((r - 1 > -1) && (c - 1 > -1)) {
            if ((isRedPlayerPieceJump(c - 1, r - 1)) || (isWhitePlayerPieceJump(c - 1, r - 1))) {
                return successfulSkip(c, r, lastMove);
            }
        }
        // forward, right jump
        if ((r - 1 > -1) && (c + 1 < 8)) {
            if ((isRedPlayerPieceJump(c + 1, r - 1)) || (isWhitePlayerPieceJump(c + 1, r - 1))) {
                return successfulSkip(c, r, lastMove);
            }
        }
        return false;
    }

    /**
     * Finds the piece in which the chosen checker piece skipped over.
     * 
     * @param c        - column of checker piece that skipped over another piece
     * @param r        - row of checker piece that skipped over another piece
     * @param lastMove - location of checker piece before skipping over the other
     *                 piece
     * @return Move with coordinates of checker piece that was skipped over
     */
    private Move findSkippedPiece(int c, int r, Move lastMove) {
        if (c > lastMove.getC() && r > lastMove.getR()) {
            // skip forward, right
            return new Move(c - 1, r - 1);
        } else if (c > lastMove.getC() && r < lastMove.getR()) {
            // skip backward, right
            return new Move(c - 1, r + 1);
        } else if (c < lastMove.getC() && r < lastMove.getR()) {
            // skip backward, left
            return new Move(c + 1, r + 1);
        } else if (c < lastMove.getC() && r > lastMove.getR()) {
            // skip forward, left
            return new Move(c + 1, r - 1);
        }
        return null;
    }

    /**
     * Adjusts the board so that a player can restart their turn.
     */
    public void undo() {
        // allow player to make another move
        turnOver = false;
        if (playerRedTurn && playerRedMoves.size() > 1) {
            Move lastMove = playerRedMoves.getLast();
            int lastR = lastMove.getR();
            int lastC = lastMove.getC();
            // if last move was a jump, then put back the old piece
            if (lastMove.getWasJump()) {
                Move skipped = skippedPieces.getLast();
                int skippedIdentity = skipped.getIdentity();
                int skippedR = skipped.getR();
                int skippedC = skipped.getC();
                if (skippedIdentity == 2) {
                    board[skippedR][skippedC] = 2;
                } else if (skippedIdentity == 4) {
                    board[skippedR][skippedC] = 4;
                }
                skippedPieces.removeLast();
                playerWhitePieces++;
            }
            // save identity of last move to fill it in later as king or not
            int identity = lastMove.getIdentity();
            // delete most recent move
            board[lastR][lastC] = 0;
            playerRedMoves.removeLast();
            // get now their last move before recent move & fill it in
            Move secondLastMove = playerRedMoves.getLast();
            int secondLastR = secondLastMove.getR();
            int secondLastC = secondLastMove.getC();
            int secondLastMoveIdentity = secondLastMove.getIdentity();
            if (identity == 3 && secondLastMoveIdentity == 3) {
                board[secondLastR][secondLastC] = 3;
            } else {
                board[secondLastR][secondLastC] = 1;
            }
        } else if (!playerRedTurn && playerWhiteMoves.size() > 1) {
            Move lastMove = playerWhiteMoves.getLast();
            int lastR = lastMove.getR();
            int lastC = lastMove.getC();
            if (lastMove.getWasJump()) {
                Move skipped = skippedPieces.getLast();
                int skippedIdentity = skipped.getIdentity();
                int skippedR = skipped.getR();
                int skippedC = skipped.getC();
                if (skippedIdentity == 1) {
                    board[skippedR][skippedC] = 1;
                } else if (skippedIdentity == 3) {
                    board[skippedR][skippedC] = 3;
                }
                playerRedPieces++;
                skippedPieces.removeLast();
            }
            int identity = lastMove.getIdentity();
            board[lastR][lastC] = 0;
            playerWhiteMoves.removeLast();
            Move secondLastMove = playerWhiteMoves.getLast();
            int secondLastR = secondLastMove.getR();
            int secondLastC = secondLastMove.getC();
            int secondLastMoveIdentity = secondLastMove.getIdentity();
            if (identity == 4 && secondLastMoveIdentity == 4) {
                board[secondLastR][secondLastC] = 4;
            } else {
                board[secondLastR][secondLastC] = 2;
            }
        }
    }

    /**
     * Checks if a current checker piece is turned a King or not.
     * 
     * @param r - row of current checker piece to be checked
     * @return boolean that is true if the checker piece is a King, false otherwise
     */
    private boolean kingStatus(int r) {
        if (playerRedTurn) {
            return r == 7;
        } else {
            return r == 0;
        }
    }

    /**
     * checkWinner checks whether the game has reached a win condition (one player
     * loses all their pieces).
     *
     * @return 0 if nobody has won yet, 1 if player red has won, and 2 if player
     *         white has won
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
     * Getter for gameOver.
     * 
     * @return true if game is over, false if it isn't
     */
    public boolean getGameOver() {
        return gameOver;
    }

    /**
     * getCell is a getter for the contents of the cell specified by the method
     * arguments.
     *
     * @param c column to retrieve
     * @param r row to retrieve
     * @return an integer denoting the contents of the corresponding cell on the
     *         game board. 0 = empty, 1 or 3 = Red Player, 2 or 4 = White Player, -1
     *         = Non-diagonal, 5 = Possible Move
     */
    public int getCell(int c, int r) {
        return board[r][c];
    }

    /**
     * Getter function for how many pieces Red Player has left.
     * 
     * @return integer denoting how many pieces Red Player has
     */
    public int getPlayerRedPieces() {
        return playerRedPieces;
    }

    /**
     * Getter function for how many pieces White Player has left.
     * 
     * @return integer denoting how many pieces White Player has
     */
    public int getPlayerWhitePieces() {
        return playerWhitePieces;
    }

    /**
     * Getter for turnOver.
     * 
     * @return true if player's turn is over, false otherwise
     */
    public boolean getTurnOver() {
        return turnOver;
    }

    /**
     * Modifies turnOver dependent on if a user already took a turn or not.
     */
    public void changeTurnOver() {
        turnOver = !turnOver;
    }

    public int[][] getBoard() {
        return board;
    }

    public LinkedList<Move> getPlayerRedMoves() {
        return playerRedMoves;
    }

    public LinkedList<Move> getPlayerWhiteMoves() {
        return playerWhiteMoves;
    }

    public LinkedList<Move> getSkippedPieces() {
        return skippedPieces;
    }

}

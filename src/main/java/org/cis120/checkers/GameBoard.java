package org.cis120.checkers;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * This class instantiates a Checkers object, which is the model for the game.
 * As the user clicks the game board, the model is updated. Whenever the model
 * is updated, the game board repaints itself and updates its status JLabel to
 * reflect the current state of the model.
 */
@SuppressWarnings("serial")
public class GameBoard extends JPanel {

    private final Checkers checkers; // model for the game
    private final JLabel status; // current status text

    // game constants
    public static final int BOARD_WIDTH = 800;
    public static final int BOARD_HEIGHT = 800;

    /**
     * Initializes the game board.
     */
    public GameBoard(JLabel statusInit) {
        // creates border around the court area, JComponent method
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // Enable keyboard focus on the court area. When this component has the
        // keyboard focus, key events are handled by its key listener.
        setFocusable(true);

        // initialize instances
        checkers = new Checkers();
        status = statusInit;

        // listens for mouse clicks
        addMouseListeners();
    }

    /**
     * Listens for mouse clicks and updates the model based on user interaction.
     * User presses which checker piece
     * they want to move, and where they want to move it to. Adjusts checker's model
     * based on this interaction.
     */
    private void addMouseListeners() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                // if player's turn is over, don't let them continue moving pieces
                if (checkers.getTurnOver()) {
                    return;
                }
                // updates the model given the coordinates of the mouseclick
                Point p = e.getPoint();
                checkers.selectPieceToMove(p.x / 100, p.y / 100);
                updateStatus(); // updates the status JLabel
                repaint(); // repaints game board
            }
        });
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                Point p = e.getPoint();
                checkers.selectPieceToMoveTo(p.x / 100, p.y / 100);
                updateStatus();
                repaint();
            }
        });
    }

    /**
     * (Re-)sets the game to its initial state.
     */
    public void reset() {
        checkers.reset();
        status.setText("Red Player's Turn");
        repaint();
        requestFocusInWindow(); // makes sure this component has keyboard/mouse focus
    }

    /**
     * Allows player to undo their last move.
     */
    public void undo() {
        checkers.undo();
        repaint();
    }

    /**
     *
     */
    public void save() {
        ArrayList<String> thingsToSave = new ArrayList<String>();
        int[][] savedBoard = checkers.getBoard();
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                thingsToSave.add("" + savedBoard[r][c]);
            }
        }
        thingsToSave.add("" + checkers.getCurrentPlayer());
        thingsToSave.add("" + checkers.getGameOver());
        thingsToSave.add("" + checkers.getTurnOver());
        thingsToSave.add("" + checkers.getPlayerRedPieces());
        thingsToSave.add("" + checkers.getPlayerWhitePieces());

        LinkedList<Move> redPlayerMoves = checkers.getPlayerRedMoves();
        if (!redPlayerMoves.isEmpty() && redPlayerMoves.size() > 1) {
            thingsToSave.add("" + redPlayerMoves.get(redPlayerMoves.size() - 2).getC());
            thingsToSave.add("" + redPlayerMoves.get(redPlayerMoves.size() - 2).getR());
            thingsToSave.add("" + redPlayerMoves.get(redPlayerMoves.size() - 2).getIdentity());
            thingsToSave.add("" + redPlayerMoves.getLast().getC());
            thingsToSave.add("" + redPlayerMoves.getLast().getR());
            thingsToSave.add("" + redPlayerMoves.getLast().getWasJump());
            thingsToSave.add("" + redPlayerMoves.getLast().getIdentity());

        } else {
            thingsToSave.add("null");
        }
        LinkedList<Move> whitePlayerMoves = checkers.getPlayerWhiteMoves();
        if (!whitePlayerMoves.isEmpty()) {
            thingsToSave.add("" + whitePlayerMoves.get(whitePlayerMoves.size() - 2).getC());
            thingsToSave.add("" + whitePlayerMoves.get(whitePlayerMoves.size() - 2).getR());
            thingsToSave.add("" + whitePlayerMoves.get(whitePlayerMoves.size() - 2).getIdentity());
            thingsToSave.add("" + whitePlayerMoves.getLast().getC());
            thingsToSave.add("" + whitePlayerMoves.getLast().getR());
            thingsToSave.add("" + whitePlayerMoves.getLast().getWasJump());
            thingsToSave.add("" + whitePlayerMoves.getLast().getIdentity());
        } else {
            thingsToSave.add("null");
        }
        LinkedList<Move> skippedMoves = checkers.getSkippedPieces();
        if (!skippedMoves.isEmpty()) {
            thingsToSave.add("" + skippedMoves.getLast().getC());
            thingsToSave.add("" + skippedMoves.getLast().getR());
            thingsToSave.add("" + skippedMoves.getLast().getIdentity());
        } else {
            thingsToSave.add("null");
        }
        checkers.save(thingsToSave);
    }

    public void load() {
        checkers.load();
        if (checkers.getCurrentPlayer()) {
            status.setText("Red Player's Turn");
        } else {
            status.setText("White Player's Turn");
        }
        repaint();
        requestFocusInWindow();
    }

    /**
     * Switches whose turn it is between the Red and White player.
     */
    public void switchPlayers() {
        checkers.switchPlayers();
    }

    /**
     * Updates the JLabel to reflect the current state of the game.
     */
    public void updateStatus() {
        if (checkers.getCurrentPlayer()) {
            status.setText("Red Player's Turn");
        } else {
            status.setText("White Player's Turn");
        }
        int winner = checkers.checkWinner();
        if (winner == 1) {
            status.setText("Red Player wins!");
        } else if (winner == 2) {
            status.setText("White Player wins!");
        }
    }

    /**
     * Updates when a player's turn is over.
     */
    public void updateTurnOver() {
        checkers.changeTurnOver();
    }

    /**
     * Draws the game board.
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawBoard(g);
        drawBlackCheckers(g);
        drawWhiteCheckers(g);
        drawCheckerPieces(g);
        System.out.println("Board");
        checkers.printGameState();
    }

    /**
     * Helper function for drawing game board.
     */
    private void drawBoard(Graphics g) {
        g.drawLine(100, 0, 100, 800);
        g.drawLine(200, 0, 200, 800);
        g.drawLine(0, 100, 800, 100);
        g.drawLine(0, 200, 800, 200);
        g.drawLine(300, 0, 300, 800);
        g.drawLine(400, 0, 400, 800);
        g.drawLine(0, 300, 800, 300);
        g.drawLine(0, 400, 800, 400);
        g.drawLine(500, 0, 500, 800);
        g.drawLine(600, 0, 600, 800);
        g.drawLine(0, 500, 800, 500);
        g.drawLine(0, 600, 800, 600);
        g.drawLine(700, 0, 700, 800);
        g.drawLine(800, 0, 800, 800);
        g.drawLine(0, 700, 800, 700);
        g.drawLine(0, 800, 800, 800);
    }

    /**
     * Helper function for drawing game board's checkers.
     */
    private void drawBlackCheckers(Graphics g) {
        g.setColor(Color.black);
        for (int r = 0; r < 800; r = r + 200) {
            for (int c = 0; c < 800; c = c + 200) {
                g.fillRect(r, c, 100, 100);
            }
        }
        for (int r = 100; r < 800; r = r + 200) {
            for (int c = 100; c < 800; c = c + 200) {
                g.fillRect(r, c, 100, 100);
            }
        }
    }

    /**
     * Helper function for drawing game board's checkers.
     */
    private void drawWhiteCheckers(Graphics g) {
        g.setColor(Color.white);
        for (int r = 0; r < 800; r = r + 200) {
            for (int c = 100; c < 800; c = c + 200) {
                g.fillRect(r, c, 100, 100);
            }
        }
        for (int r = 100; r < 800; r = r + 200) {
            for (int c = 0; c < 800; c = c + 200) {
                g.fillRect(r, c, 100, 100);
            }
        }
    }

    /**
     * Helper function for drawing game board's checker pieces.
     */
    private void drawCheckerPieces(Graphics g) {
        for (int r = 0; r < 800; r = r + 100) {
            for (int c = 0; c < 800; c = c + 100) {
                int row = returnClipped(r);
                int col = returnClipped(c);
                int state = checkers.getCell(col, row);
                if (state == 1) {
                    // red checker
                    g.setColor(Color.red);
                    g.fillOval(30 + 100 * col, 30 + 100 * row, 40, 40);
                } else if (state == 2) {
                    // white checker
                    g.setColor(Color.white);
                    g.fillOval(30 + 100 * col, 30 + 100 * row, 40, 40);
                } else if (state == 3) {
                    // red king
                    g.setColor(Color.red);
                    g.fillOval(30 + 100 * col, 30 + 100 * row, 40, 40);
                    g.drawString("K", 30 + 100 * col, 30 + 100 * row);
                } else if (state == 4) {
                    // white king
                    g.setColor(Color.white);
                    g.fillOval(30 + 100 * col, 30 + 100 * row, 40, 40);
                    g.drawString("K", 30 + 100 * col, 30 + 100 * row);
                }
            }
        }
    }

    /**
     * Helper function for finding clipped version of row and column of given
     * position in the game board.
     * 
     * @param v - position value that needs to be clipped to be found within the
     *          game board
     * @return 0 if it is 0 to avoid int division by 0, otherwise the value divided
     *         by 100 so that the value is
     *         downsized to the 2D array size of checkers model
     */
    private int returnClipped(int v) {
        if (v == 0) {
            return 0;
        } else {
            return v / 100;
        }
    }

    /**
     * Returns the size of the game board.
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(BOARD_WIDTH, BOARD_HEIGHT);
    }
}

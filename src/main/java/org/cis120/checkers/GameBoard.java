package org.cis120.checkers;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * This class instantiates a Checkers object, which is the model for the game.
 * As the user clicks the game board, the model is updated. Whenever the model
 * is updated, the game board repaints itself and updates its status JLabel to
 * reflect the current state of the model.
 */
@SuppressWarnings("serial")
public class GameBoard extends JPanel {

    private Checkers checkers; // model for the game
    private JLabel status; // current status text

    // Game constants
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

        checkers = new Checkers(); // initializes model for the game
        status = statusInit; // initializes the status JLabel

        /*
         * Listens for mouseclicks. Updates the model, then updates the game
         * board based off of the updated model.
         */
        addMouseListeners();
    }

    public void addMouseListeners() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                Point p = e.getPoint();
                // updates the model given the coordinates of the mouseclick
                checkers.selectPieceToMove(p.x / 100, p.y / 100);
                updateStatus(); // updates the status JLabel
                //repaint(); // repaints the game board
                System.out.println("Game state when clicking piece:");
                checkers.printGameState();

            }
        });
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                Point p = e.getPoint();
                // updates the model given the coordinates of the mouseclick
                checkers.selectPieceToMoveTo(p.x / 100, p.y / 100);
                updateStatus(); // updates the status JLabel
                repaint(); // repaints the game board
                System.out.println("Game state when moving piece:");
                checkers.printGameState();
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
        // Makes sure this component has keyboard/mouse focus
        requestFocusInWindow();
    }

    public void undo() {
        checkers.undo();
        repaint();
        addMouseListeners();
    }

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
        System.out.println("Red:" + checkers.getPlayerRedPieces());
        System.out.println("White" + checkers.getPlayerWhitePieces());
        int winner = checkers.checkWinner();
        if (winner == 1) {
            status.setText("Red Player wins!");
        } else if (winner == 2) {
            status.setText("White Player wins!");
        } else if (winner == 3) {
            status.setText("It's a tie.");
        }
    }

    /**
     * Draws the game board.
     * 
     * There are many ways to draw a game board. This approach
     * will not be sufficient for most games, because it is not
     * modular. All of the logic for drawing the game board is
     * in this method, and it does not take advantage of helper
     * methods. Consider breaking up your paintComponent logic
     * into multiple methods or classes, like Mushroom of Doom.
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Draws board grid
        drawBoard(g);
        drawBlackCheckers(g);
        drawWhiteCheckers(g);
        drawCheckerPieces(g);
    }

    public void drawBoard(Graphics g) {
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

    public void drawBlackCheckers(Graphics g) {
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

    public void drawWhiteCheckers(Graphics g) {
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

    public void drawCheckerPieces(Graphics g) {
        checkers.printGameState();
        for (int r = 0; r < 800; r=r+100) {
            for (int c = 0; c < 800; c=c+100) {
                int row = returnClipped(r);
                int col = returnClipped(c);
                int player = checkers.getCell(col, row);
                if (player == 1) {
                    g.setColor(Color.red);
                    g.fillOval(30 + 100 * col, 30 + 100 * row, 40, 40);
                } else if (player == 2) {
                    g.setColor(Color.white);
                    g.fillOval(30 + 100 * col, 30 + 100 * row, 40, 40);
                } else if (player == 3) {
                    g.setColor(Color.red);
                    g.fillOval(30 + 100 * col, 30 + 100 * row, 40, 40);
                    g.drawString("K", 30 + 100 * col, 30 + 100 * row);
                } else if (player == 4) {
                    g.setColor(Color.white);
                    g.fillOval(30 + 100 * col, 30 + 100 * row, 40, 40);
                    g.drawString("K", 30 + 100 * col, 30 + 100 * row);
                }
            }
        }
    }

    public int returnClipped(int v) {
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

package org.cis120.checkers;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * This class sets up the top-level frame and widgets for the GUI.
 */

public class RunCheckers implements Runnable {
    public void run() {
        // Top-level frame in which game components live
        final JFrame frame = new JFrame("Checkers");
        frame.setLocation(800, 800);

        // Status panel
        final JPanel status_panel = new JPanel();
        frame.add(status_panel, BorderLayout.SOUTH);
        final JLabel status = new JLabel("Setting up...");
        status_panel.add(status);

        // Game board
        final GameBoard board = new GameBoard(status);
        frame.add(board, BorderLayout.CENTER);

        // Reset button
        final JPanel control_panel = new JPanel();
        frame.add(control_panel, BorderLayout.NORTH);
        final JButton reset = new JButton("Reset");
        reset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                board.reset();
            }
        });
        control_panel.add(reset);

        // Undo button
        final JButton undo = new JButton("Undo");
        undo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                board.undo();
            }
        });
        control_panel.add(undo);

        // Confirm done with turn button
        final JButton confirm = new JButton("Confirm");
        confirm.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                board.switchPlayers();
            }
        });
        control_panel.add(confirm);

        // Instructions of game button
        final JButton instructions = new JButton("How To Play");
        instructions.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane jp = new JOptionPane();
                JButton close = new JButton("Close");
                jp.showMessageDialog(close, "Rules of Checkers\n" +
                        "1. Checker pieces can only move diagonally on black tiles.\n" +
                        "2. Regular checker pieces may only move forward diagonally.\n" +
                        "3. A checker piece can become a king once it reaches the opponent's end and " +
                        "cannot move forward anymore. A king can move backwards and forwards diagonally.\n" +
                        "4. The game is over once one player runs out of all their checkers.\n" +
                        "5. The player with black checkers moves first.\n"
                );
            }
        });
        control_panel.add(instructions);

        // Quit Button
        final JButton quit = new JButton("Quit");
        quit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

            }
        });
        control_panel.add(quit);

        // Put the frame on the screen
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // Start the game
        board.reset();
    }
}
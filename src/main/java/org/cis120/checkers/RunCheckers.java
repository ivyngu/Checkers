package org.cis120.checkers;

import javax.swing.*;
import java.awt.*;

/**
 * This class sets up the top-level frame and widgets for the GUI.
 */

public class RunCheckers implements Runnable {
    public void run() {
        // top level frame in which game components live
        final JFrame frame = new JFrame("Checkers");
        frame.setLocation(800, 800);

        // status panel
        final JPanel status_panel = new JPanel();
        frame.add(status_panel, BorderLayout.SOUTH);
        final JLabel status = new JLabel("Setting up...");
        status_panel.add(status);

        // game board
        final GameBoard board = new GameBoard(status);
        frame.add(board, BorderLayout.CENTER);

        // control panel
        final JPanel control_panel = new JPanel();
        frame.add(control_panel, BorderLayout.NORTH);

        // Instructions of game button
        final JButton instructions = new JButton("How To Play");
        instructions.addActionListener(e -> {
            new JOptionPane();
            JButton close = new JButton("Close");
            JOptionPane.showMessageDialog(
                    close, "Checkers: Rules & Controls\n" +
                            "Rules:\n" +
                            "1. Checker pieces can only move diagonally on black tiles.\n" +
                            "2. Regular checker pieces may only move towards the other "
                            +
                            "player's pieces diagonally.\n"
                            +
                            "3. A checker piece can become a king once it " +
                            "reaches the opponent's end and "
                            +
                            "cannot move forward anymore. A king can move " +
                            "backwards and forwards diagonally.\n"
                            +
                            "4. The game is over once one player runs out " +
                            "of all their checkers.\n"
                            +
                            "5. The player with red checkers moves first.\n" +
                            "6. If a player can skip a checker piece, they are " +
                            "only allowed to skip one at once. "
                            +
                            "No double jumps allowed.\n" +
                            "7. Players can only undo their own last move " +
                            "if they have made one during their turn. "
                            +
                            "Players cannot undo the other person's turn.\n" +
                            "Controls:\n" +
                            "- Click on the piece you want to move with the mouse.\n" +
                            "- Click to where you want to move it to with the mouse.\n" +
                            "- Every player must press the confirm button" +
                            " once they are done with their turn "
                            +
                            "before moving onto the next player. If one wishes" +
                            " to undo their last move, they can.\n"
                            +
                            "Once a player confirms their turn, they" +
                            " cannot go back to their last move.\n"
            );
        });
        control_panel.add(instructions);

        // save Button
        final JButton save = new JButton("Save & Quit");
        save.addActionListener(e -> {
            board.save();
            System.exit(0);
        });
        control_panel.add(save);

        // save Button
        final JButton reload = new JButton("Reload Previous Game");
        reload.addActionListener(e -> board.load());
        control_panel.add(reload);

        // reset button
        final JButton reset = new JButton("Reset");
        reset.addActionListener(e -> board.reset());
        control_panel.add(reset);

        // undo button
        final JButton undo = new JButton("Undo");
        undo.addActionListener(e -> board.undo());
        control_panel.add(undo);

        // confirm done with turn button
        final JButton confirm = new JButton("Confirm");
        confirm.addActionListener(e -> {
            board.switchPlayers();
            board.updateTurnOver();
            board.updateStatus();
        });
        control_panel.add(confirm);

        // put the frame on the screen
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // start the game
        board.reset();
    }
}
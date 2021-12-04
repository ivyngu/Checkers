package org.cis120;

import org.cis120.checkers.Checkers;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Game {
    /**
     * Main method run to start and run the game. Initializes the runnable game
     * class of your choosing and runs it.
     */
    public static void main(String[] args) {
        Runnable game = new org.cis120.checkers.RunCheckers();
        SwingUtilities.invokeLater(game);
        Checkers c = new Checkers();
    }
}

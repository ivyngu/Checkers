package org.cis120.checkers;

/*
 * This class keeps track of Moves made on the Checker Board by a player.
 */

public class Move {
    private int c; // column of move made in board
    private int r; // row of move made in baord
    private boolean wasJump; // if move was jump or not
    private int identity; // what type of piece it was (red or white, king)

    public Move(int c, int r) {
        this.c = c;
        this.r = r;
        wasJump = false;
        identity = 0;
    }

    public int getC() {
        return c;
    }

    public int getR() {
        return r;
    }

    public boolean getWasJump() {
        return wasJump;
    }

    public int getIdentity() {
        return identity;
    }

    public void setIdentity(int i) {
        identity = i;
    }

    public void setWasJump(boolean jump) {
        wasJump = jump;
    }
}

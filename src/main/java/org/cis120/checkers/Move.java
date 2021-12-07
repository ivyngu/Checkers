package org.cis120.checkers;

public class Move {
    private int c;
    private int r;
    private boolean wasJump;
    private int identity;

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

package edu.umkc.hfridell.umpirebuddy;

public class AtBat {

    protected int strikeCount;
    protected int ballCount;

    AtBat() {
        strikeCount = 0;
        ballCount = 0;
    }

    public int getStrikeCount() {
        return strikeCount;
    }

    public void strike() {
        this.strikeCount++;
    }

    public int getBallCount() {
        return ballCount;
    }

    public void ball() {
        this.ballCount++;
    }






}

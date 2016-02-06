package edu.umkc.hfridell.umpirebuddy;

import android.os.Parcel;
import android.os.Parcelable;

public class AtBat implements Parcelable {

    protected int strikeCount;
    protected int ballCount;

    AtBat() {
        strikeCount = 0;
        ballCount = 0;
    }

    AtBat(int strikeCount, int ballCount){
        this.strikeCount = strikeCount;
        this.ballCount = ballCount;
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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(strikeCount);
        dest.writeInt(ballCount);
    }

    public static final Parcelable.Creator<AtBat> CREATOR
            = new Parcelable.Creator<AtBat>(){
        public AtBat createFromParcel(Parcel in){
            return new AtBat(in);
        }

        @Override
        public AtBat[] newArray(int size) {
            return new AtBat[size];
        }
    };

    private AtBat(Parcel in){
        strikeCount = in.readInt();
        ballCount = in.readInt();
    }
}

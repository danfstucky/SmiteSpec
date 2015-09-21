package edu.umkc.dfsy8cmail.smitespec;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Dan on 9/20/2015.
 */
public class SmitePlayer implements Parcelable {
    private int mLevel;
    private int mLosses;
    private int mMasteryLevel;
    private String mName;
    private int mTeamId;
    private String mTeamName;
    private int mWins;


    public SmitePlayer() {

    }

    protected SmitePlayer(Parcel in) {
        mLevel = in.readInt();
        mLosses = in.readInt();
        mMasteryLevel = in.readInt();
        mName = in.readString();
        mTeamId = in.readInt();
        mTeamName = in.readString();
        mWins = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mLevel);
        dest.writeInt(mLosses);
        dest.writeInt(mMasteryLevel);
        dest.writeString(mName);
        dest.writeInt(mTeamId);
        dest.writeString(mTeamName);
        dest.writeInt(mWins);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static Creator<SmitePlayer> CREATOR = new Creator<SmitePlayer>() {
        @Override
        public SmitePlayer createFromParcel(Parcel in) {
            return new SmitePlayer(in);
        }

        @Override
        public SmitePlayer[] newArray(int size) {
            return new SmitePlayer[size];
        }
    };

    public int getLevel() {
        return mLevel;
    }

    public void setLevel(int mLevel) {
        this.mLevel = mLevel;
    }

    public int getLosses() {
        return mLosses;
    }

    public void setLosses(int mLosses) {
        this.mLosses = mLosses;
    }

    public int getMasteryLevel() {
        return mMasteryLevel;
    }

    public void setMasteryLevel(int mMasteryLevel) {
        this.mMasteryLevel = mMasteryLevel;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public int getTeamId() {
        return mTeamId;
    }

    public void setTeamId(int mTeamId) {
        this.mTeamId = mTeamId;
    }

    public String getTeamName() {
        return mTeamName;
    }

    public void setTeamName(String mTeamName) {
        this.mTeamName = mTeamName;
    }

    public int getWins() {
        return mWins;
    }

    public void setWins(int mWins) {
        this.mWins = mWins;
    }

    // Parses a json player object into java SmitePlayer object
    // throws a JSONException if parsing fails
    public void parsePlayer(JSONObject jsonPlayer) throws JSONException {
        setLevel(jsonPlayer.getInt("Level"));
        setLosses(jsonPlayer.getInt("Losses"));
        setMasteryLevel(jsonPlayer.getInt("MasteryLevel"));
        setName(jsonPlayer.getString("Name"));
        setTeamId(jsonPlayer.getInt("TeamId"));
        setTeamName(jsonPlayer.getString("Team_Name"));
        setWins(jsonPlayer.getInt("Wins"));
    }
}

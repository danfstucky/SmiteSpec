package edu.umkc.dfsy8cmail.smitespec;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Dan on 9/20/2015.
 */
public class SmitePlayer {
    private int mLevel;
    private int mLosses;
    private int mMasteryLevel;
    private String mName;
    private int mTeamId;
    private String mTeamName;
    private int mWins;

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

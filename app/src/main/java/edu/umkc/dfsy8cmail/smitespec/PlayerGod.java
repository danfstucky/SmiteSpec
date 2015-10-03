package edu.umkc.dfsy8cmail.smitespec;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Dan on 10/3/2015.
 */
public class PlayerGod {
    private int mAssists;
    private int mDeaths;
    private String mGodName;
    private int mGodId;
    private int mKills;
    private int mLosses;
    private int mMatches;
    private String mQueue;
    private int mWins;

    public PlayerGod() {

    }

    // getters and setters
    public int getAssists() {
        return mAssists;
    }

    public void setAssists(int mAssists) {
        this.mAssists = mAssists;
    }

    public int getDeaths() {
        return mDeaths;
    }

    public void setDeaths(int mDeaths) {
        this.mDeaths = mDeaths;
    }

    public String getGodName() {
        return mGodName;
    }

    public void setGodName(String mGodName) {
        this.mGodName = mGodName;
    }

    public int getGodId() {
        return mGodId;
    }

    public void setGodId(int mGodId) {
        this.mGodId = mGodId;
    }

    public int getKills() {
        return mKills;
    }

    public void setKills(int mKills) {
        this.mKills = mKills;
    }

    public int getLosses() {
        return mLosses;
    }

    public void setLosses(int mLosses) {
        this.mLosses = mLosses;
    }

    public int getMatches() {
        return mMatches;
    }

    public void setMatches(int mMatches) {
        this.mMatches = mMatches;
    }

    public String getQueue() {
        return mQueue;
    }

    public void setQueue(String mQueue) {
        this.mQueue = mQueue;
    }

    public int getWins() {
        return mWins;
    }

    public void setWins(int mWins) {
        this.mWins = mWins;
    }

    // Parses a json friend object into java PlayerGod object
    // throws a JSONException if parsing fails
    public void parse(JSONObject jsonPlayerGod) throws JSONException {
        setAssists(jsonPlayerGod.getInt("Assists"));
        setDeaths(jsonPlayerGod.getInt("Deaths"));
        setGodName(jsonPlayerGod.getString("God"));
        setGodId(jsonPlayerGod.getInt("GodId"));
        setKills(jsonPlayerGod.getInt("Kills"));
        setLosses(jsonPlayerGod.getInt("Losses"));
        setMatches(jsonPlayerGod.getInt("Matches"));
        setQueue(jsonPlayerGod.getString("Queue"));
        setWins(jsonPlayerGod.getInt("Wins"));
    }
}

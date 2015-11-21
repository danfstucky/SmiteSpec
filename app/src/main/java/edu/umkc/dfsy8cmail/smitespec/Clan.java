package edu.umkc.dfsy8cmail.smitespec;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Dan on 11/10/2015.
 */
public class Clan {
    private String mClanName;
    private int mWins;
    private int mLosses;
    private String mFounder;
    private int mNumMembers;
    private int mRating;
    private String mTag;


    public String getClanName() {
        return mClanName;
    }

    public void setClanName(String mClanName) {
        this.mClanName = mClanName;
    }

    public int getWins() {
        return mWins;
    }

    public void setWins(int mWins) {
        this.mWins = mWins;
    }

    public int getLosses() {
        return mLosses;
    }

    public void setLosses(int mLosses) {
        this.mLosses = mLosses;
    }

    public String getFounder() {
        return mFounder;
    }

    public void setFounder(String mFounder) {
        this.mFounder = mFounder;
    }

    public int getNumMembers() {
        return mNumMembers;
    }

    public void setNumMembers(int mNumMembers) {
        this.mNumMembers = mNumMembers;
    }

    public int getRating() {
        return mRating;
    }

    public void setRating(int mRating) {
        this.mRating = mRating;
    }

    public String getTag() {
        return mTag;
    }

    public void setTag(String mTag) {
        this.mTag = mTag;
    }

    // Parses a json clan object into java Clan object
    // throws a JSONException if parsing fails
    public void parseClan(JSONObject jsonClan) throws JSONException {
        setClanName(jsonClan.getString("Name"));
        setWins(jsonClan.getInt("Wins"));
        setLosses(jsonClan.getInt("Losses"));
        setFounder(jsonClan.getString("Founder"));
        setNumMembers(jsonClan.getInt("Players"));
        setRating(jsonClan.getInt("Rating"));
        setTag(jsonClan.getString("Tag"));
    }
}

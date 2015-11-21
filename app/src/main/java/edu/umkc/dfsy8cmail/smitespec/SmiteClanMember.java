package edu.umkc.dfsy8cmail.smitespec;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Dan on 11/14/2015.
 */
public class SmiteClanMember {
    private String mName;
    private FriendStatus mStatus;

    public SmiteClanMember(){

    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public void setMemberStatus(FriendStatus stat) {
        mStatus = stat;
    }

    public FriendStatus getMemberStatus() {
        return mStatus;
    }

    // Parses a json clan member object into java SmiteClanMember object
    // throws a JSONException if parsing fails
    public void parse(JSONObject jsonPlayer) throws JSONException {
        setName(jsonPlayer.getString("Name"));
    }
}

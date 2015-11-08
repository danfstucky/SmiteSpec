package edu.umkc.dfsy8cmail.smitespec;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Dan on 9/23/2015.
 */
public class SmiteFriend {
    private String mName;
    private String mPlayer_id;
    private FriendStatus mStatus;

    public SmiteFriend(){

    }

    public String getPlayer_id() {
        return mPlayer_id;
    }

    public void setPlayer_id(String player_id) {
        this.mPlayer_id = player_id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public void setFriendStatus(FriendStatus stat) {
        mStatus = stat;
    }

    public FriendStatus getFriendStatus() {
        return mStatus;
    }

    // Parses a json friend object into java SmiteFriend object
    // throws a JSONException if parsing fails
    public void parse(JSONObject jsonFriend) throws JSONException {
        setName(jsonFriend.getString("name"));
        setPlayer_id(jsonFriend.getString("player_id"));
    }
}

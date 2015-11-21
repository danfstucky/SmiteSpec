package edu.umkc.dfsy8cmail.smitespec;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dan on 9/23/2015.
 * Container Class to hold player's friends
 */
public class FriendsList {
    private List<SmiteFriend> mFriends;
    private String mPlayerId;

    public FriendsList() {
        mFriends = new ArrayList<>();
        mPlayerId = "";
    }
    public FriendsList(String player) {
        mFriends = new ArrayList<>();
        mPlayerId = player;
    }

    public List<SmiteFriend> getPlayers() {
        return mFriends;
    }

    public SmiteFriend getPlayer(String id) {
        for (SmiteFriend friend : mFriends) {
            if (friend.getPlayer_id().equals(id)) {
                return friend;
            }
        }
        return null;
    }

    public void addFriend(SmiteFriend friend) {
        mFriends.add(friend);
    }

    public String getPlayerId() {
        return mPlayerId;
    }

    public void setPlayerId(String mPlayerId) {
        this.mPlayerId = mPlayerId;
    }
}

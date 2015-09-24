package edu.umkc.dfsy8cmail.smitespec;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dan on 9/23/2015.
 * Singleton class to hold player's friends
 */
public class CurrentFriendsList {
    private static CurrentFriendsList sCurrentFriendsList;
    private List<SmiteFriend> mFriends;
    private String mPlayerId;

    public static CurrentFriendsList get(Context context, String player) {
        if (sCurrentFriendsList == null) {
            sCurrentFriendsList = new CurrentFriendsList(context, player);
        }
        return sCurrentFriendsList;
    }

    private CurrentFriendsList(Context context, String player) {
        mFriends = new ArrayList<SmiteFriend>();
        mPlayerId = player;
    }

    public List<SmiteFriend> getFriends() {
        return mFriends;
    }

    public SmiteFriend getFriend(String id) {
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

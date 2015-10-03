package edu.umkc.dfsy8cmail.smitespec;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dan on 10/3/2015.
 * Singleton class to hold a player's stats for every god
 */

public class PlayerGodsList {
    private static PlayerGodsList sPlayerGodsList;
    private List<PlayerGod> mGods;

    public static PlayerGodsList get(Context context) {
        if (sPlayerGodsList == null) {
            sPlayerGodsList = new PlayerGodsList(context);
        }
        return sPlayerGodsList;
    }

    private PlayerGodsList(Context context) {
        mGods = new ArrayList<PlayerGod>();
    }

    public List<PlayerGod> getGodsStats() {
        return mGods;
    }

    // Retrieve a god from list by id
    public PlayerGod getGodStats(int id) {
        for (PlayerGod god : mGods) {
            if (god.getGodId() ==  id) {
                return god;
            }
        }
        return null;
    }

    public void addGodStats(PlayerGod god) {
        mGods.add(god);
    }
}

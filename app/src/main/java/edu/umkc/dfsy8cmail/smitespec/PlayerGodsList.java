package edu.umkc.dfsy8cmail.smitespec;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dan on 10/3/2015.
 * Container class to hold a player's stats for every god
 */

public class PlayerGodsList {
    //private static PlayerGodsList sPlayerGodsList;
    private List<PlayerGod> mGods;
    private String mWin_Loss_Ratio;
    private String mKill_Death_Ratio;
    private PlayerGod mFavGod;
    private PlayerGod mWinsGod;
    private PlayerGod mKDGod;

    public PlayerGodsList(Context cont) {
        mGods = new ArrayList<PlayerGod>();
        mFavGod = new PlayerGod(cont);
        mWinsGod = new PlayerGod(cont);
        mKDGod = new PlayerGod(cont);
    }

    public String getWin_Loss_Ratio() {
        return mWin_Loss_Ratio;
    }

    public String getKill_Death_Ratio() {
        return mKill_Death_Ratio;
    }

    public PlayerGod getFavGod() {
        return mFavGod;
    }

    public PlayerGod getWinsGod() {
        return mWinsGod;
    }

    public PlayerGod getKDGod() {
        return mKDGod;
    }

    public List<PlayerGod> getGodsStats() {
        return mGods;
    }

    public void addGodStats(PlayerGod god) {
        mGods.add(god);
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

    //Determines a player's overall W/L ratio, K/D ratio, favorite god, god with most wins, and god with best K/D
    public void setPlayerGodStats() {
        //initialize variables
        int total_kills = 0;
        int total_deaths = 0;
        int total_wins = 0;
        int total_losses = 0;
        mFavGod = mGods.get(0);
        mWinsGod = mGods.get(0);
        mKDGod = mGods.get(0);

        for (PlayerGod god : mGods) {
            total_kills += god.getKills();
            total_deaths += god.getDeaths();
            total_wins += god.getWins();
            total_losses += god.getLosses();
            if (god.getMatches() > mFavGod.getMatches()) { mFavGod = god; }
            if (god.getWins() > mWinsGod.getWins()) { mWinsGod = god; }
            if (god.getKD() > mKDGod.getKD()) { mKDGod = god; }
        }
        //if (total_losses == 0) { mWin_Loss_Ratio = total_wins; }
        //else { mWin_Loss_Ratio = total_wins / total_losses; }
        mWin_Loss_Ratio = total_wins + "/" + total_losses;
        mKill_Death_Ratio = total_kills + "/" + total_deaths;
    }

}

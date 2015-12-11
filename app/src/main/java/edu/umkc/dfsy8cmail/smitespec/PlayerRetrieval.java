package edu.umkc.dfsy8cmail.smitespec;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.cr5315.jSmite.Smite;

/**
 * Created by Dan on 11/14/2015.
 * This class if for retrieving a Player's data from the smite api
 */
public class PlayerRetrieval {
    private static final String TAG = "PlayerRetrieval";
    private Smite mSmite;

    PlayerRetrieval(Smite smite) {
        mSmite = smite;
    }

    public SmitePlayer fetchPlayer(String query) throws JSONException {
        // Retrieve json data of player from Smite API
        String player = mSmite.getPlayer(query);
        Log.i(TAG, "Fetched player: " + player);
        // Retrieve json array and get json object from array.  Player data only ever has 1 object in array
        JSONArray data = new JSONArray(player);
        JSONObject jsonPlayer = data.getJSONObject(0);
        // Parse json data into java object
        SmitePlayer friendPlayer = new SmitePlayer();
        friendPlayer.parsePlayer(jsonPlayer);
        return friendPlayer;
    }
}

package edu.umkc.dfsy8cmail.smitespec;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

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

    private Context mContext;
    private Smite mSmite;
    private String mPlayerIdentifier;  // can be a name or id

    PlayerRetrieval(Context context, Smite smite, String id) {
        mContext = context;
        mSmite = smite;
        mPlayerIdentifier = id;
    }


    private class FetchPlayerTask extends AsyncTask<String, Void, SmitePlayer> {

        @Override
        protected SmitePlayer doInBackground(String... params) {
            try {
                String query = params[0];
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
            } catch (JSONException js) {
                // This will happen if player doesn't exist
                Log.e(TAG, "Failed to parse JSON", js);
                return null;
            }
        }

        @Override
        protected void onPostExecute(SmitePlayer friendPlayer) {

            if (friendPlayer == null) {
                Toast.makeText(mContext, "Friend cannot be found", Toast.LENGTH_LONG).show();
            }
            else {
                // Load new activity for the player
                Intent intent = new Intent(mContext, HomeActivity.class);
                //intent.putExtra(EXTRA_PLAYER_DATA, friendPlayer);
                //startActivityForResult(intent, REQUEST_CODE_HOME);
            }
        }
    }
}

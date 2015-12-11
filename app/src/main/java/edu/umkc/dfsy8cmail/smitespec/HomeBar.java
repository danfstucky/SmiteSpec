package edu.umkc.dfsy8cmail.smitespec;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

import com.cr5315.jSmite.Smite;

import org.json.JSONException;

/**
 * Created by Dan on 12/10/2015.
 */
public class HomeBar {
    private static final String TAG = "HomeBar";
    private static final String EXTRA_PLAYER_DATA = "edu.umkc.dfsy8cmail.smitespec.PLAYER_DATA";
    private static  final int REQUEST_CODE_SEARCH = 0;
    private Smite smite;
    private Context context;

    public HomeBar(Smite smite, Context context) {
        this.smite = smite;
        this.context = context;
    }

    public void redirectToHome(SmitePlayer currentPlayer) {
        new FetchPlayerTask().execute(currentPlayer.getPlayerId());
    }


    private class FetchPlayerTask extends AsyncTask<String, Void, SmitePlayer> {

        @Override
        protected SmitePlayer doInBackground(String... params) {
            try {
                PlayerRetrieval retriever = new PlayerRetrieval(smite);
                return retriever.fetchPlayer(params[0]);
            } catch (JSONException js) {
                // This will happen if player doesn't exist
                Log.e(TAG, "Failed to parse JSON", js);
                return null;
            }
        }

        @Override
        protected void onPostExecute(SmitePlayer currentPlayer) {

            if (currentPlayer == null) {
                Toast.makeText(context, "No player by that name", Toast.LENGTH_LONG).show();
            }
            else {
                //Toast.makeText(getBaseContext(), "Player found", Toast.LENGTH_SHORT).show();
                // Load new activity for the player
                Intent intent = new Intent(context, HomeActivity.class);
                intent.putExtra(EXTRA_PLAYER_DATA, currentPlayer);
                ((Activity) context).startActivityForResult(intent, REQUEST_CODE_SEARCH);
            }
        }
    }
}

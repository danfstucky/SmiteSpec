package edu.umkc.dfsy8cmail.smitespec;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.cr5315.jSmite.Smite;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PlayerStatsActivity extends AppCompatActivity {

    private static final String TAG = "PlayerStatsActivity";
    Smite smite = new Smite("1517", "4FA5E41C82DC4F718A00A3B074F22658");
    private SmitePlayer currentPlayer;
    private String gameMode;
    private PlayerGodsList player_god_stats_list = new PlayerGodsList(getBaseContext());
    com.cr5315.jSmite.Smite.Queue queue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_stats);

        // Retrieve extra data passed from HomeActivity
        Intent intent = getIntent();
        currentPlayer = intent.getExtras().getParcelable(HomeActivity.EXTRA_PLAYER_DATA);
        gameMode = intent.getStringExtra(HomeActivity.EXTRA_MODE);
        queue = getQueue();

        //Load UI content
        new FetchGodRankTask().execute();

        TextView game_type = (TextView) findViewById(R.id.game_mode);
        game_type.setText(gameMode);

    }

    private void updateStats() {
        player_god_stats_list.setPlayerGodStats();
        TextView overall_wl = (TextView)findViewById(R.id.overall_wl_value);
        overall_wl.setText(player_god_stats_list.getWin_Loss_Ratio());
        TextView overall_kd = (TextView)findViewById(R.id.overall_kd_value);
        overall_kd.setText(player_god_stats_list.getKill_Death_Ratio());
        //fav god
        TextView god_fav = (TextView)findViewById(R.id.fav_god_value);
        god_fav.setText(Integer.toString(player_god_stats_list.getFavGod().getMatches()) + " Matches");
        ImageView god_fav_img = (ImageView) findViewById(R.id.imageView_fav_god);
        god_fav_img.setImageResource(player_god_stats_list.getFavGod().getImageID());
        // most successful god
        TextView god_wl = (TextView)findViewById(R.id.wl_god_value);
        god_wl.setText(String.format("%.1f", player_god_stats_list.getWinsGod().getWLPerc()) + "% Win");
        ImageView god_wl_img = (ImageView) findViewById(R.id.imageView_wl_god);
        god_wl_img.setImageResource(player_god_stats_list.getWinsGod().getImageID());
        // most skilled god
        TextView god_kd = (TextView)findViewById(R.id.kd_god_value);
        god_kd.setText(String.format("%.2f", player_god_stats_list.getKDGod().getKD()) + " K/D");
        ImageView god_kd_img = (ImageView) findViewById(R.id.imageView_kd_god);
        god_kd_img.setImageResource(player_god_stats_list.getKDGod().getImageID());
    }

    private class FetchGodRankTask extends AsyncTask<Void, Void, PlayerGodsList> {

        @Override
        protected PlayerGodsList doInBackground(Void... params) {
            try {
                // Retrieve json data of player from Smite API
                String queueStatsJson = smite.getQueueStats(currentPlayer.getPlayerId(), queue);
                Log.i(TAG, "Fetched god ranks: " + queueStatsJson);
                // Retrieve json array and get json object from array.  Player data only ever has 1 object in array
                JSONArray data = new JSONArray(queueStatsJson);
                for (int i=0; i < data.length(); i++) {
                    JSONObject jsonPlayerGod = data.getJSONObject(i);
                    PlayerGod god = new PlayerGod(getBaseContext());
                    god.parse(jsonPlayerGod);
                    player_god_stats_list.addGodStats(god);
                }
                return player_god_stats_list;
            } catch (JSONException js) {
                Log.e(TAG, "Failed to parse JSON", js);
                return null;
            }
        }

        @Override
        protected void onPostExecute(PlayerGodsList player_god_stats_list) {
            if (player_god_stats_list != null) {
                updateStats();
            }
        }

    }


    public Smite.Queue getQueue() {
        if (gameMode.equals("Conquest")) {
            queue = Smite.Queue.CONQUEST;
        }
        else if (gameMode.equals("Joust")) {
            queue = Smite.Queue.JOUST_3V3;
        }
        else {
            queue = Smite.Queue.ASSAULT;
        }
        return queue;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_player_stats, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

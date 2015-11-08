package edu.umkc.dfsy8cmail.smitespec;

import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cr5315.jSmite.Smite;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class PlayerStatsActivity extends AppCompatActivity {

    private static final String TAG = "PlayerStatsActivity";
    Smite smite = new Smite("1517", "4FA5E41C82DC4F718A00A3B074F22658");
    private SmitePlayer currentPlayer;
    private String gameMode;
    private PlayerGodsList player_god_stats_list = new PlayerGodsList(getBaseContext());
    com.cr5315.jSmite.Smite.Queue queue;
    private RecyclerView mGodRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private GodAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_stats);

        // Retrieve extra data passed from HomeActivity
        Intent intent = getIntent();
        currentPlayer = intent.getExtras().getParcelable(HomeActivity.EXTRA_PLAYER_DATA);
        gameMode = intent.getStringExtra(HomeActivity.EXTRA_MODE);
        queue = getQueue();

        // Initialize recycler view
        mGodRecyclerView = (RecyclerView) findViewById(R.id.god_recycler_view);
        mLayoutManager = new LinearLayoutManager(this);
        mGodRecyclerView.setLayoutManager(mLayoutManager);

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
        try {
            god_fav_img.setImageResource(player_god_stats_list.getFavGod().getImageID());
        } catch (Resources.NotFoundException e) {
            Log.i(TAG, e.getMessage());
            god_fav_img.setImageResource(R.drawable.no_god);
        }

        // most successful god
        TextView god_wl = (TextView)findViewById(R.id.wl_god_value);
        god_wl.setText(String.format("%.1f", player_god_stats_list.getWinsGod().getWLPerc()) + "% Win");
        ImageView god_wl_img = (ImageView) findViewById(R.id.imageView_wl_god);
        try {
            god_wl_img.setImageResource(player_god_stats_list.getWinsGod().getImageID());
        } catch (Resources.NotFoundException e) {
            Log.i(TAG, e.getMessage());
            god_wl_img.setImageResource(R.drawable.no_god);
        }

        // most skilled god
        TextView god_kd = (TextView)findViewById(R.id.kd_god_value);
        god_kd.setText(String.format("%.2f", player_god_stats_list.getKDGod().getKD()) + " K/D");
        ImageView god_kd_img = (ImageView) findViewById(R.id.imageView_kd_god);
        try {
            god_kd_img.setImageResource(player_god_stats_list.getKDGod().getImageID());
        } catch (Resources.NotFoundException e) {
            Log.i(TAG, e.getMessage());
            god_kd_img.setImageResource(R.drawable.no_god);
        }


        // set player god recyclerview
        List<PlayerGod> gods = player_god_stats_list.getGodsStats();
        mAdapter = new GodAdapter(gods);
        mGodRecyclerView.setAdapter(mAdapter);
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

    // RecyclerView functions for player god stats list
    private class GodHolder extends RecyclerView.ViewHolder {
        //public TextView mTitleTextView;
        private PlayerGod mGod;
        private TextView kd_value;
        private TextView wl_value;
        private TextView assists_value;
        private TextView matches_value;
        private ImageView god_stats_img;

        public GodHolder(View view) {
            super(view);
            kd_value = (TextView) view.findViewById(R.id.single_kd_value);
            wl_value = (TextView) view.findViewById(R.id.single_wl_value);
            assists_value = (TextView) view.findViewById(R.id.single_assists_value);
            matches_value = (TextView) view.findViewById(R.id.single_matches_value);
            god_stats_img = (ImageView) view.findViewById(R.id.imageView_stats_god);
        }

        public void bindGod(PlayerGod god) {
            mGod = god;
            //kd_value.setText(god.getKDString());
            kd_value.setText(String.format("%.2f", god.getKD()));
            wl_value.setText(String.format("%.1f", god.getWLPerc()) + "%");
            assists_value.setText(String.valueOf(god.getAssists()));
            matches_value.setText(String.valueOf(god.getMatches()));
            try {
                god_stats_img.setImageResource(god.getImageID());
            } catch (Resources.NotFoundException e) {
                Log.i(TAG, e.getMessage());
                god_stats_img.setImageResource(R.drawable.no_god);
            }
        }
    }

    private class GodAdapter extends RecyclerView.Adapter<GodHolder> {
        private List<PlayerGod> mGods;

        public GodAdapter(List<PlayerGod> gods) {
            mGods = gods;
        }

        @Override
        public GodHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater
                    .inflate(R.layout.god_stats_layout, parent, false);
            return new GodHolder(view);
        }

        @Override
        public void onBindViewHolder(GodHolder holder, int position) {
            PlayerGod god  = mGods.get(position);
            holder.bindGod(god);
        }

        @Override
        public int getItemCount() {
            return mGods.size();
        }
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

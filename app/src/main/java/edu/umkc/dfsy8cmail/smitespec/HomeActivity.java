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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cr5315.jSmite.Smite;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.List;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String EXTRA_PLAYER_DATA = "edu.umkc.dfsy8cmail.smitespec.PLAYER_DATA";
    public static final String EXTRA_MODE = "edu.umkc.dfsy8cmail.smitespec.GAME_MODE";
    public static final String EXTRA_CLAN_NAME = "edu.umkc.dfsy8cmail.smitespec.CLAN_NAME";
    private static final String TAG = "HomeActivity";
    private static  final int REQUEST_CODE_HOME= 1;
    Smite smite = new Smite("1517", "4FA5E41C82DC4F718A00A3B074F22658");  // It may be more efficient to pass this object b/w activities instead of creating new each time
    private RecyclerView mFriendRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private FriendAdapter mAdapter;
    private SmitePlayer currentPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Receive extra data from search intent
        Intent intent = getIntent();
        String caller = this.getCallingActivity().getClassName();
        Log.i(TAG, "Calling Activity: " + caller);
        if (caller.equals("edu.umkc.dfsy8cmail.smitespec.SearchActivity")) {
            currentPlayer = intent.getExtras().getParcelable(SearchActivity.EXTRA_PLAYER_DATA);
        }else {
            currentPlayer = intent.getExtras().getParcelable(HomeActivity.EXTRA_PLAYER_DATA);
        }

        // Populate page with player's data and buttons
        TextView player_name = (TextView) findViewById(R.id.player_name_value);
        player_name.setText(currentPlayer.getName());

        ImageButton bConquest = (ImageButton) findViewById(R.id.button_conquest);
        bConquest.setOnClickListener(this);

        ImageButton bJoust = (ImageButton) findViewById(R.id.button_joust);
        bJoust.setOnClickListener(this);

        ImageButton bAssault = (ImageButton) findViewById(R.id.button_assault);
        bAssault.setOnClickListener(this);

        Button bClan = (Button) findViewById(R.id.button_clan);
        bClan.setOnClickListener(this);

        // Add player friends list
        // Initialize recycler view
        mFriendRecyclerView = (RecyclerView) findViewById(R.id.friend_recycler_view);
        mLayoutManager = new LinearLayoutManager(this);
        mFriendRecyclerView.setLayoutManager(mLayoutManager);
        // Retrieve the player's list of friends and update recycler view layout
        FriendsList friendsList = new FriendsList(currentPlayer.getPlayerId());
        new FetchFriendsTask().execute(friendsList);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        Bundle extras = new Bundle();
        extras.putParcelable(EXTRA_PLAYER_DATA, currentPlayer);
        switch (v.getId()) {
            case R.id.button_conquest:
                Log.i(TAG, "conquest button2: " + R.id.button_conquest);
                intent = new Intent(this, PlayerStatsActivity.class);
                extras.putString(EXTRA_MODE, "Conquest");
                intent.putExtras(extras);
                startActivity(intent);
                finish();
                break;

            case R.id.button_joust:
                intent = new Intent(this, PlayerStatsActivity.class);
                extras.putString(EXTRA_MODE, "Joust");
                intent.putExtras(extras);
                startActivity(intent);
                finish();
                break;

            case R.id.button_assault:
                intent = new Intent(this, PlayerStatsActivity.class);
                extras.putString(EXTRA_MODE, "Assault");
                intent.putExtras(extras);
                startActivity(intent);
                finish();
                break;

            case R.id.button_clan:
                intent = new Intent(this, ClanActivity.class);
                extras.putString(EXTRA_CLAN_NAME, currentPlayer.getTeamId());
                intent.putExtras(extras);
                startActivity(intent);
                finish();
                break;
        }
        Log.i(TAG, "v.getId= " + v.getId());
    }

    private class FetchFriendsTask extends AsyncTask<FriendsList, Void, FriendsList> {

        @Override
        protected FriendsList doInBackground(FriendsList... params) {
            try {
                FriendsList playerFriends = params[0];
                // Retrieve json data of player friends from Smite API
                String friends_List_RawJSON = smite.getFriends(playerFriends.getPlayerId());
                Log.i(TAG, "Fetched friends: " + friends_List_RawJSON);
                // Retrieve json array and populate custom FriendsList object
                JSONArray data = new JSONArray(friends_List_RawJSON);
                for (int i=0; i < data.length(); i++) {
                    JSONObject jsonFriend = data.getJSONObject(i);
                    SmiteFriend friend = new SmiteFriend();
                    friend.parse(jsonFriend);
                    // Retrieve online status of friend
                    String friend_name = friend.getName();
                    if (!friend_name.equals("")) {
                        String friend_status_RawJSON = smite.getPlayerStatus(friend.getName());
                        Log.i(TAG, "Fetched friend status: " + friend_status_RawJSON);
                        JSONArray status_data = new JSONArray(friend_status_RawJSON);
                        JSONObject jsonStatus = status_data.getJSONObject(0);
                        FriendStatus status = new FriendStatus(getBaseContext());
                        status.parseStatus(jsonStatus);
                        friend.setFriendStatus(status);
                        Log.i(TAG, "Status: " + friend.getFriendStatus().getStatus());
                        playerFriends.addFriend(friend);
                    }
                }
                return playerFriends;
            } catch (JSONException js) {
                // This will happen if player has no friends
                Log.e(TAG, "Failed to parse JSON", js);
                return null;
            }
        }

        @Override
        protected void onPostExecute(FriendsList friendsList) {

            if (friendsList != null) {
                updateFriendsUI(friendsList);
            }
        }
    }

    private class FetchPlayerTask extends AsyncTask<String, Void, SmitePlayer>{

        @Override
        protected SmitePlayer doInBackground(String... params) {
            try {
                String query = params[0];
                // Retrieve json data of player from Smite API
                String player = smite.getPlayer(query);
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
                Toast.makeText(getBaseContext(), "Friend cannot be found", Toast.LENGTH_LONG).show();
                // should previous player data be cleared here?
            }
            else {
                // Load new activity for the player
                Intent intent = new Intent(getBaseContext(), HomeActivity.class);
                intent.putExtra(EXTRA_PLAYER_DATA, friendPlayer);
                startActivityForResult(intent, REQUEST_CODE_HOME);
            }
        }
    }

    // RecyclerView functions for friends list
    private class FriendHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private SmiteFriend mFriend;
        private TextView friend_name;
        private TextView online_status;
        private ImageView status_img;

        public FriendHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            friend_name = (TextView) view.findViewById(R.id.player_name);
            online_status = (TextView) view.findViewById(R.id.online_status);
            status_img = (ImageView) view.findViewById(R.id.online_status_img);
        }

        public void bindFriend(SmiteFriend friend) {
            mFriend = friend;
            friend_name.setText(mFriend.getName());
            String status = mFriend.getFriendStatus().getStatus();
            online_status.setText(status);
            try {
                status_img.setImageResource(mFriend.getFriendStatus().getImageID());
            } catch (Resources.NotFoundException e) {
                status_img.setImageResource(R.drawable.status_offline);
            }
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(getBaseContext(), mFriend.getName() + " was clicked!", Toast.LENGTH_LONG).show();
            new FetchPlayerTask().execute(mFriend.getPlayer_id());
        }
    }

    private class FriendAdapter extends RecyclerView.Adapter<FriendHolder> {
        private List<SmiteFriend> mFriends;

        public FriendAdapter(List<SmiteFriend> friends) {
            mFriends = friends;
        }

        @Override
        public FriendHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater
                    .inflate(R.layout.player_item_layout, parent, false);
            return new FriendHolder(view);
        }

        @Override
        public void onBindViewHolder(FriendHolder holder, int position) {
            SmiteFriend friend = mFriends.get(position);
            holder.bindFriend(friend);
        }

        @Override
        public int getItemCount() {
            return mFriends.size();
        }
    }

    private void updateFriendsUI(FriendsList friendsList) {
        List<SmiteFriend> friends = friendsList.getPlayers();

        mAdapter = new FriendAdapter(friends);
        mFriendRecyclerView.setAdapter(mAdapter);
    }

    // Menu Functions
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
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

package edu.umkc.dfsy8cmail.smitespec;

import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;

import com.cr5315.jSmite.Smite;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "HomeActivity";
    Smite smite = new Smite("1517", "4FA5E41C82DC4F718A00A3B074F22658");  // It may be more efficient to pass this object b/w activities instead of creating new each time
    private RecyclerView mFriendRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private FriendAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Receive extra data from search intent
        Intent intent = getIntent();
        SmitePlayer currentPlayer = intent.getExtras().getParcelable(SearchActivity.EXTRA_PLAYER_DATA);

        // Populate page with player's data
        TextView player_name = (TextView) findViewById(R.id.player_name_value);
        player_name.setText(currentPlayer.getName());

        // Add player friends list
        // Initialize recycler view
        mFriendRecyclerView = (RecyclerView) findViewById(R.id.friend_recycler_view);
        mLayoutManager = new LinearLayoutManager(this);
        mFriendRecyclerView.setLayoutManager(mLayoutManager);
        // Retrieve the player's list of friends and update recycler view layout
        CurrentFriendsList friendsList = CurrentFriendsList.get(this.getBaseContext(), currentPlayer.getPlayerId());
        new FetchFriendsTask().execute(friendsList);
    }

    private class FetchFriendsTask extends AsyncTask<CurrentFriendsList, Void, CurrentFriendsList> {

        @Override
        protected CurrentFriendsList doInBackground(CurrentFriendsList... params) {
            try {
                CurrentFriendsList playerFriends = params[0];
                // Retrieve json data of player friends from Smite API
                String friends_List_RawJSON = smite.getFriends(playerFriends.getPlayerId());
                Log.i(TAG, "Fetched friends: " + friends_List_RawJSON);
                // Retrieve json array and populate custom CurrentFriendsList object
                JSONArray data = new JSONArray(friends_List_RawJSON);
                for (int i=0; i < data.length(); i++) {
                    JSONObject jsonFriend = data.getJSONObject(i);
                    SmiteFriend friend = new SmiteFriend();
                    friend.parse(jsonFriend);
                    playerFriends.addFriend(friend);
                }
                return playerFriends;
            } catch (JSONException js) {
                // This will happen if player has no friends
                Log.e(TAG, "Failed to parse JSON", js);
                return null;
            }
        }

        @Override
        protected void onPostExecute(CurrentFriendsList friendsList) {

            if (friendsList == null) {
                Toast.makeText(getBaseContext(), "No friends", Toast.LENGTH_LONG).show();
            }
            else {
                // Update UI with friends list
                updateFriendsUI(friendsList);
                Toast.makeText(getBaseContext(), "You have friends!", Toast.LENGTH_LONG).show();
            }
        }
    }

    // RecyclerView functions for friends list
    private class FriendHolder extends RecyclerView.ViewHolder {
        public TextView mTitleTextView;

        public FriendHolder(View view) {
            super(view);
            mTitleTextView = (TextView) view;  // add .findViewById(R.id.friend_recycler_view) when expanding this to a layout.
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
                    .inflate(android.R.layout.simple_list_item_1, parent, false);
            return new FriendHolder(view);
        }

        @Override
        public void onBindViewHolder(FriendHolder holder, int position) {
            SmiteFriend friend = mFriends.get(position);
            holder.mTitleTextView.setText(friend.getName());
        }

        @Override
        public int getItemCount() {
            return mFriends.size();
        }
    }

    private void updateFriendsUI(CurrentFriendsList friendsList) {
        List<SmiteFriend> friends = friendsList.getFriends();

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

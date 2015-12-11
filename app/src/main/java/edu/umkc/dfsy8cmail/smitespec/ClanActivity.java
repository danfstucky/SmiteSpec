package edu.umkc.dfsy8cmail.smitespec;

import android.content.Context;
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
import android.widget.Toast;

import com.cr5315.jSmite.Smite;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ClanActivity extends AppCompatActivity {

    private static final String TAG = "ClanActivity";
    public static final String EXTRA_PLAYER_DATA = "edu.umkc.dfsy8cmail.smitespec.PLAYER_DATA";
    private static  final int REQUEST_CODE_HOME= 1;
    private Smite smite;

    private RecyclerView mClanMemberRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private MemberAdapter mAdapter;
    private Context mContext = this;
    private SmitePlayer currentPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clan);
        API api = new API();
        smite = new Smite(api.getDevID(), api.getAuthKey());

        Intent intent = getIntent();
        String clan_id = intent.getStringExtra(HomeActivity.EXTRA_CLAN_NAME);
        currentPlayer = intent.getExtras().getParcelable(HomeActivity.EXTRA_PLAYER_DATA);
        Log.i(TAG, "Clan: " + clan_id);
        new FetchClanTask().execute(clan_id);
        // Initialize recycler view
        mClanMemberRecyclerView = (RecyclerView) findViewById(R.id.clan_recycler_view);
        mLayoutManager = new LinearLayoutManager(this);
        mClanMemberRecyclerView.setLayoutManager(mLayoutManager);
        new FetchClanMembersTask().execute(clan_id);
    }

    private class FetchClanTask extends AsyncTask<String, Void, Clan> {

        @Override
        protected Clan doInBackground(String... params) {
            try {
                String clan_id = params[0];
                // Retrieve json data of clan from Smite API
                String clan = smite.getTeamDetails(clan_id);
                Log.i(TAG, "Fetched clan: " + clan);
                // Retrieve json array and get json object from array.  Clan data only ever has 1 object in array
                JSONArray data = new JSONArray(clan);
                JSONObject jsonClan = data.getJSONObject(0);
                // Parse json data into java object
                Clan current_clan = new Clan();
                current_clan.parseClan(jsonClan);
                return current_clan;
            } catch (JSONException js) {
                // This will happen if clan_id doesn't exist
                Log.e(TAG, "Failed to parse JSON", js);
                return null;
            }
        }

        @Override
        protected void onPostExecute(Clan current_clan) {

            if (current_clan == null) {
                Toast.makeText(getBaseContext(), "Clan cannot be found", Toast.LENGTH_LONG).show();
            }
            else {
                // Update UI with clan stats
                updateClanStats(current_clan);
            }
        }

    }

    private class FetchClanMembersTask extends AsyncTask<String, Void, ArrayList> {

        @Override
        protected ArrayList doInBackground(String... params) {
            try {
                String clan_id = params[0];
                // Retrieve json data of clan members from Smite API
                String members_List_RawJSON = smite.getTeamPlayers(clan_id);
                Log.i(TAG, "Fetched clan members: " + members_List_RawJSON);
                // Retrieve json array and populate custom FriendsList object
                JSONArray data = new JSONArray(members_List_RawJSON);
                ArrayList<SmiteClanMember> clanMembers = new ArrayList<>();
                for (int i=0; i < data.length(); i++) {
                    JSONObject jsonPlayer = data.getJSONObject(i);
                    SmiteClanMember member = new SmiteClanMember();
                    member.parse(jsonPlayer);
                    // Retrieve online status of clan member
                    String member_name = member.getName();
                    if (!member_name.equals("")) {
                        String member_status_RawJSON = smite.getPlayerStatus(member.getName());
                        Log.i(TAG, "Fetched clan member status: " +  member_status_RawJSON);
                        JSONArray status_data = new JSONArray(member_status_RawJSON);
                        JSONObject jsonStatus = status_data.getJSONObject(0);
                        FriendStatus status = new FriendStatus(getBaseContext());
                        status.parseStatus(jsonStatus);
                        member.setMemberStatus(status);
                        Log.i(TAG, "Status: " + member.getMemberStatus().getStatus());
                        clanMembers.add(member);
                    }
                }
                return clanMembers;
            } catch (JSONException js) {
                // This will happen if clan has no members found
                Log.e(TAG, "Failed to parse JSON", js);
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList clanMembers) {

            if (clanMembers != null) {
                // update clan member list
                updateClanMembersUI(clanMembers);
            }
        }
    }

    private class FetchPlayerTask extends AsyncTask<String, Void, SmitePlayer>{

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
        protected void onPostExecute(SmitePlayer friendPlayer) {

            if (friendPlayer == null) {
                Toast.makeText(getBaseContext(), "Friend cannot be found", Toast.LENGTH_LONG).show();
            }
            else {
                // Load new activity for the player
                Intent intent = new Intent(getBaseContext(), HomeActivity.class);
                intent.putExtra(EXTRA_PLAYER_DATA, friendPlayer);
                startActivityForResult(intent, REQUEST_CODE_HOME);
            }
        }
    }

    private void updateClanStats(Clan current_clan) {
        // Set values for Clan data:
        // Clan name, tag, wins, losses, founder, # members, rating
        TextView clan_name = (TextView) findViewById(R.id.clan_name);
        clan_name.setText(current_clan.getClanName());
        TextView clan_tag = (TextView) findViewById(R.id.clan_tag);
        clan_tag.setText("[" + current_clan.getTag() + "]");
        TextView wins = (TextView) findViewById(R.id.clan_wins_value);
        wins.setText(String.valueOf(current_clan.getWins()));
        TextView losses = (TextView) findViewById(R.id.clan_loss_value);
        losses.setText(String.valueOf(current_clan.getLosses()));
        TextView founder = (TextView) findViewById(R.id.clan_founder_value);
        founder.setText(current_clan.getFounder());
        TextView size = (TextView) findViewById(R.id.clan_num_members_value);
        size.setText(String.valueOf(current_clan.getNumMembers()));
        TextView rating = (TextView) findViewById(R.id.clan_rating_value);
        rating.setText(String.valueOf(current_clan.getRating()));
    }

    // RecyclerView functions for clan members list
    private class MemberHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private SmiteClanMember mMember;
        private TextView member_name;
        private TextView online_status;
        private ImageView status_img;

        public MemberHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            member_name = (TextView) view.findViewById(R.id.player_name);
            online_status = (TextView) view.findViewById(R.id.online_status);
            status_img = (ImageView) view.findViewById(R.id.online_status_img);
        }

        public void bindMember(SmiteClanMember member) {
            mMember = member;
            member_name.setText(mMember.getName());
            String status = mMember.getMemberStatus().getStatus();
            online_status.setText(status);
            try {
                status_img.setImageResource(mMember.getMemberStatus().getImageID());
            } catch (Resources.NotFoundException e) {
                status_img.setImageResource(R.drawable.status_offline);
            }
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(getBaseContext(), mMember.getName() + " was clicked!", Toast.LENGTH_SHORT).show();
            new FetchPlayerTask().execute(mMember.getName());
        }
    }

    private class MemberAdapter extends RecyclerView.Adapter<MemberHolder> {
        private List<SmiteClanMember> mMembers;

        public MemberAdapter(List<SmiteClanMember> members) {
            mMembers = members;
        }

        @Override
        public MemberHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater
                    .inflate(R.layout.player_item_layout, parent, false);
            return new MemberHolder(view);
        }

        @Override
        public void onBindViewHolder(MemberHolder holder, int position) {
            SmiteClanMember member = mMembers.get(position);
            holder.bindMember(member);
        }

        @Override
        public int getItemCount() {
            return mMembers.size();
        }
    }

    private void updateClanMembersUI(ArrayList<SmiteClanMember> clanList) {
        mAdapter = new MemberAdapter(clanList);
        mClanMemberRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        MenuBar menubar = new MenuBar(smite, menu, mContext);
        menubar.createMenu();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch(id) {
            case R.id.menu_item_home:
                new HomeBar(smite, mContext).redirectToHome(currentPlayer);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

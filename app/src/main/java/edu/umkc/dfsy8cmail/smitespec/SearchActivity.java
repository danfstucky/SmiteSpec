package edu.umkc.dfsy8cmail.smitespec;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.cr5315.jSmite.Smite;

import org.json.*;


public class SearchActivity extends AppCompatActivity {

    private static final String TAG = "SearchActivity";
    private static  final int REQUEST_CODE_SEARCH = 0;
    Smite smite = new Smite("1517", "4FA5E41C82DC4F718A00A3B074F22658");
    //SmitePlayer currentPlayer = new SmitePlayer();
    Context mContext;
    public static final String EXTRA_PLAYER_DATA = "edu.umkc.dfsy8cmail.smitespec.search.PLAYER_DATA";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        mContext = this;

        // Handle search queries from search bar in layout
        // This is done in same way as a search from the action bar
        SearchView search_bar = (SearchView) findViewById(R.id.search_bar);
        search_bar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d(TAG, "QueryTextSubmit: " + query);
                new FetchPlayerTask().execute(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d(TAG, "QueryTextChange: " + newText);
                return false;
            }
        });
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
                SmitePlayer currentPlayer = new SmitePlayer();
                currentPlayer.parsePlayer(jsonPlayer);
                return currentPlayer;
            } catch (JSONException js) {
                // This will happen if player doesn't exist
                Log.e(TAG, "Failed to parse JSON", js);
                return null;
            }
        }

        @Override
        protected void onPostExecute(SmitePlayer currentPlayer) {

            if (currentPlayer == null) {
                Toast.makeText(getBaseContext(), "No player by that name", Toast.LENGTH_LONG).show();
                // should previous player data be cleared here?
            }
            else {
                //Toast.makeText(getBaseContext(), "Player found", Toast.LENGTH_SHORT).show();
                // Load new activity for the player
                Intent intent = new Intent(mContext, HomeActivity.class);
                intent.putExtra(EXTRA_PLAYER_DATA, currentPlayer);
                startActivityForResult(intent, REQUEST_CODE_SEARCH);
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);

        MenuItem searchItem = menu.findItem(R.id.menu_item_search);

        // can the following commands be moved to onOptionsItemsSelected()?
        final SearchView searchView = (SearchView) searchItem.getActionView();

        // Handle search action bar queries
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d(TAG, "QueryTextSubmit: " + query);
                new FetchPlayerTask().execute(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d(TAG, "QueryTextChange: " + newText);
                return false;
            }
        });
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

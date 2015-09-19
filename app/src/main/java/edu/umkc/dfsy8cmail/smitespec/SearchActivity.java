package edu.umkc.dfsy8cmail.smitespec;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.cr5315.jSmite.Smite;

public class SearchActivity extends AppCompatActivity {

    private static final String TAG = "SearchActivity";

    Smite smite = new Smite("1517", "4FA5E41C82DC4F718A00A3B074F22658");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        new FetchPlayerTask().execute();
        //String status = smite.getPlayerStatus("Kaitrono");
       // Log.d(TAG, status);
    }

    private class FetchPlayerTask extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... params) {
            String status = smite.getPlayerStatus("Kaitrono");
            Log.i(TAG, "Fetched player status: " + status);
            return null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
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

package edu.umkc.dfsy8cmail.smitespec;

import android.content.Context;
import android.preference.PreferenceManager;

/**
 * Created by Dan on 9/20/2015.
 * currently not used
 * May use at later date for persistent storage
 */
public class QueryPreferences {
    private static final String PREF_SEARCH_QUERY = "searchQuery";  //query preference key

    // returns query value stored in shared preferences
    public static String getStoredPlayerName(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(PREF_SEARCH_QUERY, null);
    }

    // write query input to default shared preferences for given context
    public static void setStoredPlayerName(Context context, String query) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(PREF_SEARCH_QUERY, query)
                .apply();
    }
}

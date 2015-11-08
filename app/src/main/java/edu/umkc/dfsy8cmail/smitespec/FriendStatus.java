package edu.umkc.dfsy8cmail.smitespec;

import android.content.Context;
import android.content.res.Resources;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Dan on 10/28/2015.
 */
public class FriendStatus {
    private Context context;
    private String mStatus;
    private String mImg;

    public FriendStatus(Context cont) {
        context = cont;
    }

    public void setStatus(int stat) {
        if (stat == 0) {
            mStatus = "Offline";
            mImg = "status_offline";
        }
        else if (stat == 1 || stat == 2 || stat == 4) {
            mStatus = "Main Menu";
            mImg = "status_lobby";
        }
        else if (stat == 3) {
            mStatus = "In Match";
            mImg = "status_online";
        }
        else {
            mStatus = "Unknown";
            mImg = "status_offline";
        }
    }

    public String getStatus() { return mStatus; }

    // Returns the drawable resource identifier of a player's status
    // Throws NotFoundException if fails to find identifier.
    public int getImageID() throws Resources.NotFoundException {
        int resID = context.getResources().getIdentifier(mImg, "drawable", context.getPackageName());
        if (resID != 0) {
            return resID;
        } else {
            Resources.NotFoundException e = new Resources.NotFoundException("failed to find image");
            throw e;
        }
    }

    public void parseStatus(JSONObject jsonStat) throws JSONException {
        setStatus(Integer.valueOf(jsonStat.getString("status")));
    }
}

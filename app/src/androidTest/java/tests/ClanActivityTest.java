package tests;

import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.TextView;
import edu.umkc.dfsy8cmail.smitespec.ClanActivity;
import edu.umkc.dfsy8cmail.smitespec.HomeActivity;
import edu.umkc.dfsy8cmail.smitespec.R;

/**
 * Created by Dan on 11/20/2015.
 */
public class ClanActivityTest extends ActivityInstrumentationTestCase2<ClanActivity> {
    private ClanActivity activity;

    public ClanActivityTest() {
        super(ClanActivity.class);
    }

    // If you want to send key events via your test, you have to turn off
    // the touch mode in the emulator via setActivityInitialTouchMode(false)
    // in your setup() method of the test.
    public void setUp(  )  {
        // setActivityInitialTouchMode(false) must be called before the
        //   activity is created.
        setActivityInitialTouchMode(false);

        // mock the intent to ClanActivity
        Intent addClan = new Intent();
        addClan.setClassName("edu.umkc.dfsy8cmail.smitespec", "edu.umkc.dfsy8cmail.smitespec.ClanActivity");
        // '593232' is id to a test clan.
        // Clan name = 'EliteFour'
        // Clan tag = 'AXEW'
        addClan.putExtra(HomeActivity.EXTRA_CLAN_NAME, "593232");
        setActivityIntent(addClan);
        activity = getActivity();

    }


    public void testClanLabel() throws Exception {
        TextView clanName;
        clanName = (TextView) activity.findViewById(R.id.clan_name);
        assertEquals("Clan name not loaded correctly", "EliteFour", clanName.getText());
    }

    public void testClanTag() throws Exception {
        TextView clanTag;
        clanTag = (TextView) activity.findViewById(R.id.clan_tag);
        assertEquals("Clan tag not loaded correctly", "[AXEW]", clanTag.getText());
    }

}

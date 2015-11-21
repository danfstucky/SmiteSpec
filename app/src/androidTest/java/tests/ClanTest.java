package tests;

import junit.framework.TestCase;

import org.json.JSONException;
import org.json.JSONObject;

import edu.umkc.dfsy8cmail.smitespec.Clan;

/**
 * Created by Dan on 11/16/2015.
 */
public class ClanTest extends TestCase {
    private Clan actual_clan;
    JSONObject clan_info;

    public void setUp() throws JSONException {
        actual_clan = new Clan();
    }

    public void tearDown(  ) {
        // If class under test had any cleanup methods, we would call
        //   them here.
        actual_clan =  null;
    }

    public void testParseClanValid() throws JSONException {
        String jStr = "{\"Founder\":\"PlayerName\",\"FounderId\":\"1234\",\"Losses\":5,\"Name\":\"ClanName\",\"Players\":5,\"Rating\":5,\"Tag\":\"TAG\",\"TeamId\":1234,\"Wins\":5,\"ret_msg\":null}";
        clan_info = new JSONObject(jStr);
        actual_clan.parseClan(clan_info);
        assertEquals("parseClan assigned incorrect ClanName", "ClanName", actual_clan.getClanName());
        assertEquals("parseClan assigned incorrect Wins", 5, actual_clan.getWins());
        assertEquals("parseClan assigned incorrect Losses", 5, actual_clan.getLosses());
        assertEquals("parseClan assigned incorrect Founder", "PlayerName", actual_clan.getFounder());
        assertEquals("parseClan assigned incorrect NumMembers", 5, actual_clan.getNumMembers());
        assertEquals("parseClan assigned incorrect Rating", 5, actual_clan.getRating());
        assertEquals("parseClan assigned incorrect Tag", "TAG", actual_clan.getTag());
    }
}

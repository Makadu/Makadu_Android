package br.com.makadu.makaduevento.DAOParse;

import android.app.Activity;
import android.util.Log;

import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;


/**
 * Created by lucasschwalbeferreira on 11/04/15.
 */
public class AnalitcsDAO  extends Activity {

    public void saveDataAnalitcsWithUser( ParseUser user, String type, String screen, String description, ParseObject event) {
        ParseObject analitics = new ParseObject("Analitics");
        analitics.put("user", user);
        analitics.put("type", type);
        analitics.put("screen", screen);
        analitics.put("description", description);
        analitics.put("event", event);
        analitics.saveEventually();
    }

    public void saveDataAnalitcsWithUser( ParseUser user, String type, String screen, String description, ParseObject event, ParseObject talk) {
        ParseObject analitics = new ParseObject("Analitics");
        analitics.put("user", user);
        analitics.put("type", type);
        analitics.put("screen", screen);
        analitics.put("description", description);
        analitics.put("event", event);
        analitics.put("talk",talk);
        analitics.saveEventually();
    }

    public void saveDataAnalitcsWithUser(ParseUser user, String type, String screen, String description) {
        ParseObject analitics = new ParseObject("Analitics");
        analitics.put("user", user);
        analitics.put("type", type);
        analitics.put("screen", screen);
        analitics.put("description", description);
        analitics.saveEventually();
    }

    public void saveDataAnalitcsWithType(String type, String screen, String description, ParseObject object) {}

    public void saveDataAnalitcsWithUser() {}

}

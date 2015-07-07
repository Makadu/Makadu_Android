package br.com.makadu.makaduevento.UI;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.parse.ParseUser;

/**
 * Created by lucasschwalbeferreira on 11/05/15.
 */
public class DispatchActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(ParseUser.getCurrentUser() != null) {
            Log.d("UserIni","onCreate, got user, " + ParseUser.getCurrentUser().getUsername());
            startActivity(new Intent(this,EventActivity.class));
        } else {
            Log.d("UserIni","onCreate, no user");
            startActivity(new Intent(this,LoginActivity.class));
        }
    }
}

package br.com.makadu.makaduevento;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseCrashReporting;


/**
 * Created by lucasschwalbeferreira on 11/03/15.
 */
public class MakaduApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Initialize Crash Reporting.
        ParseCrashReporting.enable(this);

        // Enable Local Datastore.
        //Parse.enableLocalDatastore(this);

        // Add your initialization code here
        Parse.initialize(this,getString(R.string.app_k1),getString(R.string.app_k2));

        //ParseUser.enableAutomaticUser();
        //ParseACL defaultACL = new ParseACL();
        // Optionally enable public read access.
        //defaultACL.setPublicReadAccess(true);
        //ParseACL.setDefaultACL(defaultACL, true);


    }
}

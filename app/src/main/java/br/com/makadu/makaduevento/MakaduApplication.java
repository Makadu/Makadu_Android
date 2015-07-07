package br.com.makadu.makaduevento;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseClassName;
import com.parse.ParseCrashReporting;
import com.parse.ParseObject;


/**
 * Created by lucasschwalbeferreira on 11/03/15.
 */
public class MakaduApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        ParseCrashReporting.enable(this);

        Parse.initialize(this, getString(R.string.app_key1), getString(R.string.app_key2));

    }
}


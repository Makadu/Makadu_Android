package br.com.makadu.makaduevento;

import android.app.Application;
import android.util.Log;

import com.localytics.android.LocalyticsActivityLifecycleCallbacks;
import com.parse.Parse;
import com.parse.ParseCrashReporting;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.SaveCallback;


/**
 * Created by lucasschwalbeferreira on 11/03/15.
 */
public class MakaduApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        registerActivityLifecycleCallbacks(new LocalyticsActivityLifecycleCallbacks(this));

        ParseCrashReporting.enable(this);

        Parse.initialize(this, getString(R.string.app_key1), getString(R.string.app_key2));

        ParsePush.subscribeInBackground("", new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d("com.parse.push", "successfully subscribed to the broadcast channel.");
                } else {
                    Log.e("com.parse.push", "failed to subscribe for push", e);
                }
            }
        });

    }
}


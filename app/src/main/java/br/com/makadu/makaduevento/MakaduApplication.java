package br.com.makadu.makaduevento;

import android.app.Application;
import android.support.v7.app.ActionBar;
import android.util.Log;

import com.localytics.android.LocalyticsActivityLifecycleCallbacks;


/**
 * Created by lucasschwalbeferreira on 11/03/15.
 */
public class MakaduApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        registerActivityLifecycleCallbacks(new LocalyticsActivityLifecycleCallbacks(this));
    }

}
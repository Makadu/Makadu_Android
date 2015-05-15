package com.parse.starter;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseCrashReporting;
import com.parse.ParseUser;

public class ParseApplication extends Application {

  @Override
  public void onCreate() {
    super.onCreate();

    // Initialize Crash Reporting.
    ParseCrashReporting.enable(this);

    // Enable Local Datastore.
    Parse.enableLocalDatastore(this);

    // Add your initialization code here
      Parse.initialize(this, "ELhVHncQTjUeE1vB6JrezPlK0wvC0rWj2B79Spha", "xyR5MkJGfdRv1K59zwnBAGy6XtTuwnx2YHLf776s");


    ParseUser.enableAutomaticUser();
    ParseACL defaultACL = new ParseACL();
      ParseUser.getCurrentUser().saveInBackground();
    // Optionally enable public read access.
    defaultACL.setPublicReadAccess(true);
    ParseACL.setDefaultACL(defaultACL, true);


  }
}

package com.parse.starter;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.parse.*;

public class ParseStarterProjectActivity extends Activity {
	/** Called when the activity is first created. */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

       try {

           ParseObject testObject = new ParseObject("TestObject");
           testObject.put("foo", "bar");
           testObject.put("2", "2");
           testObject.put("3", "3");
           testObject.saveInBackground();

       }catch (Exception e){
           Log.v("erroteste",e.getMessage() + e.getStackTrace());}

		ParseAnalytics.trackAppOpenedInBackground(getIntent());


	}
}

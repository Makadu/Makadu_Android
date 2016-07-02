package br.com.makadu.makaduevento.Util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by lucasschwalbeferreira on 10/6/15.
 */
public class SessionManager {
    private static String TAG = SessionManager.class.getSimpleName();
    SharedPreferences pref;

    SharedPreferences.Editor editor;
    Context _context;

    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "makadupref";
    private static final String KEY_IS_LOGGEDIN = "isLoggedIn";
    private static final String KEY_IS_USER = "user_id";
    private static final String KEY_IS_USERNAME = "user_name";

    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setLogin(boolean isLoggedIn,String user_id,String username) {
        editor.putBoolean(KEY_IS_LOGGEDIN, isLoggedIn);
        editor.putString(KEY_IS_USER, user_id);
        editor.putString(KEY_IS_USERNAME,username);
        editor.commit();
        Log.d(TAG, "User login session modified!");
    }

    public boolean isLoggedIn(){
        return pref.getBoolean(KEY_IS_LOGGEDIN, false);
    }

    public String returnUserId(){
        return pref.getString(KEY_IS_USER, "");
    }

    public String returnUsername(){
        return pref.getString(KEY_IS_USERNAME,"");
    }

}

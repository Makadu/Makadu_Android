package br.com.makadu.makaduevento.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;


/**
 * Created by lucasschwalbeferreira on 06/06/15.
 */
public class Favorites  {

    private Context ctx;


    public Favorites(Context ctx) {
        this.ctx = ctx;
    }

    public boolean contains(Talk talk) {
        ArrayList<String> ids = new ArrayList<String>();
        ids = findLocally(ctx);
        return ids.contains(talk.getId());
    }

    public void add(Talk talk) {
        add_remove_save(talk.getId(), true);
    }

    public void remove(Talk talk) {
        add_remove_save(talk.getId(), false);
    }

    private void add_remove_save(String favorite,boolean add) {
        final JSONObject json = toJSON(favorite,add);
        new AsyncTask<Void, Void, Exception>() {
            @Override
            protected Exception doInBackground(Void... unused) {
                try {
                    String jsonString = json.toString();
                    SharedPreferences prefs = ctx.getSharedPreferences("favorites.json", Context.MODE_PRIVATE);
                    prefs.edit().putString("json", jsonString).commit();
                } catch (Exception e) {
                    return e;
                }
                return null;
            }

            @Override
            protected void onPostExecute(Exception error) {
                if (error != null) {
                    Toast toast = Toast.makeText(ctx, error.getMessage(), Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        }.execute();
    }

    private JSONObject toJSON(String favorite, boolean add) {
        JSONArray favorites = new JSONArray();
        ArrayList<String> id = new ArrayList<String>();
        id = findLocally(ctx);

        if (add) {
            id.add(favorite);
        } else {
            id.remove(favorite);
        }

        for (String objectId : id) {
            favorites.put(objectId);
        }

        JSONObject json = new JSONObject();
        try {
            json.put("favorites", favorites);
        } catch (JSONException e) {
            // This can't happen.
            throw new RuntimeException(e);
        }
        return json;
    }

    public ArrayList<String> findLocally(Context ctx) {

        SharedPreferences prefs = ctx.getSharedPreferences("favorites.json", Context.MODE_PRIVATE);
        String jsonString = prefs.getString("json", "{}");
        Log.v("pega_json", jsonString);

        JSONObject json = null;
        try {
            json = new JSONObject(jsonString);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONArray favorites = json.optJSONArray("favorites");

        if (favorites == null) {
            favorites = new JSONArray();
        }

        ArrayList<String> id = new ArrayList<String>();

        for (int i = 0; i < favorites.length(); ++i) {
            String objectId = favorites.optString(i);
            id.add(objectId);
        }

        return id;
    }

    private void saveLocally(final Context context, String favorite) {
        final JSONObject json = new JSONObject();

        new AsyncTask<Void, Void, Exception>() {
            @Override
            protected Exception doInBackground(Void... unused) {
                try {
                    String jsonString = json.toString();
                    SharedPreferences prefs = context.getSharedPreferences("favorites.json", Context.MODE_PRIVATE);
                    prefs.edit().putString("json", jsonString).commit();
                } catch (Exception e) {
                    return e;
                }
                return null;
            }

            @Override
            protected void onPostExecute(Exception error) {
                if (error != null) {
                    Toast toast = Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        }.execute();
    }

}

package br.com.makadu.makaduevento.UI;

import br.com.makadu.makaduevento.DAOParse.EventDAO;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;

import com.localytics.android.Localytics;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import br.com.makadu.makaduevento.R;
import br.com.makadu.makaduevento.Util.Util;
import br.com.makadu.makaduevento.adapters.EventAdapter;
import br.com.makadu.makaduevento.model.Event;


public class EventActivity extends ActionBarActivity implements SwipeRefreshLayout.OnRefreshListener {

    ListView list;
    ProgressDialog mProgressDialog;
    Util util;
    private EventAdapter mAdapter;
    EventDAO eventDAO = new EventDAO();
    List<Event> events;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    ProgressBar progress;
    boolean cache = false;

    @Override
    protected void  onResume () {
        super.onResume ();
        Localytics.openSession();
        Localytics.tagScreen("Evento");
        Localytics.upload ();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(br.com.makadu.makaduevento.R.layout.activity_event);

        util = new Util(getBaseContext());

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.icon_white);

        progress = (ProgressBar)findViewById(R.id.progressBar_event);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeLoading_Event);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(R.color.Verde_Makadu);

        events = new ArrayList<Event>();

        list = (ListView) findViewById(R.id.listview_view_evento);

        try {
            if (returnFirstAccess()) {
                loadData(true);
            } else {
                loadData(false);
            }
        }catch (Exception e) {
            loadData(false);
        }

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(view.getContext(), Tab_EventDetail_Talk.class);
                final Event ev = mAdapter.getItem(position);
                intent.putExtra("obj_event", ev);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(br.com.makadu.makaduevento.R.menu.menu_principal, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(mQueryTextListener);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.action_logout){
            ParseUser.logOut();
            startActivity(new Intent(this,DispatchActivity.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    private SearchView.OnQueryTextListener mQueryTextListener = new SearchView.OnQueryTextListener() {

        @Override
        public boolean onQueryTextSubmit(String query) {

            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {

            List<Event> filtered;

            List<Event> list_event = new ArrayList<Event>();

            list_event = eventDAO.returnAllEvents(false,Boolean.TRUE);

            filtered = filter(newText, list_event);
            mAdapter.setData(filtered);
            mAdapter.notifyDataSetChanged();

            return false;

        }

    };

    public List<Event> filter(String text, List<Event> target) {
        List<Event> result = new ArrayList<Event>();
        for (Event element: target) {
            if (element.getName().toLowerCase(Locale.getDefault()).contains(text.toLowerCase(Locale.getDefault()))) {
                result.add(element);
            }
        }
        return result;
    }

    @Override
    public void onRefresh() {
        loadData(false);
    }

    public void loadData(final boolean cache) {
        this.cache = cache;

        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                progress.setVisibility(View.VISIBLE);
            }

            @Override
            protected Void doInBackground(Void... arg) {
                events = (ArrayList<Event>) eventDAO.returnAllEvents(util.isConnected(),cache);
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                progress.setVisibility(View.INVISIBLE);
                mAdapter = new EventAdapter(getBaseContext(), R.layout.row_event,events);
                list.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();

                if(!returnFirstAccess()) {
                    SharedPreferences prefs = getSharedPreferences("MyPreferences",Context.MODE_PRIVATE);
                    prefs.edit().putBoolean("init_app", true).commit();
                    Log.v("first_acess_log","primeiro acesso");
                }

                if(mSwipeRefreshLayout.isRefreshing()) {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }
        };
        task.execute((Void[]) null);
    }

    private boolean returnFirstAccess() {
        SharedPreferences prefs = getSharedPreferences("MyPreferences",Context.MODE_PRIVATE);
        boolean init = prefs.getBoolean("init_app",false);

        return init;
    }

}

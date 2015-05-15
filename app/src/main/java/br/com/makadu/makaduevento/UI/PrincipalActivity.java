package br.com.makadu.makaduevento.UI;

import br.com.makadu.makaduevento.DAOParse.EventoDAO;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import br.com.makadu.makaduevento.R;
import br.com.makadu.makaduevento.adapter.EventAdapter;
import br.com.makadu.makaduevento.model.Event;


public class PrincipalActivity extends ActionBarActivity implements SwipeRefreshLayout.OnRefreshListener {

    ProgressDialog mProgressDialog;

    private EventAdapter mAdapter;
    EventoDAO eventDAO = new EventoDAO();
    private List<Event> events;
    private SwipeRefreshLayout mSwipeRefreshLayout;
   /* RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(br.com.makadu.makaduevento.R.layout.activity_principal);


        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.icon_white);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeLoading);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(R.color.Verde_Makadu);

       /* mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_evento);
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);*/

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();

        events = new ArrayList<Event>();

        //mAdapter = new EventAdapter(this, R.layout.row_evento,eventDAO.returnAllEvents(ni) ); todo 1
        mAdapter = new EventAdapter(this, R.layout.row_evento,events);

        ListView list = (ListView) findViewById(R.id.listview_view_evento);
        list.setAdapter(mAdapter);

        onRefresh();

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(view.getContext() , Tab_DetalheEvento_Programacao.class);
                Event ev = mAdapter.getItem(position);
                intent.putExtra("obj_event", ev);
                startActivity(intent);
            }
        });

       /* mAdapter = new EventoAdapter(eventDAO.returnAllEvents());
        mRecyclerView.setAdapter(mAdapter);*/
    }

    private void refreshEventList() {

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Events");
        query.whereEqualTo("active",true);
        query.orderByAscending("start_date");

        query.setCachePolicy(ParseQuery.CachePolicy.CACHE_THEN_NETWORK);


        query.findInBackground(new FindCallback<ParseObject>() {

            @Override
            public void done(List<ParseObject> eventList, ParseException e) {
                if(e == null) {
                    events.clear();
                    for(ParseObject i_evento : eventList) {

                        Event event = new Event();
                        event.setId(i_evento.getObjectId());
                        event.setName((String) i_evento.get("event_name"));
                        event.setDescription((String) i_evento.get("event_description"));
                        event.setLocal((String) i_evento.get("local"));
                        event.setAddress((String) i_evento.get("address"));
                        event.setCity((String) i_evento.get("city"));
                        event.setState((String) i_evento.get("state"));

                        Date data_ini = (Date)i_evento.get("start_date");
                        Date data_fim = (Date)i_evento.get("end_date");

                        SimpleDateFormat out = new SimpleDateFormat("dd/MM/yyyy");

                        event.setStart_date(out.format(data_ini));
                        event.setEnd_date(out.format(data_fim));

                        ParseFile fileObjectlogo = (ParseFile) i_evento.get("logo");
                        try {
                            event.setFile_img_event(fileObjectlogo.getData());

                            ParseFile fileObjectpatrocinador = (ParseFile) i_evento.get("patronage");
                            event.setFile_img_patronage(fileObjectpatrocinador.getData());

                        } catch (ParseException e1) {
                            e1.printStackTrace();
                        }

                        events.add(event);
                        mAdapter.notifyDataSetChanged();
                    }

                }
                if(mSwipeRefreshLayout.isRefreshing()) {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
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

            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo ni = cm.getActiveNetworkInfo();
            List<Event> filtered = filter(newText, eventDAO.returnAllEvents(ni));
            mAdapter.setData(filtered);
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
        refreshEventList();

    }
}

package br.com.makadu.makaduevento.UI;

import br.com.makadu.makaduevento.DAOParse.AnalitcsDAO;
import br.com.makadu.makaduevento.DAOParse.EventDAO;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

import com.parse.DeleteCallback;
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
import br.com.makadu.makaduevento.Util.Util;
import br.com.makadu.makaduevento.adapters.EventAdapter;
import br.com.makadu.makaduevento.adapters.NoticeAdapter;
import br.com.makadu.makaduevento.model.Event;
import br.com.makadu.makaduevento.model.Notice;


public class EventActivity extends ActionBarActivity implements SwipeRefreshLayout.OnRefreshListener {

    ListView list;
    ProgressDialog mProgressDialog;
    Util util;
    private EventAdapter mAdapter;
    AnalitcsDAO analitics;
    EventDAO eventDAO = new EventDAO();
    List<Event> events;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    ProgressBar progress;
   /* RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(br.com.makadu.makaduevento.R.layout.activity_event);

        util = new Util(getBaseContext());
        analitics = new AnalitcsDAO();

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.icon_white);

        progress = (ProgressBar)findViewById(R.id.progressBar_event);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeLoading);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(R.color.Verde_Makadu);

       /* mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_evento);
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);*/

        events = new ArrayList<Event>();

        //mAdapter = new EventAdapter(this, R.layout.row_event,eventDAO.returnAllEvents(ni) ); todo 1

        list = (ListView) findViewById(R.id.listview_view_evento);

        onRefresh();

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(view.getContext() , Tab_EventDetail_Talk.class);
                final Event ev = mAdapter.getItem(position);
                intent.putExtra("obj_event", ev);
                startActivity(intent);

                new Thread(){
                    public void run(){
                        try {
                            if(util.isConnected()) {
                                ParseObject eventObject = eventDAO.returnEvent(ev.getId_Parse(),util.isConnected());
                                if (eventObject != null) {
                                    analitics.saveDataAnalitcsWithUser(ParseUser.getCurrentUser(), "Clicou", "Lista de Eventos", "O usuário acessou um evento", eventObject);
                                }
                            }
                        }catch (Exception e) {
                            Log.v("erro_parse_analitics",e.getMessage());
                        }
                    }
                }.start();
            }
        });

       /* mAdapter = new EventoAdapter(eventDAO.returnAllEvents());
        mRecyclerView.setAdapter(mAdapter);*/
    }

    private void refreshEventList() {

        /*ParseQuery<ParseObject> query = ParseQuery.getQuery("Events");
        query.whereEqualTo("active", true);
        query.orderByAscending("start_date");

        query.setCachePolicy(ParseQuery.CachePolicy.CACHE_THEN_NETWORK);

        query.findInBackground(new FindCallback<ParseObject>() {

            @Override
            public void done(final List<ParseObject> eventList, ParseException e) {
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
                            Log.v("Erro_Event", e1.getMessage());
                        }

                        event.save();

                        events.add(event);
                        mAdapter.notifyDataSetChanged();
                    }

                }
                if(mSwipeRefreshLayout.isRefreshing()) {
                    mSwipeRefreshLayout.setRefreshing(false);
                }

            }
        });*/

        loadData();

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

            if(util.isConnected()) {
                analitics.saveDataAnalitcsWithUser(ParseUser.getCurrentUser(), "Realizou", "Lista de Eventos", "O usuário efetuou o logoff.");
            }
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
            //eventDAO.returnAllEvents(ni);

            list_event = eventDAO.returnAllEvents(false,true);

            filtered = filter(newText, list_event);
            mAdapter.setData(filtered);
            mAdapter.notifyDataSetChanged();
            //filtered = filter(newText, events);

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
        //Event.deleteAll(Event.class);
        refreshEventList();
    }

    public void loadData() {

        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                progress.setVisibility(View.VISIBLE);

            }

            @Override
            protected Void doInBackground(Void... arg0) {

                events = (ArrayList<Event>) eventDAO.returnAllEvents(util.isConnected(),false);
                //events = (ArrayList<Event>) eventDAO.retornAllEvents_inBack();

               /* ParseQuery<ParseObject> query = ParseQuery.getQuery("Events");
                query.whereEqualTo("active", true);
                query.orderByAscending("start_date");

                query.setCachePolicy(ParseQuery.CachePolicy.CACHE_THEN_NETWORK);

                query.findInBackground(new FindCallback<ParseObject>() {

                    @Override
                    public void done(final List<ParseObject> eventList, ParseException e) {
                        if (e == null) {
                            events.clear();
                            for (ParseObject i_evento : eventList) {

                                Event event = new Event();
                                event.setId(i_evento.getObjectId());
                                event.setName((String) i_evento.get("event_name"));
                                event.setDescription((String) i_evento.get("event_description"));
                                event.setLocal((String) i_evento.get("local"));
                                event.setAddress((String) i_evento.get("address"));
                                event.setCity((String) i_evento.get("city"));
                                event.setState((String) i_evento.get("state"));

                                Date data_ini = (Date) i_evento.get("start_date");
                                Date data_fim = (Date) i_evento.get("end_date");

                                SimpleDateFormat out = new SimpleDateFormat("dd/MM/yyyy");

                                event.setStart_date(out.format(data_ini));
                                event.setEnd_date(out.format(data_fim));

                                ParseFile fileObjectlogo = (ParseFile) i_evento.get("logo");
                                try {
                                    event.setFile_img_event(fileObjectlogo.getData());

                                    ParseFile fileObjectpatrocinador = (ParseFile) i_evento.get("patronage");
                                    event.setFile_img_patronage(fileObjectpatrocinador.getData());

                                } catch (ParseException e1) {
                                    Log.v("Erro_Event", e1.getMessage());
                                }

                                event.save();

                                events.add(event);
                                //mAdapter.notifyDataSetChanged();
                            }

                            progress.setVisibility(View.INVISIBLE);

                        }
                        //if(mSwipeRefreshLayout.isRefreshing()) {
                        //    mSwipeRefreshLayout.setRefreshing(false);
                        // }

                    }
                });*/

                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                progress.setVisibility(View.INVISIBLE);
                mAdapter = new EventAdapter(getBaseContext(), R.layout.row_event,events);
                //noticesAdapter = new NoticeAdapter(getBaseContext(),list_notices );
                //mListView.setAdapter(noticesAdapter);
                list.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();

                if(mSwipeRefreshLayout.isRefreshing()) {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }

        };
        task.execute((Void[]) null);
    }
}

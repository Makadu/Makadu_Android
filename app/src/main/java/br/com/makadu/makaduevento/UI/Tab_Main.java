package br.com.makadu.makaduevento.UI;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.localytics.android.Localytics;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import br.com.makadu.makaduevento.DAO.dao.entityDao.EventDao;
import br.com.makadu.makaduevento.R;
import br.com.makadu.makaduevento.Util.SessionManager;
import br.com.makadu.makaduevento.Util.Util;
import br.com.makadu.makaduevento.adapters.EventAdapter;
import br.com.makadu.makaduevento.model.Event;
import br.com.makadu.makaduevento.push.RegistrationIntentService;
import br.com.makadu.makaduevento.servicesRetrofit.returnAPI.GetRestAdapter;


public class Tab_Main extends ActionBarmakadu implements ActionBar.TabListener  {

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private CharSequence mTitle;
    SectionsPagerAdapter mSectionsPagerAdapter;

    static EventAdapter mAdapterTodosEventos = null;

    ViewPager mViewPager;
    private BroadcastReceiver registrationBroadcastReceiver;

    //variable meus eventos
    static View rootViewMyEvents;
    static ProgressBar progressMyEvents;
    static SwipeRefreshLayout mSwipeRefreshLayoutMyEvents;
    static List<Event> eventsMyEvents;
    static ListView listMyEvents;
    static boolean cacheMyEvents = false;
    static SessionManager sessionMyEvents;
    static EventAdapter mAdapterMyEvents;

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_event_main);

        mTitle = getTitle();

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);


        //getSupportActionBar().setIcon(R.drawable.icon_white);

        //actionBar.setIcon(R.drawable.makadu_bar_g);


        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.pagermain);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            //.setIcon(mSectionsPagerAdapter.setIcone(i))
                            .setTabListener(Tab_Main.this));
        }


        registrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                SharedPreferences sharedPreferences =
                        PreferenceManager.getDefaultSharedPreferences(context);
                boolean sentToken = sharedPreferences
                        .getBoolean(RegistrationIntentService.SENT_TOKEN_TO_SERVER, false);

                if(sentToken){
                }
                else{

                }
            }
        };

        registerReceiver();

        if(checkPlayServices()){
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i("MAKADU", "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    private void registerReceiver(){
        LocalBroadcastManager.getInstance(this).registerReceiver(registrationBroadcastReceiver,
                new IntentFilter(RegistrationIntentService.REGISTRATION_COMPLETE));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu_context) {
        getMenuInflater().inflate(R.menu.menu_principal, menu_context);

        MenuItem searchItem = menu_context.findItem(R.id.action_search_main);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(mQueryTextListener);

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {

                ArrayList<Event> list_event = new ArrayList<Event>();

                list_event = (ArrayList<Event>) new EventDao(getApplication().getApplicationContext()).getAll();

                mAdapterTodosEventos.setData(list_event);
                mAdapterTodosEventos.notifyDataSetChanged();

                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        SessionManager sess = new SessionManager(this);

        int id = item.getItemId();

        if(id == R.id.action_logout){
            sess.setLogin(false,"nao logado","nao logado");
            startActivity(new Intent(this,CreateAccountActivity.class));
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private SearchView.OnQueryTextListener mQueryTextListener = new SearchView.OnQueryTextListener() {

        @Override
        public boolean onQueryTextSubmit(String newText) {

            List<Event> filtered;
            List<Event> list_event = new ArrayList<Event>();
            list_event = new EventDao(getApplication().getApplicationContext()).getAll();
            filtered = filter(newText, list_event);

            mAdapterTodosEventos.setData(filtered);
            mAdapterTodosEventos.notifyDataSetChanged();

            return false;
        }

        public List<Event> filter(String text, List<Event> target) {
            List<Event> result = new ArrayList<Event>();
            for (Event element: target) {
                if (element.getTitle().toLowerCase(Locale.getDefault()).contains(text.toLowerCase(Locale.getDefault()))) {
                    result.add(element);
                }
            }
            return result;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            List<Event> filtered;
            List<Event> list_event = new ArrayList<Event>();
            list_event = new EventDao(getApplication().getApplicationContext()).getAll();
            filtered = filter(newText, list_event);

            mAdapterTodosEventos.setData(filtered);
            mAdapterTodosEventos.notifyDataSetChanged();
            return false;
        }

    };

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        //actionBar.setDisplayShowTitleEnabled(true);
        //actionBar.setTitle(mTitle);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        mViewPager.setCurrentItem(tab.getPosition());

        if(tab.getPosition() == 1) {
            try {
                loadDataMeusEventos(false, getApplication().getApplicationContext());
            }catch (Exception e) {
                Toast.makeText(getApplication().getApplicationContext(),"lgum problema com a conexão",Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) { }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    return Fragment_Todos_Evento.newInstance(position + 1);
                case 1:
                    return Fragment_Meus_Eventos.newInstance(position + 1);

            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_todos).toUpperCase(l);
                case 1:
                    return getString(R.string.title_meusEventos).toUpperCase(l);

            }
            return null;
        }

    }

    public static class Fragment_Todos_Evento extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

        private static final String ARG_SECTION_NUMBER = "section_number";
        View rootView;
        ProgressBar progress;
        private SwipeRefreshLayout mSwipeRefreshLayout;
        List<Event> events;
        ListView list;
        boolean cache = false;
        private SessionManager session;


        public static Fragment_Todos_Evento newInstance(int sectionNumber) {
            Fragment_Todos_Evento fragment = new Fragment_Todos_Evento();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public Fragment_Todos_Evento() { }

        @Override
        public void  onResume() {
            super.onResume();
            session = new SessionManager(getActivity().getApplicationContext());
            Localytics.openSession();
            Localytics.tagScreen("Evento");
            Localytics.upload();
        }

        @Override
        public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
            rootView = inflater.inflate(R.layout.fragment_tab_event, container, false);
            progress = (ProgressBar)rootView.findViewById(R.id.progressBar_event);

            mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeLoading_Event);
            mSwipeRefreshLayout.setOnRefreshListener(this);
            mSwipeRefreshLayout.setColorSchemeColors(R.color.Verde_Makadu);

            events = new ArrayList<>();

            list = (ListView) rootView.findViewById(R.id.listview_view_evento);

            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    Intent intent = new Intent(view.getContext(), Tab_EventDetail_Talk_Paper.class);
                    final Event ev = mAdapterTodosEventos.getItem(position);
                    intent.putExtra("obj_event", ev);
                    startActivity(intent);
                }
            });

            try {
                if (returnFirstAccess()) {
                    loadData(true);
                } else {
                    loadData(false);
                }
            }catch (Exception e) {
                loadData(false);
            }

            return rootView;
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

                    try {
                        EventDao eDAO = new EventDao(getActivity().getApplicationContext());

                        if(!cache) {

                            Context context =  getActivity().getApplicationContext();

                            if(new Util(context).isConnected()) {
                                events = new GetRestAdapter().returnAllEvents();

                                try {
                                    Log.e("LOG_EVENT", "ANTES UPSERT");
                                    eDAO.upsert(events);
                                } catch (Exception ex) {
                                    Log.e("erro", ex.getMessage());
                                }
                            } else {
                                events = eDAO.getAll();
                            }
                        }
                        else  {
                            events = eDAO.getAll();
                        }

                    } catch (Exception e) {
                        Log.e("log_chamada",e.getMessage());
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Void result) {
                    progress.setVisibility(View.INVISIBLE);
                    mAdapterTodosEventos = new EventAdapter(getActivity().getApplicationContext(), R.layout.row_event, events);
                    list.setAdapter(mAdapterTodosEventos);
                    mAdapterTodosEventos.notifyDataSetChanged();

                    if(!returnFirstAccess()) {
                        SharedPreferences prefs = getActivity().getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
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
            SharedPreferences prefs = rootView.getContext().getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
            boolean init = prefs.getBoolean("init_app",false);

            return init;
        }
    }

    public static class Fragment_Meus_Eventos extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
        private static final String ARG_SECTION_NUMBER = "section_number";

        public static Fragment_Meus_Eventos newInstance(int sectionNumber) {
            Fragment_Meus_Eventos fragment = new Fragment_Meus_Eventos();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public Fragment_Meus_Eventos() { }

        @Override
        public void  onResume() {
            super.onResume();
            sessionMyEvents = new SessionManager(getActivity().getApplicationContext());
            Localytics.openSession();
            Localytics.tagScreen("Evento");
            Localytics.upload();
        }

        @Override
        public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
            rootViewMyEvents = inflater.inflate(R.layout.fragment_tab_event, container, false);
            progressMyEvents = (ProgressBar)rootViewMyEvents.findViewById(R.id.progressBar_event);

            mSwipeRefreshLayoutMyEvents = (SwipeRefreshLayout) rootViewMyEvents.findViewById(R.id.swipeLoading_Event);
            mSwipeRefreshLayoutMyEvents.setOnRefreshListener(this);
            mSwipeRefreshLayoutMyEvents.setColorSchemeColors(R.color.Verde_Makadu);

            eventsMyEvents = new ArrayList<Event>();

            listMyEvents = (ListView) rootViewMyEvents.findViewById(R.id.listview_view_evento);

            listMyEvents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    Intent intent = new Intent(view.getContext(), Tab_EventDetail_Talk_Paper.class);
                    final Event ev = mAdapterMyEvents.getItem(position);
                    intent.putExtra("obj_event", ev);
                    startActivity(intent);
                }
            });

            try {
                if (isFirstAcessMyEvents()) {
                    loadDataMeusEventos(false,getActivity().getApplicationContext());
                } else {
                    loadDataMeusEventos(true,getActivity().getApplicationContext());
                }
            }catch (Exception e) {
                loadDataMeusEventos(false,getActivity().getApplicationContext());
            }

            return rootViewMyEvents;
        }

        @Override
        public void onRefresh() {
            loadDataMeusEventos(false,getActivity().getApplicationContext());
        }

    }

    static boolean isFirstAcessMyEvents() {
        SharedPreferences prefs = rootViewMyEvents.getContext().getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        boolean init = prefs.getBoolean("init_app",false);

        return init;
    }

    static void loadDataMeusEventos(final boolean cache, final Context ctx) {
        cacheMyEvents = cache;

        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                progressMyEvents.setVisibility(View.VISIBLE);
            }

            @Override
            protected Void doInBackground(Void... arg) {

                try {

                    EventDao eDAO = new EventDao(ctx);

                    if(!cache) {
                        if(new Util(ctx).isConnected()) {
                            //onde está user id

                            eventsMyEvents = new GetRestAdapter().returnEventFavorite(sessionMyEvents.returnUserId());

                            try {
                                Log.e("LOG_EVENT", "ANTES UPSERT");
                                //  eDAO.upsert(events);
                            } catch (Exception ex) {
                                Log.e("erro", ex.getMessage());
                            }
                        } else {
                            //events = eDAO.getAll();
                        }
                    }
                    else  {
                        //events = eDAO.getAll();
                    }

                } catch (Exception e) {
                    Log.e("log_chamada",e.getMessage());
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                progressMyEvents.setVisibility(View.INVISIBLE);
                mAdapterMyEvents = new EventAdapter(ctx, R.layout.row_event,eventsMyEvents);
                listMyEvents.setAdapter(mAdapterMyEvents);
                mAdapterMyEvents.notifyDataSetChanged();

                if(!isFirstAcessMyEvents()) {
                    SharedPreferences prefs = ctx.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
                    prefs.edit().putBoolean("init_app", true).commit();
                    Log.v("first_acess_log","primeiro acesso");
                }

                if(mSwipeRefreshLayoutMyEvents.isRefreshing()) {
                    mSwipeRefreshLayoutMyEvents.setRefreshing(false);
                }
            }
        };
        task.execute((Void[]) null);
    }

}

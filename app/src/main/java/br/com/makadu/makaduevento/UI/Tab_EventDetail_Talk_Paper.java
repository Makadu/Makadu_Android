package br.com.makadu.makaduevento.UI;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.localytics.android.Localytics;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import br.com.makadu.makaduevento.DAO.dao.entityDao.PaperDao;
import br.com.makadu.makaduevento.DAO.dao.entityDao.TalkDao;
import br.com.makadu.makaduevento.R;
import br.com.makadu.makaduevento.Util.PicassoBigCache;
import br.com.makadu.makaduevento.Util.SessionManager;
import br.com.makadu.makaduevento.Util.Util;
import br.com.makadu.makaduevento.adapters.PaperExpandableAdapter;
import br.com.makadu.makaduevento.adapters.TalkExpandableAdapter;
import br.com.makadu.makaduevento.model.Event;
import br.com.makadu.makaduevento.model.Favorites;
import br.com.makadu.makaduevento.model.Paper;
import br.com.makadu.makaduevento.model.ResponseJson;
import br.com.makadu.makaduevento.model.Resposta;
import br.com.makadu.makaduevento.model.Talk;
import br.com.makadu.makaduevento.servicesRetrofit.returnAPI.GetRestAdapter;
import br.com.makadu.makaduevento.servicesRetrofit.returnAPI.PostRestAdapter;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;


public class Tab_EventDetail_Talk_Paper extends AppCompatActivity implements ActionBar.TabListener  {

    private static String inputedPassword;
    private CharSequence mTitle;

    static Util util;
    static TalkExpandableAdapter adapterTalk = null;
    static PaperExpandableAdapter adapterPaper = null;
    SectionsPagerAdapter mSectionsPagerAdapter;
    ViewPager mViewPager;
    Context ctx;
    private boolean isPaper = false;

    private static SessionManager session;
    static Event obj_event;

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_event_detail);

        session = new SessionManager(getApplication().getApplicationContext());
        ctx = this;
        util = new Util(getBaseContext());
        mTitle = getTitle();
        obj_event=(Event)getIntent().getSerializableExtra("obj_event");

        isPaper = obj_event.have_papers;

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setIcon(R.drawable.icon_white);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.pager);
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
                            .setTabListener(Tab_EventDetail_Talk_Paper.this));
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu_context) {
        getMenuInflater().inflate(R.menu.menu_tab_detalhe_evento_evento, menu_context);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id == android.R.id.home) {
            finish();
            return true;
        }
        if (id == R.id.action_bell) {
            Intent it = new Intent(Tab_EventDetail_Talk_Paper.this,NoticeActivity.class);
            it.putExtra("id",obj_event.getId());
            startActivity(it);
        }
        if (id == R.id.action_favorite) {
            Favorites fa = new Favorites(ctx);
            fa.findLocally(getBaseContext());
            Intent it = new Intent(Tab_EventDetail_Talk_Paper.this,FavoriteActivity.class);
            it.putExtra("obj_evento",obj_event);
            startActivity(it);
        }

        return super.onOptionsItemSelected(item);
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

        mViewPager.setCurrentItem(tab.getPosition());
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

            if(isPaper) {
                switch (position) {
                    case 0:
                        return Fragment_Delalhe_Evento.newInstance(position + 1);
                    case 1:
                        return Fragment_Programacao.newInstance(position + 1);
                    case 2:
                        return Fragment_Paper.newInstance(position + 1);

                }
            } else {
                switch (position) {
                    case 0:
                        return Fragment_Delalhe_Evento.newInstance(position + 1);
                    case 1:
                        return Fragment_Programacao.newInstance(position + 1);
                }
            }
            return null;
        }

        @Override
        public int getCount() {
            return isPaper ? 3 : 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section2).toUpperCase(l);
                case 2:
                    return getString(R.string.title_section3).toUpperCase(l);

            }
            return null;
        }

    }

    public static class Fragment_Delalhe_Evento extends Fragment {

        private static final String ARG_SECTION_NUMBER = "section_number";

        public static Fragment_Delalhe_Evento newInstance(int sectionNumber) {
            Fragment_Delalhe_Evento fragment = new Fragment_Delalhe_Evento();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public Fragment_Delalhe_Evento() { }

        @Override
        public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
            final View rootView = inflater.inflate(R.layout.fragment_tab_event_detail, container, false);

            Localytics.openSession();
            Localytics.tagScreen("Detalhe Evento");
            Localytics.tagEvent(obj_event.getTitle());
            Localytics.upload();

            TextView txt_nome_evento = (TextView)rootView.findViewById(R.id.txt_detalhe_evento_nome);
            txt_nome_evento.setText(obj_event.getTitle());

            TextView txt_local = (TextView)rootView.findViewById(R.id.txtLocal);
            txt_local.setText(obj_event.getVenue());

            TextView txt_endereco = (TextView)rootView.findViewById(R.id.txtEndereço);
            txt_endereco.setText(obj_event.getAddress());

            TextView txt_cidade = (TextView)rootView.findViewById(R.id.txtCidade);
            txt_cidade.setText(obj_event.getCity());

            TextView txt_UF = (TextView)rootView.findViewById(R.id.txtUF);
            txt_UF.setText(obj_event.getState());

            TextView txt_data_ini = (TextView)rootView.findViewById(R.id.txtDataInicio);
            txt_data_ini.setText(new Util().convertStringtoStringDateBrazil(obj_event.getStart_date()));

            TextView txt_data_fim = (TextView)rootView.findViewById(R.id.txtDataFim);
            txt_data_fim.setText(new Util().convertStringtoStringDateBrazil(obj_event.getEnd_date()));

            TextView txt_descricao = (TextView)rootView.findViewById(R.id.txt_Textodescricao);
            txt_descricao.setText(obj_event.getDescription());

            ImageView img_Logo = (ImageView)rootView.findViewById(R.id.img_logo_detalhe);

            final Button btn_add_remove_favorite = (Button)rootView.findViewById(R.id.btn_favoritar_desfavoritar);

            final Button btn_evaluation = (Button)rootView.findViewById(R.id.btn_evaluation);

            btn_evaluation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getActivity().getApplicationContext(),EvaluationActivity.class);
                    i.putExtra("event_id", obj_event.getId());
                    startActivity(i);
                }
            });

            SharedPreferences sp = rootView.getContext().getSharedPreferences("MyPreferencesFavoriteEvents", Context.MODE_PRIVATE);
            Set<String> favorites = sp.getStringSet("favorite_events", new HashSet<String>());

            if(util.isConnected()) {

                boolean isFavorite = false;
                Call<Resposta> responseCall = null;
                try {
                    responseCall = new GetRestAdapter().isFavoriteEvent(session.returnUserId(), obj_event.getId());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                responseCall.enqueue(new Callback<Resposta>() {

                    @Override
                    public void onResponse(Response<Resposta> response, Retrofit retrofit) {
                        Log.v("LOG Email", response.message());
                        if(response.body().resposta == true) {
                            btn_add_remove_favorite.setText(getString(R.string.remove_my_events));
                        }
                        else
                        {
                            btn_add_remove_favorite.setText(getString(R.string.add_my_events));
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {

                    }
                });
            }
            else {
                if(favorites.contains(obj_event.getId())){
                    btn_add_remove_favorite.setText(getString(R.string.remove_my_events));
                }
                else{
                    btn_add_remove_favorite.setText(getString(R.string.add_my_events));
                }
            }

            PicassoBigCache.INSTANCE.getPicassoBigCache(rootView.getContext()).load(obj_event.logo_medium).into(img_Logo);

            btn_add_remove_favorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String text = btn_add_remove_favorite.getText().toString();

                    if(text.equalsIgnoreCase(getString(R.string.add_my_events))){
                        boolean allowEvent = false;

                        if(obj_event.getEvent_type() != null && obj_event.getEvent_type().equalsIgnoreCase("Privado")){

                            AlertDialog.Builder b = new AlertDialog.Builder(rootView.getContext());
                            b.setTitle("Digite a senha do evento.\nCaso não tenha, procure  organização.");

                            final EditText input = new EditText(rootView.getContext());
                            b.setView(input);
                            b.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    inputedPassword = input.getText().toString();

                                    if(obj_event.getPassword().contentEquals(inputedPassword)){
                                        addFavoriteEvent(btn_add_remove_favorite, rootView);
                                    }
                                    else
                                    {
                                        AlertDialog.Builder al = new AlertDialog.Builder(rootView.getContext());
                                        al.setMessage(getString(R.string.password_to));
                                        al.setNeutralButton("OK", null);
                                        al.show();
                                    }
                                }
                            });
                            b.setNegativeButton("Cancelar", null);
                            b.create().show();
                        }
                        else {
                            addFavoriteEvent(btn_add_remove_favorite, rootView);
                        }
                    }
                    else{

                        btn_add_remove_favorite.setText(getString(R.string.add_my_events));
                        try {
                            Call<ResponseJson> responseCall = new PostRestAdapter().deleteFavoriteEvent(session.returnUserId(), obj_event.getId());
                            responseCall.enqueue(new Callback<ResponseJson>() {

                                @Override
                                public void onResponse(Response<ResponseJson> response, Retrofit retrofit) {
                                    Log.d("MAKADU", response.message());
                                    SharedPreferences sp = rootView.getContext().getSharedPreferences("MyPreferencesFavoriteEvents", Context.MODE_PRIVATE);
                                    Set<String> favorites = sp.getStringSet("favorite_events", new HashSet<String>());

                                    if (favorites.contains(obj_event.getId())) {
                                        favorites.remove(obj_event.getId());
                                    }

                                    sp.edit().putStringSet("favorite_events", favorites).apply();

                                    AlertDialog.Builder alert = new AlertDialog.Builder(rootView.getContext());
                                    alert.setTitle("Evento Removido com Sucesso. \n");
                                    alert.setNeutralButton("OK", null);
                                    alert.show();
                                }

                                @Override
                                public void onFailure(Throwable t) {
                                }
                            });

                        } catch (Exception e) {

                            AlertDialog.Builder alert = new AlertDialog.Builder(rootView.getContext());
                            alert.setTitle("Ocorreu algum problema em Remover o Evento. \n");
                            alert.setNeutralButton("OK", null);
                            alert.show();
                        }
                    }

                }
            });

            /*byte[] img_byte_pat = obj_event.getFile_img_event();
            Bitmap bitmap_pat = null;

            if( img_byte_pat != null) {
                bitmap_pat = BitmapFactory.decodeByteArray(img_byte_pat, 0, img_byte_pat.length);
                img_Logo.setImageBitmap(bitmap_pat);
            }*/

            return rootView;
        }

        private void addFavoriteEvent(Button btn_add_remove_favorite, final View rootView) {
            btn_add_remove_favorite.setText(getString(R.string.remove_my_events));

            try {
                Call<ResponseJson> call = new PostRestAdapter().newEventFavorite(session.returnUserId(), obj_event.getId());

                call.enqueue(new Callback<ResponseJson>() {
                    @Override
                    public void onResponse(Response<ResponseJson> response, Retrofit retrofit) {

                        Log.d("MAKADU", response.message());
                        SharedPreferences sp = rootView.getContext().getSharedPreferences("MyPreferencesFavoriteEvents", Context.MODE_PRIVATE);
                        Set<String> favorites = sp.getStringSet("favorite_events", new HashSet<String>());

                        if (!favorites.contains(obj_event.getId())) {
                            favorites.add(obj_event.getId());
                        }

                        sp.edit().putStringSet("favorite_events", favorites).apply();

                        AlertDialog.Builder alert = new AlertDialog.Builder(rootView.getContext());
                        alert.setTitle("Evento Adicionado com Sucesso. \n");
                        alert.setNeutralButton("OK", null);
                        alert.show();
                    }

                    @Override
                    public void onFailure(Throwable t) {

                    }
                });

            } catch (IOException e) {
                AlertDialog.Builder alert = new AlertDialog.Builder(rootView.getContext());
                alert.setTitle("Ocorreu algum problema em Adicionar o Evento. \n");
                alert.setNeutralButton("OK", null);
                alert.show();
                e.printStackTrace();
            }
        }
    }

    public static class Fragment_Programacao extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

        ProgressBar progress;
        ArrayList<Talk> array_pro;
        List<Talk> talks;
        ArrayList<Talk> list_talk;
        private SwipeRefreshLayout mSwipeRefreshLayout;
        View rootView;
        private static final String ARG_SECTION_NUMBER = "section_number";
        boolean cache = false;
        View viewtask;

        public static Fragment_Programacao newInstance(int sectionNumber) {
            Fragment_Programacao fragment = new Fragment_Programacao();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public Fragment_Programacao() {}

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            setHasOptionsMenu(true);
        }

        private ArrayList<Talk> filter(String text, ArrayList<Talk> target) {
            ArrayList<Talk> result = new ArrayList<Talk>();

            for (Talk element: target) {

                if ( (element.getTitulo().toLowerCase(Locale.getDefault()).contains(text.toLowerCase(Locale.getDefault())) ) ||
                        (element.getLocal().toLowerCase(Locale.getDefault()).contains(text.toLowerCase(Locale.getDefault())) ) ||
                        (element.speakers.toLowerCase(Locale.getDefault()).contains(text.toLowerCase(Locale.getDefault())) )) {
                    result.add(element);
                }
            }
            return result;
        }

        private SearchView.OnQueryTextListener mQueryTextListenerTalk = new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String newText) {

                ArrayList<Talk> filtered;
                ArrayList<Talk> list_talk = new ArrayList<Talk>();

                TalkDao tDAO = new TalkDao(getActivity().getApplicationContext());
                list_talk = (ArrayList<Talk>) tDAO.getListTalkForEventIdTAB_TALK(Long.parseLong(obj_event.id));

                filtered = filter(newText, list_talk);
                agrupamento(getActivity().getApplicationContext(), filtered, false);

                adapterTalk.notifyDataSetChanged();

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<Talk> filtered;
                ArrayList<Talk> list_talk = new ArrayList<Talk>();

                TalkDao tDAO = new TalkDao(getActivity().getApplicationContext());
                list_talk = (ArrayList<Talk>) tDAO.getListTalkForEventIdTAB_TALK(Long.parseLong(obj_event.id));

                filtered = filter(newText, list_talk);
                agrupamento(getActivity().getApplicationContext(), filtered, false);

                adapterTalk.notifyDataSetChanged();
                return false;
            }
        };


            @Override
        public void  onResume () {
            super.onResume();
            Localytics.openSession();
            Localytics.tagScreen("Programacao");
            Localytics.upload();
        }

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            super.onCreateOptionsMenu(menu, inflater);

            inflater.inflate(R.menu.menu_tab_detalhe_evento_programacao, menu);

            MenuItem searchItem = menu.findItem(R.id.action_search_tab_detalhe_programacao);
            SearchView searchView = (SearchView) searchItem.getActionView();
            searchView.setOnQueryTextListener(mQueryTextListenerTalk);

            searchView.setOnCloseListener(new SearchView.OnCloseListener() {
                @Override
                public boolean onClose() {
                    ArrayList<Talk> list_talk = new ArrayList<Talk>();

                    list_talk = (ArrayList<Talk>) new TalkDao(getActivity().getApplicationContext()).getListTalkForEventIdTAB_TALK(Long.parseLong(obj_event.id));

                    agrupamento(getActivity().getApplicationContext(), list_talk, false);

                    adapterTalk.notifyDataSetChanged();

                    return false;
                }
            });
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            rootView = inflater.inflate(R.layout.fragment_tab_talk, container, false);

            mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeLoading_Talk);
            mSwipeRefreshLayout.setOnRefreshListener(this);
            mSwipeRefreshLayout.setColorSchemeColors(R.color.Verde_Makadu);

            list_talk  = new ArrayList<Talk>();

            progress = (ProgressBar) rootView.findViewById(R.id.progressBar_talk);

            try {
                if (returnFirstAccess(obj_event.getId())) {
                    Log.v("first_acess_log", "retornou primeiro acesso true if");
                    loadDataTalk(rootView, false);
                } else {
                    Log.v("first_acess_log", "retornou primeiro acesso false else");
                    loadDataTalk(rootView, true);
                }
            }catch (Exception e) {
                Log.v("erro_cache",e.getMessage());
                loadDataTalk(rootView, false);
            }

            return rootView;

        }

        public void loadDataTalk(final View view, final boolean cache_load) {

            this.viewtask = view;

            AsyncTask<Void, Void, ArrayList<Talk>> task = new AsyncTask<Void, Void, ArrayList<Talk>>() {

                @Override
                protected void onPreExecute() {
                    progress.setVisibility(View.VISIBLE);
                }

                @Override
                protected ArrayList<Talk> doInBackground(Void... arg0) {

                    try {
                        String id = obj_event.getId();
                        TalkDao tDAO = new TalkDao(getActivity().getApplicationContext());

                        if(util.isConnected()) {
                            list_talk = (ArrayList<Talk>) new GetRestAdapter().returnAllTalks(id);
                            try {
                                Log.e("LOG_EVENT", "ANTES UPSERT");
                                tDAO.upsert(list_talk,id);
                            } catch (Exception ex) {
                                Log.e("erro", ex.getMessage());
                            }
                        }
                        else
                        {
                            list_talk = (ArrayList<Talk>) tDAO.getListTalkForEventIdTAB_TALK(Long.parseLong(id));
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    return list_talk;
                }

                @Override
                protected void onPostExecute(ArrayList<Talk> array_talk) {

                    ExpandableListView expandableListView = (ExpandableListView) viewtask.findViewById(R.id.expandableListViewTalk);
                    agrupamento(viewtask.getContext(),array_talk,true);
                    expandableListView.setAdapter(adapterTalk);

                    //expandi ExpandableListView por defaut
                    //for(int i = 0; i< adapterTalk.getGroupCount();i++)
                    //  expandableListView.expandGroup(i);

                    expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                        @Override
                        public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                            final Talk talk = (Talk) parent.getExpandableListAdapter().getChild(groupPosition,childPosition);

                            Intent intent = new Intent(v.getContext(), TalkDetailActivity.class);
                            intent.putExtra("id", talk);
                            intent.putExtra("event_id",obj_event.getId());
                            v.getContext().startActivity(intent);

                            return false;
                        }
                    });

                    expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
                        @Override
                        public void onGroupExpand(int groupPosition) {
                        }
                    });

                    expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
                        @Override
                        public void onGroupCollapse(int groupPosition) {
                        }
                    });

                    //expandableListView.setGroupIndicator(getResources().getDrawable(R.drawable.icon_group));
                    expandableListView.setIndicatorBounds(expandableListView.getRight() - 390,expandableListView.getWidth());

                    TextView txt_nome_evento = (TextView)viewtask.findViewById(R.id.txt_detalhe_evento_nome);
                    txt_nome_evento.setText(obj_event.getTitle());

                    progress.setVisibility(View.INVISIBLE);

                    if(returnFirstAccess(obj_event.getId())) {
                        save_preferences_json_event(obj_event.getId());
                    }

                    if(mSwipeRefreshLayout.isRefreshing()) {
                        mSwipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(viewtask.getContext(),"Programação Atualizada",Toast.LENGTH_SHORT).show();
                    }
                }
            };
            task.execute((Void[]) null);
        }

        public ArrayList<String> findLocally(Context ctx)  {

            SharedPreferences prefs = ctx.getSharedPreferences("PreferencesEvent.json", Context.MODE_PRIVATE);
            String jsonString = prefs.getString("json", "{}");
            Log.v("pega_json_event", jsonString);

            JSONObject json = null;
            try {
                json = new JSONObject(jsonString);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JSONArray events = json.optJSONArray("events");

            if (events == null) {
                events = new JSONArray();
            }

            ArrayList<String> id = new ArrayList<String>();

            for (int i = 0; i < events.length(); ++i) {
                String objectId = events.optString(i);
                id.add(objectId);
            }

            return id;
        }

        @Override
        public void onRefresh() {
            loadDataTalk(rootView, false);
        }

        private boolean returnFirstAccess(String id_parse) {

            boolean first = true;

            ArrayList<String> id = findLocally(rootView.getContext());

            for(String id_event : id) {
                if(id_event.equals(id_parse)) {
                    first = false;
                    break;
                }
            }
            return first;
        }

        private void save_preferences_json_event(String event) {

            final JSONObject json = toJSON(event);

            new AsyncTask<Void, Void, Exception>() {
                @Override
                protected Exception doInBackground(Void... unused) {
                    try {
                        String jsonString = json.toString();
                        SharedPreferences prefs = rootView.getContext().getSharedPreferences("PreferencesEvent.json", Context.MODE_PRIVATE);
                        prefs.edit().putString("json", jsonString).apply();
                    } catch (Exception e) {
                        return e;
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Exception error) {
                    if (error != null) {
                        Toast toast = Toast.makeText(rootView.getContext(), "Erro ao gravar as preferencias do evento: " + error.getMessage() +". Feche e abra a aplição.", Toast.LENGTH_LONG);
                        toast.show();
                    }
                }
            }.execute();
        }

        private JSONObject toJSON(String event) {
            JSONArray events = new JSONArray();
            ArrayList<String> id = new ArrayList<String>();
            id = findLocally(rootView.getContext());

            id.add(event);

            for (String objectId : id) {
                events.put(objectId);
            }

            JSONObject json = new JSONObject();
            try {
                json.put("events", events);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            return json;
        }

    }

    public static class Fragment_Paper extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

        ProgressBar progress;
        ArrayList<Paper> array_paper;
        List<Paper> papers;
        ArrayList<Paper> list_paper;
        private SwipeRefreshLayout mSwipeRefreshLayout;
        View rootView;
        private static final String ARG_SECTION_NUMBER = "section_number";
        boolean cache = false;
        View viewtask;

        public static Fragment_Paper newInstance(int sectionNumber) {
            Fragment_Paper fragment = new Fragment_Paper();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public Fragment_Paper() {}

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            setHasOptionsMenu(true);
        }

        @Override
        public void  onResume () {
            super.onResume ();
            Localytics.openSession();
            Localytics.tagScreen("Paper");
            Localytics.upload();
        }

        private ArrayList<Paper> filterPaper(String text, ArrayList<Paper> target) {
            ArrayList<Paper> result = new ArrayList<Paper>();

            for (Paper element: target) {

                if ( (element.reference.toLowerCase(Locale.getDefault()).contains(text.toLowerCase(Locale.getDefault())) ) ||
                        (element.title.toLowerCase(Locale.getDefault()).contains(text.toLowerCase(Locale.getDefault())) ) ||
                        (element.authors.toLowerCase(Locale.getDefault()).contains(text.toLowerCase(Locale.getDefault())) )) {
                    result.add(element);
                }
            }
            return result;
        }

        private SearchView.OnQueryTextListener mQueryTextListenerPaper = new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String newText) {

                ArrayList<Paper> filtered;
                ArrayList<Paper> list_paper = new ArrayList<Paper>();

                try {
                    String id = obj_event.getId();
                    Log.v("LOGPAPER","antes query submit: ");
                    list_paper = (ArrayList<Paper>) new PaperDao(getActivity().getApplicationContext()).getListPaperForEventIdTAB_PAPER(Long.parseLong(id));
                    Log.v("LOGPAPER","depoisquery submit: ");
                } catch (Exception e) {
                    Log.v("LOGPAPER","erro query submit: " + e.getMessage());
                }

                filtered = filterPaper(newText, list_paper);

                carrega_adapter(getActivity().getApplicationContext(),filtered,false);

                adapterPaper.notifyDataSetChanged();

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        };

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            super.onCreateOptionsMenu(menu, inflater);

            inflater.inflate(R.menu.menu_tab_detalhe_evento_paper, menu);

            MenuItem searchItem = menu.findItem(R.id.action_search_tab_detalhe_paper);
            SearchView searchView = (SearchView) searchItem.getActionView();
            searchView.setOnQueryTextListener(mQueryTextListenerPaper);

            searchView.setOnCloseListener(new SearchView.OnCloseListener() {
                @Override
                public boolean onClose() {
                    List<Paper> filtered;
                    ArrayList<Paper> list_paper = new ArrayList<Paper>();

                    try {
                        String id = obj_event.getId();
                        Log.v("LOGPAPER", "antes: ");
                        list_paper = (ArrayList<Paper>) new PaperDao(getActivity().getApplicationContext()).getListPaperForEventIdTAB_PAPER(Long.parseLong(id));
                        Log.v("LOGPAPER", "depois: ");
                    } catch (Exception e) {
                        Log.v("LOGPAPER", "erro: " + e.getMessage());
                    }

                    carrega_adapter(getActivity().getApplicationContext(), list_paper, false);

                    adapterPaper.notifyDataSetChanged();

                    return false;
                }
            });
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            rootView = inflater.inflate(R.layout.fragment_tab_paper, container, false);

            mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeLoading_Paper);
            mSwipeRefreshLayout.setOnRefreshListener(this);
            mSwipeRefreshLayout.setColorSchemeColors(R.color.Verde_Makadu);

            list_paper  = new ArrayList<Paper>();

            progress = (ProgressBar) rootView.findViewById(R.id.progressBar_paper);

            try {
                loadDataPaper(rootView, false);
            }catch (Exception e) {
                Log.v("erro_cache",e.getMessage());
                loadDataPaper(rootView, false);
            }

            return rootView;

        }

        public void loadDataPaper(final View view, final boolean cache_load) {

            this.viewtask = view;

            AsyncTask<Void, Void, ArrayList<Paper>> task = new AsyncTask<Void, Void, ArrayList<Paper>>() {

                @Override
                protected void onPreExecute() {
                    progress.setVisibility(View.VISIBLE);
                }

                @Override
                protected ArrayList<Paper> doInBackground(Void... arg0) {

                    try {
                        String id = obj_event.getId();
                        PaperDao pDao = new PaperDao(getActivity().getApplicationContext());
                        if(util.isConnected()) {
                            list_paper = (ArrayList<Paper>) new GetRestAdapter().returnPapers(id);
                            try {
                                Log.e("LOG_EVENT", "ANTES UPSERT");
                                pDao.upsert(list_paper,id);
                            } catch (Exception ex) {
                                Log.e("erro", ex.getMessage());
                            }
                        }
                        else
                        {
                            list_paper = (ArrayList<Paper>) pDao.getListPaperForEventIdTAB_PAPER(Long.parseLong(id));
                        }

                    } catch (Exception e) {}

                    return list_paper;
                }

                @Override
                protected void onPostExecute(ArrayList<Paper> array_paper) {

                    ExpandableListView expandableListView = (ExpandableListView) viewtask.findViewById(R.id.expandableListViewPaper);
                    carrega_adapter(viewtask.getContext(), array_paper, true);
                    expandableListView.setAdapter(adapterPaper);

                    //expandi ExpandableListView por defaut
                    //for(int i = 0; i< adapterTalk.getGroupCount();i++)
                    //  expandableListView.expandGroup(i);

                    expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
                        @Override
                        public void onGroupExpand(int groupPosition) {
                        }
                    });

                    expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
                        @Override
                        public void onGroupCollapse(int groupPosition) {
                        }
                    });

                    //expandableListView.setGroupIndicator(getResources().getDrawable(R.drawable.icon_group));
                    expandableListView.setCacheColorHint(Color.WHITE);


                    progress.setVisibility(View.INVISIBLE);

                    if(returnFirstAccess(obj_event.getId())) {
                        save_preferences_json_event(obj_event.getId());
                    }

                    if(mSwipeRefreshLayout.isRefreshing()) {
                        mSwipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(viewtask.getContext(),"Trabalhos Atualizados",Toast.LENGTH_SHORT).show();
                    }
                }
            };
            task.execute((Void[]) null);
        }

        public ArrayList<String> findLocally(Context ctx)  {

            SharedPreferences prefs = ctx.getSharedPreferences("PreferencesEvent.json", Context.MODE_PRIVATE);
            String jsonString = prefs.getString("json", "{}");
            Log.v("pega_json_event", jsonString);

            JSONObject json = null;
            try {
                json = new JSONObject(jsonString);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JSONArray events = json.optJSONArray("events");

            if (events == null) {
                events = new JSONArray();
            }

            ArrayList<String> id = new ArrayList<String>();

            for (int i = 0; i < events.length(); ++i) {
                String objectId = events.optString(i);
                id.add(objectId);
            }

            return id;
        }

        @Override
        public void onRefresh() {
            loadDataPaper(rootView, false);
        }

        private boolean returnFirstAccess(String id_parse) {

            boolean first = true;

            ArrayList<String> id = findLocally(rootView.getContext());

            for(String id_event : id) {
                if(id_event.equals(id_parse)) {
                    first = false;
                    break;
                }
            }
            return first;
        }

        private void save_preferences_json_event(String event) {

            final JSONObject json = toJSON(event);

            new AsyncTask<Void, Void, Exception>() {
                @Override
                protected Exception doInBackground(Void... unused) {
                    try {
                        String jsonString = json.toString();
                        SharedPreferences prefs = rootView.getContext().getSharedPreferences("PreferencesEvent.json", Context.MODE_PRIVATE);
                        prefs.edit().putString("json", jsonString).apply();
                    } catch (Exception e) {
                        return e;
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Exception error) {
                    if (error != null) {
                        Toast toast = Toast.makeText(rootView.getContext(), "Erro ao gravar as preferencias do evento: " + error.getMessage() +". Feche e abra a aplição.", Toast.LENGTH_LONG);
                        toast.show();
                    }
                }
            }.execute();
        }

        private JSONObject toJSON(String event) {
            JSONArray events = new JSONArray();
            ArrayList<String> id = new ArrayList<String>();
            id = findLocally(rootView.getContext());

            id.add(event);

            for (String objectId : id) {
                events.put(objectId);
            }

            JSONObject json = new JSONObject();
            try {
                json.put("events", events);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            return json;
        }

    }

    public static void carrega_adapter(Context ctx,ArrayList<Paper> array_paper,boolean update) {

        List<String> listGroup;
        HashMap<String, Paper> listData;

        listGroup = new ArrayList<String>();
        listData = new HashMap<String, Paper>();

        Collections.sort(array_paper, new Comparator<Paper>() {
            @Override
            public int compare(Paper t1, Paper t2) {
                return t1.reference.compareTo(t2.reference);
            }
        });

        for (Paper paper_gru : array_paper) {
            String grupo = paper_gru.reference + " - " + paper_gru.title;

            if (grupo.length() > 65) {
                grupo = grupo.substring(0, 65);
                grupo += "...";
            }

            listGroup.add(grupo);
            listData.put(grupo, paper_gru);
        }

        if (update)
            adapterPaper = new PaperExpandableAdapter(ctx, listGroup, listData, (ArrayList<Paper>) array_paper, obj_event.getId(), session.returnUsername());
        else
            adapterPaper.setData(array_paper, listGroup, listData);
    }

    public static void agrupamento(Context ctx,ArrayList<Talk> array_pro,boolean update) {

        List<String> listGroup;
        HashMap<String, List<Talk>> listData;

        listGroup = new ArrayList<String>();
        listData = new HashMap<String, List<Talk>>();

        String dataAux="";
        boolean gruponovo = true;
        Date grupo_data = new Date();

        Collections.sort(array_pro, new Comparator<Talk>() {
            @Override
            public int compare(Talk t1, Talk t2) {
                return t1.getData().compareTo(t2.getData());
            }
        });

        for(Talk prod_gru : array_pro) {
            grupo_data = null;
            Log.v("grup","agrup id -> " + prod_gru.getId() + " grup data -> " + prod_gru.getData() );
            grupo_data = prod_gru.getData();

            String dataddmmyyyy = "";
            dataddmmyyyy = util.getDateTalk(grupo_data);

            Log.v("grup","dataddmmyyyy -> " + dataddmmyyyy );

            if(dataAux.equals(dataddmmyyyy)) {
                gruponovo = false;
                continue;
            }
            else {
                Log.v("grup","listGroup.add(dataddmmyyyy); -> " + dataddmmyyyy );
                listGroup.add(dataddmmyyyy);
            }

            List<Talk> auxList = new ArrayList<Talk>();
            auxList.remove(auxList);
            for (Talk prod : array_pro) {
                if(util.getDateTalk(prod.getData()).equals(dataddmmyyyy))
                    auxList.add(prod);
            }

            listData.put(dataddmmyyyy, auxList);
            dataAux = dataddmmyyyy;
        }

        if(update)
            adapterTalk = new TalkExpandableAdapter(ctx, listGroup, listData, (ArrayList<Talk>) array_pro, obj_event.getId(), session.returnUsername());
        else
            adapterTalk.setData(array_pro,listGroup,listData);
    }

    public static AlertDialog.Builder buildAlert(Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Senha");

// Set up the input
        final EditText input = new EditText(context);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(input);

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        return builder;
    }

}

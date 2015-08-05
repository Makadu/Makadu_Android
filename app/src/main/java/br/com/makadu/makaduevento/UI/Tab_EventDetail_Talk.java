package br.com.makadu.makaduevento.UI;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.localytics.android.Localytics;
import com.parse.ParseException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import br.com.makadu.makaduevento.DAOParse.EventDAO;
import br.com.makadu.makaduevento.DAOParse.TalkDAO;
import br.com.makadu.makaduevento.R;
import br.com.makadu.makaduevento.Util.Util;
import br.com.makadu.makaduevento.adapters.TalkExpandableAdapter;
import br.com.makadu.makaduevento.model.Event;
import br.com.makadu.makaduevento.model.Favorites;
import br.com.makadu.makaduevento.model.Talk;


public class Tab_EventDetail_Talk extends ActionBarActivity implements ActionBar.TabListener  {

    private CharSequence mTitle;

    static Util util;
    static TalkDAO proDAO = new TalkDAO();
    static EventDAO eventDAO = new EventDAO();
    static TalkExpandableAdapter adapter = null;
    SectionsPagerAdapter mSectionsPagerAdapter;
    ViewPager mViewPager;
    Context ctx;

    static Event obj_event;

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_event_detail);

        ctx = this;
        util = new Util(getBaseContext());
        mTitle = getTitle();
        obj_event=(Event)getIntent().getSerializableExtra("obj_event");

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
                            .setTabListener(Tab_EventDetail_Talk.this));
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu_context) {
        getMenuInflater().inflate(R.menu.menu_tab_detalhe_evento, menu_context);

        MenuItem searchItem = menu_context.findItem(R.id.action_search_tab);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(mQueryTextListener);

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                List<Talk> filtered;

                List<Talk> list_talk = new ArrayList<Talk>();

                try {
                    list_talk = proDAO.returnProgramacaoList(obj_event.getId_Parse(), util.isConnected(),true,false,Tab_EventDetail_Talk.this.getBaseContext());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                agrupamento(getApplicationContext(), list_talk, false);

                adapter.notifyDataSetChanged();

                return false;
            }
        });

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
            Intent it = new Intent(Tab_EventDetail_Talk.this,NoticeActivity.class);
            it.putExtra("id",obj_event.getId_Parse());
            startActivity(it);
        }
        if (id == R.id.action_favorite) {
            Favorites fa = new Favorites(ctx);
            fa.findLocally(getBaseContext());
            Intent it = new Intent(Tab_EventDetail_Talk.this,FavoriteActivity.class);
            it.putExtra("obj_evento",obj_event);
            startActivity(it);
        }

        return super.onOptionsItemSelected(item);
    }



    private SearchView.OnQueryTextListener mQueryTextListener = new SearchView.OnQueryTextListener() {

        @Override
        public boolean onQueryTextSubmit(String newText) {

            List<Talk> filtered;

            List<Talk> list_talk = new ArrayList<Talk>();

            try {
                list_talk = proDAO.returnProgramacaoList(obj_event.getId_Parse(), util.isConnected(),true,false,Tab_EventDetail_Talk.this.getBaseContext());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            filtered = filter(newText, list_talk);
            agrupamento(getApplicationContext(), filtered,false);

            adapter.notifyDataSetChanged();

            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            return false;
        }

    };

    public List<Talk> filter(String text, List<Talk> target) {
        List<Talk> result = new ArrayList<Talk>();

        for (Talk element: target) {

            if ( (element.getTitulo().toLowerCase(Locale.getDefault()).contains(text.toLowerCase(Locale.getDefault())) ) ||
                    (element.getLocal().toLowerCase(Locale.getDefault()).contains(text.toLowerCase(Locale.getDefault())) ) ) {
                result.add(element);
            }
        }
        return result;
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

            switch (position) {
                case 0:
                    return Fragment_Delalhe_Evento.newInstance(position + 1);
                case 1:
                    return Fragment_Programacao.newInstance(position + 1);

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
                    return getString(R.string.title_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section2).toUpperCase(l);

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
        public void  onResume () {
            super.onResume ();
            Localytics.openSession();
            Localytics.tagScreen ("Detalhe Evento");
            Localytics.upload ();
        }

        @Override
        public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
            final View rootView = inflater.inflate(R.layout.fragment_tab_event_detail, container, false);

            Localytics.openSession();
            Localytics.tagScreen("Detalhe Evento");
            Localytics.tagEvent(obj_event.getName());
            Localytics.upload ();

            TextView txt_nome_evento = (TextView)rootView.findViewById(R.id.txt_detalhe_evento_nome);
            txt_nome_evento.setText(obj_event.getName());

            TextView txt_local = (TextView)rootView.findViewById(R.id.txtLocal);
            txt_local.setText(obj_event.getLocal());

            TextView txt_endereco = (TextView)rootView.findViewById(R.id.txtEndereço);
            txt_endereco.setText(obj_event.getAddress());

            TextView txt_cidade = (TextView)rootView.findViewById(R.id.txtCidade);
            txt_cidade.setText(obj_event.getCity());

            TextView txt_UF = (TextView)rootView.findViewById(R.id.txtUF);
            txt_UF.setText(obj_event.getState());

            TextView txt_data_ini = (TextView)rootView.findViewById(R.id.txtDataInicio);
            txt_data_ini.setText(obj_event.getStart_date());

            TextView txt_data_fim = (TextView)rootView.findViewById(R.id.txtDataFim);
            txt_data_fim.setText(obj_event.getEnd_date());

            TextView txt_descricao = (TextView)rootView.findViewById(R.id.txt_Textodescricao);
            txt_descricao.setText(obj_event.getDescription());

            ImageView img_Logo = (ImageView)rootView.findViewById(R.id.img_logo_detalhe);

            byte[] img_byte_pat = obj_event.getFile_img_event();
            Bitmap bitmap_pat = null;

            if( img_byte_pat != null) {
                bitmap_pat = BitmapFactory.decodeByteArray(img_byte_pat, 0, img_byte_pat.length);
                img_Logo.setImageBitmap(bitmap_pat);
            }

            return rootView;
        }
    }

    public static class Fragment_Programacao extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

        ProgressBar progress;
        ArrayList<Talk> array_pro;
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
        public void  onResume () {
            super.onResume ();
            Localytics.openSession();
            Localytics.tagScreen("Programacao");
            Localytics.upload();
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            rootView = inflater.inflate(R.layout.fragment_tab_talk, container, false);

            mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeLoading_Talk);
            mSwipeRefreshLayout.setOnRefreshListener(this);
            mSwipeRefreshLayout.setColorSchemeColors(R.color.Verde_Makadu);

            progress = (ProgressBar) rootView.findViewById(R.id.progressBar_talk);

            try {
                if (returnFirstAccess(obj_event.getId_Parse())) {
                    Log.v("first_acess_log", "retornou primeiro acesso true if");
                    loadData(rootView, false);
                } else {
                    Log.v("first_acess_log", "retornou primeiro acesso false else");
                    loadData(rootView, true);
                }
            }catch (Exception e) {
                Log.v("erro_cache",e.getMessage());
                loadData(rootView, false);
            }

            return rootView;

        }

        public void loadData(final View view, final boolean cache) {

            this.viewtask = view;
            this.cache = cache;

            AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {

                @Override
                protected void onPreExecute() {
                    progress.setVisibility(View.VISIBLE);
                }

                @Override
                protected Void doInBackground(Void... arg0) {

                    Log.v("conectado", util.isConnected() + "");
                    try {
                        array_pro = (ArrayList<Talk>) proDAO.returnProgramacaoList(obj_event.getId_Parse(), util.isConnected(),cache,false,viewtask.getContext());
                        Log.v("conectado", array_pro.toString());
                        if(array_pro.isEmpty() || array_pro == null){
                            Log.v("conectado", "deu pau mas restabeleceu");
                            array_pro = (ArrayList<Talk>) proDAO.returnProgramacaoList(obj_event.getId_Parse(), util.isConnected(),false,false,viewtask.getContext());
                        }
                    } catch (ParseException e) {
                        Log.v("erro_parse", "erro do parseexception");
                    }

                    return null;
                }

                @Override
                protected void onPostExecute(Void result) {


                    ExpandableListView expandableListView = (ExpandableListView) viewtask.findViewById(R.id.expandableListViewTalk);
                    agrupamento(viewtask.getContext(),array_pro,true);
                    expandableListView.setAdapter(adapter);

                    //expandi ExpandableListView por defaut
                    //for(int i = 0; i< adapter.getGroupCount();i++)
                    //  expandableListView.expandGroup(i);

                    expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                        @Override
                        public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                            final Talk talk = (Talk) parent.getExpandableListAdapter().getChild(groupPosition,childPosition);

                            Intent intent = new Intent(v.getContext(), TalkDetailActivity.class);
                            intent.putExtra("id", talk);
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

                    expandableListView.setGroupIndicator(getResources().getDrawable(R.drawable.icon_group));

                    TextView txt_nome_evento = (TextView)viewtask.findViewById(R.id.txt_detalhe_evento_nome);
                    txt_nome_evento.setText(obj_event.getName());

                    progress.setVisibility(View.INVISIBLE);

                    if(returnFirstAccess(obj_event.getId_Parse())) {
                        save_preferences_json_event(obj_event.getId_Parse());
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
            loadData(rootView, false);
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
                        prefs.edit().putString("json", jsonString).commit();
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

    public static void agrupamento(Context ctx,List<Talk> array_pro,boolean update) {

        List<String> listGroup;
        HashMap<String, List<Talk>> listData;

        listGroup = new ArrayList<String>();
        listData = new HashMap<String, List<Talk>>();

        String dataAux="";
        boolean gruponovo = true;
        Date grupo_data = new Date();

        for(Talk prod_gru : array_pro) {

            grupo_data = null;
            grupo_data = prod_gru.getData();

            String dataddmmyyyy = "";
            dataddmmyyyy = util.getDate(grupo_data);

            if(dataAux.equals(dataddmmyyyy)) {
                gruponovo = false;
                continue;
            }
            else {
                listGroup.add(dataddmmyyyy);
            }

            List<Talk> auxList = new ArrayList<Talk>();
            auxList.remove(auxList);
            for (Talk prod : array_pro) {

                if(util.getDate(prod.getData()).equals(dataddmmyyyy))
                {
                    auxList.add(prod);
                }
            }
            listData.put(dataddmmyyyy, auxList);
            dataAux = dataddmmyyyy;
        }

        if(update)
            adapter = new TalkExpandableAdapter(ctx, listGroup, listData, (ArrayList<Talk>) array_pro);
        else
            adapter.setData(array_pro,listGroup,listData);
    }

}

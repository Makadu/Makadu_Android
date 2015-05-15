package br.com.makadu.makaduevento.UI;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import br.com.makadu.makaduevento.DAOParse.ProgramacaoDAO;
import br.com.makadu.makaduevento.R;
import br.com.makadu.makaduevento.Util.Util;
import br.com.makadu.makaduevento.adapter.TalkExpandableAdapter;
import br.com.makadu.makaduevento.model.Event;
import br.com.makadu.makaduevento.model.Programacao;


public class Tab_DetalheEvento_Programacao extends ActionBarActivity implements ActionBar.TabListener  {

    private Context context;
    private static ProgressDialog pd;
    private Handler mhandler;
    private CharSequence mTitle;

    SectionsPagerAdapter mSectionsPagerAdapter;
    ViewPager mViewPager;

    static Event obj_event;

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_tab_detalhe_evento);
        context = this;

        mTitle = getTitle();
        //  mhandler = new Handler();
        // progressDialog = ProgressDialog.show(this, "Carregando Programação do Event", "Carregando...");


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
                            .setTabListener(Tab_DetalheEvento_Programacao.this));
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_tab_detalhe_evento, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == android.R.id.home) {
            finish();
            return true;
        }

        if (id == R.id.action_search) {
            Intent it = new Intent(Tab_DetalheEvento_Programacao.this,NotificationActivity.class);
            it.putExtra("id",obj_event.getId());
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
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

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
            // Show 2 total pages.
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

        public Fragment_Delalhe_Evento() {
        }

        @Override
        public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
            final View rootView = inflater.inflate(R.layout.fragment_tab_detalhe_evento, container, false);

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
            if( img_byte_pat != null)
            {
                bitmap_pat = BitmapFactory.decodeByteArray(img_byte_pat, 0, img_byte_pat.length);
                img_Logo.setImageBitmap(bitmap_pat);
            }

            return rootView;
        }
    }

    public static class Fragment_Programacao extends Fragment  {


        ArrayList<Programacao> array_pro;
        ProgramacaoDAO pro = new ProgramacaoDAO();
        /*private RecyclerView mRecyclerView;
        private RecyclerView.Adapter mAdapter;
        private RecyclerView.LayoutManager mLayoutManager;
        */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public static Fragment_Programacao newInstance(int sectionNumber) {
            Fragment_Programacao fragment = new Fragment_Programacao();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public Fragment_Programacao() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            final View rootView = inflater.inflate(R.layout.fragment_tab_programacao, container, false);

            AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {

                @Override
                protected void onPreExecute() {

                    pd = new ProgressDialog(rootView.getContext());
                    pd.setTitle("Carregando Programação do Evento");
                    pd.setMessage("Carregando...");
                    pd.setCancelable(false);
                    pd.setIndeterminate(true);
                    pd.show();
                }

                @Override
                protected Void doInBackground(Void... arg0) {
                    ConnectivityManager cm = (ConnectivityManager) getActivity().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo ni = cm.getActiveNetworkInfo();
                    array_pro = (ArrayList<Programacao>) pro.returnProgramacaoList(obj_event.getId(),ni);

                    return null;
                }

                @Override
                protected void onPostExecute(Void result) {
                    if (pd!=null) {

                        final List<String> listGroup;
                        final HashMap<String, List<Programacao>> listData;
                        listGroup = new ArrayList<String>();
                        listData = new HashMap<String, List<Programacao>>();

                        ExpandableListView expandableListView = (ExpandableListView) rootView.findViewById(R.id.expandableListViewTalk);

                        String dataAux="";
                        boolean gruponovo = true;
                        Date grupo_data = new Date();
                        Util util = new Util();
                        for(Programacao prod_gru : array_pro) {

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

                            List<Programacao> auxList = new ArrayList<Programacao>();
                            auxList.remove(auxList);
                            for (Programacao prod : array_pro) {

                                if(util.getDate(prod.getData()).equals(dataddmmyyyy))
                                {
                                    auxList.add(prod);
                                }
                            }
                            listData.put(dataddmmyyyy, auxList);
                            dataAux = dataddmmyyyy;
                        }

                        TalkExpandableAdapter adapter = new TalkExpandableAdapter(rootView.getContext(), listGroup, listData,array_pro);

                        expandableListView.setAdapter(adapter);

                        for(int i = 0; i< adapter.getGroupCount();i++)
                            expandableListView.expandGroup(i);

                        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener(){
                            @Override
                            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                                Programacao talk = (Programacao) parent.getExpandableListAdapter().getChild(groupPosition,childPosition);

                                Intent intent = new Intent(v.getContext(), DetalheProgramacaoActivity.class);

                                intent.putExtra("id", talk);

                                v.getContext().startActivity(intent);

                                return false;
                            }
                        });

                        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener(){
                            @Override
                            public void onGroupExpand(int groupPosition) {
                                //.makeText(rootView.getContext(), "Group (Expand): "+groupPosition, Toast.LENGTH_SHORT).show();
                            }
                        });

                        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener(){
                            @Override
                            public void onGroupCollapse(int groupPosition) {
                                //Toast.makeText(rootView.getContext(), "Group (Collapse): "+groupPosition, Toast.LENGTH_SHORT).show();
                            }
                        });

                        expandableListView.setGroupIndicator(getResources().getDrawable(R.drawable.icon_group));

                        //mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_programacao);
                        //mRecyclerView.setHasFixedSize(true);
                        //mLayoutManager = new LinearLayoutManager(rootView.getContext());
                        //mRecyclerView.setLayoutManager(mLayoutManager);

                        TextView txt_nome_evento = (TextView)rootView.findViewById(R.id.txt_detalhe_evento_nome);
                        txt_nome_evento.setText(obj_event.getName());


                        //mAdapter = new ProgramacaoAdapter(array_pro);
                        //mRecyclerView.setAdapter(mAdapter);


                        pd.dismiss();
                    }
                }

            };
            task.execute((Void[])null);


            return rootView;

        }

    }

}

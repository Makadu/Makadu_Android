package br.com.makadu.makaduevento.UI;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.localytics.android.Localytics;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import br.com.makadu.makaduevento.DAO.dao.entityDao.TalkDao;
import br.com.makadu.makaduevento.R;
import br.com.makadu.makaduevento.Util.Util;
import br.com.makadu.makaduevento.adapters.TalkExpandableAdapter;
import br.com.makadu.makaduevento.model.Event;
import br.com.makadu.makaduevento.model.Favorites;
import br.com.makadu.makaduevento.model.Talk;
import br.com.makadu.makaduevento.servicesRetrofit.returnAPI.GetRestAdapter;

public class FavoriteActivity extends ActionBarActivity {

    ProgressBar progress;
    ArrayList<Talk> array_pro;
    List<Talk> lista_talk = null;
    TalkExpandableAdapter adapter = null;
    Event obj_event;
    TextView txt_favorite_not;
    Util util;
    Context ctx;

    @Override
    protected void  onResume () {
        super.onResume ();
        Localytics.openSession();
        Localytics.tagScreen ("Favorito");
        Localytics.upload();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        obj_event = (Event)getIntent().getSerializableExtra("obj_evento");

        util = new Util(getBaseContext());
        progress = (ProgressBar) findViewById(R.id.progressBar_favorite);
        txt_favorite_not = (TextView)findViewById(R.id.txt_favorite_not);

        ctx = getBaseContext();

        loadData();

    }

    public void loadData() {

        AsyncTask<Void, Void, ArrayList<Talk>> task = new AsyncTask<Void, Void, ArrayList<Talk>>() {

            @Override
            protected void onPreExecute() {
                progress.setVisibility(View.VISIBLE);
            }

            @Override
            protected ArrayList<Talk> doInBackground(Void... arg0) {

                TalkDao tDAO = new TalkDao(getApplicationContext());

                try {
                    //array_pro = (ArrayList<Talk>) new GetRestAdapter().returnAllTalks(obj_event.getId());
                    Log.v("LOG_EVENT", "Evento: " + obj_event.id);
                    array_pro = (ArrayList<Talk>) tDAO.getListTalkForEventIdTAB_TALK(Long.parseLong(obj_event.id));

                    lista_talk = new ArrayList<Talk>();

                    Favorites favorites;
                    ArrayList<String> arraylist_objectid = null;

                    Log.v("log_talk","isFavorite");
                    favorites = new Favorites(ctx);
                    arraylist_objectid = new ArrayList<String>();
                    arraylist_objectid = favorites.findLocally(ctx);

                    for(Talk t : array_pro) {

                        if(arraylist_objectid.contains(t.getId())) {
                            lista_talk.add(t);
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                return (ArrayList<Talk>) lista_talk;
            }

            @Override
            protected void onPostExecute(ArrayList<Talk> array_pro_async) {


                ExpandableListView expandableListView = (ExpandableListView) findViewById(R.id.expandableListViewTalk);

                if (!(array_pro_async == null)) {
                    agrupamento(getBaseContext(), array_pro_async, true);


                    expandableListView.setAdapter(adapter);

                    for (int i = 0; i < adapter.getGroupCount(); i++)
                        expandableListView.expandGroup(i);

                    expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                        @Override
                        public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                            final Talk talk = (Talk) parent.getExpandableListAdapter().getChild(groupPosition, childPosition);

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

                }
                progress.setVisibility(View.INVISIBLE);
                if(array_pro_async.isEmpty())
                    txt_favorite_not.setVisibility(View.VISIBLE);
                else
                    txt_favorite_not.setVisibility(View.INVISIBLE);
            }

        };

        task.execute((Void[]) null);

    }

    public void agrupamento(Context ctx,List<Talk> array_pro,boolean update) {

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
            adapter = new TalkExpandableAdapter(ctx, listGroup, listData, (ArrayList<Talk>) array_pro, obj_event.getId(), "");
        else
            adapter.setData(array_pro,listGroup,listData);
    }

}

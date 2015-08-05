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

import com.localytics.android.Localytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import br.com.makadu.makaduevento.DAOParse.EventDAO;
import br.com.makadu.makaduevento.DAOParse.TalkDAO;
import br.com.makadu.makaduevento.R;
import br.com.makadu.makaduevento.Util.Util;
import br.com.makadu.makaduevento.adapters.TalkExpandableAdapter;
import br.com.makadu.makaduevento.model.Event;
import br.com.makadu.makaduevento.model.Talk;

public class FavoriteActivity extends ActionBarActivity {

    ProgressBar progress;
    ArrayList<Talk> array_pro;
    TalkExpandableAdapter adapter = null;
    TalkDAO proDAO = new TalkDAO();
    EventDAO eventDAO = new EventDAO();
    Event obj_event;
    Util util;
    Context ctx;

    @Override
    protected void  onResume () {
        super.onResume ();
        Localytics.openSession();
        Localytics.tagScreen ("Favorito");
        Localytics.upload ();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        obj_event = (Event)getIntent().getSerializableExtra("obj_evento");

        util = new Util(getBaseContext());
        progress = (ProgressBar) findViewById(R.id.progressBar_favorite);

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

                try {
                    array_pro = (ArrayList<Talk>) proDAO.returnProgramacaoList(obj_event.getId_Parse(), util.isConnected(),false,true,getBaseContext());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                return array_pro;
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

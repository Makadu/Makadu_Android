package br.com.makadu.makaduevento.UI;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.localytics.android.Localytics;

import java.io.IOException;
import java.util.ArrayList;


import br.com.makadu.makaduevento.R;
import br.com.makadu.makaduevento.Util.Util;
import br.com.makadu.makaduevento.adapters.NoticeAdapter;
import br.com.makadu.makaduevento.model.Notice;
import br.com.makadu.makaduevento.servicesRetrofit.returnAPI.GetRestAdapter;


public class NoticeActivity extends ActionBarActivity {

    private ListView mListView;
    NoticeAdapter noticesAdapter;
    ArrayList<Notice> list_notices;
    ProgressBar progress;
    TextView txt_notification;
    String id;

    @Override
    protected void  onResume () {
        super.onResume ();
        Localytics.openSession();
        Localytics.tagScreen ("Notificacao");
        Localytics.upload ();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        progress = (ProgressBar) findViewById(R.id.progressBar_notification);

        mListView = (ListView) findViewById(R.id.listView_notice);
        txt_notification = (TextView)findViewById(R.id.txt_notification_not);

        id = (String)getIntent().getSerializableExtra("id");

        loadData();

    }

    public void loadData() {

        list_notices = new ArrayList<Notice>();

        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {

            Util util = new Util(getBaseContext());


            @Override
            protected void onPreExecute() {
                progress.setVisibility(View.VISIBLE);
            }

            @Override
            protected Void doInBackground(Void... arg0) {

                //list_notices = (ArrayList<Notice>) nDAO.returnNoticeList(id,util.isConnected());
                try {
                    list_notices = (ArrayList<Notice>) new GetRestAdapter().returnAllNotices(id);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                progress.setVisibility(View.INVISIBLE);
                noticesAdapter = new NoticeAdapter(getBaseContext(),list_notices );
                mListView.setAdapter(noticesAdapter);
                noticesAdapter.notifyDataSetChanged();
                Log.d("LOGNOT", "list_notices.isEmpty()" + list_notices.isEmpty());
                Log.d("LOGNOT","list_notices.size()" + list_notices.size());
                if(list_notices.isEmpty())
                    txt_notification.setVisibility(View.VISIBLE);
                else
                    txt_notification.setVisibility(View.INVISIBLE);

            }

        };
        task.execute((Void[]) null);
    }

}

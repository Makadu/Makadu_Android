package br.com.makadu.makaduevento.UI;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.localytics.android.Localytics;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import br.com.makadu.makaduevento.DAOParse.NoticeDAO;
import br.com.makadu.makaduevento.DAOParse.QuestionDAO;
import br.com.makadu.makaduevento.R;
import br.com.makadu.makaduevento.Util.Util;
import br.com.makadu.makaduevento.adapters.NoticeAdapter;
import br.com.makadu.makaduevento.model.Notice;
import br.com.makadu.makaduevento.model.Question;


public class NoticeActivity extends ActionBarActivity {

    private ListView mListView;
    NoticeAdapter noticesAdapter;
    ArrayList<Notice> list_notices;
    ProgressBar progress;
    TextView txt_notification;
    NoticeDAO nDAO = new NoticeDAO();
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
        txt_notification.setText("");

        id = (String)getIntent().getSerializableExtra("id");

        loadData();

    }

    public void loadData() {

        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {

            Util util = new Util(getBaseContext());


            @Override
            protected void onPreExecute() {
                progress.setVisibility(View.VISIBLE);
            }

            @Override
            protected Void doInBackground(Void... arg0) {

                list_notices = (ArrayList<Notice>) nDAO.returnNoticeList(id,util.isConnected());

                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                progress.setVisibility(View.INVISIBLE);
                noticesAdapter = new NoticeAdapter(getBaseContext(),list_notices );
                mListView.setAdapter(noticesAdapter);
                noticesAdapter.notifyDataSetChanged();
            }

        };
        task.execute((Void[]) null);
    }

}

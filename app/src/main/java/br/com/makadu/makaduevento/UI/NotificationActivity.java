package br.com.makadu.makaduevento.UI;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;

import br.com.makadu.makaduevento.DAOParse.NoticeDAO;
import br.com.makadu.makaduevento.R;
import br.com.makadu.makaduevento.adapter.NoticeAdapter;
import br.com.makadu.makaduevento.model.Notice;


public class NotificationActivity extends ActionBarActivity {

    private ListView mListView;
    ArrayList<Notice> list_notices = new ArrayList<Notice>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        mListView = (ListView) findViewById(R.id.listView_notice);

        NoticeDAO nDAO = new NoticeDAO();

        String id = (String)getIntent().getSerializableExtra("id");

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();

        NoticeAdapter noticesAdapter = new NoticeAdapter(this, nDAO.returnNoticeList(id,ni));

        mListView.setAdapter(noticesAdapter);
    }

}

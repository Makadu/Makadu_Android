package br.com.makadu.makaduevento.UI;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;

import br.com.makadu.makaduevento.R;

/**
 * Created by lucasschwalbeferreira on 5/14/16.
 */
public class ActionBarmakadu extends AppCompatActivity {

    @Override
    protected void onStart() {
        super.onStart();
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_makadu);
    }

}

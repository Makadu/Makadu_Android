package br.com.makadu.makaduevento.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import br.com.makadu.makaduevento.R;
import br.com.makadu.makaduevento.model.Event;

public class OptionsActivity extends ActionBarmakadu {

    private Event obj_event;
    LinearLayout infoGerais;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        obj_event=(Event)getIntent().getSerializableExtra("obj_event");

        infoGerais = (LinearLayout) findViewById(R.id.linear_info_gerais);

        infoGerais.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(OptionsActivity.this,Tab_EventDetail_Talk_Paper.class);
                i.putExtra("obj_event", obj_event);
                startActivity(i);
            }
        });
    }
}

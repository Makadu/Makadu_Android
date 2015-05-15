package br.com.makadu.makaduevento.UI;

import android.accounts.NetworkErrorException;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.com.makadu.makaduevento.DAOParse.ProgramacaoDAO;
import br.com.makadu.makaduevento.DAOParse.QuestionDAO;
import br.com.makadu.makaduevento.R;
import br.com.makadu.makaduevento.Util.Email;
import br.com.makadu.makaduevento.Util.Util;
import br.com.makadu.makaduevento.adapter.PalestranteAdapter;
import br.com.makadu.makaduevento.adapter.TalkDetailExpandableAdapter;
import br.com.makadu.makaduevento.model.Palestrante;
import br.com.makadu.makaduevento.model.Programacao;
import br.com.makadu.makaduevento.model.Question;

public class DetalheProgramacaoActivity extends ActionBarActivity {

    static Programacao obj_prog;
    ArrayList<Palestrante> list_palestrantes = new ArrayList<Palestrante>();
    ListView listviewPalestrante;

    List<String> listGroup;
    HashMap<String, List<Programacao>> listDataProg;
    HashMap<String, List<Palestrante>> listDataPalestrante;
    HashMap<String, List<Question>> listDataQuestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_detalhe_programacao);

        obj_prog = (Programacao)getIntent().getSerializableExtra("id");

        LinearLayout bt_download = (LinearLayout)findViewById(R.id.btn_detalhe_pro_Download);
        if(!obj_prog.isAllow_file()){ bt_download.setVisibility(LinearLayout.GONE); }else{bt_download.setVisibility(LinearLayout.VISIBLE);};

        LinearLayout bt_questino = (LinearLayout)findViewById(R.id.btn_detalhe_pro_Pergunta);
        if(!obj_prog.isAllow_question()){ bt_questino.setVisibility(LinearLayout.GONE); }else{bt_questino.setVisibility(LinearLayout.VISIBLE);};

        listGroup = new ArrayList<String>();
        listDataProg = new HashMap<String, List<Programacao>>();
        listDataPalestrante = new HashMap<String,List<Palestrante>>();
        listDataQuestion = new HashMap<String,List<Question>>();

        ExpandableListView expandableListView = (ExpandableListView)findViewById(R.id.expandableListViewTalkAbout);

        final Util util = new Util();

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        ProgramacaoDAO proDAO = new ProgramacaoDAO();

        List<Programacao> listTalk = new ArrayList<Programacao>();
        listTalk.add(obj_prog);

        listDataProg.put("Sobre esta Atividade", listTalk);

        listDataPalestrante.put("Palestrante", obj_prog.getPalestrantes());

        QuestionDAO qDAO = new QuestionDAO();

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        listDataQuestion.put("Perguntas", qDAO.returnQuestionList(obj_prog.getId(),ni));


        // GROUP
        listGroup.add("Sobre esta Atividade");
        listGroup.add("Palestrante");
        listGroup.add("Perguntas");

        TalkDetailExpandableAdapter adapter = new TalkDetailExpandableAdapter(this, listGroup, listDataProg, listDataPalestrante,listDataQuestion, obj_prog);

        expandableListView.setAdapter(adapter);

        for(int i = 0; i< adapter.getGroupCount();i++)
            expandableListView.expandGroup(i);

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener(){
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                //Toast.makeText(DetalheProgramacaoActivity.this, "Group: "+groupPosition+"| Item: "+childPosition, Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener(){
            @Override
            public void onGroupExpand(int groupPosition) {
                //Toast.makeText(, "Group (Expand): "+groupPosition, Toast.LENGTH_SHORT).show();
            }
        });

        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener(){
            @Override
            public void onGroupCollapse(int groupPosition) {
                //Toast.makeText(DetalheProgramacaoActivity.this, "Group (Collapse): "+groupPosition, Toast.LENGTH_SHORT).show();
            }
        });

        expandableListView.setGroupIndicator(getResources().getDrawable(R.drawable.icon_group));

        TextView txt_titulo_prog = (TextView)findViewById(R.id.txtTituloProgramacao);
        txt_titulo_prog.setText(obj_prog.getTitulo());

        adapter.notifyDataSetChanged();

        String LocalDataHora = "";
        LocalDataHora += obj_prog.getLocal();
        LocalDataHora += " " + util.getDate(obj_prog.getData());
        LocalDataHora += " " + obj_prog.getHoraInicio();

        TextView txt_local_programacao = (TextView)findViewById(R.id.txt_detalhe_programacao_local);
        txt_local_programacao.setText(LocalDataHora);

        LinearLayout btn_Download = (LinearLayout)findViewById(R.id.btn_detalhe_pro_Download);

        btn_Download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Email email = new Email();
                try {
                    email.sendEmailConteudoProgramacao(v.getContext(), obj_prog);
                }catch (Exception e){
                    Toast.makeText(v.getContext(),"Ocorreu algum erro no DOWNLOAD!!!",Toast.LENGTH_LONG).show();
                }

            }
        });

        LinearLayout btn_Pergunta = (LinearLayout)findViewById(R.id.btn_detalhe_pro_Pergunta);
        btn_Pergunta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                util.talk(v.getContext(),obj_prog.getId());

            }
        });

        /*RatingBar rating = (RatingBar)findViewById(R.id.rating_inicial);

        rating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {

            @Override
            public void onRatingChanged(RatingBar ratingBar_on, float rating, boolean fromUser) {
                LayoutInflater inflater = LayoutInflater.from(DetalheProgramacaoActivity.this);
                AlertDialog.Builder alert = new AlertDialog.Builder(DetalheProgramacaoActivity.this);
                alert.setTitle(null);
                View alert_ratingView = inflater.inflate(R.layout.alert_rating,null,false);

                TextView txtAvaliacao = (TextView)alert_ratingView.findViewById(R.id.txt_avalie_essa_palestra);
                txtAvaliacao.setText("Avaliacão de " + ParseUser.getCurrentUser().getUsername());

                RatingBar ratingBar = (RatingBar) alert_ratingView.findViewById(R.id.ratingBar_rating);

                ratingBar.setRating(rating);
                alert.setView(alert_ratingView);
                alert.create();
                alert.setPositiveButton("Enviar", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(DetalheProgramacaoActivity.this,"Avaliacao Efetuada",Toast.LENGTH_LONG).show();
                    }
                });
                alert.show();

            }
        });*/

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.global, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == android.R.id.home) {
            finish();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }
}

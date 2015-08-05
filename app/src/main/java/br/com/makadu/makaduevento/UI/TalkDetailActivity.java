package br.com.makadu.makaduevento.UI;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.localytics.android.Localytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import br.com.makadu.makaduevento.DAOParse.TalkDAO;
import br.com.makadu.makaduevento.DAOParse.QuestionDAO;
import br.com.makadu.makaduevento.R;
import br.com.makadu.makaduevento.Util.Email;
import br.com.makadu.makaduevento.Util.Util;
import br.com.makadu.makaduevento.Util.Question_talk;
import br.com.makadu.makaduevento.adapters.TalkDetailExpandableAdapter;
import br.com.makadu.makaduevento.model.Speaker;
import br.com.makadu.makaduevento.model.Talk;
import br.com.makadu.makaduevento.model.Question;

public class TalkDetailActivity extends ActionBarActivity {

    static Talk obj_prog;
    ArrayList<Speaker> list_palestrantes = new ArrayList<Speaker>();
    ListView listviewPalestrante;

    TalkDetailExpandableAdapter adapter;
    ProgressBar progress;
    boolean avaliou;
    ProgressDialog pd;
    List<String> listGroup;
    HashMap<String, List<Talk>> listDataProg;
    HashMap<String, List<Speaker>> listDataPalestrante;
    HashMap<String, List<Question>> listDataQuestion;
    Util util;

    @Override
    protected void  onResume () {
        super.onResume ();
        Localytics.openSession();
        Localytics.tagScreen ("Detalhe Programacao");
        Localytics.upload ();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_talk_detail);

        util = new Util(getBaseContext());
        progress = (ProgressBar) findViewById(R.id.progressBar_talk_detail);
        obj_prog = (Talk)getIntent().getSerializableExtra("id");

        LinearLayout bt_download = (LinearLayout)findViewById(R.id.btn_detalhe_pro_Download);

        if (!obj_prog.isAllow_file()) {
            bt_download.setVisibility(LinearLayout.GONE);
        } else {
            bt_download.setVisibility(LinearLayout.VISIBLE);
        }

        LinearLayout bt_questino = (LinearLayout)findViewById(R.id.btn_detalhe_pro_Pergunta);

        if (!obj_prog.isAllow_question()) {
            bt_questino.setVisibility(LinearLayout.GONE);
        } else {
            bt_questino.setVisibility(LinearLayout.VISIBLE);
        }

        listGroup = new ArrayList<String>();
        listDataProg = new HashMap<String, List<Talk>>();
        listDataPalestrante = new HashMap<String,List<Speaker>>();
        listDataQuestion = new HashMap<String,List<Question>>();

        ExpandableListView expandableListView = (ExpandableListView)findViewById(R.id.expandableListViewTalkAbout);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        List<Talk> listTalk = new ArrayList<Talk>();
        listTalk.add(obj_prog);

        listDataProg.put("Sobre esta Atividade", listTalk);

        return_question_loading();
        return_speaker_loading();

        loaData();

        // GROUP
        listGroup.add("Sobre esta Atividade");
        listGroup.add("Palestrante");
        listGroup.add("Perguntas");

        adapter = new TalkDetailExpandableAdapter(this, listGroup, listDataProg, listDataPalestrante,listDataQuestion, obj_prog);

        expandableListView.setAdapter(adapter);

        for(int i = 0; i< adapter.getGroupCount();i++)
            expandableListView.expandGroup(i);

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                return false;
            }
        });

        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) { }
        });

        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) { }
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

                try {
                    new Email().sendEmailConteudoProgramacao(v.getContext(), obj_prog);
                } catch (Exception e) {
                    Toast.makeText(v.getContext(), "Ocorreu algum erro no DOWNLOAD!!!", Toast.LENGTH_LONG).show();
                }
            }
        });

        LinearLayout btn_Pergunta = (LinearLayout)findViewById(R.id.btn_detalhe_pro_Pergunta);
        btn_Pergunta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Question_talk().question(v.getContext(), obj_prog.getId());
                adapter.notifyDataSetChanged();

            }
        });

        loadRating();

    }

    private void loaData() {

        AsyncTask<Void, Void, Void> taskSpeaker = new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                progress.setVisibility(View.VISIBLE);
            }

            @Override
            protected Void doInBackground(Void... arg0) {

                ArrayList<Speaker> speakers = obj_prog.getSpeakers();

                try {
                    if (!speakers.isEmpty()) {
                        listDataPalestrante.put("Palestrante", speakers);
                    } else {
                        return_no_speaker();
                    }
                }catch (Exception e) {
                    return_no_speaker();
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                progress.setVisibility(View.INVISIBLE);
                adapter.notifyDataSetChanged();
            }

        };

        AsyncTask<Void, Void, Void> taskQuestion = new AsyncTask<Void, Void, Void>() {

            QuestionDAO qDAO = new QuestionDAO();

            @Override
            protected void onPreExecute() {
                progress.setVisibility(View.VISIBLE);
            }

            @Override
            protected Void doInBackground(Void... arg0) {

                ArrayList<Question> questions = (ArrayList<Question>) qDAO.returnQuestionList(obj_prog.getId(), util.isConnected());

                if(!questions.isEmpty()) {
                    listDataQuestion.put("Perguntas", questions);
                } else {
                    return_no_question();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                progress.setVisibility(View.INVISIBLE);
                adapter.notifyDataSetChanged();
            }
        };

        if(util.isConnected()) {
            taskSpeaker.execute((Void[]) null);
            taskQuestion.execute((Void[]) null);
        }
    }

    private void loadRating() {
        RatingBar rating = (RatingBar)findViewById(R.id.rating_inicial);
        /*
        ParseQuery<ParseObject> pqprogram = ParseQuery.getQuery("Talks");
        ParseObject program = new ParseObject("Talks");

        try {
            program = pqprogram.get(obj_prog.getId());
            ParseQuery<ParseObject> query_rat = ParseQuery.getQuery("Rating");
            query_rat.whereEqualTo("talk", program);
            query_rat.whereEqualTo("user",ParseUser.getCurrentUser());
            query_rat.orderByAscending("createdAt");
            query_rat.setCachePolicy(ParseQuery.CachePolicy.CACHE_ELSE_NETWORK);

            int rat_int = 0;
            List<ParseObject> list_PO_Rating = null;
            if(util.isConnected()) {
                list_PO_Rating = query_rat.find();

                for(ParseObject ra : list_PO_Rating ) {
                    rat_int = (Integer) ra.get("note");
                }
            }

            rating.setRating(rat_int);

        } catch (ParseException e) {
            Log.d("erro_rating",e.getMessage());
        }

        final ParseObject finalProgram = program;
        */
        rating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {

            @Override
            public void onRatingChanged(RatingBar ratingBar_on, float rating, boolean fromUser) {
                LayoutInflater inflater = LayoutInflater.from(TalkDetailActivity.this);
                AlertDialog.Builder alert = new AlertDialog.Builder(TalkDetailActivity.this);
                alert.setTitle(null);
                View alert_ratingView = inflater.inflate(R.layout.alert_rating,null,false);

                TextView txtAvaliacao = (TextView)alert_ratingView.findViewById(R.id.txt_avalie_essa_palestra);
                txtAvaliacao.setText("Avaliacão de " + ParseUser.getCurrentUser().get("full_name"));

                final EditText edtDescricao = (EditText)alert_ratingView.findViewById(R.id.edt_Descricao_rating);

                final RatingBar ratingBar = (RatingBar) alert_ratingView.findViewById(R.id.ratingBar_rating);

                ratingBar.setRating(rating);
                alert.setView(alert_ratingView);
                alert.create();
                alert.setPositiveButton("Enviar", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        AsyncTask<Void, Void, Void> ratingTask = new AsyncTask<Void, Void, Void>() {

                            boolean ok = true;

                            @Override
                            protected void onPreExecute() {
                                pd = new ProgressDialog(TalkDetailActivity.this);
                                pd.setTitle("Avaliando Palestra");
                                pd.setMessage("Carregando...");
                                pd.setCancelable(false);
                                pd.setIndeterminate(true);
                                pd.show();
                            }

                            @Override
                            protected Void doInBackground(Void... arg0) {
                                try {
                                    ParseObject rating = new ParseObject("Rating");
                                    ParseObject program = new ParseObject("Talks");
                                    ParseQuery<ParseObject> pqprogram = ParseQuery.getQuery("Talks");
                                    program = pqprogram.get(obj_prog.getId());

                                    rating.put("note", ratingBar.getRating());
                                    rating.put("user", ParseUser.getCurrentUser());
                                    rating.put("talk", program);
                                    rating.put("description", edtDescricao.getText().toString());
                                    rating.saveInBackground();

                                } catch (Exception e) {
                                    ok = false;
                                }

                                return null;
                            }

                            @Override
                            protected void onPostExecute(Void result) {
                                if (pd != null) {

                                    if (ok) {
                                        Toast.makeText(TalkDetailActivity.this, "Avaliacao Efetuada \n" + "Nota: " + ratingBar.getRating(), Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(TalkDetailActivity.this, "Sem conexão com a internet, tente fazer a pergunta novamente mais tarde.", Toast.LENGTH_LONG).show();
                                    }

                                    pd.dismiss();
                                }
                            }
                        };

                        ratingTask.execute((Void[]) null);
                    }
                });
                alert.show();
            }
        });
    }

    private void return_speaker_loading() {
        Speaker speaker = new Speaker("0","Carregando...","carregando Palestrante");
        List<Speaker> list_speaker = new ArrayList<Speaker>();
        list_speaker.add(speaker);
        listDataPalestrante.put("Palestrante", list_speaker);
    }

    private void return_question_loading() {
        Date date = new Date(System.currentTimeMillis());
        Question question = new Question("0","Carregando...","carregando pergunta",date);
        List<Question> list_questions = new ArrayList<Question>();
        list_questions.add(question);
        listDataQuestion.put("Perguntas", list_questions);
    }

    private void return_no_speaker() {
        Speaker speaker = new Speaker("0","Palestrante à definir","");
        List<Speaker> list_speaker = new ArrayList<Speaker>();
        list_speaker.add(speaker);
        listDataPalestrante.put("Palestrante", list_speaker);
    }

    private void return_no_question() {
        Date date = new Date(System.currentTimeMillis());
        Question question = new Question("0","Sem Perguntas até o momento","Verificacao às",date);
        List<Question> list_quastion = new ArrayList<Question>();
        list_quastion.add(question);
        listDataQuestion.put("Perguntas", list_quastion);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_talk_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == android.R.id.home) {
            finish();
            return true;
        }

        if(id == R.id.action_refresh) {
            loaData();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

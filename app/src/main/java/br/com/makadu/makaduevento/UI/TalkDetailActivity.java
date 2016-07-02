package br.com.makadu.makaduevento.UI;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import br.com.makadu.makaduevento.DAO.dao.entityDao.EventDao;
import br.com.makadu.makaduevento.DAO.dao.entityDao.TalkDao;
import br.com.makadu.makaduevento.R;
import br.com.makadu.makaduevento.Util.Email;
import br.com.makadu.makaduevento.Util.SessionManager;
import br.com.makadu.makaduevento.Util.Util;
import br.com.makadu.makaduevento.Util.Question_talk;
import br.com.makadu.makaduevento.adapters.TalkDetailExpandableAdapter;
import br.com.makadu.makaduevento.model.RequestJson;
import br.com.makadu.makaduevento.model.RequestRating;
import br.com.makadu.makaduevento.model.ResponseJson;
import br.com.makadu.makaduevento.model.Resposta;
import br.com.makadu.makaduevento.model.Speaker;
import br.com.makadu.makaduevento.model.Talk;
import br.com.makadu.makaduevento.model.Question;
import br.com.makadu.makaduevento.model.TalkSpeaker;
import br.com.makadu.makaduevento.servicesRetrofit.returnAPI.GetRestAdapter;
import br.com.makadu.makaduevento.servicesRetrofit.returnAPI.PostRestAdapter;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class TalkDetailActivity extends ActionBarActivity {

    static Talk obj_prog;
    static String event_id;
    ArrayList<Speaker> list_palestrantes = new ArrayList<Speaker>();
    ListView listviewPalestrante;

    private SessionManager session;

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
        super.onResume();
        Localytics.openSession();
        Localytics.tagScreen("Detalhe Programacao");
        Localytics.upload();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_talk_detail);

        session = new SessionManager(this);

        util = new Util(getBaseContext());
        progress = (ProgressBar) findViewById(R.id.progressBar_talk_detail);
        obj_prog = (Talk)getIntent().getSerializableExtra("id");
        event_id = getIntent().getStringExtra("event_id");

        // verificar para aprimorar
        Localytics.openSession();
        Localytics.tagEvent(obj_prog.getTitle());
        Localytics.upload();

        LinearLayout bt_download = (LinearLayout)findViewById(R.id.btn_detalhe_pro_Download);

        if (!obj_prog.isAllow_file()) {
            bt_download.setVisibility(LinearLayout.GONE);
        } else {
            bt_download.setVisibility(LinearLayout.VISIBLE);
        }

        LinearLayout bt_question = (LinearLayout)findViewById(R.id.btn_detalhe_pro_Pergunta);

        if (!obj_prog.isAllow_question()) {
            bt_question.setVisibility(LinearLayout.GONE);
        } else {
            bt_question.setVisibility(LinearLayout.VISIBLE);
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

        listDataProg.put(getString(R.string.about_activity), listTalk);

        return_question_loading();
        return_speaker_loading();

        loaData();

        // GROUP
        listGroup.add(getString(R.string.about_activity));
        listGroup.add(getString(R.string.about_Speaker));
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
                    new Email().sendEmailConteudoProgramacao(v.getContext(), obj_prog,event_id);
                } catch (Exception e) {
                    Toast.makeText(v.getContext(), "Ocorreu algum erro no DOWNLOAD!!!", Toast.LENGTH_LONG).show();
                }
            }
        });

        LinearLayout btn_Pergunta = (LinearLayout)findViewById(R.id.btn_detalhe_pro_Pergunta);
        btn_Pergunta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Question_talk().question(v.getContext(), obj_prog.getId(), event_id, session.returnUsername());
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

                try {
                    ArrayList<Speaker> speakers = (ArrayList<Speaker>) loaSpeakers();

                    if (!speakers.isEmpty()) {
                        listDataPalestrante.put(getString(R.string.about_Speaker), speakers);
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

            @Override
            protected void onPreExecute() {
                progress.setVisibility(View.VISIBLE);
            }

            @Override
            protected Void doInBackground(Void... arg0) {
                ArrayList<Question> questions = new ArrayList<Question>();

                try {
                    questions = (ArrayList<Question>) new GetRestAdapter().returnQuestion(event_id,obj_prog.getId());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if(questions != null) {

                    if(!questions.isEmpty()) {
                    listDataQuestion.put("Perguntas", questions);
                    } else {
                        return_no_question();
                    }
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

    private List<Speaker> loaSpeakers() throws IOException {

        TalkDao tDAO = new TalkDao(getApplication().getApplicationContext());

        List<Speaker> listSpeakers = new ArrayList<Speaker>();

        if(util.isConnected()) {
            listSpeakers = (ArrayList<Speaker>) new GetRestAdapter().returnTalk(event_id,obj_prog.getId());
            try {
                //tDAO.upsert(list_talk,id);
            } catch (Exception ex) {
                Log.e("erro", ex.getMessage());
            }
        }
        else
        {
            //list_talk = (ArrayList<Talk>) tDAO.getListTalkForEventIdTAB_TALK(Long.parseLong(id));
        }

        return listSpeakers;
    }

    private void addFavoriteEvent(final Context c) {

        try {
            Call<ResponseJson> call = new PostRestAdapter().newEventFavorite(session.returnUserId(), event_id);

            call.enqueue(new Callback<ResponseJson>() {
                @Override
                public void onResponse(Response<ResponseJson> response, Retrofit retrofit) {

                    Log.d("MAKADU", response.message());
                    SharedPreferences sp = c.getSharedPreferences("MyPreferencesFavoriteEvents", Context.MODE_PRIVATE);
                    Set<String> favorites = sp.getStringSet("favorite_events", new HashSet<String>());

                    if (!favorites.contains(event_id)) {
                        favorites.add(event_id);
                    }

                    sp.edit().putStringSet("favorite_events", favorites).apply();

                    AlertDialog.Builder alert = new AlertDialog.Builder(c);
                    alert.setTitle("Evento Adicionado com Sucesso. \n");
                    alert.setNeutralButton("OK", null);
                    alert.show();
                }

                @Override
                public void onFailure(Throwable t) {

                }
            });

        } catch (IOException e) {
            AlertDialog.Builder alert = new AlertDialog.Builder(c);
            alert.setTitle("Ocorreu algum problema em Adicionar o Evento. \n");
            alert.setNeutralButton("OK", null);
            alert.show();
            e.printStackTrace();
        }
    }

    private void loadRating() {
        RatingBar rating = (RatingBar)findViewById(R.id.rating_inicial);

        rating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {

            @Override
            public void onRatingChanged(RatingBar ratingBar_on, final float rating, boolean fromUser) {

                if (new EventDao(getApplication().getApplicationContext()).eventPrivate(event_id)) {
                    if (new Util(getApplication().getApplicationContext()).isConnected()) {

                        boolean isFavorite = false;
                        Call<Resposta> responseCall = null;
                        try {
                            responseCall = new GetRestAdapter().isFavoriteEvent(session.returnUserId(), event_id);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        responseCall.enqueue(new Callback<Resposta>() {

                            @Override
                            public void onResponse(Response<Resposta> response, Retrofit retrofit) {
                                Log.v("LOG Email", response.message());
                                if (response.body().resposta == true) {

                                    LayoutInflater inflater = LayoutInflater.from(TalkDetailActivity.this);
                                    AlertDialog.Builder alert = new AlertDialog.Builder(TalkDetailActivity.this);
                                    alert.setTitle(null);
                                    View alert_ratingView = inflater.inflate(R.layout.alert_rating, null, false);

                                    TextView txtAvaliacao = (TextView) alert_ratingView.findViewById(R.id.txt_avalie_essa_palestra);
                                    //txtAvaliacao.setText("Avaliacão de " + ParseUser.getCurrentUser().get("full_name"));

                                    final EditText edtDescricao = (EditText) alert_ratingView.findViewById(R.id.edt_Descricao_rating);

                                    final RatingBar ratingBar = (RatingBar) alert_ratingView.findViewById(R.id.ratingBar_rating);

                                    Log.v("LOGRATING", "RAT ID: " + rating);

                                    ratingBar.setRating(rating);
                                    alert.setView(alert_ratingView);
                                    alert.create();
                                    alert.setPositiveButton("Enviar", new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            final int rating = (int)ratingBar.getRating();
                                            final String descricaoRating = edtDescricao.getText().toString();

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

                                                        Log.v("LOGRATING", " rating: " +rating);

                                                        RequestRating requestRate = new RequestRating(session.returnUsername(), rating, descricaoRating);

                                                        Log.v("LOGRATING", "EVENT ID: " + event_id + " TALK ID: " + obj_prog.getId() + " USERNAME: " + requestRate.username + " rating: " + requestRate.value + " descricao: " + requestRate.commentary);

                                                        retrofit.Call<ResponseJson> json = null;
                                                        json = new PostRestAdapter().rateEvent(event_id, obj_prog.getId(), requestRate);

                                                        json.enqueue(new Callback<ResponseJson>() {
                                                            @Override
                                                            public void onResponse(Response<ResponseJson> response, Retrofit retrofit) {
                                                                ok = true;
                                                            }

                                                            @Override
                                                            public void onFailure(Throwable t) {
                                                                Log.e("LOGRATING", t.getLocalizedMessage());
                                                                ok = false;
                                                            }
                                                        });

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

                                } else {
                                    AlertDialog.Builder b = new AlertDialog.Builder(TalkDetailActivity.this);
                                    b.setTitle("Coloque a senha");
                                    final EditText input = new EditText(TalkDetailActivity.this);
                                    b.setView(input);
                                    b.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int whichButton) {
                                            String inputedPassword = input.getText().toString();

                                            if (new EventDao(TalkDetailActivity.this).eventPassword(event_id).contentEquals(inputedPassword)) {
                                                addFavoriteEvent(TalkDetailActivity.this);
                                            } else {
                                                AlertDialog.Builder al = new AlertDialog.Builder(TalkDetailActivity.this);
                                                al.setMessage("Digite a senha do evento. Caso não tenha, procure a organização do evento");
                                                al.setNeutralButton("OK", null);
                                                al.show();
                                            }
                                        }
                                    });
                                    b.setNegativeButton("Cancelar", null);
                                    b.create().show();
                                }
                            }

                            @Override
                            public void onFailure(Throwable t) {

                            }
                        });
                    }
                } else {

                    LayoutInflater inflater = LayoutInflater.from(TalkDetailActivity.this);
                    AlertDialog.Builder alert = new AlertDialog.Builder(TalkDetailActivity.this);
                    alert.setTitle(null);
                    View alert_ratingView = inflater.inflate(R.layout.alert_rating, null, false);

                    TextView txtAvaliacao = (TextView) alert_ratingView.findViewById(R.id.txt_avalie_essa_palestra);
                    //txtAvaliacao.setText("Avaliacão de " + ParseUser.getCurrentUser().get("full_name"));

                    final EditText edtDescricao = (EditText) alert_ratingView.findViewById(R.id.edt_Descricao_rating);

                    final RatingBar ratingBar = (RatingBar) alert_ratingView.findViewById(R.id.ratingBar_rating);

                    ratingBar.setRating(rating);
                    alert.setView(alert_ratingView);
                    alert.create();
                    alert.setPositiveButton("Enviar", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            final int rating = (int)ratingBar.getRating();
                            final String descricaoRating = edtDescricao.getText().toString();

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

                                        RequestRating requestRate = new RequestRating(session.returnUsername(), rating, descricaoRating);

                                        Log.v("LOGRATING", "EVENT ID: " + event_id + " TALK ID: " + obj_prog.getId() + " USERNAME: " + requestRate.username + " rating: " + requestRate.value + " descricao: " + requestRate.commentary);

                                        retrofit.Call<ResponseJson> json = null;
                                        json = new PostRestAdapter().rateEvent(event_id, obj_prog.getId(), requestRate);

                                        json.enqueue(new Callback<ResponseJson>() {
                                            @Override
                                            public void onResponse(Response<ResponseJson> response, Retrofit retrofit) {
                                                ok = true;
                                            }

                                            @Override
                                            public void onFailure(Throwable t) {
                                                Log.e("LOGRATING", t.getLocalizedMessage());
                                                ok = false;
                                            }
                                        });

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
            }
        });

    }

    private void return_speaker_loading() {
        Speaker speaker = new Speaker("0","Carregando...","carregando Palestrante");
        List<Speaker> list_speaker = new ArrayList<Speaker>();
        list_speaker.add(speaker);
        listDataPalestrante.put(getString(R.string.about_Speaker), list_speaker);
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
        listDataPalestrante.put(getString(R.string.about_Speaker), list_speaker);
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

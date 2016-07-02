package br.com.makadu.makaduevento.UI;

import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.OrientationListener;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.concurrent.atomic.AtomicInteger;

import br.com.makadu.makaduevento.R;
import br.com.makadu.makaduevento.Util.SessionManager;
import br.com.makadu.makaduevento.Util.Util;
import br.com.makadu.makaduevento.adapters.EvaluationAdapter;
import br.com.makadu.makaduevento.model.Evaluation;
import br.com.makadu.makaduevento.model.FeedbackGet;
import br.com.makadu.makaduevento.model.FeedbackPost;
import br.com.makadu.makaduevento.model.Interactive;
import br.com.makadu.makaduevento.model.ResponseJson;
import br.com.makadu.makaduevento.servicesRetrofit.returnAPI.GetRestAdapter;
import br.com.makadu.makaduevento.servicesRetrofit.returnAPI.PostRestAdapter;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class EvaluationActivity extends Activity {

    LinearLayout linear_list;
    private EvaluationAdapter mAdapter;
    ProgressBar progress;
    static String event_id;
    ArrayList<FeedbackGet> list_feedback;
    Button btn_send_evaluation;
    List<FeedbackPost> list_feedback_post;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluation);

        session = new SessionManager(this);

        event_id = getIntent().getStringExtra("event_id");

        list_feedback_post = new ArrayList<FeedbackPost>();

        progress = (ProgressBar) findViewById(R.id.progressBar_evaluation);
        linear_list = (LinearLayout) findViewById(R.id.Linear_evaluation);

        loadDataTalk();

        btn_send_evaluation = (Button)findViewById(R.id.btn_send_evaluation);

        btn_send_evaluation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncTask<Void, Void, Void> ratingTask = new AsyncTask<Void, Void, Void>() {

                    boolean ok = true;

                    @Override
                    protected void onPreExecute() {

                    }

                    @Override
                    protected Void doInBackground(Void... arg0) {
                        try {


                        } catch (Exception e) {
                            ok = false;
                        }

                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void result) {
                        if (ok) {

                            String teste = "";

                            try {

                                int count = 1;
                                for (FeedbackPost item : list_feedback_post) {

                                    int id_linear = new Util().Return_id_Linear(count);
                                    Log.v("LOG_ID", "linear port : " + id_linear);
                                    LinearLayout l = (LinearLayout) linear_list.findViewById(id_linear);

                                    RatingBar r = (RatingBar) l.findViewById(new Util().Return_id_rating_bar(count));
                                    EditText e = (EditText) l.findViewById(new Util().Return_id_edit_text(count));
                                    list_feedback_post.get(count - 1).value = ((int) r.getRating()) + "";
                                    list_feedback_post.get(count - 1).commentary = e.getText().toString();
                                    //teste += item.evaluation_question_id + " nota: "+ String.valueOf(r.getRating()) +  " texto: "+ e.getText() + "  \n";
                                    teste += " nota: " + String.valueOf(r.getRating()) + " texto: " + e.getText() + "  \n";
                                    count++;

                                }

                                Evaluation evaluation = new Evaluation(session.returnUserId(),list_feedback_post);

                                retrofit.Call<ResponseJson> json = null;
                                json = new PostRestAdapter().postEvaluations(event_id, evaluation);

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

                            }catch (Exception e) {
                                Log.e("LOGRATING", e.getMessage());
                                ok = false;
                            }

                            if(ok) {
                                Toast.makeText(EvaluationActivity.this, "Avaliacao Efetuada: \n" + teste , Toast.LENGTH_LONG).show();
                            }else {
                                Toast.makeText(EvaluationActivity.this, "Ops. Ocorreu algum erro. Tente mais tarde" , Toast.LENGTH_LONG).show();
                            }


                                } else {
                                    Toast.makeText(EvaluationActivity.this, "Sem conexão com a internet, tente fazer a pergunta novamente mais tarde.", Toast.LENGTH_LONG).show();
                                }

                    }
                };

                ratingTask.execute((Void[]) null);

            }
        });


    }

    public void loadDataTalk() {

        AsyncTask<Void, Void, ArrayList<FeedbackGet>> task = new AsyncTask<Void, Void, ArrayList<FeedbackGet>>() {

            @Override
            protected void onPreExecute() {
                progress.setVisibility(View.VISIBLE);
            }

            @Override
            protected ArrayList<FeedbackGet> doInBackground(Void... arg0) {

                try {
                    String id = event_id;

                    if(new Util(EvaluationActivity.this).isConnected()) {
                        list_feedback = (ArrayList<FeedbackGet>) new GetRestAdapter().returnEvaluations(id);
                    }
                    else
                    {
                        list_feedback = new ArrayList<FeedbackGet>();
                        Toast.makeText(EvaluationActivity.this,"Sem conexão com a Internet",Toast.LENGTH_LONG).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                return list_feedback;
            }

            @Override
            protected void onPostExecute(ArrayList<FeedbackGet> array_feedback) {

                int count = array_feedback.size();

                TextView t = (TextView)findViewById(R.id.no_evaluation);
                if(count == 0) {
                    t.setVisibility(View.VISIBLE);
                }else{
                    t.setVisibility(View.INVISIBLE);
                }

                for(int i = 1; i <= count; i++) {
                    LinearLayout l = (LinearLayout)findViewById(new Util().Return_id_Linear(i));
                    l.setVisibility(View.VISIBLE);
                }

                count = 1;

                for(FeedbackGet item : array_feedback) {

                    final AtomicInteger sNextGeneratedId = new AtomicInteger(1);

                    FeedbackPost post = new FeedbackPost(item.id);
                    list_feedback_post.add(post);

                    TextView tv_question_name = (TextView)findViewById(new Util().Return_id_text(count));
                    tv_question_name.setText(item.value);


                    int id_edit = new Util().Return_id_edit_text(count);

                    count++;
                }


                progress.setVisibility(View.INVISIBLE);

            }
        };
        task.execute((Void[]) null);
    }

}

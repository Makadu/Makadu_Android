package br.com.makadu.makaduevento.UI;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;

import br.com.makadu.makaduevento.R;
import br.com.makadu.makaduevento.Util.Util;
import br.com.makadu.makaduevento.model.Answers;
import br.com.makadu.makaduevento.model.Interactive;
import br.com.makadu.makaduevento.servicesRetrofit.returnAPI.GetRestAdapter;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class BeforeVoteActivity extends Activity {

    String talk_id;
    Button btn_vote;
    Button btn_close;
    Interactive interactive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_before_vote);

        talk_id = (String)getIntent().getStringExtra("id");

        btn_close = (Button)findViewById(R.id.btn_close_before_vote);
        btn_vote = (Button)findViewById(R.id.btn_vote_before);

        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_vote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Call<Interactive> responseCall = null;
                    try {
                        responseCall = (Call<Interactive>) new GetRestAdapter().returnInteractive(talk_id);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    responseCall.enqueue(new Callback<Interactive>() {

                        @Override
                        public void onResponse(Response<Interactive> response, Retrofit retrofit) {
                            interactive = response.body();

                            if (new Util(BeforeVoteActivity.this).isNullOrEmpty(interactive.resposta)) {

                                Log.v("LOGMAK", "QUESTION: " + interactive.question_interactive);
                                for (Answers a : interactive.answers) {
                                    Log.v("LOGMAK", "perguntas : " + a.answer);
                                }
                                Intent i = new Intent(BeforeVoteActivity.this, InteractiveVoting.class);
                                i.putExtra("obj_interactive", interactive);
                                startActivity(i);
                            } else {
                                AlertDialog.Builder alert = new AlertDialog.Builder(BeforeVoteActivity.this);
                                alert.setMessage("A votação não está aberta.");
                                alert.setNeutralButton("OK", null);
                                alert.show();
                            }
                        }

                        @Override
                        public void onFailure(Throwable t) {
                            Toast.makeText(BeforeVoteActivity.this,"Ocorreu algum erro",Toast.LENGTH_SHORT);
                        }
                    });

            }
        });
    }

}

package br.com.makadu.makaduevento.UI;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.app.Activity;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import br.com.makadu.makaduevento.R;
import br.com.makadu.makaduevento.Util.SessionManager;
import br.com.makadu.makaduevento.Util.Util;
import br.com.makadu.makaduevento.model.Answers;
import br.com.makadu.makaduevento.model.Interactive;
import br.com.makadu.makaduevento.model.RequestJson;
import br.com.makadu.makaduevento.model.ResponseJson;
import br.com.makadu.makaduevento.servicesRetrofit.returnAPI.PostRestAdapter;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class InteractiveVoting extends Activity {

    Interactive obj_interactive;
    TextView question;
    RadioGroup radioGroupInteractive;
    private MyCountDowTimer timer;
    Button btn_votar;
    public SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interactive_voting);

        session = new SessionManager(this);

        obj_interactive = (Interactive)getIntent().getSerializableExtra("obj_interactive");

        //long segundos = new Util().diferencaSegundos(obj_interactive.getEnd_Date());

        Date datainicial = new Date();

        Log.v("GMAK1","end_time: " + obj_interactive.end_time);
        Log.v("GMAK1","data current: " + obj_interactive.current_time);

        Date dataFim = null;
        Date dataCurrent = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        SimpleDateFormat sdfcurrent = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        try {
            dataFim = sdf.parse(obj_interactive.end_time);
            dataCurrent = sdfcurrent.parse(obj_interactive.current_time);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Log.v("GMAK1","data final: " + dataFim);
        Log.v("GMAK1","data current: " + dataCurrent);

        long segundos = (dataFim.getTime() - dataCurrent.getTime()) / 1000;

        Log.v("GMAK1","segundos: " +segundos);

        TextView txt_timer = (TextView) findViewById(R.id.chronometer_interactive);
        timer = new MyCountDowTimer(this,txt_timer,segundos*1000,1000);
        timer.start();

        question = (TextView)findViewById(R.id.txt_question_interactive);
        radioGroupInteractive = (RadioGroup)findViewById(R.id.radioGroupInterativa);

        question.setText(obj_interactive.question_interactive);

        for (Answers answers : obj_interactive.answers) {
            RadioButton radioButton = new RadioButton(InteractiveVoting.this);
            radioButton.setText(answers.answer);
            radioButton.setId(answers.id);
            radioGroupInteractive.addView(radioButton);
         }

        btn_votar = (Button)findViewById(R.id.btn_vota_interativa);

        btn_votar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (radioGroupInteractive.getCheckedRadioButtonId() != -1) {

                    RequestJson request = new RequestJson();
                    request.user_id = session.returnUserId();
                    request.answer_id = radioGroupInteractive.getCheckedRadioButtonId() + "";

                    retrofit.Call<ResponseJson> json = null;
                    try {
                        json = new PostRestAdapter().addQuestionInt(obj_interactive.talk_id + "", obj_interactive.id + "", request);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    json.enqueue(new Callback<ResponseJson>() {
                        @Override
                        public void onResponse(Response<ResponseJson> response, Retrofit retrofit) {

                            AlertDialog.Builder alert = new AlertDialog.Builder(InteractiveVoting.this);
                            alert.setMessage("Voto efetuado com Sucesso!");
                            alert.setNeutralButton("OK", null);
                            alert.show();
                        }

                        @Override
                        public void onFailure(Throwable t) {
                            Log.e("MAKADU", t.getLocalizedMessage());

                            Toast.makeText(InteractiveVoting.this, "Problema ao enviar a pergunta", Toast.LENGTH_LONG).show();
                        }
                    });

                } else {
                    AlertDialog.Builder alert = new AlertDialog.Builder(InteractiveVoting.this);
                    alert.setMessage("Escolha pelo menos uma alternativa!");
                    alert.setNeutralButton("Selecionar", null);
                    alert.show();
                }
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(timer != null)
            timer.onFinish();
    }

    private class MyCountDowTimer extends CountDownTimer {

        private Context context;
        private TextView tv;
        private long timeInFuture;
        private long interval;

        public MyCountDowTimer(Context context, TextView tv, long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
            this.context = context;
            this.tv = tv;
        }

        @Override
        public void onTick(long millisUntilFinished) {
            timeInFuture = millisUntilFinished;
            tv.setText(getCorrectTimer(true,millisUntilFinished) + ":" + getCorrectTimer(false,millisUntilFinished));
        }

        @Override
        public void onFinish() {
            timeInFuture -= 1000;
            tv.setText(getCorrectTimer(true, timeInFuture) + ":" + getCorrectTimer(false, timeInFuture));

            InteractiveVoting.this.finish();

            Toast.makeText(context, "Tempo Finalizado", Toast.LENGTH_LONG).show();
        }

        private String getCorrectTimer(boolean isMinute,long millisUntilFinished) {
            String aux;
            int constCalendar = isMinute ? Calendar.MINUTE : Calendar.SECOND;
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(millisUntilFinished);

            aux = c.get(constCalendar) < 10 ? "0" + c.get(constCalendar) : "" +c.get(constCalendar);

            return aux;
        }
    }


}

package br.com.makadu.makaduevento.Util;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;



import br.com.makadu.makaduevento.model.Programacao;

/**
 * Created by lucasschwalbeferreira on 27/03/15.
 */
public class Email {

    ProgressDialog pd;

    private boolean agendamento(Programacao talk, Context c) throws IOException{

        boolean ok = true;

        ParseQuery<ParseObject> pqprogram = ParseQuery.getQuery("Talks");

        ParseObject program = null;
        try {
        program = pqprogram.get (talk.getId());

        }catch (Exception e){
            ok = false;
            throw new IOException("erro http");

        }
        ParseObject schedule = new ParseObject("Schedule");
        schedule.put("talk", program);
        schedule.put("user", ParseUser.getCurrentUser());
        try {
            schedule.saveInBackground();
        }catch (Exception e)
        {
            ok = false;
            throw new IOException("erro http");
        }

        return ok;
    }

    public void sendEmailConteudoProgramacao(final Context c, final Programacao talk) {

        final String TO = ParseUser.getCurrentUser().getEmail().toString();

        Map<String, String> params = new HashMap<String, String>();

        params.put("toEmail", TO);
        params.put("fromEmail", "no-reply@makadu.net");

        String text = "Material da palestra - " + talk.getTitulo() + " \n Clique no link abaixo para realizar o download do material \n\n" + talk.getUrl();
        params.put("text", text);
        params.put("subject", "Material da palestra - " + talk.getTitulo());

        Log.d("talk", talk.getUrl() + "");

        if (talk.getUrl().startsWith("http")) {
            ParseCloud.callFunctionInBackground("sendMail", params, new FunctionCallback<String>() {
                public void done(String result, ParseException e) {

                    if (e == null) {
                        AlertDialog.Builder alert = new AlertDialog.Builder(c);
                        alert.setMessage("O material foi enviado para o email " + TO + " com sucesso!");
                        alert.setNeutralButton("OK", null);
                        alert.show();
                        //Toast.makeText(c, "O material da palestra foi enviado para o e-mail com sucesso! Confira seu Email", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(c, "Sem conexão com a internet, tente novamente mais tarde.", Toast.LENGTH_LONG).show();
                    }
                }
            });


        }
        else {
                AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {

                    boolean ok = true;

                    @Override
                    protected void onPreExecute() {

                        pd = new ProgressDialog(c);
                        pd.setTitle("Agendando Download");
                        pd.setMessage("Carregando...");
                        pd.setCancelable(false);
                        pd.setIndeterminate(true);
                        pd.show();
                    }

                    @Override
                    protected Void doInBackground(Void... arg0) {

                        try {
                            if (!agendamento(talk, c)) {
                                ok = false;
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            ok = false;
                        }


                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void result) {
                        if (pd != null) {
                            if(ok) {
                                AlertDialog.Builder alert = new AlertDialog.Builder(c);
                                alert.setMessage("O material será enviado para " + TO + " quando disponível.");
                                alert.setNeutralButton("OK", null);
                                alert.show();
                            }
                            else{
                                Toast.makeText(c,"Sem conexão com a internet, tente novamente mais tarde.", Toast.LENGTH_LONG).show();
                            }
                            pd.dismiss();
                        }
                    }

                };
                task.execute((Void[]) null);

            }
            }

}

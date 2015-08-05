package br.com.makadu.makaduevento.Util;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import br.com.makadu.makaduevento.R;

/**
 * Created by lucasschwalbeferreira on 16/07/15.
 */
public class Question_talk {

    private ProgressDialog pd;

    public void question(final Context c, final String id_talk) {
        AlertDialog.Builder alert = new AlertDialog.Builder(c);
        final EditText box = new EditText(c);
        box.setSingleLine(false);
        box.setLines(3);
        box.setPadding(5,10,5,10);

        alert.setTitle("Envie uma pergunta!");
        alert.setView(box);
        alert.setIcon(R.drawable.makadu_login);
        alert.setPositiveButton("Enviar", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {

                            boolean ok = true;
                            boolean white = false;

                            @Override
                            protected void onPreExecute() {
                                pd = new ProgressDialog(c);
                                pd.setTitle("Enviando pergunta");
                                pd.setMessage("Carregando...");
                                pd.setCancelable(false);
                                pd.setIndeterminate(true);
                                pd.show();
                            }

                            @Override
                            protected Void doInBackground(Void... arg0) {

                                if(!(box.getText().toString().isEmpty())) {

                                    try {
                                        ParseQuery<ParseObject> pqprogram = ParseQuery.getQuery("Talks");
                                        ParseObject program = new ParseObject("Talks");
                                        program = pqprogram.get(id_talk);

                                        ParseObject pergunta = new ParseObject("Questions");
                                        pergunta.put("active", true);
                                        pergunta.put("question", box.getText().toString());
                                        pergunta.put("questioning", ParseUser.getCurrentUser());
                                        pergunta.put("talk", program);
                                        pergunta.saveInBackground();

                                    } catch (Exception e) {
                                        ok = false;
                                    }
                                }
                                else {
                                    white = true;
                                }

                                return null;
                            }

                            @Override
                            protected void onPostExecute(Void result) {
                                if (pd != null) {
                                    if(!white) {
                                        if (ok) {
                                            Toast.makeText(c, R.string.pergunta_enviada_sucesso, Toast.LENGTH_LONG).show();
                                        } else {
                                            Toast.makeText(c, "Sem conexão com a internet, tente fazer a pergunta novamente mais tarde.", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                    else {
                                        Toast.makeText(c, "O campo está em branco. Faça sua pergunta.", Toast.LENGTH_LONG).show();
                                    }
                                    pd.dismiss();
                                }
                            }
                        };

                        task.execute((Void[]) null);
                    }
                }
        );
        alert.setNegativeButton("cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alert.show();
    }

}

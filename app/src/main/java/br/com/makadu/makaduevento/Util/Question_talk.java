package br.com.makadu.makaduevento.Util;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import br.com.makadu.makaduevento.DAO.dao.entityDao.EventDao;
import br.com.makadu.makaduevento.R;
import br.com.makadu.makaduevento.model.RequestJson;
import br.com.makadu.makaduevento.model.ResponseJson;
import br.com.makadu.makaduevento.model.Resposta;
import br.com.makadu.makaduevento.servicesRetrofit.returnAPI.GetRestAdapter;
import br.com.makadu.makaduevento.servicesRetrofit.returnAPI.PostRestAdapter;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by lucasschwalbeferreira on 16/07/15.
 */
public class Question_talk {

    private String event_id_geral;
    public SessionManager session;
    private ProgressDialog pd;

    public void question(final Context c, final String id_talk, final String eventId, final String username) {

        event_id_geral = eventId;

        session = new SessionManager(c);

        if(new EventDao(c).eventPrivate(eventId)) {

            if (new Util(c).isConnected()) {

                boolean isFavorite = false;
                Call<Resposta> responseCall = null;
                try {
                    responseCall = new GetRestAdapter().isFavoriteEvent(session.returnUserId(), event_id_geral);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                responseCall.enqueue(new Callback<Resposta>() {

                    @Override
                    public void onResponse(Response<Resposta> response, Retrofit retrofit) {
                        Log.v("LOG Email", response.message());
                        if (response.body().resposta == true) {
                            AlertDialog.Builder alert = new AlertDialog.Builder(c);
                            final EditText box = new EditText(c);
                            box.setSingleLine(false);
                            box.setLines(3);
                            box.setPadding(5, 10, 5, 10);

                            alert.setTitle(R.string.envie_perg);
                            alert.setView(box);
                            alert.setIcon(R.drawable.makadu_login);

                            alert.setPositiveButton(R.string.submit, new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            final String question = box.getText().toString();
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

                                                    if (!(question.isEmpty())) {

                                                        try {

                                                        } catch (Exception e) {
                                                            ok = false;
                                                        }
                                                    } else {
                                                        white = true;
                                                    }

                                                    return null;
                                                }

                                                @Override
                                                protected void onPostExecute(Void result) {
                                                    if (pd != null) {
                                                        if (!white) {
                                                            if (ok) {

                                                                RequestJson request = new RequestJson(username, question);

                                                                retrofit.Call<ResponseJson> json = null;
                                                                try {
                                                                    json = new PostRestAdapter().addQuestion(eventId, id_talk, request);
                                                                } catch (IOException e) {
                                                                    e.printStackTrace();
                                                                }

                                                                json.enqueue(new Callback<ResponseJson>() {
                                                                    @Override
                                                                    public void onResponse(Response<ResponseJson> response, Retrofit retrofit) {
                                                                        Toast.makeText(c, R.string.pergunta_enviada_sucesso, Toast.LENGTH_LONG).show();
                                                                    }

                                                                    @Override
                                                                    public void onFailure(Throwable t) {
                                                                        Log.e("MAKADU", t.getLocalizedMessage());

                                                                        Toast.makeText(c, "Problema ao enviar a pergunta", Toast.LENGTH_LONG).show();
                                                                    }
                                                                });

                                                            } else {
                                                                Toast.makeText(c, "Sem conexão com a internet, tente fazer a pergunta novamente mais tarde.", Toast.LENGTH_LONG).show();
                                                            }
                                                        } else {
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

                        } else {
                            AlertDialog.Builder b = new AlertDialog.Builder(c);
                            b.setTitle("Coloque a senha");
                            final EditText input = new EditText(c);
                            b.setView(input);
                            b.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    String inputedPassword = input.getText().toString();

                                    if(new EventDao(c).eventPassword(event_id_geral).contentEquals(inputedPassword)){
                                        addFavoriteEvent(c);
                                    }
                                    else
                                    {
                                        AlertDialog.Builder al = new AlertDialog.Builder(c);
                                        al.setMessage(R.string.password_to);
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
        }
        else {

            AlertDialog.Builder alert = new AlertDialog.Builder(c);
            final EditText box = new EditText(c);
            box.setSingleLine(false);
            box.setLines(3);
            box.setPadding(5, 10, 5, 10);

            alert.setTitle(R.string.send_question);
            alert.setView(box);
            alert.setIcon(R.drawable.makadu_login);

            alert.setPositiveButton("Enviar", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            final String question = box.getText().toString();
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

                                    if (!(question.isEmpty())) {

                                        try {

                                        } catch (Exception e) {
                                            ok = false;
                                        }
                                    } else {
                                        white = true;
                                    }

                                    return null;
                                }

                                @Override
                                protected void onPostExecute(Void result) {
                                    if (pd != null) {
                                        if (!white) {
                                            if (ok) {

                                                RequestJson request = new RequestJson(username, question);

                                                retrofit.Call<ResponseJson> json = null;
                                                try {
                                                    json = new PostRestAdapter().addQuestion(eventId, id_talk, request);
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }

                                                json.enqueue(new Callback<ResponseJson>() {
                                                    @Override
                                                    public void onResponse(Response<ResponseJson> response, Retrofit retrofit) {
                                                        Toast.makeText(c, R.string.pergunta_enviada_sucesso, Toast.LENGTH_LONG).show();
                                                    }

                                                    @Override
                                                    public void onFailure(Throwable t) {
                                                        Log.e("MAKADU", t.getLocalizedMessage());

                                                        Toast.makeText(c, "Problema ao enviar a pergunta", Toast.LENGTH_LONG).show();
                                                    }
                                                });

                                            } else {
                                                Toast.makeText(c, "Sem conexão com a internet, tente fazer a pergunta novamente mais tarde.", Toast.LENGTH_LONG).show();
                                            }
                                        } else {
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

    private void addFavoriteEvent(final Context c) {

        try {
            Call<ResponseJson> call = new PostRestAdapter().newEventFavorite(session.returnUserId(), event_id_geral);

            call.enqueue(new Callback<ResponseJson>() {
                @Override
                public void onResponse(Response<ResponseJson> response, Retrofit retrofit) {

                    Log.d("MAKADU", response.message());
                    SharedPreferences sp = c.getSharedPreferences("MyPreferencesFavoriteEvents", Context.MODE_PRIVATE);
                    Set<String> favorites = sp.getStringSet("favorite_events", new HashSet<String>());

                    if (!favorites.contains(event_id_geral)) {
                        favorites.add(event_id_geral);
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

}

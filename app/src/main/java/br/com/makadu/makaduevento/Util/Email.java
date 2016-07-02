package br.com.makadu.makaduevento.Util;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


import br.com.makadu.makaduevento.DAO.dao.entityDao.EventDao;
import br.com.makadu.makaduevento.DAO.dao.entityDao.TalkDao;
import br.com.makadu.makaduevento.R;
import br.com.makadu.makaduevento.model.RequestJson;
import br.com.makadu.makaduevento.model.ResponseJson;
import br.com.makadu.makaduevento.model.Resposta;
import br.com.makadu.makaduevento.model.Talk;
import br.com.makadu.makaduevento.servicesRetrofit.returnAPI.GetRestAdapter;
import br.com.makadu.makaduevento.servicesRetrofit.returnAPI.PostRestAdapter;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by lucasschwalbeferreira on 27/03/15.
 */
public class Email {

    public String event_id_geral;
    public SessionManager session;
    ProgressDialog pd;
    private boolean agendamento(Talk talk, Context c) throws IOException {

        SessionManager session = new SessionManager(c);
        boolean ok = true;
        return ok;
    }

    public void sendEmailConteudoProgramacao(final Context c, final Talk talk,final String event_id) {

        event_id_geral = event_id;

        session = new SessionManager(c);

        if(new EventDao(c).eventPrivate(event_id)) {

            if (new Util(c).isConnected()) {

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
                            try {
                                final String username = session.returnUsername();
                                //final String event_id = new TalkDao(c).returnEventId(Long.parseLong(talk.getId()));

                                Log.d("LOG Email", "Talk - > " + talk.getId() + "  username: " + session.returnUsername() + "  user_id: " + session.returnUserId() + " event_id: " + event_id);
                                RequestJson r = new RequestJson(session.returnUsername());
                                // capturar o id do evento
                                Call<ResponseJson> responseCall = new PostRestAdapter().newDownload(event_id, talk.getId(), r);
                                responseCall.enqueue(new Callback<ResponseJson>() {

                                    @Override
                                    public void onResponse(Response<ResponseJson> response, Retrofit retrofit) {
                                        Log.v("LOG Email", response.message());
                                        if (response.body().result.equalsIgnoreCase("link enviado")) {
                                            Toast.makeText(c, "Email enviado com sucesso.", Toast.LENGTH_LONG).show();
                                        } else if (response.body().result.equalsIgnoreCase("download agendado")) {
                                            Toast.makeText(c, "O material será enviado para " + username + " quando disponível.", Toast.LENGTH_LONG).show();
                                        } else {
                                            Toast.makeText(c, "Ocorreu algum erro: " + response.body().result, Toast.LENGTH_LONG).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Throwable t) {
                                        Toast.makeText(c, "Tivemos alguma dificuldade em enviar o email.", Toast.LENGTH_LONG).show();
                                    }
                                });
                            } catch (Exception e) {
                                Toast.makeText(c, "Tivemos alguma dificuldade em enviar o email.", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            AlertDialog.Builder b = new AlertDialog.Builder(c);
                            b.setTitle("Coloque a senha");
                            final EditText input = new EditText(c);
                            b.setView(input);
                            b.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int whichButton) {
                                   String inputedPassword = input.getText().toString();

                                    if(new EventDao(c).eventPassword(event_id).contentEquals(inputedPassword)){
                                        addFavoriteEvent(c);
                                    }
                                    else
                                    {
                                        AlertDialog.Builder al = new AlertDialog.Builder(c);
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
        }
        else {
            try {

                final String username = session.returnUsername();
                //final String event_id = new TalkDao(c).returnEventId(Long.parseLong(talk.getId()));

                Log.d("LOG Email", "Talk - > " + talk.getId() + "  username: " + session.returnUsername() + "  user_id: " + session.returnUserId() + " event_id: " + event_id);
                RequestJson r = new RequestJson(session.returnUsername());
                // capturar o id do evento
                Call<ResponseJson> responseCall = new PostRestAdapter().newDownload(event_id, talk.getId(), r);
                responseCall.enqueue(new Callback<ResponseJson>() {

                    @Override
                    public void onResponse(Response<ResponseJson> response, Retrofit retrofit) {
                        Log.v("LOG Email", response.message());
                        if (response.body().result.equalsIgnoreCase("link enviado")) {
                            Toast.makeText(c, "Email enviado com sucesso.", Toast.LENGTH_LONG).show();
                        } else if (response.body().result.equalsIgnoreCase("download agendado")) {
                            Toast.makeText(c, "O material será enviado para " + username + " quando disponível.", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(c, "Ocorreu algum erro: " + response.body().result, Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        Toast.makeText(c, "Tivemos alguma dificuldade em enviar o email.", Toast.LENGTH_LONG).show();
                    }
                });
            } catch (Exception e) {
                Toast.makeText(c, "Tivemos alguma dificuldade em enviar o email.", Toast.LENGTH_LONG).show();
            }
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

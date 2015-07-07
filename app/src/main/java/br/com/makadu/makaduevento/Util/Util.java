package br.com.makadu.makaduevento.Util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import br.com.makadu.makaduevento.R;

/**
 * Created by lucasschwalbeferreira on 09/04/15.
 */
public class Util {

    private Context ctx;
    private ProgressDialog pd;
    private final String webservice_url = "http://www.google.com";

    public Util(){}

    public Util(Context ctx){
        this.ctx = ctx;
    }

    public boolean isConnected() {

        boolean connected = false;

        final ConnectivityManager connMgr = (ConnectivityManager)ctx.getSystemService(Activity.CONNECTIVITY_SERVICE);
        final NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected() ) {
           connected = true;
        }
        Log.v("log_con", connected +"");
        return connected;

    }

    private boolean isReachable(final String server) {
        final boolean[] result = {true};
        new Thread() {
            public void run() {
                try {
                    HttpGet request = new HttpGet(server);
                    HttpParams httpParameters = new BasicHttpParams();
                    HttpConnectionParams.setConnectionTimeout(httpParameters, 4000);
                    HttpClient httpClient = new DefaultHttpClient(httpParameters);
                    HttpResponse response = httpClient.execute(request);
                    int status = response.getStatusLine().getStatusCode();
                    if (status == HttpStatus.SC_OK) {
                        result[0] = true;
                    }
                }catch (SocketTimeoutException e){
                    result[0] = false; // this is somewhat expected
                } catch (ClientProtocolException e) {
                    result[0] = false;
                } catch (IOException e) {
                    result[0] = false;
                }
            }
        }.start();
        return result[0];
    }

    private boolean IsReachable2() {
        boolean isReachable = false;

        try {
            URL url = new URL(webservice_url);
            HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
            urlc.setRequestProperty("User-Agent","Android Application");
            urlc.setRequestProperty("Connection","close");
            urlc.setConnectTimeout(30 * 1000);
            urlc.connect();
            isReachable = (urlc.getResponseCode() == 200);
        } catch (IOException e) {
            Log.e("erro_reachable",e.getMessage());
        }

        return isReachable;
    }

    public String getDate(Date sData) {
        Calendar data = new GregorianCalendar();
        SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");
        return f.format(sData);
    }

    public String getDateHour(Date sData) {
        Calendar data = new GregorianCalendar();
        SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy hh:mm");
        return f.format(sData);
    }

    public void talk(final Context c, final String id_talk) {
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

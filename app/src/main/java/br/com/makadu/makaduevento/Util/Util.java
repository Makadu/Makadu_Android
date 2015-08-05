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
import android.support.v7.widget.ListPopupWindow;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
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
        Log.v("log_con", connected + "");
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

    // fecha o teclado virtual se aberto
    public void closeVirtualKeyboard(View view) {
        InputMethodManager imn = (InputMethodManager)ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
        imn.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}

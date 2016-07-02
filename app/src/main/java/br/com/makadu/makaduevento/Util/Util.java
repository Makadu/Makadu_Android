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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

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

        ConnectivityManager cm = (ConnectivityManager)ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        Log.v("log_con", isConnected + "");
        return isConnected;

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

    public String getDateTalk(Date sData) {
        Calendar data = new GregorianCalendar();
        SimpleDateFormat f = new SimpleDateFormat("dd.MM");
        return f.format(sData);
    }

    public String getDate(Date sData) {
        Calendar data = new GregorianCalendar();
        SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");
        return f.format(sData);
    }

    public Date convertStringtoDate(String dateString) {

        //SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

        //formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date date = new Date();

        try {

            date = formatter.parse(dateString);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;

    }

    public String convertStringtoStringDateBrazil(String data) {
        String dataFormatada="";
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        try{
            Date convertDate = f.parse(data);
            SimpleDateFormat fb = new SimpleDateFormat("dd/MM/yyyy");
            dataFormatada = fb.format(convertDate);
        }catch (Exception e){
            dataFormatada = "";
        }

        return dataFormatada;
    }

    public String getDateHour(Date sData) {
        Calendar data = new GregorianCalendar();
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        return f.format(sData);
    }

    public String getHour(Date sData) {
        DateFormat outputformat = new SimpleDateFormat("HH:mm");
        return outputformat.format(sData);
    }

    public String getStringDate(String dataStr) {

        String dataFinal = "";
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        //SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        Date d = null;
       try {
            d = f.parse(dataStr);
            dataFinal = getDateHour(d);
        } catch (ParseException e) {
           Log.e("lg_erro_data", e.getMessage());
        }

        return dataFinal;
    }

    // fecha o teclado virtual se aberto
    public void closeVirtualKeyboard(View view) {
        InputMethodManager imn = (InputMethodManager)ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
        imn.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public String convertEventoDatas(String data) {
        Date dataCurrent = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

        try {
            dataCurrent = sdf.parse(data);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return getDate(dataCurrent);
    }

    public String convertFormatDate(String start_date) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date data = convertStringtoDate(start_date);

        return formatter.format(data);
    }

    public boolean isNullOrEmpty(String s) {
        return (s == null || s.equals(""));
    }

    public long diferencaSegundos(Date datafinal) {
        Date datainicial = new Date();
        Log.v("GMAK1","data inicial: " + datainicial);
        Log.v("GMAK1","data final: " + datafinal);
        long segundos = (datainicial.getTime() - datafinal.getTime()) / 1000;

        return segundos;
    }

    public int Return_id_Linear(int count) {
        int id = 0;

        if(count == 1) {
            id = R.id.linear1;
        }else if(count == 2) {
            id = R.id.linear2;
        }else if(count == 3) {
            id = R.id.linear3;
        }else if(count == 4) {
            id = R.id.linear4;
        }else if(count == 5) {
            id = R.id.linear5;
        }else if(count == 6) {
            id = R.id.linear6;
        }else if(count == 7) {
            id = R.id.linear7;
        }else if(count == 8) {
            id = R.id.linear8;
        } else if(count == 9) {
            id = R.id.linear9;
        }else if(count == 10) {
            id = R.id.linear10;
        }

        return id;
    }
    public int Return_id_rating_bar(int count) {
        int id = 0;

        if(count == 1) {
            id = R.id.rating1;
        }else if(count == 2) {
            id = R.id.rating2;
        }else if(count == 3) {
            id = R.id.rating3;
        }else if(count == 4) {
            id = R.id.rating4;
        }else if(count == 5) {
            id = R.id.rating5;
        }else if(count == 6) {
            id = R.id.rating6;
        }else if(count == 7) {
            id = R.id.rating7;
        }else if(count == 8) {
            id = R.id.rating8;
        } else if(count == 9) {
            id = R.id.rating9;
        }else if(count == 10) {
            id = R.id.rating10;
        }


        return id;
    }
    public int Return_id_text(int count) {
        int id = 0;

        if(count == 1) {
            id = R.id.text1;
        }else if(count == 2) {
            id = R.id.text2;
        }else if(count == 3) {
            id = R.id.text3;
        }else if(count == 4) {
            id = R.id.text4;
        }else if(count == 5) {
            id = R.id.text5;
        }else if(count == 6) {
            id = R.id.text6;
        }else if(count == 7) {
            id = R.id.text7;
        }else if(count == 8) {
            id = R.id.text8;
        }else if(count == 9) {
            id = R.id.text9;
        }else if(count == 10) {
            id = R.id.text10;
        }


        return id;
    }
    public int Return_id_edit_text(int count) {
        int id = 0;

        if(count == 1) {
            id = R.id.edit1;
        }else if(count == 2) {
            id = R.id.edit2;
        }else if(count == 3) {
            id = R.id.edit3;
        }else if(count == 4) {
            id = R.id.edit4;
        }else if(count == 5) {
            id = R.id.edit5;
        }else if(count == 6) {
            id = R.id.edit6;
        }else if(count == 7) {
            id = R.id.edit7;
        }else if(count == 8) {
            id = R.id.edit8;
        }else if(count == 9) {
            id = R.id.edit9;
        }else if(count == 10) {
            id = R.id.edit10;
        }

        return id;
    }

    //BigDecimal minutos = new BigDecimal(Minutes.minutesBetween(dataInicial, dataFinal).getMinutes());
}

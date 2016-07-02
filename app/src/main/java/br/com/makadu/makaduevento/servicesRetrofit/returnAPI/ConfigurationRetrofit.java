package br.com.makadu.makaduevento.servicesRetrofit.returnAPI;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.OkHttpClient;

import br.com.makadu.makaduevento.servicesRetrofit.methodService.MakaduAPI;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * Created by lucasschwalbeferreira on 10/7/15.
 */
public class ConfigurationRetrofit {

    private final String URL = "URL_SERVER";

    private Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            .create();

    private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();

    protected MakaduAPI service = retrofit.create(MakaduAPI.class);

}

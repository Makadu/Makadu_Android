package br.com.makadu.makaduevento.servicesRetrofit.returnAPI;

import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import br.com.makadu.makaduevento.model.Evaluation;
import br.com.makadu.makaduevento.model.FeedbackPost;
import br.com.makadu.makaduevento.model.RequestJson;
import br.com.makadu.makaduevento.model.RequestRating;
import br.com.makadu.makaduevento.model.ResponseJson;
import br.com.makadu.makaduevento.model.User;
import retrofit.Call;

/**
 * Created by lucasschwalbeferreira on 11/16/15.
 */
public class PostRestAdapter extends ConfigurationRetrofit {

    //private JSONObject userResponse;

    public User login(String username, String password) throws IOException {
        Call<User> call = service.login(username, password);
        User userResponse = call.execute().body();

        return userResponse;
    }

    public Call<User> createUser(User user) throws IOException {

        Call<User> call = service.createAccount(user);

        return call;
    }

    public Call<ResponseJson> newEventFavorite(String user_id, String eventId) throws IOException {
        Call<ResponseJson> call = service.new_event_favorite(user_id, eventId);

        return call;
    }

    public Call<ResponseJson> newDownload(String eventId,String talkId,RequestJson username) throws IOException {
        Call<ResponseJson> call = service.new_talk_download(eventId, talkId, username);

        return call;
    }

    public Call<ResponseJson> deleteFavoriteEvent(String userId, String eventId){
        Call<ResponseJson> s = service.deleteFavoriteEvent(userId, eventId);

        return s;
    }

    public Call<ResponseJson> addQuestion(String eventId,String talkId,RequestJson username) throws IOException {
        Call<ResponseJson> call = service.addQuestion(eventId, talkId, username);

        return call;
    }

    public Call<ResponseJson> addQuestionInt(String talk_id,String question_id,RequestJson r) throws IOException {
        Call<ResponseJson> call = service.addQuestionInterative(talk_id, question_id, r);

        return call;
    }

    public Call<ResponseJson> recovery(RequestJson username) throws IOException {
        Call<ResponseJson> call = service.recovery(username);

        return call;
    }

    public Call<ResponseJson> rateEvent(String eventId, String talkId, RequestRating request){
        Call<ResponseJson> json = service.rateEvent(eventId, talkId, request);

        return json;
    }

    public Call<ResponseJson> postEvaluations(String eventId, Evaluation request){
        Call<ResponseJson> json = service.postEvaluations(eventId, request);

        return json;
    }

}
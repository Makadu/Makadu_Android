package br.com.makadu.makaduevento.servicesRetrofit.methodService;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import br.com.makadu.makaduevento.model.Evaluation;
import br.com.makadu.makaduevento.model.Event;
import br.com.makadu.makaduevento.model.FeedbackGet;
import br.com.makadu.makaduevento.model.FeedbackPost;
import br.com.makadu.makaduevento.model.Interactive;
import br.com.makadu.makaduevento.model.Notice;
import br.com.makadu.makaduevento.model.Paper;
import br.com.makadu.makaduevento.model.Question;
import br.com.makadu.makaduevento.model.RequestJson;
import br.com.makadu.makaduevento.model.RequestRating;
import br.com.makadu.makaduevento.model.ResponseJson;
import br.com.makadu.makaduevento.model.Resposta;
import br.com.makadu.makaduevento.model.Talk;
import br.com.makadu.makaduevento.model.TalkSpeaker;
import br.com.makadu.makaduevento.model.User;
import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;


/**
 * Created by lucasschwalbeferreira on 9/29/15.
 */
public interface MakaduAPI {

    @GET("/events")
    Call<List<Event>> loadEvents();

    @GET("/events/{id}")
    Call<Event> loadEventId(@Path("id") String id);

    @GET("/events/{id}/talks")
    Call<List<Talk>> loadTalks(@Path("id") String id);

    @GET("/events/{id}/evaluation")
    Call<List<FeedbackGet>> loadEvaluations(@Path("id") String id);

    @GET("/events/{event_id}/talks/{talk_id}")
    Call<TalkSpeaker> loadTalkId(@Path("event_id") String event_id,@Path("talk_id") String talk_id);

    @GET("/events/{event_id}/notices")
    Call<List<Notice>> loadNotices(@Path("event_id") String event_id);

    @GET("/events/{event_id}/talks/{talk_id}/questions")
    Call<List<Question>> loadQuestions(@Path("event_id") String event_id,@Path("talk_id") String talk_id);

    @FormUrlEncoded
    @POST("/users/auth")
    Call<User> login(@Field("username") String username,  @Field("password") String password);

    @POST("/users/new")
    Call<User> createAccount(@Body User user);

    @POST("/users/password_recovery")
    Call<ResponseJson> recovery(@Body RequestJson request);

    @POST("/interativas/{talk_id}/question/{question_id}")
    Call<ResponseJson> addQuestionInterative(@Path("talk_id") String talk_id, @Path("question_id") String question_id, @Body RequestJson request);

    @POST("/events/{event_id}/talks/{talk_id}/questions/add")
    Call<ResponseJson> addQuestion(@Path("event_id") String id, @Path("talk_id") String talkId, @Body RequestJson request);

    @GET("/users/{user_id}/favorites/event")
    Call<List<Event>> loadEventFavorite(@Path("user_id") String user_id);

    @GET("/events/{event_id}/papers")
    Call<List<JsonObject>> loadPaper(@Path("event_id") String event_id);

    @GET("/interativas/{talk_id}")
    Call<Interactive> loadInteractive(@Path("talk_id") String talk_id);



    @GET("/users/{user_id}/favorites/event/{event_id}")
    Call<Resposta> isFavoriteEvent(@Path("user_id") String user_id, @Path("event_id") String event_id);

    @POST("/users/{user_id}/favorites/event/{event_id}")
    Call<ResponseJson> new_event_favorite(@Path("user_id") String user_id, @Path("event_id") String event_id);

    @DELETE("users/{user_id}/favorites/event/{event_id}")
    Call<ResponseJson> deleteFavoriteEvent(@Path("user_id") String user_id, @Path("event_id") String event_id);

    @GET("/events/{event_id}/talks/{talk_id}/download")
    Call<Boolean> isDownload(@Path("username") String username);

    @POST("/events/{event_id}/talks/{talk_id}/download")
    Call<ResponseJson> new_talk_download(@Path("event_id") String event_id, @Path("talk_id") String talk_id, @Body RequestJson request);

    @POST("/events/{event_id}/talks/{talk_id}/ratings/add_edit")
    Call<ResponseJson> rateEvent(@Path("event_id") String event_id, @Path("talk_id") String talk_id, @Body RequestRating request);

    @POST("/events/{event_id}/feedback")
    Call<ResponseJson> postEvaluations(@Path("event_id") String event_id,@Body Evaluation request);

}

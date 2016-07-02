package br.com.makadu.makaduevento.servicesRetrofit.returnAPI;


import android.util.Log;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import br.com.makadu.makaduevento.model.Event;
import br.com.makadu.makaduevento.model.FeedbackGet;
import br.com.makadu.makaduevento.model.Interactive;
import br.com.makadu.makaduevento.model.Notice;
import br.com.makadu.makaduevento.model.Paper;
import br.com.makadu.makaduevento.model.Question;
import br.com.makadu.makaduevento.model.RequestJson;
import br.com.makadu.makaduevento.model.ResponseJson;
import br.com.makadu.makaduevento.model.Resposta;
import br.com.makadu.makaduevento.model.Speaker;
import br.com.makadu.makaduevento.model.Talk;
import br.com.makadu.makaduevento.model.TalkSpeaker;
import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by lucasschwalbeferreira on 10/1/15.
 */
public class GetRestAdapter extends ConfigurationRetrofit {

    public List<Event> returnAllEvents() throws IOException {
        List<Event> events;
        Call<List<Event>> call = service.loadEvents();
        events = call.execute().body();

        return events;
    }

    public Event returnEvent(String id) throws IOException {
        Event event;
        Call<Event> call = service.loadEventId(id);
        event = call.execute().body();

        return event;
    }

    public List<Talk> returnAllTalks(String id) throws IOException {
        List<Talk> talks;
        Call<List<Talk>> call = service.loadTalks(id);
        talks = call.execute().body();

        return talks;
    }

    public List<FeedbackGet> returnEvaluations(String id) throws IOException {
        List<FeedbackGet> evaluations;
        Call<List<FeedbackGet>> call = service.loadEvaluations(id);
        evaluations = call.execute().body();

        return evaluations;
    }

    public List<Speaker> returnTalk(String event_id,String talk_id) throws IOException {
        TalkSpeaker talks;
        Call<TalkSpeaker> call = service.loadTalkId(event_id, talk_id);
        talks = call.execute().body();

        return talks.speakers;
    }

    public List<Question> returnQuestion(String event_id,String talk_id) throws IOException {
        List<Question> listQuestion = new ArrayList<Question>();
        Call<List<Question>> call = service.loadQuestions(event_id, talk_id);
        listQuestion = call.execute().body();

        return listQuestion;
    }

    public Call<Interactive> returnInteractive(String talk_id) throws IOException {
        Interactive oneInteractive = new Interactive();
        Call<Interactive> call = service.loadInteractive(talk_id);
        //oneInteractive = call.execute().body();

        return call;
    }

    public List<Paper> returnPapers(String event_id) throws IOException {
        List<JsonObject> jsonPaper = new ArrayList<JsonObject>();
        List<Paper> listPaper = new ArrayList<Paper>();
        Call<List<JsonObject>> call = service.loadPaper(event_id);
        jsonPaper = call.execute().body();

        for (JsonObject jo : jsonPaper ) {
            Paper paper = new Paper();

            paper.id = Integer.parseInt(jo.get("id").getAsString());
            paper.title = jo.get("title").getAsString();
            paper.reference = jo.get("reference").getAsString();
            paper.authors = jo.get("authors").toString();
            paper.Abstract = jo.get("abstract").toString();

            listPaper.add(paper);
        }

        return listPaper;
    }

    public List<Notice> returnAllNotices(String id) throws IOException {
        List<Notice> notice;
        Call<List<Notice>> call = service.loadNotices(id);
        notice = call.execute().body();

        return notice;
    }

    public List<Event> returnEventFavorite(String user_id) throws IOException {
        List<Event> events;
        Call<List<Event>> call = service.loadEventFavorite(user_id);
        events = call.execute().body();

        return events;
    }
/*
    public Boolean isFavoriteEvent(String user_id,String event_id) {
        Resposta resp = new Resposta();
        Call<Resposta> call = service.isFavoriteEvent(user_id, event_id);
        try {
            resp = call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resp.resposta;
    }
*/
    public Call<Resposta> isFavoriteEvent(String user_id,String event_id) throws IOException {
        Call<Resposta> call = service.isFavoriteEvent(user_id, event_id);

        return call;
    }

}

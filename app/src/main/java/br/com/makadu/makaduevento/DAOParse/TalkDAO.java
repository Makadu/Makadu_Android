package br.com.makadu.makaduevento.DAOParse;

import android.app.Activity;
import android.content.Context;
import android.net.NetworkInfo;
import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import br.com.makadu.makaduevento.model.Favorites;
import br.com.makadu.makaduevento.model.Speaker;
import br.com.makadu.makaduevento.model.Talk;

/**
 * Created by lucasschwalbeferreira on 08/04/15.
 */
public class TalkDAO {

    public List<Talk> returnProgramacaoList(String event, final boolean isConnected, boolean isCache,boolean isFavorite, Context ctx) throws ParseException {

        Favorites favorites;
        ArrayList<String> arraylist_objectid = null;

        if(isFavorite) {
            favorites = new Favorites(ctx);
            arraylist_objectid = new ArrayList<String>();
            arraylist_objectid = favorites.findLocally(ctx);
        }

        List<ParseObject> list_PO_Programacao;
        final ArrayList<Talk> talk_list = new ArrayList<Talk>();

        final ParseQuery<ParseObject> evento = ParseQuery.getQuery("Events");

        if(isCache) {
            evento.setCachePolicy(ParseQuery.CachePolicy.CACHE_ONLY);
        } else {
            if (!isConnected) {
                evento.setCachePolicy(ParseQuery.CachePolicy.CACHE_ONLY);
            } else {
                if (evento.hasCachedResult()) {
                    evento.setCachePolicy(ParseQuery.CachePolicy.CACHE_ONLY);
                    evento.setMaxCacheAge(10);

                } else {
                    evento.setCachePolicy(ParseQuery.CachePolicy.CACHE_ELSE_NETWORK);
                    evento.setMaxCacheAge(10);
                }
            }
        }

        ParseObject ev = new ParseObject("Events");

        try {
            ev = evento.get(event);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Talks");
        if(isCache) {
            query.setCachePolicy(ParseQuery.CachePolicy.CACHE_ONLY);
        } else {
            if (!isConnected) {
                query.setCachePolicy(ParseQuery.CachePolicy.CACHE_ONLY);
            } else {

                if (query.hasCachedResult()) {
                    query.setCachePolicy(ParseQuery.CachePolicy.CACHE_ONLY);
                    query.setMaxCacheAge(10);
                } else {
                    query.setCachePolicy(ParseQuery.CachePolicy.CACHE_ELSE_NETWORK);
                    query.setMaxCacheAge(10);
                }

            }
        }

        query.setLimit(800);
        query.whereEqualTo("active", true);
        query.whereEqualTo("event", ev);
        query.orderByAscending("date_talk");
        if(isFavorite) {
            query.whereContainedIn("objectId",arraylist_objectid);
        }

        list_PO_Programacao = query.find();

        for (ParseObject i_programacao : list_PO_Programacao) {

            final Talk talk = new Talk();

            talk.setId(i_programacao.getObjectId());
            talk.setTitulo((String) i_programacao.get("title"));
            talk.setDescricao((String) i_programacao.get("description"));
            talk.setData((Date) i_programacao.getDate("date_talk"));
            talk.setHoraInicio((String) i_programacao.get("start_hour"));
            talk.setHoraFim((String) i_programacao.get("end_hour"));
            talk.setLocal((String) i_programacao.get("local"));
            talk.setAllow_file((Boolean) i_programacao.get("allow_file"));
            talk.setAllow_question((Boolean) i_programacao.get("allow_question"));

            ParseFile fileObject = (ParseFile) i_programacao.get("file");

            if (fileObject == null) {
                talk.setUrl(" ");
            } else {
                talk.setUrl((String) fileObject.getUrl() + "");
            }

            // Palestrantes
            ParseRelation relationPalestrante = i_programacao.getRelation("speakers");
            ParseQuery querySpeaker = relationPalestrante.getQuery();
            if(isCache) {
                querySpeaker.setCachePolicy(ParseQuery.CachePolicy.CACHE_ONLY);
            } else {
                if (!isConnected) {
                    querySpeaker.setCachePolicy(ParseQuery.CachePolicy.CACHE_ONLY);
                } else {
                    if (querySpeaker.hasCachedResult()) {
                        querySpeaker.setCachePolicy(ParseQuery.CachePolicy.CACHE_ONLY);
                        evento.setMaxCacheAge(10);
                    } else {
                        querySpeaker.setCachePolicy(ParseQuery.CachePolicy.CACHE_THEN_NETWORK);
                        querySpeaker.setMaxCacheAge(10);
                    }

                }
            }
            //List<ParseObject> list_PO_Palestrante = queryPalestrante.find();
            querySpeaker.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> palestrantelist, ParseException e) {
                    if (e == null) {

                        ArrayList<Speaker> speaker_list = new ArrayList<Speaker>();

                        for (ParseObject i_palestrante : palestrantelist) {
                            Speaker speaker = new Speaker();
                            speaker.setId(i_palestrante.getObjectId());
                            speaker.setNome((String) i_palestrante.get("full_name"));
                            speaker.setDescricao_palestrante((String) i_palestrante.get("about_speaker"));

                            speaker_list.add(speaker);
                        }

                        talk.setSpeakers(speaker_list);
                    }
                }
            });

            talk_list.add(talk);
        }

        return talk_list;
    }

    public ParseObject returnTalkParseObject(String talk,boolean isConnect) {

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Talks");
        ParseObject tk = new ParseObject("Talks");
        if (!isConnect) {
            query.setCachePolicy(ParseQuery.CachePolicy.CACHE_ONLY);
        } else {
            if(query.hasCachedResult()) {
                query.setCachePolicy(ParseQuery.CachePolicy.CACHE_ONLY);
                query.setMaxCacheAge(10);
            }else {
                query.setCachePolicy(ParseQuery.CachePolicy.CACHE_ELSE_NETWORK);
                query.setMaxCacheAge(10);
            }
        }
        try {
            tk = query.get(talk);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return tk;
    }
}

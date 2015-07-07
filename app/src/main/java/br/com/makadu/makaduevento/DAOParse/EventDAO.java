package br.com.makadu.makaduevento.DAOParse;

import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.makadu.makaduevento.model.Event;

/**
 * Created by lucasschwalbeferreira on 26/03/15.
 */
public class EventDAO {

    public List<Event> returnAllEvents(boolean isConnected,boolean cache) {

        List<ParseObject> list_PO_Evento;
        List<Event> eventos_list = null;

        eventos_list = new ArrayList<Event>();

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Events");

        if(cache) {
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
        query.whereEqualTo("active",true);
        query.orderByDescending("start_date");

        try {
            list_PO_Evento = query.find();

            for (ParseObject i_evento : list_PO_Evento) {

                Event event = new Event();
                event.setId(i_evento.getObjectId());
                event.setName((String) i_evento.get("event_name"));
                event.setDescription((String) i_evento.get("event_description"));
                event.setLocal((String) i_evento.get("local"));
                event.setAddress((String) i_evento.get("address"));
                event.setCity((String) i_evento.get("city"));
                event.setState((String) i_evento.get("state"));

                Date data_ini = (Date)i_evento.get("start_date");
                Date data_fim = (Date)i_evento.get("end_date");

                SimpleDateFormat out = new SimpleDateFormat("dd/MM/yyyy");

                event.setStart_date(out.format(data_ini));
                event.setEnd_date(out.format(data_fim));

                ParseFile fileObjectlogo = (ParseFile) i_evento.get("logo");
                event.setFile_img_event(fileObjectlogo.getData());

                ParseFile fileObjectpatrocinador = (ParseFile) i_evento.get("patronage");
                event.setFile_img_patronage(fileObjectpatrocinador.getData());

                //event.save();

                eventos_list.add(event);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return eventos_list;
    }

    public ParseObject returnEvent(String event,boolean isConnect) {

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Events");
        ParseObject ev = new ParseObject("Events");
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
            ev = query.get(event);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return ev;
    }

    public List<Event> retornAllEvents_inBack() {

        final List<Event> events = new ArrayList<Event>();

        final ParseQuery<ParseObject> query = ParseQuery.getQuery("Events");
        query.whereEqualTo("active", true);
        query.orderByAscending("start_date");

        query.setCachePolicy(ParseQuery.CachePolicy.CACHE_THEN_NETWORK);

        query.findInBackground(new FindCallback<ParseObject>() {

            @Override
            public void done(final List<ParseObject> eventList, ParseException e) {
                if(e == null) {
                    for(ParseObject i_evento : eventList) {

                        Event event = new Event();
                        event.setId(i_evento.getObjectId());
                        event.setName((String) i_evento.get("event_name"));
                        event.setDescription((String) i_evento.get("event_description"));
                        event.setLocal((String) i_evento.get("local"));
                        event.setAddress((String) i_evento.get("address"));
                        event.setCity((String) i_evento.get("city"));
                        event.setState((String) i_evento.get("state"));

                        Date data_ini = (Date)i_evento.get("start_date");
                        Date data_fim = (Date)i_evento.get("end_date");

                        SimpleDateFormat out = new SimpleDateFormat("dd/MM/yyyy");

                        event.setStart_date(out.format(data_ini));
                        event.setEnd_date(out.format(data_fim));

                        ParseFile fileObjectlogo = (ParseFile) i_evento.get("logo");
                        try {
                            event.setFile_img_event(fileObjectlogo.getData());

                            ParseFile fileObjectpatrocinador = (ParseFile) i_evento.get("patronage");
                            event.setFile_img_patronage(fileObjectpatrocinador.getData());

                        } catch (ParseException e1) {
                            Log.v("Erro_Event", e1.getMessage());
                        }

                        //event.save();

                        events.add(event);

                    }
                }
            }
        });

        return events;
    }

    public List<Event> returnAllEvents_CACHE() {

        List<ParseObject> list_PO_Evento;
        List<Event> eventos_list = null;

        eventos_list = new ArrayList<Event>();

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Events");

        query.setCachePolicy(ParseQuery.CachePolicy.CACHE_ONLY);


        query.whereEqualTo("active", true);
        query.orderByAscending("start_date");

        try {
            list_PO_Evento = query.find();

            for (ParseObject i_evento : list_PO_Evento) {

                Event event = new Event();
                event.setId(i_evento.getObjectId());
                event.setName((String) i_evento.get("event_name"));
                event.setDescription((String) i_evento.get("event_description"));
                event.setLocal((String) i_evento.get("local"));
                event.setAddress((String) i_evento.get("address"));
                event.setCity((String) i_evento.get("city"));
                event.setState((String) i_evento.get("state"));

                Date data_ini = (Date)i_evento.get("start_date");
                Date data_fim = (Date)i_evento.get("end_date");

                SimpleDateFormat out = new SimpleDateFormat("dd/MM/yyyy");

                event.setStart_date(out.format(data_ini));
                event.setEnd_date(out.format(data_fim));

                ParseFile fileObjectlogo = (ParseFile) i_evento.get("logo");
                event.setFile_img_event(fileObjectlogo.getData());

                ParseFile fileObjectpatrocinador = (ParseFile) i_evento.get("patronage");
                event.setFile_img_patronage(fileObjectpatrocinador.getData());

               // event.save();

                eventos_list.add(event);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return eventos_list;
    }

}

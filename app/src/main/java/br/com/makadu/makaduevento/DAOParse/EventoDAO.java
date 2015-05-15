package br.com.makadu.makaduevento.DAOParse;

import android.net.NetworkInfo;

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
public class EventoDAO {

    public List<Event> returnAllEvents(NetworkInfo ni) {
        List<ParseObject> list_PO_Evento;
        List<Event> eventos_list = null;

        eventos_list = new ArrayList<Event>();

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Events");
        if (ni == null) {
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
        query.whereEqualTo("active",true);
        query.orderByAscending("start_date");

        try {
            list_PO_Evento = query.find();

            for (ParseObject i_evento : list_PO_Evento) {

                final Event event = new Event();
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


                eventos_list.add(event);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return eventos_list;
    }
}

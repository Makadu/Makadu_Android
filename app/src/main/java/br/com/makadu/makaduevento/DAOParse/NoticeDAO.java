package br.com.makadu.makaduevento.DAOParse;

import android.net.NetworkInfo;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.makadu.makaduevento.model.Notice;
import br.com.makadu.makaduevento.model.Palestrante;
import br.com.makadu.makaduevento.model.Programacao;

/**
 * Created by lucasschwalbeferreira on 08/04/15.
 */
public class NoticeDAO {

    public List<Notice> returnNoticeList(String event, NetworkInfo ni) {

        List<ParseObject> list_PO_Notice;
        ArrayList<Notice> notice_list = new ArrayList<Notice>();

        ParseQuery<ParseObject> evento = ParseQuery.getQuery("Events");

        if(ni == null) {
            evento.setCachePolicy(ParseQuery.CachePolicy.CACHE_ONLY);
        }else {
            if(evento.hasCachedResult()){
                evento.setCachePolicy(ParseQuery.CachePolicy.CACHE_ONLY);
                evento.setMaxCacheAge(10);
            }else{
            evento.setCachePolicy(ParseQuery.CachePolicy.CACHE_ELSE_NETWORK);
            evento.setMaxCacheAge(10);
            }
        }

        ParseObject ev = new ParseObject("Events");
        try {
            ev = evento.get(event);
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Notices");
            if(ni == null) {
                query.setCachePolicy(ParseQuery.CachePolicy.CACHE_ONLY);
            }
            else{
                if (query.hasCachedResult()){
                    query.setCachePolicy(ParseQuery.CachePolicy.CACHE_ONLY);
                    query.setMaxCacheAge(10);
                }else {
                    query.setCachePolicy(ParseQuery.CachePolicy.CACHE_ELSE_NETWORK);
                    query.setMaxCacheAge(10);
                }
            }

            query.whereEqualTo("active", true);
            query.whereEqualTo("event", ev);
            query.orderByAscending("createdAt");

            try {

                list_PO_Notice = query.find();

                for (ParseObject i_notices : list_PO_Notice) {

                    final Notice notice = new Notice();

                    notice.setName_notice((String)i_notices.get("notice"));
                    notice.setDetail((String)i_notices.get("detail"));

                    notice_list.add(notice);
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return notice_list;
    }
}

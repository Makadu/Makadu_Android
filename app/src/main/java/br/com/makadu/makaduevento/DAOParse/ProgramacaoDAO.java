package br.com.makadu.makaduevento.DAOParse;

import android.net.NetworkInfo;
import android.util.Log;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.makadu.makaduevento.model.Palestrante;
import br.com.makadu.makaduevento.model.Programacao;
import br.com.makadu.makaduevento.model.Question;

/**
 * Created by lucasschwalbeferreira on 08/04/15.
 */
public class ProgramacaoDAO {

    public List<Programacao> returnProgramacaoList(String event,NetworkInfo ni) {

        List<ParseObject> list_PO_Programacao;
        ArrayList<Programacao> programacao_list = new ArrayList<Programacao>();

        ParseQuery<ParseObject> evento = ParseQuery.getQuery("Events");

        if (ni == null) {
            evento.setCachePolicy(ParseQuery.CachePolicy.CACHE_ONLY);
        } else {
            if(evento.hasCachedResult()){
                evento.setCachePolicy(ParseQuery.CachePolicy.CACHE_ONLY);
                evento.setMaxCacheAge(10);
            }
            else {
                evento.setCachePolicy(ParseQuery.CachePolicy.CACHE_ELSE_NETWORK);
                evento.setMaxCacheAge(10);
            }
        }

        ParseObject ev = new ParseObject("Events");
        try {
            ev = evento.get(event);
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Talks");
            if(ni == null){

                query.setCachePolicy(ParseQuery.CachePolicy.CACHE_ONLY);
            }
            else {

                if(query.hasCachedResult()){
                    query.setCachePolicy(ParseQuery.CachePolicy.CACHE_ONLY);
                    query.setMaxCacheAge(10);
                } else {
                    query.setCachePolicy(ParseQuery.CachePolicy.CACHE_ELSE_NETWORK);
                    query.setMaxCacheAge(10);
                }


            }
            query.whereEqualTo("active", true);
            query.whereEqualTo("event", ev);
            query.orderByAscending("date_talk");

            try {

                list_PO_Programacao = query.find();

                for (ParseObject i_programacao : list_PO_Programacao) {

                    final Programacao programacao = new Programacao();

                    programacao.setId(i_programacao.getObjectId());
                    programacao.setTitulo((String) i_programacao.get("title"));
                    programacao.setDescricao((String) i_programacao.get("description"));
                    programacao.setData((Date) i_programacao.getDate("date_talk"));
                    programacao.setHoraInicio((String) i_programacao.get("start_hour"));
                    programacao.setHoraFim((String) i_programacao.get("end_hour"));
                    programacao.setLocal((String) i_programacao.get("local"));
                    programacao.setAllow_file((Boolean) i_programacao.get("allow_file"));
                    programacao.setAllow_question((Boolean) i_programacao.get("allow_question"));

                    ParseFile fileObject = (ParseFile) i_programacao.get("file");

                    //Log.d("arq",fileObject.toString());

                    if (fileObject == null) {
                        programacao.setUrl(" ");

                    } else {
                        programacao.setUrl((String) fileObject.getUrl() + "");
                    }

                    // Palestrantes
                    ParseRelation relationPalestrante = i_programacao.getRelation("speakers");
                    ParseQuery queryPalestrante = relationPalestrante.getQuery();
                    if (ni == null) {
                        queryPalestrante.setCachePolicy(ParseQuery.CachePolicy.CACHE_ONLY);
                    } else {
                        if(queryPalestrante.hasCachedResult())
                        {
                            queryPalestrante.setCachePolicy(ParseQuery.CachePolicy.CACHE_ONLY);
                            evento.setMaxCacheAge(10);
                        }
                        else {
                            queryPalestrante.setCachePolicy(ParseQuery.CachePolicy.CACHE_ELSE_NETWORK);
                            queryPalestrante.setMaxCacheAge(10);
                        }

                    }
                    List<ParseObject> list_PO_Palestrante = queryPalestrante.find();
                    ArrayList<Palestrante> palestrante_list = new ArrayList<Palestrante>();

                    for(ParseObject i_palestrante : list_PO_Palestrante) {

                        Palestrante palestrante = new Palestrante();
                        palestrante.setId(i_palestrante.getObjectId());
                        palestrante.setNome((String)i_palestrante.get("full_name"));
                        palestrante.setDescricao_palestrante((String)i_palestrante.get("about_speaker"));

                        palestrante_list.add(palestrante);
                    }
                    programacao.setPalestrantes(palestrante_list);


                    programacao_list.add(programacao);
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return programacao_list;
    }
}

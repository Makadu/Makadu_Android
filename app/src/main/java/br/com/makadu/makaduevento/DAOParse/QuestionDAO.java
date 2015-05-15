package br.com.makadu.makaduevento.DAOParse;

import android.net.NetworkInfo;
import android.util.Log;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.makadu.makaduevento.model.Question;

/**
 * Created by lucasschwalbeferreira on 08/04/15.
 */
public class QuestionDAO {


    public List<Question> returnQuestionList(String talk,NetworkInfo ni) {

        List<ParseObject> list_PO_Question;
        ArrayList<Question> question_list = new ArrayList<Question>();

        ParseQuery<ParseObject> programacao = ParseQuery.getQuery("Talks");
        if(ni == null) {
            programacao.setCachePolicy(ParseQuery.CachePolicy.CACHE_ONLY);
        }
        else
        {
            if(programacao.hasCachedResult())
            {
                programacao.setCachePolicy(ParseQuery.CachePolicy.CACHE_ONLY);
                programacao.setMaxCacheAge(10);

            }
            else {
                programacao.setCachePolicy(ParseQuery.CachePolicy.CACHE_ELSE_NETWORK);
                programacao.setMaxCacheAge(10);
            }
        }
        ParseObject tk = new ParseObject("Talks");
        try {
            tk = programacao.get(talk);
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Questions");
            if (ni == null){
                query.setCachePolicy(ParseQuery.CachePolicy.CACHE_ONLY);
            } else {
                if(programacao.hasCachedResult()) {
                    query.setCachePolicy(ParseQuery.CachePolicy.CACHE_ONLY);
                    query.setMaxCacheAge(10);
                }else {
                    query.setCachePolicy(ParseQuery.CachePolicy.CACHE_ELSE_NETWORK);
                    query.setMaxCacheAge(10);
                }
            }
            query.whereNotEqualTo("active", false);
            query.whereEqualTo("talk", tk);
            query.orderByAscending("createdAt");

            try {
                list_PO_Question = query.find();
                for (ParseObject i_question : list_PO_Question) {
                    Question question = new Question();

                    question.setId(i_question.getObjectId());
                    question.setQuestion((String)i_question.get("question"));
                    question.setDate((Date) i_question.getCreatedAt());

                    question_list.add(question);
                }
            } catch (ParseException e) {
                Log.d("question_exception",e.getMessage());
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return question_list;
    }
}

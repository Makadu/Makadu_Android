package br.com.makadu.makaduevento.model;

import java.util.Date;

import br.com.makadu.makaduevento.Util.Util;

/**
 * Created by lucasschwalbeferreira on 11/04/15.
 */
public class Question {

    // "created_at": "2016-01-08T16:12:52.280-02:00",

    private String id;
    private String question;
    private String Speaker;
    private Date date;
    public String string_datehour_created_at;

    // retrofit server
    public String user_id;
    public String talk_id;
    public String value;
    public Date created_at;


    public Question(){}

    public Question(String id, String question, String speaker, Date date) {
        this.id = id;
        this.question = question;
        Speaker = speaker;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuestion() {
        return value;
    }

    public void setQuestion(String question) {
        this.value = question;
    }

    public String getSpeaker() {
        return Speaker;
    }

    public void setSpeaker(String speaker) {
        Speaker = speaker;
    }

    public String getDate() {
        if(this.created_at != null)
            this.string_datehour_created_at = new Util().getDateHour(this.created_at);

        return string_datehour_created_at;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}

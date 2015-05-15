package br.com.makadu.makaduevento.model;

import java.util.Date;

/**
 * Created by lucasschwalbeferreira on 11/04/15.
 */
public class Question {

    private String id;
    private String question;
    private String Speaker;
    private Date date;

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
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getSpeaker() {
        return Speaker;
    }

    public void setSpeaker(String speaker) {
        Speaker = speaker;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}

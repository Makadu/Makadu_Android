package br.com.makadu.makaduevento.model;


import java.io.Serializable;

/**
 * Created by lucasschwalbeferreira on 21/03/16.
 */

public class RequestJson implements Serializable {

    public RequestJson() {}

    public RequestJson(String username) {
        this.username = username;
    }

    public RequestJson(String username,String question) {
        this.username = username;

        QuestionRequest q = new QuestionRequest();

        q.username = username;
        q.question = question;

        this.question = q;
    }

    public RequestJson(String username,String value,String commentary) {
        this.username = username;
        this.value = value;
        this.commentary = commentary;
    }

    // JSON RETROFIT
    public String username;
    public String value;
    public String commentary;
    public QuestionRequest question;

    public String user_id;
    public String answer_id;

}

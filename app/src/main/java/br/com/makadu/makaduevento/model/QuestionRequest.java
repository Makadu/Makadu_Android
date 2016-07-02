package br.com.makadu.makaduevento.model;

import java.io.Serializable;

/**
 * Created by lucasschwalbeferreira on 3/31/16.
 */
public class QuestionRequest implements Serializable {

    public QuestionRequest(){}

    public QuestionRequest(String username, String question) {
        this.username = username;
        this.question = question;
    }

    public String username;
    public String question;
}

package br.com.makadu.makaduevento.model;

import java.io.Serializable;

/**
 * Created by lucasschwalbeferreira on 4/1/16.
 */
public class RequestRating implements Serializable {

    public RequestRating(String username, int value, String commentary) {
        this.username = username;
        this.value = value;
        this.commentary = commentary;
    }

    public void RequestEvaluation(String username, int value, String evaluation_question_id) {
        this.evaluation_question_id = evaluation_question_id;
        this.value = value;
        this.commentary = commentary;
    }

    public String evaluation_question_id;
    public String username;
    public int value;
    public String commentary;
}

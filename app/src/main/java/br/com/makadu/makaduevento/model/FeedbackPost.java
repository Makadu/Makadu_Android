package br.com.makadu.makaduevento.model;

import java.io.Serializable;

/**
 * Created by lucasschwalbeferreira on 4/18/16.
 */
public class FeedbackPost implements Serializable {

    public FeedbackPost (String id) {
        evaluation_question_id = id;
    }

    public String evaluation_question_id;
    public String value;
    public String commentary;
}

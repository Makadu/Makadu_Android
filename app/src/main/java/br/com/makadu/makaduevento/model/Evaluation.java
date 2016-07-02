package br.com.makadu.makaduevento.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lucasschwalbeferreira on 4/18/16.
 */
public class Evaluation implements Serializable{

    public Evaluation(String id,List<FeedbackPost> feed){
        user_id = id;
        feedbacks = feed;
    }

    public String user_id;
    public List<FeedbackPost> feedbacks;
}

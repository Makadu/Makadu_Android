package br.com.makadu.makaduevento.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import br.com.makadu.makaduevento.Util.Util;

/**
 * Created by lucasschwalbeferreira on 4/13/16.
 */
public class Interactive implements Serializable {

    public int id;
    public int talk_id;
    public String question_interactive;
    public List<Answers> answers;
    public String start_time;
    public String end_time;
    public String resposta;
    public String current_time;

    public Date getEnd_Date() {
       return new Util().convertStringtoDate(this.end_time);
    }
}

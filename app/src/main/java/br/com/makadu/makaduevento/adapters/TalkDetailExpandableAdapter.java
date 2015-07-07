package br.com.makadu.makaduevento.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import br.com.makadu.makaduevento.R;
import br.com.makadu.makaduevento.Util.Util;
import br.com.makadu.makaduevento.model.Speaker;
import br.com.makadu.makaduevento.model.Talk;
import br.com.makadu.makaduevento.model.Question;

/**
 * Created by lucasschwalbeferreira on 08/04/15.
 */
public class TalkDetailExpandableAdapter extends BaseExpandableListAdapter {

    private Talk talk;
    private List<String> listGroup;
    private HashMap<String, List<Talk>> listDataProg;
    private HashMap<String, List<Speaker>> listDataPalestrante;
    private HashMap<String, List<Question>> listDataQuestion;
    private LayoutInflater inflater;

    public TalkDetailExpandableAdapter(Context context, List<String> listGroup, HashMap<String, List<Talk>> listDataProg,HashMap<String,List<Speaker>> listDataPalestrante,HashMap<String, List<Question>> listDataQuestion, Talk talk) {
        this.talk = talk;
        this.listGroup = listGroup;
        this.listDataProg = listDataProg;
        this.listDataPalestrante = (HashMap<String, List<Speaker>>) listDataPalestrante;
        this.listDataQuestion = listDataQuestion;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getGroupCount() {
        return listGroup.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {

        if(groupPosition == 0) {
            return listDataProg.get(listGroup.get(groupPosition)).size();
        }
        else if(groupPosition == 1) {
            return listDataPalestrante.get(listGroup.get(groupPosition)).size();
        }
        else {
            return listDataQuestion.get(listGroup.get(groupPosition)).size();
        }

    }

    @Override
    public Object getGroup(int groupPosition) {
        return listGroup.get(groupPosition);
    }

    @Override
    public Talk getChild(int groupPosition, int childPosition) {
        return listDataProg.get(listGroup.get(groupPosition)).get(childPosition);
    }

    public Speaker getChildPalestrante(int groupPosition, int childPosition) {
        return listDataPalestrante.get(listGroup.get(groupPosition)).get(childPosition);
    }

    public Question getChildQuestion(int groupPosition, int childPosition) {
        return listDataQuestion.get(listGroup.get(groupPosition)).get(childPosition);
    }



    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        ViewHolderGroup holder;

        if(convertView == null) {
            convertView = inflater.inflate(R.layout.header_expandable_list_view_programacao_about, null);
            holder = new ViewHolderGroup();
            convertView.setTag(holder);

            holder.tvGroup = (TextView) convertView.findViewById(R.id.txt_header_programacao_about);
        }
        else {
            holder = (ViewHolderGroup) convertView.getTag();
        }

        holder.tvGroup.setText(listGroup.get(groupPosition));

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition,final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        ViewHolderItemAbout holderAbout;
        ViewHolderItemPalestrante holderPalestrante;
        ViewHolderItemQuestion holderQuestion;
        Util util = new Util();

        Log.d("erro_detail", "childPosition: " + childPosition + " groupPosition: " + groupPosition);

        if(convertView == null) {
            if(groupPosition == 0) {

                final Talk val = (Talk) getChild(groupPosition, childPosition);
                convertView = inflater.inflate(R.layout.row_about_talk, null);
                holderAbout = new ViewHolderItemAbout();
                convertView.setTag(holderAbout);

                holderAbout.about = (TextView)convertView.findViewById(R.id.txt_about_talk);
                holderAbout.about.setText(val.getDescricao());

            }
            if(groupPosition == 1) {

                final Speaker val = (Speaker) getChildPalestrante(groupPosition, childPosition);
                convertView = inflater.inflate(R.layout.row_speaker, null);
                holderPalestrante = new ViewHolderItemPalestrante();
                convertView.setTag(holderPalestrante);
                holderPalestrante.name = (TextView)convertView.findViewById(R.id.txtNome_palestrante);
                holderPalestrante.name.setText(val.getNome());

                holderPalestrante.about_speaker = (TextView)convertView.findViewById(R.id.txtObs);
                holderPalestrante.about_speaker.setText(val.getDescricao_palestrante());

            }
            if(groupPosition == 2) {

                final Question val = (Question) getChildQuestion(groupPosition, childPosition);
                convertView = inflater.inflate(R.layout.row_question, null);
                holderQuestion = new ViewHolderItemQuestion();
                convertView.setTag(holderQuestion);

                holderQuestion.question = (TextView)convertView.findViewById(R.id.txt_question_talk);
                holderQuestion.question.setText(val.getQuestion());

                holderQuestion.speaker = (TextView)convertView.findViewById(R.id.txt_palestrante_talk);
                holderQuestion.speaker.setText(val.getSpeaker());

                holderQuestion.date = (TextView)convertView.findViewById(R.id.txt_date_hora_talk);
                holderQuestion.date.setText(util.getDateHour(val.getDate()));

            }

        } else{

            if(groupPosition == 0) {
                final Talk val = (Talk) getChild(groupPosition, childPosition);
                convertView = inflater.inflate(R.layout.row_about_talk, null);
                holderAbout = new ViewHolderItemAbout();
                convertView.setTag(holderAbout);

                holderAbout.about = (TextView)convertView.findViewById(R.id.txt_about_talk);
                holderAbout.about.setText(val.getDescricao());

            }
            if(groupPosition == 1) {
                final Speaker val = (Speaker) getChildPalestrante(groupPosition, childPosition);
                convertView = inflater.inflate(R.layout.row_speaker, null);
                holderPalestrante = new ViewHolderItemPalestrante();
                convertView.setTag(holderPalestrante);

                holderPalestrante.name = (TextView)convertView.findViewById(R.id.txtNome_palestrante);
                holderPalestrante.name.setText(val.getNome());

                holderPalestrante.about_speaker = (TextView)convertView.findViewById(R.id.txtObs);
                holderPalestrante.about_speaker.setText(val.getDescricao_palestrante());

            }
            if(groupPosition == 2) {
                final Question val = (Question) getChildQuestion(groupPosition, childPosition);
                convertView = inflater.inflate(R.layout.row_question, null);
                holderQuestion = new ViewHolderItemQuestion();
                convertView.setTag(holderQuestion);

                holderQuestion.question = (TextView)convertView.findViewById(R.id.txt_question_talk);
                holderQuestion.question.setText(val.getQuestion());

                holderQuestion.speaker = (TextView)convertView.findViewById(R.id.txt_palestrante_talk);
                holderQuestion.speaker.setText(val.getSpeaker());

                holderQuestion.date = (TextView)convertView.findViewById(R.id.txt_date_hora_talk);
                holderQuestion.date.setText(util.getDateHour(val.getDate()));

            }
        }

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    class ViewHolderGroup {
        TextView tvGroup;
    }

    class ViewHolderItemAbout {
        TextView about;
    }

    class ViewHolderItemPalestrante {
        TextView name;
        TextView about_speaker;
    }

    class ViewHolderItemQuestion {
        TextView question;
        TextView speaker;
        TextView date;
    }

}

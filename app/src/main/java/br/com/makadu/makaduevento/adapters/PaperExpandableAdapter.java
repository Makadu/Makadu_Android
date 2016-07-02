package br.com.makadu.makaduevento.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.com.makadu.makaduevento.R;
import br.com.makadu.makaduevento.model.Paper;


/**
 * Created by lucasschwalbeferreira on 11/04/16.
 */
public class PaperExpandableAdapter extends BaseExpandableListAdapter {

    private List<Paper> papers;
    private List<String> listGroup;
    private HashMap<String, Paper> listData;
    private LayoutInflater inflater;
    private String eventId;
    private String username;

    public PaperExpandableAdapter(Context context, List<String> listGroup, HashMap<String, Paper> listData,
                                  ArrayList<Paper> papers, String eventId, String username) {
        this.papers = papers;
        this.listGroup = listGroup;
        this.listData = listData;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.eventId = eventId;
        this.username = username;
    }

    @Override
    public int getGroupCount() {
        return listGroup.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return listGroup.get(groupPosition);
    }

    @Override
    public Paper getChild(int groupPosition, int childPosition) {
        Log.d("erro_ex", "groupPosition: " + groupPosition + " childPosition: " + childPosition);
        Log.d("erro_ex", "autor: " + listData.get(listGroup.get(groupPosition)).authors);
        Log.d("erro_ex", "titulo: " + listData.get(listGroup.get(groupPosition)).title);

        return listData.get(listGroup.get(groupPosition));
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
            convertView = inflater.inflate(R.layout.header_expandable_list_view_paper, null);
            holder = new ViewHolderGroup();
            convertView.setTag(holder);

            holder.tvGroup = (TextView) convertView.findViewById(R.id.txt_header_paper);
        }
        else {
            holder = (ViewHolderGroup) convertView.getTag();
        }

        //final Paper val = (Paper) getChild(groupPosition, childPosition);

        holder.tvGroup.setText(listGroup.get(groupPosition));

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition,int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final ViewHolderItem holder;

        Log.d("erro_ex", "childPosition: " + childPosition + " groupPosition: " + groupPosition);

        if(convertView == null) {
            convertView = inflater.inflate(R.layout.row_paper, null);
            holder = new ViewHolderItem();
            convertView.setTag(holder);

            holder.title = (TextView)convertView.findViewById(R.id.txtNome_Titulo);
            holder.authors = (TextView)convertView.findViewById(R.id.txtNome_Autor);
            holder.Abstract = (TextView)convertView.findViewById(R.id.txtDescricaoPaper);

        }
        else {
            holder = (ViewHolderItem) convertView.getTag();
        }

        final Paper val = (Paper) getChild(groupPosition, childPosition);

        holder.title.setText(val.title);
        holder.authors.setText(val.authors);
        holder.Abstract.setText(val.Abstract);

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    class ViewHolderGroup {
        TextView tvGroup;
    }

    class ViewHolderItem {
        TextView title;
        TextView authors;
        TextView Abstract;

    }

    public void setData(List<Paper> paper_filter,List<String> lGroup,HashMap<String, Paper> lData) {
        papers.clear();
        papers.addAll(paper_filter);

        listGroup.clear();
        listGroup.addAll(lGroup);

        listData.clear();
        listData.putAll(lData);

        this.papers = paper_filter;
        this.notifyDataSetChanged();
    }

}

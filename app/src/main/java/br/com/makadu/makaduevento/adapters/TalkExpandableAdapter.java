package br.com.makadu.makaduevento.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.com.makadu.makaduevento.R;
import br.com.makadu.makaduevento.Util.Email;
import br.com.makadu.makaduevento.Util.Question_talk;
import br.com.makadu.makaduevento.model.Favorites;
import br.com.makadu.makaduevento.model.Talk;

/**
 * Created by lucasschwalbeferreira on 08/04/15.
 */
public class TalkExpandableAdapter extends BaseExpandableListAdapter {

    private List<Talk> programacoes;
    private List<String> listGroup;
    private HashMap<String, List<Talk>> listData;
    private LayoutInflater inflater;

    public TalkExpandableAdapter(Context context, List<String> listGroup, HashMap<String, List<Talk>> listData,ArrayList<Talk> programacoes) {
        this.programacoes = programacoes;
        this.listGroup = listGroup;
        this.listData = listData;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getGroupCount() {
        return listGroup.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return listData.get(listGroup.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return listGroup.get(groupPosition);
    }

    @Override
    public Talk getChild(int groupPosition, int childPosition) {
        return listData.get(listGroup.get(groupPosition)).get(childPosition);
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
            convertView = inflater.inflate(R.layout.header_expandable_list_view_programacao, null);
            holder = new ViewHolderGroup();
            convertView.setTag(holder);

            holder.tvGroup = (TextView) convertView.findViewById(R.id.txt_header_programacao);
        }
        else {
            holder = (ViewHolderGroup) convertView.getTag();
        }

        holder.tvGroup.setText(listGroup.get(groupPosition));

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition,int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final ViewHolderItem holder;

        Log.d("erro_ex", "childPosition: " + childPosition + " groupPosition: " + groupPosition);

        if(convertView == null) {
            convertView = inflater.inflate(R.layout.row_talk, null);
            holder = new ViewHolderItem();
            convertView.setTag(holder);

            holder.horaIni1 = (TextView)convertView.findViewById(R.id.txt_programacao_hora_ini_1);
            holder.titulo = (TextView)convertView.findViewById(R.id.txtTitulo);
            holder.local = (TextView)convertView.findViewById(R.id.txt_Local_programacao);
            holder.palestrantes = (TextView)convertView.findViewById(R.id.txt_Palestrante_programacao);
            holder.favorite = (ImageView)convertView.findViewById(R.id.img_star_favoritar);

            holder.linear_download = (LinearLayout)convertView.findViewById(R.id.linear_download_programacao);
            holder.linear_question = (LinearLayout)convertView.findViewById(R.id.linear_pergunta_programacao);
            holder.linear_favorite = (LinearLayout)convertView.findViewById(R.id.linear_favoritar_programacao);

        }
        else {
            holder = (ViewHolderItem) convertView.getTag();
        }

        final Talk val = (Talk) getChild(groupPosition, childPosition);
        final Favorites favorites = new Favorites(convertView.getContext());

        holder.linear_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    new Email().sendEmailConteudoProgramacao(v.getContext(), val);
                }catch (Exception e){
                    Toast.makeText(v.getContext(),"Ocorreu algum erro no DOWNLOAD!!!",Toast.LENGTH_LONG).show();
                }
            }
        });

        holder.linear_question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 new Question_talk().question(v.getContext(), val.getId());
            }
        });

        final LinearLayout favoriteButton = holder.linear_favorite;

        if (favorites.contains(val)) {
            holder.favorite.setImageResource(android.R.drawable.star_big_on);
        } else {
            holder.favorite.setImageResource(android.R.drawable.btn_star_big_off);
        }

        favoriteButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (favorites.contains(val)) {
                    holder.favorite.setImageResource(android.R.drawable.btn_star_big_off);
                    favorites.remove((Talk)val);
                } else {
                    holder.favorite.setImageResource(android.R.drawable.star_big_on);
                    favorites.add((Talk)val);
                }
            }
        });

        holder.horaIni1.setText(val.getHoraInicio());
        holder.titulo.setText(val.getTitulo());
        holder.local.setText(val.getLocal() + " - " + val.getHoraInicio() + " às " + val.getHoraFim());
        holder.palestrantes.setText(val.retornaPalestrantesList());

        if(!val.isAllow_file()) {
            holder.linear_download.setVisibility(LinearLayout.GONE);
        } else {
            holder.linear_download.setVisibility(LinearLayout.VISIBLE);
        }

        if(!val.isAllow_question()) {
            holder.linear_question.setVisibility(LinearLayout.GONE);
        }
        else {
            holder.linear_question.setVisibility(LinearLayout.VISIBLE);
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

    class ViewHolderItem {
        TextView horaIni1;
        TextView horaIni;
        TextView horaFim;
        TextView titulo;
        TextView local;
        TextView palestrantes;
        ImageView favorite;
        LinearLayout linear_download;
        LinearLayout linear_question;
        LinearLayout linear_favorite;
    }

    public void setData(List<Talk> talk_filter,List<String> lGroup,HashMap<String, List<Talk>> lData) {
        programacoes.clear();
        programacoes.addAll(talk_filter);

        listGroup.clear();
        listGroup.addAll(lGroup);

        listData.clear();
        listData.putAll(lData);

        this.programacoes = talk_filter;
        this.notifyDataSetChanged();
    }

}

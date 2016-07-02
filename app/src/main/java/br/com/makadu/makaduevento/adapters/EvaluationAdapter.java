package br.com.makadu.makaduevento.adapters;

import android.content.Context;
import android.os.AsyncTask;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.com.makadu.makaduevento.R;
import br.com.makadu.makaduevento.model.FeedbackGet;

/**
 * Created by lucasschwalbeferreira on 4/18/16.
 */
public class EvaluationAdapter extends ArrayAdapter<FeedbackGet> {

    private Context context;
    private List<FeedbackGet> feedbacks = null;
    int layoutResourceId;

    public EvaluationAdapter(Context context,int resource, List<FeedbackGet> feedbacks) {
        super(context,resource, feedbacks);

        this.feedbacks = feedbacks;
        this.context = context;
        layoutResourceId = resource;
    }

    @Override
    public View getView(final int position,View convertView, ViewGroup parent) {
        final ViewHolderItem holder;
        FeedbackGet feedback = feedbacks.get(position);

        View view = convertView;
        if(view == null){
            LayoutInflater vi;
            vi = LayoutInflater.from(context);
            view = vi.inflate(layoutResourceId, parent, false);
            holder = new ViewHolderItem();
            view.setTag(holder);

            holder.nome = (TextView)view.findViewById(R.id.txt_evaluation_question_name);
            holder.ratingValue = (RatingBar)view.findViewById(R.id.ratingBar_evaluation);
            holder.Descricao = (EditText)view.findViewById(R.id.edt_Descricao_rating);

            view.setTag(holder);
        } else{
            holder = (ViewHolderItem) view.getTag();
        }

        TextView nome = (TextView)view.findViewById(R.id.txt_evaluation_question_name);

        if(feedback != null){
            nome.setText(feedback.value);

        }

        return view;
    }

    class ViewHolderItem {
        TextView nome;
        EditText Descricao;
        RatingBar ratingValue;
    }

    @Override
    public int getCount() {
        return feedbacks.size();
    }

    public void setData(List<FeedbackGet> data) {
        this.clear();
        this.addAll(data);
        this.feedbacks = data;
        this.notifyDataSetChanged();
    }
}

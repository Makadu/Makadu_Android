package br.com.makadu.makaduevento.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import br.com.makadu.makaduevento.R;
import br.com.makadu.makaduevento.model.Notice;


/**
 * Created by lucasschwalbeferreira on 17/02/15.
 */
public class NoticeAdapter extends ArrayAdapter<Notice> {

    private Context context;
    private List<Notice> notices = null;

    public NoticeAdapter(Context context, List<Notice> notices) {
        super(context,0, notices);

        this.notices = notices;
        this.context = context;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        Notice notice = notices.get(position);

        if(view == null)
        {
            view = LayoutInflater.from(context).inflate(R.layout.row_notices, null);
        }

        TextView name_notice = (TextView)view.findViewById(R.id.txt_title_notice);
        TextView datail = (TextView)view.findViewById(R.id.txt_detail_notice);

        name_notice.setText(notices.get(position).getName_notice());
        datail.setText(notices.get(position).getDetail());

        return view;
    }

    @Override
    public int getCount() {
        return notices.size();
    }

}

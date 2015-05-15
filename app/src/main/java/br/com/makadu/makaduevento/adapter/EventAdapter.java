package br.com.makadu.makaduevento.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import br.com.makadu.makaduevento.R;
import br.com.makadu.makaduevento.model.Event;

/**
 * Created by lucasschwalbeferreira on 17/02/15.
 */
public class EventAdapter extends ArrayAdapter<Event> {

    private Context context;
    List<Event> events = null;
    int layoutResourceId;

    public EventAdapter(Context context,int resource, List<Event> events) {
        super(context,resource, events);

        this.events = events;
        this.context = context;
        layoutResourceId = resource;
    }


    @Override
    public View getView(final int position,View convertView, ViewGroup parent) {

        Event event = events.get(position);

        View view = convertView;
        if(view == null){
            LayoutInflater vi;
            vi = LayoutInflater.from(context);
            view = vi.inflate(layoutResourceId, parent, false);
        }

        TextView nome = (TextView)view.findViewById(R.id.txt_principal_Nome);
        TextView cidade = (TextView)view.findViewById(R.id.txtCidade);
        TextView dataini = (TextView)view.findViewById(R.id.txtDataInicio);
        TextView datafim = (TextView)view.findViewById(R.id.txtDataFim);
        ImageView img_pat = (ImageView)view.findViewById(R.id.img_patrocinador);

        nome.setText(events.get(position).getName());
        cidade.setText(events.get(position).getCity());
        dataini.setText(events.get(position).getStart_date());
        datafim.setText(events.get(position).getEnd_date());

        byte[] img_byte_pat = events.get(position).getFile_img_patronage();
        if(img_byte_pat != null){
            Bitmap bitmap_pat = BitmapFactory.decodeByteArray(img_byte_pat, 0, img_byte_pat.length);
            img_pat.setImageBitmap(bitmap_pat);
        }

        return view;
    }

    @Override
    public int getCount() {
        return events.size();
    }

    public void setData(List<Event> data){
        this.clear();
        this.addAll(data);
        this.events = data;
        this.notifyDataSetChanged();
    }

}

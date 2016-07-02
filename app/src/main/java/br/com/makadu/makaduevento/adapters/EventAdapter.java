package br.com.makadu.makaduevento.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import br.com.makadu.makaduevento.DAO.dao.entityDao.EventDao;
import br.com.makadu.makaduevento.R;
import br.com.makadu.makaduevento.Util.PicassoBigCache;
import br.com.makadu.makaduevento.Util.Util;
import br.com.makadu.makaduevento.model.Event;

/**
 * Created by lucasschwalbeferreira on 17/02/15.
 */
public class EventAdapter extends ArrayAdapter<Event> {

    private Context context;
    private List<Event> events = null;
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

        if(event != null){
            nome.setText(event.getTitle());
            cidade.setText(event.getCity());
            dataini.setText(new Util().convertStringtoStringDateBrazil(event.getStart_date()));
            datafim.setText(new Util().convertStringtoStringDateBrazil(event.getEnd_date()));
        }

        try {
            PicassoBigCache.INSTANCE.getPicassoBigCache(context).load(events.get(position).logo).into(img_pat);
            //Resources res = getContext().getResources();
            //Bitmap b = BitmapFactory.decodeResource(res,R.drawable.teste_event);
            //img_pat.setImageBitmap(b);

        }catch (Exception e) {
            Log.e("log_Erro",e.getMessage());
        }

        return view;
    }

    @Override
    public int getCount() {
        return events.size();
    }

    public void setData(List<Event> data) {
        this.clear();
        this.addAll(data);
        this.events = data;
        this.notifyDataSetChanged();
    }

}

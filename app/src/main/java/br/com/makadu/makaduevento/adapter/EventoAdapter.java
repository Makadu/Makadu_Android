package br.com.makadu.makaduevento.adapter;

import br.com.makadu.makaduevento.R;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.com.makadu.makaduevento.model.Event;

/**
 * Created by lucasschwalbeferreira on 05/02/15.
 */
public class EventoAdapter extends RecyclerView.Adapter<EventoAdapter.ViewHolder> {
    private List<Event> events;
    private List<Event> visibleObjects;

    public void flushFilter(){
        visibleObjects=new ArrayList<>();
        visibleObjects.addAll(events);
        notifyDataSetChanged();
    }

    public void setFilter(String queryText) {

        visibleObjects = new ArrayList<>();
        //constraint = constraint.toString().toLowerCase();
        for (Event item: events) {
            if (item.toString().toLowerCase().contains(queryText))
                visibleObjects.add(item);
        }
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View view;
        public ViewHolder(View v) {
            super(v);
            view = v;
        }

    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public EventoAdapter(List<Event> events) {
        this.events = events;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public EventoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_evento, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        TextView nome = (TextView)holder.view.findViewById(R.id.txt_principal_Nome);
        TextView cidade = (TextView)holder.view.findViewById(R.id.txtCidade);
        TextView dataini = (TextView)holder.view.findViewById(R.id.txtDataInicio);
        TextView datafim = (TextView)holder.view.findViewById(R.id.txtDataFim);
        ImageView img_pat = (ImageView)holder.view.findViewById(R.id.img_patrocinador);

        nome.setText(events.get(position).getName());
        cidade.setText(events.get(position).getCity());
        dataini.setText(events.get(position).getStart_date());
        datafim.setText(events.get(position).getEnd_date());

        //events.get(position).getFile_img_patronage()

        byte[] img_byte_pat = events.get(position).getFile_img_patronage();
        if(img_byte_pat != null){
            Bitmap bitmap_pat = BitmapFactory.decodeByteArray(img_byte_pat, 0, img_byte_pat.length);
            img_pat.setImageBitmap(bitmap_pat);
        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return events.size();
    }

    public void setData(List<Event> data){


    }
}

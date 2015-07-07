package br.com.makadu.makaduevento.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import br.com.makadu.makaduevento.R;
import br.com.makadu.makaduevento.model.Speaker;

/**
 * Created by lucasschwalbeferreira on 17/02/15.
 */
public class SpeakerAdapter extends ArrayAdapter<Speaker> {

    private Context context;
    private List<Speaker> speakers = null;

    public SpeakerAdapter(Context context, List<Speaker> speakers) {
        super(context,0, speakers);

        this.speakers = speakers;
        this.context = context;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        Speaker speaker = speakers.get(position);

        if(view == null)
        {
            view = LayoutInflater.from(context).inflate(R.layout.row_speaker, null);
        }

        TextView nomePalestrante = (TextView)view.findViewById(R.id.txtNome_palestrante);
        TextView obsPalestrante = (TextView)view.findViewById(R.id.txtObs);

        nomePalestrante.setText(speakers.get(position).getNome());
        obsPalestrante.setText(speakers.get(position).getDescricao_palestrante());

        return view;
    }

    @Override
    public int getCount() {
        return speakers.size();
    }

}

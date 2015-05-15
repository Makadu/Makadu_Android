package br.com.makadu.makaduevento.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.com.makadu.makaduevento.R;
import br.com.makadu.makaduevento.model.Palestrante;

/**
 * Created by lucasschwalbeferreira on 17/02/15.
 */
public class PalestranteAdapter extends ArrayAdapter<Palestrante> {

    private Context context;
    private List<Palestrante> palestrantes = null;

    public PalestranteAdapter(Context context, List<Palestrante> palestrantes) {
        super(context,0, palestrantes);

        this.palestrantes = palestrantes;
        this.context = context;
    }


    @Override
    public View getView(int position, View view, ViewGroup parent) {

        Palestrante palestrante = palestrantes.get(position);

        if(view == null)
        {
            view = LayoutInflater.from(context).inflate(R.layout.row_palestrante, null);
        }

        TextView nomePalestrante = (TextView)view.findViewById(R.id.txtNome_palestrante);
        TextView obsPalestrante = (TextView)view.findViewById(R.id.txtObs);

        nomePalestrante.setText(palestrantes.get(position).getNome());
        obsPalestrante.setText(palestrantes.get(position).getDescricao_palestrante());

        return view;
    }

    @Override
    public int getCount() {
        return palestrantes.size();
    }


}

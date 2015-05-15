package br.com.makadu.makaduevento.adapter;

import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.makadu.makaduevento.R;
import br.com.makadu.makaduevento.UI.DetalheProgramacaoActivity;
import br.com.makadu.makaduevento.model.Palestrante;
import br.com.makadu.makaduevento.model.Programacao;

/**
 * Created by lucasschwalbeferreira on 17/02/15.
 */
public class ProgramacaoAdapter extends RecyclerView.Adapter<ProgramacaoAdapter.ViewHolder> {

    private ArrayList<Programacao> programacoes;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View view;
        public ViewHolder(View v) {
            super(v);
            view = v;
        }

    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ProgramacaoAdapter(ArrayList<Programacao> programacoes) {
        this.programacoes = programacoes;
    }

    @Override
    public ProgramacaoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_programacao, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        TextView horaIni1 = (TextView)holder.view.findViewById(R.id.txt_programacao_hora_ini_1);
        TextView horaIni = (TextView)holder.view.findViewById(R.id.txt_programacao_hora_ini);
        TextView horaFim = (TextView)holder.view.findViewById(R.id.txt_programacao_hora_fim);
        TextView titulo = (TextView)holder.view.findViewById(R.id.txtTitulo);
        TextView local = (TextView)holder.view.findViewById(R.id.txt_Local_programacao);
        TextView palestrantes = (TextView)holder.view.findViewById(R.id.txt_Palestrante_programacao);


        horaIni1.setText(programacoes.get(position).getHoraInicio());
        horaIni.setText(programacoes.get(position).getHoraInicio());
        horaFim.setText(programacoes.get(position).getHoraFim());
        titulo.setText(programacoes.get(position).getTitulo());
        local.setText(programacoes.get(position).getLocal() + " -");

        palestrantes.setText(programacoes.get(position).retornaPalestrantesList());


        CardView card_evento =(CardView)holder.view.findViewById(R.id.card_view_programacao);

        card_evento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(v.getContext(), "Clicou na Programacao de id: " + programacoes.get(position).getId(), Toast.LENGTH_LONG).show();

                Intent intent = new Intent(holder.view.getContext(), DetalheProgramacaoActivity.class);

                Programacao pg = programacoes.get(position);

                intent.putExtra("id", pg.getId());

                holder.view.getContext().startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() { return programacoes.size(); }


}

package br.feevale.ameacasambientais;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by adonis on 17/10/16.
 */
public class ListAmeacasAdapter extends BaseAdapter {

    private List<Ameaca> ameacas_ambientais;
    private AmeacasAmbientaisActivity ctx;
    AmeacaDBDAO db;
    public ListAmeacasAdapter(AmeacasAmbientaisActivity ctx){
        this.ctx = ctx;
        db = ctx.db;
        ameacas_ambientais = db.recuperarAmeacas();
    }

    @Override
    public int getCount() {
        ameacas_ambientais = db.recuperarAmeacas();
        return ameacas_ambientais.size();
    }

    @Override
    public Object getItem(int position) {
        ameacas_ambientais = db.recuperarAmeacas();
        return ameacas_ambientais.get(position);
    }

    @Override
    public long getItemId(int position) {
        ameacas_ambientais = db.recuperarAmeacas();
        if(ameacas_ambientais.size() == 0){
            return 0;
        }
        return ameacas_ambientais.get(position).getId();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ameacas_ambientais = db.recuperarAmeacas();
        LayoutInflater inflater = ctx.getLayoutInflater();
        View row;
        row = inflater.inflate(R.layout.ameaca_item, parent, false);

        TextView txtDescricao = (TextView)row.findViewById(R.id.txt_descricao);
        TextView txtEndereco = (TextView)row.findViewById(R.id.txt_endereco);
        TextView txtBairoo = (TextView)row.findViewById(R.id.txt_bairro);
        TextView txtPotencialRisco = (TextView)row.findViewById(R.id.txt_potencial_risco);
        txtDescricao.setText(ameacas_ambientais.get(position).getDescricao());
        txtEndereco.setText(ameacas_ambientais.get(position).getEndereco());
        txtBairoo.setText(ameacas_ambientais.get(position).getBairro());
        txtPotencialRisco.setText(ameacas_ambientais.get(position).getPotencialImpacto().toString());
        return row;
    }
}

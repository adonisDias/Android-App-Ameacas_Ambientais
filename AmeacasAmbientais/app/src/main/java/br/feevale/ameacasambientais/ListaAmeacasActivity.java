package br.feevale.ameacasambientais;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class ListaAmeacasActivity extends AppCompatActivity {

    ListView listAmeacas;
    AmeacaDBDAO db;
    ListAmeacasAdapterFull ameacasAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_ameacas);

        listAmeacas = (ListView) findViewById(R.id.listaAmeacasNovo);
        Intent it = getIntent();

        db = new AmeacaDBDAO(this);
        db.abrir();
        ameacasAdapter = new ListAmeacasAdapterFull(this);
        listAmeacas.setAdapter(ameacasAdapter);

        listAmeacas.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("ID",""+id);
                db.deletarAmeaca((int) id);
                ameacasAdapter.notifyDataSetChanged();
                return true;
            }
        });

    }
}

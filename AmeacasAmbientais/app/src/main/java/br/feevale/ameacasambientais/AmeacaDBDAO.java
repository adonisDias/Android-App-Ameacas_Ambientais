package br.feevale.ameacasambientais;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by adonis on 17/10/16.
 */
public class AmeacaDBDAO {
    private static final String NOME_BANCO = "ameaca.db";
    private static final String TAB_AMEACAS_AMBIENTAIS = "AMEACAS_AMBIENTAIS";
    private static final int VERSAO_BANCO = 1;

    private static final String COL_ID = "ID";
    private static final String COL_DESCRICAO = "DESCRICAO";
    private static final String COL_ENDERECO = "ENDERECO";
    private static final String COL_BAIRRO = "BAIRRO";
    private static final String COL_POTENCIAL_IMPACTO = "POTENCIAL_IMPACTO";

    private static final String SQL_TAB_AMEACAS_AMBIENTAIS = "create table " + TAB_AMEACAS_AMBIENTAIS +
            "(" + COL_ID + " integer primary key autoincrement," +
            COL_DESCRICAO + " text not null," +
            COL_ENDERECO + " text not null," +
            COL_BAIRRO + " text not null," +
            COL_POTENCIAL_IMPACTO + " int not null)";

    private ameacaDBHelper dbHelper;
    private SQLiteDatabase db;
    private Context ctx;

    public AmeacaDBDAO(Context ctx){
        dbHelper = new ameacaDBHelper(ctx, NOME_BANCO, null, VERSAO_BANCO);
        this.ctx = ctx;
    }

    public void abrir(){
        db = dbHelper.getWritableDatabase();
    }

    public void fechar(){
        db.close();
    }

    public void adicionarAmeaca(Ameaca a){
        ContentValues values = new ContentValues();
        values.put(COL_DESCRICAO, a.getDescricao());
        values.put(COL_ENDERECO, a.getEndereco());
        values.put(COL_BAIRRO, a.getBairro());
        values.put(COL_POTENCIAL_IMPACTO, a.getPotencialImpacto());
        db.insert(TAB_AMEACAS_AMBIENTAIS, null, values);
    }

    public List<Ameaca> recuperarAmeacas(){
        List<Ameaca> ameacas_ambientais = new ArrayList<Ameaca>();
        Ameaca ameaca;
        String[] colunas = {COL_ID, COL_DESCRICAO, COL_ENDERECO, COL_BAIRRO, COL_POTENCIAL_IMPACTO};
        Cursor cursor = db.query(TAB_AMEACAS_AMBIENTAIS, colunas,null,null,null,null,COL_ID);
        if(cursor.getCount() == 0){
            return ameacas_ambientais;
        }

        cursor.moveToFirst();
        do{
            ameaca = new Ameaca();
            ameaca.setId(cursor.getInt(cursor.getColumnIndex(COL_ID)));
            ameaca.setDescricao(cursor.getString(cursor.getColumnIndex(COL_DESCRICAO)));
            ameaca.setEndereco(cursor.getString(cursor.getColumnIndex(COL_ENDERECO)));
            ameaca.setBairro(cursor.getString(cursor.getColumnIndex(COL_BAIRRO)));
            ameaca.setPotencialImpacto(cursor.getInt(cursor.getColumnIndex(COL_POTENCIAL_IMPACTO)));
            ameacas_ambientais.add(ameaca);
        }while(cursor.moveToNext());
        return ameacas_ambientais;
    }

    public Integer deletarAmeaca(int id){
        return db.delete(TAB_AMEACAS_AMBIENTAIS, COL_ID + "=" + id, null);
    }
    public Integer deletarAmeacas(){
        return db.delete(TAB_AMEACAS_AMBIENTAIS, null, null);
    }

    private static class ameacaDBHelper extends SQLiteOpenHelper {

        public ameacaDBHelper(Context ctx, String nome, SQLiteDatabase.CursorFactory factory, int versao){
            super(ctx, nome, factory, versao);

        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SQL_TAB_AMEACAS_AMBIENTAIS);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TAB_AMEACAS_AMBIENTAIS);
            onCreate(db);
        }
    }
}

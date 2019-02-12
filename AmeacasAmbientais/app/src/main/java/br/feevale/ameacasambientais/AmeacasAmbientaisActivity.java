package br.feevale.ameacasambientais;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class AmeacasAmbientaisActivity extends AppCompatActivity {

    public static final String TAG_LOG = "LOG";

    ListView listAmeacas;
    ListAmeacasAdapter ameacasAdapter;
    EditText txtDescricao;
    EditText txtEndereco;
    EditText txtBairro;
    EditText txtPotencialRisco;
    AmeacaDBDAO db;
    Ameaca ameaca;
    String json;
    private List<Ameaca> ameacas_ambientais;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ameacas_ambientais);

        txtDescricao = (EditText) findViewById(R.id.txtDescricaoAmeaca);
        txtEndereco = (EditText) findViewById(R.id.txtEndereco);
        txtBairro = (EditText) findViewById(R.id.txtBairro);
        txtPotencialRisco = (EditText) findViewById(R.id.txtPotencialAmeaca);
        listAmeacas = (ListView) findViewById(R.id.listaAmeacas);

        db = new AmeacaDBDAO(this);
        db.abrir();

        ameacasAdapter = new ListAmeacasAdapter(this);
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


    public void addAmeaca(View view) {
        Ameaca a = new Ameaca();
        a.setDescricao(txtDescricao.getText().toString());
        a.setEndereco(txtEndereco.getText().toString());
        a.setBairro(txtBairro.getText().toString());
        a.setPotencialImpacto(Integer.parseInt(String.valueOf(txtPotencialRisco.getText())));
        db.adicionarAmeaca(a);
        Log.d(TAG_LOG,"adicionou ameaca");
        ameacasAdapter.notifyDataSetChanged();
    }

    public void showAmeacas(View view) {

        ameacasAdapter = new ListAmeacasAdapter(this);
        listAmeacas.setAdapter(ameacasAdapter);
        Log.d("MSG","listar ameacas");
    }

    public void listaAmeacasNovo(View view){

        Intent it = new Intent(getBaseContext(),ListaAmeacasActivity.class);
        startActivity(it);

    }

    @Override
    public void onResume() {
        super.onResume();

        ameacasAdapter.notifyDataSetChanged();
    }

    public void enviarParaServidor(View v){
        EnviaServidorTask task = new EnviaServidorTask();
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public void buscarParaServidor(View v){
        new HTTPAsyncRequest().execute("http://10.0.2.2/content.txt");

    }

    public String jsonCreateSample(){
        //Exemplo de criação de um JSon para duas tabelas.
        JSONObject tabelas = new JSONObject();
        JSONArray tabAmeacas = new JSONArray();

        JSONObject instanciaTabAmeacas;

        for (int i = 0; i < ameacas_ambientais.size(); i++) {
            try {
                instanciaTabAmeacas = new JSONObject();
                ameaca = ameacas_ambientais.get(i);

                instanciaTabAmeacas.put("id",ameaca.getId().toString());
                instanciaTabAmeacas.put("descricao",ameaca.getDescricao());
                instanciaTabAmeacas.put("endereco",ameaca.getEndereco());
                instanciaTabAmeacas.put("bairro",ameaca.getBairro());
                instanciaTabAmeacas.put("potencial_impacto",ameaca.getPotencialImpacto());

                tabAmeacas.put(instanciaTabAmeacas);

                tabelas.put("ameacas", tabAmeacas);

            } catch (Exception e){
                e.printStackTrace();
            }
        }

        Log.d("JSON",tabelas.toString());
        return tabelas.toString();
    }


    public void jsonReconstructSample(String txt){
        //Exemplo de recuperação e processamento do JSon criado pela operação anterior.
        //Precisa ser adaptado para fazer as inserções no banco de dados.
        //Observar as impressões no log.
        JSONObject tabelas;
        try {
            tabelas = new JSONObject(txt);
            Log.d("JSON_RECONSTRUCT",tabelas.toString());
            JSONArray tabAmeacas = tabelas.getJSONArray("ameacas");
            JSONObject instancia;

            for(int i = 0; i < tabAmeacas.length(); i++){
                instancia = tabAmeacas.getJSONObject(i);
                ameaca = new Ameaca();
                ameaca.setId(Integer.parseInt(instancia.get("id").toString()));
                ameaca.setDescricao(instancia.get("descricao").toString());
                ameaca.setBairro(instancia.get("bairro").toString());
                ameaca.setEndereco(instancia.get("endereco").toString());
                ameaca.setPotencialImpacto(Integer.parseInt(instancia.get("potencial_impacto").toString()));
                db.adicionarAmeaca(ameaca);
                //Log.d("id",instancia.get("id").toString());
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    class EnviaServidorTask extends AsyncTask<Void, Integer, String>{

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(Void... params) {
            Log.d("ASYNC","doInBackground");
            String result = " ";
            ameacas_ambientais = db.recuperarAmeacas();
            String json = jsonCreateSample();
            try {
                URL url = new URL("http://10.0.2.2/post_content.php");
                String content = json; //"O conteudo...";
                String urlParameters  = "content=" + content;
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
                wr.writeBytes(urlParameters);
                wr.flush();
                wr.close();

                int responseCode = conn.getResponseCode();
                result = responseCode + "";
            }catch (Exception e){
                Log.d("EXP",e.getMessage());
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String value) {

        }

        @Override
        protected void onProgressUpdate(Integer... values) {

        }
    }


    private class HTTPAsyncRequest extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... input) {
            try {
                db.deletarAmeacas();
                return downloadUrl(input[0]);
            } catch (IOException e) {
                e.printStackTrace();
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            Log.d("RESULT",result);
            json = result.toString();
            jsonReconstructSample(json);
            ameacasAdapter.notifyDataSetChanged();
        }
    }

    private String downloadUrl(String urlString) throws IOException {
        InputStream is = null;
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            int response = conn.getResponseCode();
            Log.d("CODE", "The response is: " + response);
            is = conn.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(is));
            String contentAsString = in.readLine();
            return contentAsString;
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }
}

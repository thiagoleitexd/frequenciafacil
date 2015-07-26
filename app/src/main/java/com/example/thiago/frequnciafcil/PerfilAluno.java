package com.example.thiago.frequnciafcil;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PerfilAluno extends ActionBarActivity {

    public ImageView img;
    public Bitmap bitmap;
    public String id;
    private TextView nome,matricula,email;
    private Map<String, String> params;
    private RequestQueue rq;

    private ListView lstItems;
    private List<String> items;
    private TextView total_faltas_perfil;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_aluno);

        nome = (TextView) findViewById(R.id.RperfilNome);
        matricula = (TextView) findViewById(R.id.RperfilMatricula);
        email = (TextView) findViewById(R.id.RperfilEmail);

        String url2 = "https://www.google.com.br/logos/doodles/2015/special-olympics-world-games-2015-5710263202349056-hp.gif";

        Intent intent = getIntent();
        nome.setText(intent.getStringExtra("nome"));
        matricula.setText(intent.getStringExtra("matricula"));
        id = intent.getStringExtra("id");
        email.setText(intent.getStringExtra("email"));

        String botaoinvisivel = intent.getStringExtra("tirarbotao");


        if (botaoinvisivel != null && botaoinvisivel.equals("1")){
            Button botao = (Button) findViewById(R.id.alunoFaltou);
            botao.setVisibility(View.INVISIBLE);
        }

        //new LoadImage().execute(url);

        //inicio list view

        items = new ArrayList<String>();
        total_faltas_perfil = (TextView) findViewById(R.id.total_faltas_perfil);
        lstItems = (ListView) findViewById(R.id.lista_faltas_perfil);

        String url = MainActivityAluno.urlGeral + "listarFaltas2";
        params = new HashMap<String, String>();
        params.put("id", id);

        CustomJsonObjectResquestProf cjor = new CustomJsonObjectResquestProf(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
            //Função executada quando Houver sucesso
            @Override
            public void onResponse(JSONObject response) {
                int i;

                try {
                    JSONArray lista_data = (JSONArray) response.get("data");



                    for (i = 0; i < lista_data.length(); i++) {
                        items.add(lista_data.get(i).toString());
//                        items.add("Item 2");
//                        items.add("Item 3");
//                          System.out.println(lista_data.getJSONObject(i).get("nome"));
//                          System.out.println(lista_data.getJSONObject(i).get("matricula"));
                    }
                    total_faltas_perfil.setText(String.valueOf(lista_data.length()));
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                            android.R.layout.simple_list_item_1,
                            items){

                        @Override
                        public View getView(int position, View convertView,
                                            ViewGroup parent) {
                            View view =super.getView(position, convertView, parent);

                            TextView textView=(TextView) view.findViewById(android.R.id.text1);

                        /*YOUR CHOICE OF COLOR*/
                            textView.setTextColor(Color.parseColor("#B20000"));

                            return view;
                        }
                    };

                    lstItems.setAdapter(adapter);


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            //Função executada quando Houver Erro
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof NoConnectionError) {
                    Toast.makeText(PerfilAluno.this, "Não foi possível conectar com o servidor, verifique sua conexão de internet.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(PerfilAluno.this, error.toString(), Toast.LENGTH_SHORT).show();
                }

            }
        });

        rq = Volley.newRequestQueue(PerfilAluno.this);
        cjor.setTag("tag");
        rq.add(cjor);

        //fim












    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_perfil_aluno, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class LoadImage extends AsyncTask<String, String, Bitmap> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }
        protected Bitmap doInBackground(String... args) {
            try {
                bitmap = BitmapFactory.decodeStream((InputStream) new URL(args[0]).getContent());

            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        protected void onPostExecute(Bitmap image) {

            if(image != null){
                img = (ImageView)findViewById(R.id.img);
                img.setImageBitmap(image);

            }else{

                Toast.makeText(PerfilAluno.this, "Image Does Not exist or Network Error", Toast.LENGTH_SHORT).show();

            }
        }
    }

    public void retirarpresencaaluno (View v){

        String url = MainActivityAluno.urlGeral + "removerPresenca";
        params = new HashMap<String, String>();
        params.put("id", String.valueOf(id));

        CustomJsonObjectResquestProf cjor = new CustomJsonObjectResquestProf(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
            //Função executada quando Houver sucesso

            @Override
            public void onResponse(JSONObject response) {

                Log.i("Teste2", "Sucesso: " + response);


                Toast.makeText(PerfilAluno.this,
                        "Presença retirada com sucesso",
                        Toast.LENGTH_SHORT)
                        .show();

                Intent intent = new Intent(PerfilAluno.this, AlunosPresentes.class);
                finish();
                startActivity(intent);

            }
        }, new Response.ErrorListener() {
            //Função executada quando Houver Erro

            @Override
            public void onErrorResponse(VolleyError error) {

                if (error instanceof NoConnectionError) {
                    Toast.makeText(PerfilAluno.this, "Não foi possível conectar com o servidor, verifique sua conexão de internet.", Toast.LENGTH_LONG).show();
                }  else{
                    Toast.makeText(PerfilAluno.this, "Problema na conexão com o servidor ou com sua internet, tente mais tarde.", Toast.LENGTH_LONG).show();
                }

            }
        });

        cjor.setTag("tag");
        rq.add(cjor);

    }



}





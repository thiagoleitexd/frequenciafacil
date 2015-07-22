package com.example.thiago.frequnciafcil;

import android.app.AlertDialog;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AlunoFaltas extends ActionBarActivity {


    private Map<String, String> params;
    private RequestQueue rq;
    private ListView lstItems;
    private List<String> items;
    private TextView total_faltas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aluno_faltas);


        items = new ArrayList<String>();
        total_faltas = (TextView) findViewById(R.id.total_faltas);

        //inicio
        String url = MainActivityAluno.urlGeral + "listarFaltas";

        CustomJsonObjectResquestProf cjor = new CustomJsonObjectResquestProf(Request.Method.GET, url, params, new Response.Listener<JSONObject>() {
            //Função executada quando Houver sucesso
            @Override
            public void onResponse(JSONObject response) {
                int i;

                try {
                    JSONArray lista_data = (JSONArray) response.get("data");
                    for (i = 0; i < lista_data.length(); i++) {

                        items.add(lista_data.getJSONObject(i).toString());
//                        items.add("Item 2");
//                        items.add("Item 3");
//                          System.out.println(lista_data.getJSONObject(i).get("nome"));
//                          System.out.println(lista_data.getJSONObject(i).get("matricula"));
                    }
                    lstItems = (ListView) findViewById(R.id.lista_faltas);
                    total_faltas.setText(String.valueOf(lista_data.length()));
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                            android.R.layout.simple_list_item_1,
                            items);
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
                    Toast.makeText(AlunoFaltas.this, "Não foi possível conectar com o servidor, verifique sua conexão de internet.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(AlunoFaltas.this, error.toString(), Toast.LENGTH_SHORT).show();
                }

            }
        });

        rq = Volley.newRequestQueue(AlunoFaltas.this);
        cjor.setTag("tag");
        rq.add(cjor);

        //fim


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_aluno_faltas, menu);
        return true;
    }

    public void open(View view) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Pagela Virtual");
        alertDialogBuilder.setMessage("Versão 1.0");
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {
            case R.id.action_settings:
                open(null);
                return true;
            case R.id.action_exit:
                System.out.println("exit");
                System.exit(1);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
package com.example.thiago.frequnciafcil;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AlunosPresentes extends ActionBarActivity {


    private Map<String, String> params;
    private RequestQueue rq;
    private ListView lstItems;
    private List<String> items;
    private TextView total_presentes;
    public String [] matriculas;
    public String [] ids;
    public String [] emails;
    public String [] nomes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alunos_presentes);


        items = new ArrayList<String>();
        total_presentes = (TextView) findViewById(R.id.total_presentes);

        //inicio
        String url = MainActivityAluno.urlGeral+"listarAlunosPresentes";

        CustomJsonObjectResquestProf cjor = new CustomJsonObjectResquestProf(Request.Method.GET, url, params, new Response.Listener<JSONObject>() {
            //Função executada quando Houver sucesso
            @Override
            public void onResponse(final JSONObject response) {
                int i;
                nomes = new String[200];
                matriculas = new String[200];
                emails = new String[200];
                ids = new String[200];

                try {


                    JSONArray lista_data = (JSONArray) response.get("alunos");
                    for(i=0; i< lista_data.length(); i++){
                        items.add(lista_data.getJSONObject(i).get("nome").toString());
                        nomes[i] = String.valueOf(lista_data.getJSONObject(i).get("nome").toString());
                        matriculas[i]= String.valueOf(lista_data.getJSONObject(i).get("matricula").toString());
                        ids[i] = String.valueOf(lista_data.getJSONObject(i).get("id").toString());
                        emails[i] = String.valueOf(lista_data.getJSONObject(i).get("email").toString());

                    }
                    lstItems = (ListView) findViewById(R.id.lista_presentes);
                    total_presentes.setText(String.valueOf(lista_data.length()));
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                            android.R.layout.simple_list_item_1,
                            items){

                        @Override
                        public View getView(int position, View convertView,
                                            ViewGroup parent) {
                            View view =super.getView(position, convertView, parent);

                            TextView textView=(TextView) view.findViewById(android.R.id.text1);

                        /*YOUR CHOICE OF COLOR*/
                            textView.setTextColor(Color.parseColor("#0029A3"));

                            return view;
                        }
                    };

                    lstItems.setAdapter(adapter);
//parte referente  aos 'links' do list view
                    lstItems.setOnItemClickListener(new AdapterView.OnItemClickListener()
                    {
                        public void onItemClick(AdapterView<?> arg0, View v, int position, long id)
                        {

                            Intent intent = new Intent(AlunosPresentes.this, PerfilAluno.class);
                            intent.putExtra("nome",nomes[position]);
                            intent.putExtra("email",emails[position]);
                            intent.putExtra("matricula",matriculas[position]);
                            intent.putExtra("id",ids[position]);
                            finish();
                            startActivity(intent);

                            //retirarpresença(Integer.parseInt(ids[position]));

                        }
                    });
// fim da parte referente  aos 'links' do list view

                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }
        }, new Response.ErrorListener() {
            //Função executada quando Houver Erro
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof NoConnectionError) {
                    Toast.makeText(AlunosPresentes.this, "Não foi possível conectar com o servidor, verifique sua conexão de internet.", Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(AlunosPresentes.this, error.toString(), Toast.LENGTH_SHORT).show();
                }

            }
        });

        rq = Volley.newRequestQueue(AlunosPresentes.this);
        cjor.setTag("tag");
        rq.add(cjor);

        //fim


    }






    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_alunos_presentes, menu);
        return true;
    }

    public void open(View view){
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
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
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

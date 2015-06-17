package com.example.thiago.frequnciafcil;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends ActionBarActivity {
    private RequestQueue rq;
    private Map<String, String> params;
    private EditText login;
    private EditText password;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        url = "http://trabalhoderedes.esy.es/checkin/v1/login";
        login = (EditText) findViewById(R.id.idLogin);
        password = (EditText) findViewById(R.id.idSenha);
        rq = Volley.newRequestQueue(MainActivity.this);
    }



    public void callJsonobject(View v){
        params = new HashMap<String, String>();
        params.put("email", login.getText().toString());
        params.put("password", password.getText().toString());


        CustomJsonObjectResquest cjor = new CustomJsonObjectResquest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
            //Função executada quando Houver sucesso
            @Override
            public void onResponse(JSONObject response) {
                Log.i("Teste2", "Sucesso: " + response);



                try {

                    Intent intent = new Intent(MainActivity.this, ModuloAluno.class);
                    intent.putExtra("apiKey", response.getString("apiKey"));
                    intent.putExtra("name", response.getString("name"));

                    Toast.makeText(MainActivity.this,
                            "Login Efetuado com Sucesso.",
                            Toast.LENGTH_SHORT)
                            .show();

                    startActivity(intent);
                } catch (JSONException e) {

                    Toast.makeText(MainActivity.this,
                            "Login ou Senha incorretos.",
                            Toast.LENGTH_SHORT)
                            .show();

                    e.printStackTrace();

                }



            }
        }, new Response.ErrorListener() {
            //Função executada quando Houver Erro
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Erro: "+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        cjor.setTag("tag");
        rq.add(cjor);
    }

    public void listartarefas(View v){
        url = "http://trabalhoderedes.esy.es/checkin/v1/tasks/1";
        CustomJsonObjectResquest cjor = new CustomJsonObjectResquest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("Teste2", "Sucesso: "+response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Erro: "+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        cjor.setTag("tag");
        rq.add(cjor);
    }

    @Override
    public void onStop(){
        super.onStop();
        rq.cancelAll("tag");
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
}

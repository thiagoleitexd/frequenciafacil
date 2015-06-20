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

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class Register extends ActionBarActivity {
    private RequestQueue rq;
    private Map<String, String> params;
    private EditText login;
    private EditText password;
    private EditText password2;
    private EditText nome;
    private EditText matricula;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        url = "http://redesdecomputadores.esy.es/checkin/v1/register";
        nome = (EditText) findViewById(R.id.idNomeR);
        login = (EditText) findViewById(R.id.idLoginR);
        password = (EditText) findViewById(R.id.idSenhaR);
        password2 = (EditText) findViewById(R.id.idSenha2R);
        matricula = (EditText) findViewById(R.id.idMatriculaR);
        rq = Volley.newRequestQueue(Register.this);
    }



    public void callJsonobject2(View v){
        params = new HashMap<String, String>();
        params.put("name", nome.getText().toString());
        params.put("email", login.getText().toString());
        params.put("password", password.getText().toString());
        params.put("matricula", matricula.getText().toString());

            if(password.getText().toString().equals(password2.getText().toString())){

                CustomJsonObjectResquest cjor = new CustomJsonObjectResquest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
                    //Função executada quando Houver sucesso
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("Teste2", "Sucesso: " + response);

                        Intent intent = new Intent(Register.this, MainActivity.class);
                        Toast.makeText(Register.this,
                                "Cadastro Realizado com Sucesso\nSeu login é: "+login.getText().toString(),
                                Toast.LENGTH_LONG)
                                .show();
                        startActivity(intent);


                    }
                }, new Response.ErrorListener() {
                    //Função executada quando Houver Erro
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Register.this, "Erro: "+error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

                cjor.setTag("tag");
                rq.add(cjor);


            }
        else{
                Toast.makeText(Register.this,
                        "AS senhas digitadas são diferentes.\nÉ necessário que elas sejam iguais.",
                        Toast.LENGTH_LONG)
                        .show();

            }


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
                Toast.makeText(Register.this, "Erro: "+error.getMessage(), Toast.LENGTH_SHORT).show();
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

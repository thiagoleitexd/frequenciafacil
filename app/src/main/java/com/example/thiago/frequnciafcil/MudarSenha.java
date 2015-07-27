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

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MudarSenha extends ActionBarActivity {
    private RequestQueue rq;
    private Map<String, String> params;

    private EditText passwordAntiga;
    private EditText passwordNovo;
    private EditText passwordNovo2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mudar_senha);

        passwordAntiga = (EditText) findViewById(R.id.senhaAntiga);
        passwordNovo = (EditText) findViewById(R.id.senha1);
        passwordNovo2 = (EditText) findViewById(R.id.senha22);



        rq = Volley.newRequestQueue(MudarSenha.this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_mudar_senha, menu);
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

    public void mudarSenha(View V) {

        params = new HashMap<String, String>();


        String url = MainActivityAluno.urlGeral + "mudarSenha";

        int cont = 0;
        String passNovo1, passNovo2, passAntiga;
        passAntiga = passwordAntiga.getText().toString();
        passNovo1 = passwordNovo.getText().toString();
        passNovo2 = passwordNovo2.getText().toString();

            if ((passAntiga.length() < 6)){
                passwordAntiga.requestFocus(); //seta o foco para o campo password
                passwordAntiga.setError("Sua Senha deve conter pelo menos 6 caracteres");
                cont ++;
            }
            if ((passNovo1.length() < 6)){
                passwordNovo.requestFocus(); //seta o foco para o campo password
                passwordNovo.setError("Sua Senha deve conter pelo menos 6 caracteres");
                cont++;
            }
            if (!passNovo1.equals(passNovo2)){
                passwordNovo2.requestFocus(); //seta o foco para o campo password
                passwordNovo2.setError("Os valores dos campos Senha e Confirmar Senha devem ser iguais");
                cont ++;
            }
            if(cont == 0){
                params.put("oldpassword", passAntiga);
                params.put("newpassword", passNovo1);

                CustomJsonObjectResquest cjor = new CustomJsonObjectResquest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {

                    //Função executada quando Houver sucesso
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.i("Teste2", "Sucesso: " + response);

                        Intent intent = new Intent(MudarSenha.this, MainActivityAluno.class);
                        Toast.makeText(MudarSenha.this,
                                "Mudança da Senha Realizada com Sucesso.",
                                Toast.LENGTH_LONG)
                                .show();
                        finish();
                        startActivity(intent);
                    }

                }, new Response.ErrorListener() {
                    //Função executada quando Houver Erro
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error instanceof NoConnectionError) {
                            Toast.makeText(MudarSenha.this, "Não foi possível conectar com o servidor, verifique sua conexão de internet.", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(MudarSenha.this, "Problema na conexão com o servidor ou com sua internet, tente mais tarde.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                cjor.setTag("tag");
                rq.add(cjor);
            }
    }

}


package com.example.thiago.frequnciafcil;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class MainActivity extends ActionBarActivity {
    public static String urlGeral = "http://redesdecomputadores.esy.es/checkin/v1/";
    private RequestQueue rq;
    private Map<String, String> params;
    private EditText login;
    private EditText password;
    private String url;
    ProgressDialog p_dialog;
    private int flag;
    CustomJsonObjectResquest cjor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String login_adquirido = null;
        Intent intent = getIntent();
        login_adquirido = intent.getStringExtra("login");
        if (login_adquirido != null) {
            login = (EditText) findViewById(R.id.idLogin);
            login.setText(login_adquirido);
        }
        rq = Volley.newRequestQueue(MainActivity.this);
    }

    public void comunicacacao() {
            p_dialog = ProgressDialog.show(this, "Verificando Login e Senha", "Aguarde...", false, true);

            url = urlGeral + "login";
            params = new HashMap<String, String>();
            params.put("email", login.getText().toString());
            params.put("password", password.getText().toString());
            System.out.println("errando2");
            cjor = new CustomJsonObjectResquest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
                //Função executada quando Houver sucesso

                @Override
                public void onResponse(JSONObject response) {

                    Log.i("Teste2", "Sucesso: " + response);
                    flag = 1;
                    p_dialog.dismiss();
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
                        System.out.println("errando5");
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


                    if (error instanceof NoConnectionError) {
                        Toast.makeText(MainActivity.this, "Não foi possível conectar com o servidor, verifique sua conexão de internet.", Toast.LENGTH_LONG).show();
                        p_dialog.dismiss();
                    }  else{
                        Toast.makeText(MainActivity.this, "Problema na conexão com o servidor ou com sua internet, tente mais tarde.", Toast.LENGTH_LONG).show();
                        SystemClock.sleep(7000);
                        comunicacacao();
                    }

                }
            });


            cjor.setTag("tag");
            rq.add(cjor);

    }


    public void callJsonobject(View V) {

        login = (EditText) findViewById(R.id.idLogin);
        password = (EditText) findViewById(R.id.idSenha);

        if (validateFields()) {

            comunicacacao();
        }

    }

    public void listartarefas(View v){
        url = urlGeral+"tasks/1";
        CustomJsonObjectResquest cjor = new CustomJsonObjectResquest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("Teste2", "Sucesso: "+response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Erro: "+error.getMessage(), Toast.LENGTH_LONG).show();
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


    public void registrar(View v) {
        Intent intent = new Intent(MainActivity.this, Register.class);
        startActivity(intent);

    }

    //inicio do about
    public void open(View view){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Frequência Fácil");
        alertDialogBuilder.setMessage("Versão 1.0");
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
    //fim do about

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



    private boolean validateFields() {
        String loginV = login.getText().toString().trim();
        String senhaV = password.getText().toString().trim();
        return (!isEmptyFields(loginV, senhaV));
    }



    private boolean isEmptyFields(String user, String pass) {
        if (TextUtils.isEmpty(user)) {
            login.requestFocus(); //seta o foco para o campo user
            login.setError("Digite seu email neste campo");
            return true;
        } else if (TextUtils.isEmpty(pass)) {
            password.requestFocus(); //seta o foco para o campo password
            password.setError("Digite sua senha cadastrada");
            return true;
        }
        return false;
    }

}

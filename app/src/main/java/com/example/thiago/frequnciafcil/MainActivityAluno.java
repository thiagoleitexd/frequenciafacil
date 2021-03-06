package com.example.thiago.frequnciafcil;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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


public class MainActivityAluno extends ActionBarActivity {
    public static String apikey;
    private int contBack =0;
    public static String levelacess;
    public static String urlGeral = "http://redesdecomputadores.esy.es/checkin/v1/";
    private RequestQueue rq;
    private Map<String, String> params;
    private EditText login;
    private EditText password;
    private String url;
    ProgressDialog p_dialog;
    private int flag;
    CustomJsonObjectResquest cjor;
    private Button cadastrar;
    //private View linha;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_aluno);

        login = (EditText) findViewById(R.id.idLogin);
        password = (EditText) findViewById(R.id.idSenha);

        SharedPreferences prefs = getSharedPreferences("loginEsenha", MODE_PRIVATE);
        login.setText(prefs.getString("loginsalvo", "")) ;//"No name defined" is the default value.
        password.setText(prefs.getString("senhasalvo", ""));




        cadastrar = (Button) findViewById(R.id.registrar);
        //linha = (View) findViewById(R.id.view);
        String login_adquirido = null;
        Intent intent = getIntent();
        login_adquirido = intent.getStringExtra("login");
        levelacess = intent.getStringExtra("levelacess");

        if (levelacess.equals("1")){
               cadastrar.setVisibility(View.INVISIBLE);
               //linha.setVisibility(View.INVISIBLE);

        }
        if (login_adquirido != null) {
            login.setText(login_adquirido);
            password.setText("");
        }
        rq = Volley.newRequestQueue(MainActivityAluno.this);
    }

    public void comunicacacao() {
        p_dialog = ProgressDialog.show(this, "Verificando Login e Senha", "Aguarde...", false, true);

        url = urlGeral + "login";
        params = new HashMap<String, String>();
        params.put("email", login.getText().toString());
        params.put("password", password.getText().toString());

        cjor = new CustomJsonObjectResquest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
            //Função executada quando Houver sucesso

            @Override
            public void onResponse(JSONObject response) {

                Log.i("Teste2", "Sucesso: " + response);
                flag = 1;
                p_dialog.dismiss();
                try {
                    Intent intent;

                    apikey = response.getString("apiKey");


                    SharedPreferences.Editor editor = getSharedPreferences("loginEsenha", MODE_PRIVATE).edit();
                    editor.putString("loginsalvo", login.getText().toString());
                    editor.putString("senhasalvo", password.getText().toString());
                    editor.commit();




                    if(response.getString("tipo").equals("2") && levelacess.equals("2") ) {
                        intent = new Intent(MainActivityAluno.this, MenuAluno.class);
                        intent.putExtra("apiKey", response.getString("apiKey"));
                        intent.putExtra("name", response.getString("name"));

                        Toast.makeText(MainActivityAluno.this,
                                "Login Efetuado com Sucesso.",
                                Toast.LENGTH_SHORT)
                                .show();

                        startActivity(intent);
                    }
                    else if(response.getString("tipo").equals("1") && levelacess.equals("1")) {
                        intent = new Intent(MainActivityAluno.this, MenuProfessor.class);
                        intent.putExtra("apiKey", response.getString("apiKey"));
                        intent.putExtra("name", response.getString("name"));

                        Toast.makeText(MainActivityAluno.this,
                                "Login Efetuado com Sucesso.",
                                Toast.LENGTH_SHORT)
                                .show();

                        startActivity(intent);
                    }
                    else if (levelacess.equals("1")) {
                        Toast.makeText(MainActivityAluno.this,
                                "Não há professor que corresponda com esse login e senha.",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                    else if (levelacess.equals("2")) {
                        Toast.makeText(MainActivityAluno.this,
                                "Não há aluno que corresponda com esse esse login e senha.",
                                Toast.LENGTH_LONG)
                                .show();
                    }

                } catch (JSONException e) {
                    Toast.makeText(MainActivityAluno.this,
                            "Login ou Senha incorretos.",
                            Toast.LENGTH_LONG)
                            .show();
                    e.printStackTrace();

                }

            }
        }, new Response.ErrorListener() {
            //Função executada quando Houver Erro

            @Override
            public void onErrorResponse(VolleyError error) {

                p_dialog.dismiss();
                if (error instanceof NoConnectionError) {
                    Toast.makeText(MainActivityAluno.this, "Não foi possível conectar com o servidor, verifique sua conexão de internet.", Toast.LENGTH_LONG).show();
                }  else{
                    Toast.makeText(MainActivityAluno.this, "Problema na conexão com o servidor ou com sua internet, tente mais tarde.", Toast.LENGTH_LONG).show();
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
                Toast.makeText(MainActivityAluno.this, "Erro: "+error.getMessage(), Toast.LENGTH_LONG).show();
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
        Intent intent = new Intent(MainActivityAluno.this, Register.class);
        startActivity(intent);

    }

    //inicio do about
    public void open(View view){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Pagela Virtual");
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


    public void onBackPressed() {//metodo responsável pelo botão 'back' do android;

        Toast.makeText(MainActivityAluno.this,"Pressione novamente para sair do aplicativo.",Toast.LENGTH_LONG).show();
        if (contBack != 0) {
            contBack = 0;
            finish();
        }
        contBack ++;
    }

}


package com.example.thiago.frequnciafcil;
//teste 4x

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Register extends ActionBarActivity {
    private RequestQueue rq;
    private Map<String, String> params;
    private EditText login;
    private EditText password;
    private EditText password2;
    private EditText nome;
    private EditText mat;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        url = MainActivity.urlGeral+"register";
        nome = (EditText) findViewById(R.id.idNomeR);
        login = (EditText) findViewById(R.id.idLoginR);
        password = (EditText) findViewById(R.id.idSenhaR);
        password2 = (EditText) findViewById(R.id.idSenha2R);
        mat = (EditText) findViewById(R.id.idMatriculaR);
        rq = Volley.newRequestQueue(Register.this);
    }



    public void callJsonobject2(View v){

        boolean validade3 = validarSenhas();
        boolean validade = emailValidator(login.getText().toString());
        boolean validade2 = validateFields();

        System.out.println(validade);
        System.out.println(validade2);
        System.out.println(validade3);

        if(validade && validade2 && validade3) {

            params = new HashMap<String, String>();
            params.put("name", nome.getText().toString());
            params.put("email", login.getText().toString());
            params.put("password", password.getText().toString());
            params.put("matricula", mat.getText().toString());

            //if (password.getText().toString().equals(password2.getText().toString())) {

                CustomJsonObjectResquest cjor = new CustomJsonObjectResquest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {

                    //Função executada quando Houver sucesso
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.i("Teste2", "Sucesso: " + response);

                        Intent intent = new Intent(Register.this, MainActivity.class);

                        Toast.makeText(Register.this,
                                "Cadastro Realizado com Sucesso\nSeu login é: " + login.getText().toString(),
                                Toast.LENGTH_LONG)
                                .show();
                        startActivity(intent);


                    }
                }, new Response.ErrorListener() {
                    //Função executada quando Houver Erro
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Register.this, "Erro: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

                cjor.setTag("tag");
                rq.add(cjor);


          //  } else {
            //    Toast.makeText(Register.this,
              //          "AS senhas digitadas são diferentes.\nÉ necessário que elas sejam iguais.",
                //        Toast.LENGTH_LONG)
                  //      .show();

            //}

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




        public boolean emailValidator(String email)
        {
            Pattern pattern;
            Matcher matcher;
            final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
            pattern = Pattern.compile(EMAIL_PATTERN);
            matcher = pattern.matcher(email);

            if(matcher.matches() == false) {

                login.requestFocus(); //seta o foco para o campo password
                login.setError("E-mail inválido");

            }
            return matcher.matches();
        }










    private boolean validateFields() {
        String loginV = login.getText().toString().trim();
        String nomeV = nome.getText().toString().trim();
        String matV = mat.getText().toString().trim();
        String pass1V = password.getText().toString().trim();
        String pass2V = password.getText().toString().trim();
        return (!isEmptyFields(nomeV, matV, loginV, pass1V, pass2V));
    }



    private boolean isEmptyFields(String nomeV, String matV, String loginV, String pass1V, String pass2V) {
        int cont = 0;
        if (TextUtils.isEmpty(pass2V)) {
            password2.requestFocus(); //seta o foco para o campo password
            password2.setError("Digite sua senha igual ao campo anterior");
            cont++;
            //return true;
        }
        if (TextUtils.isEmpty(pass1V)) {
            password.requestFocus(); //seta o foco para o campo password
            password.setError("Digite sua senha");
            cont ++;
            //return true;
        }
        if (TextUtils.isEmpty(matV)) {
            mat.requestFocus(); //seta o foco para o campo password
            mat.setError("Digite sua Matrícula");
            cont ++;
            //return true;
        }
        if (TextUtils.isEmpty(nomeV)) {
            nome.requestFocus(); //seta o foco para o campo user
            nome.setError("Digite seu Nome Completo neste campo");
            cont ++;
            //return true;
        }
       if(cont == 0){
            return false;
        }
        else {
            return true;
        }
    }

    private boolean validarSenhas() {
        String senha1V = password.getText().toString().trim();
        String senha2V = password2.getText().toString().trim();

        if (! (TextUtils.isEmpty(senha1V) && TextUtils.isEmpty(senha2V))) {

            return (iguais(senha1V, senha2V));
        }
        return false;
    }


    private boolean iguais(String pass1, String pass2) {
        if (!pass1.equals(pass2)) {
            password.requestFocus(); //seta o foco para o campo password
            password.setError("Os valores dos campos Senha e Confirmar Senha devem ser iguais");
            password2.requestFocus(); //seta o foco para o campo password
            password2.setError("Os valores dos campos Senha e Confirmar Senha devem ser iguais");
            return false;
        }
        return true;
    }

}

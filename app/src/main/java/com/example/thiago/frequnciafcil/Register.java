package com.example.thiago.frequnciafcil;
//teste 4x

import android.app.AlertDialog;
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
    private String msg;
    private String erro;
    private String codigo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        codigo = "0";
        url = MainActivityAluno.urlGeral+"register";
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


            //estas variaveis vao ser usadas, caso a FOTO funcione!
            final String nomeIntent,emailIntent,passwordIntent,matriculaIntent;
            nomeIntent = nome.getText().toString();
            passwordIntent = password.getText().toString();
            matriculaIntent = mat.getText().toString();
            emailIntent = login.getText().toString();

            //fim da declaracao de variaveis que serão usadas caso a FOTO FUNCIONE

                            //essas variaveis vao ser usadas caso o lance da FOTO FUNCIONE
                            Intent intent = new Intent(Register.this, Foto.class);
                            intent.putExtra("name", nomeIntent);
                            intent.putExtra("password", passwordIntent);
                            intent.putExtra("matricula", matriculaIntent);
                            intent.putExtra("email", emailIntent);
                          //fim das variaveis que vao ser usadas caso o lance da foto funcione !!!

                            intent.putExtra("levelacess", "2");
                            Toast.makeText(Register.this,
                                    "Selecione uma foto",
                                    Toast.LENGTH_LONG)
                                    .show();

                            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register, menu);
        return true;
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


    public boolean isAlpha(String name) {
        return name.matches("[0-9]+");
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
            password2.setError("Os campos Senha e Confirmar devem ser iguais");
            cont++;
            //return true;
        }
        if (TextUtils.isEmpty(pass1V)|| (pass1V.length() < 6)) {
            password.requestFocus(); //seta o foco para o campo password
            password.setError("Sua Senha deve conter pelo menos 6 caracteres");
            cont ++;
            //return true;
        }
        if ((TextUtils.isEmpty(matV)) || (matV.length() != 8) || (!isAlpha(matV))){
            mat.requestFocus(); //seta o foco para o campo password
            mat.setError("Sua Matrícula deve conter somente números com exatamente 8 digitos");
            cont ++;
            //return true;
        }
        if (TextUtils.isEmpty(nomeV)) {
            nome.requestFocus(); //seta o foco para o campo user
            nome.setError("Digite seu Nome Completo");
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
            password2.requestFocus(); //seta o foco para o campo password
            password2.setError("Os valores dos campos Senha e Confirmar Senha devem ser iguais");
            return false;
        }
        return true;
    }

}

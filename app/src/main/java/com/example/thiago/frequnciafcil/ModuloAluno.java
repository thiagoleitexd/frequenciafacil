package com.example.thiago.frequnciafcil;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
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


public class ModuloAluno extends ActionBarActivity {

    private RequestQueue rq;
    private String url;
    private String array_spinner[];
    private Map<String, String> params;
    //private HashMap<String, String> header;
    private TextView senhaAula;
    ProgressDialog p_dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modulo_aluno);

        TextView apresentacao = (TextView) findViewById(R.id.apresentacao);
        senhaAula = (EditText) findViewById(R.id.senhaaula);
        url = MainActivityAluno.urlGeral+"registrarPresenca";
        array_spinner=new String[1];
        array_spinner[0]="Redes de computadores";
        Spinner s = (Spinner) findViewById(R.id.disciplinas);
        ArrayAdapter adapter = new ArrayAdapter(this,
        android.R.layout.simple_spinner_item, array_spinner);
        s.setAdapter(adapter);

        rq = Volley.newRequestQueue(ModuloAluno.this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_modulo_aluno, menu);
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
            case R.id.action_relatorio_aluno:
                Intent intent = new Intent(ModuloAluno.this, AlunoFaltas.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //inicio
    public void presente(View v){

        if (validateFields()) {

            params = new HashMap<String, String>();
            params.put("password", senhaAula.getText().toString());
            p_dialog = ProgressDialog.show(this, "Conectando ao Servidor", "Aguarde...", false, true);

            CustomJsonObjectResquest cjor = new CustomJsonObjectResquest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
                //Função executada quando Houver sucesso
                @Override
                public void onResponse(JSONObject response) {


                    p_dialog.dismiss();

                    Log.i("Teste2", "Sucesso: " + response);

                    try {
                        if (response.getString("code").equals("4")){

                            Toast.makeText(ModuloAluno.this,
                                    "Presença Registrada com Sucesso!",
                                    Toast.LENGTH_SHORT)
                                    .show();

                            AlertDialog.Builder presencaconfirmada = new AlertDialog.Builder(ModuloAluno.this);
                            presencaconfirmada.setTitle("Presença Confirmada.");
                            presencaconfirmada.setMessage("Aproveite a Aula.");

                            presencaconfirmada.setPositiveButton("Sair",
                                    new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface arg0, int arg1) {

                                        }
                                    });


                            AlertDialog alertDialog = presencaconfirmada.create();
                            alertDialog.show();



                        }
                        else{

                            Toast.makeText(ModuloAluno.this,
                                    response.getString("message"),
                                    Toast.LENGTH_SHORT)
                                    .show();

                            AlertDialog.Builder presencaconfirmada = new AlertDialog.Builder(ModuloAluno.this);
                            presencaconfirmada.setTitle("Presença");
                            presencaconfirmada.setMessage(response.getString("message"));

                            presencaconfirmada.setPositiveButton("Sair",
                                    new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface arg0, int arg1) {

                                        }
                                    });


                            AlertDialog alertDialog = presencaconfirmada.create();
                            alertDialog.show();


                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                //Função executada quando Houver Erro
                @Override
                public void onErrorResponse(VolleyError error) {

                    p_dialog.dismiss();

                    if (error instanceof NoConnectionError) {
                        Toast.makeText(ModuloAluno.this, "Não foi possível conectar com o servidor, verifique sua conexão de internet", Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(ModuloAluno.this, "Problema na conexão com o servidor ou com sua internet, tente mais tarde.", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            cjor.setTag("tag");
            rq.add(cjor);

        }
    }
    //fim


    private boolean validateFields() {
        String senhaAulaV = senhaAula.getText().toString().trim();
        return (!isEmptyFields(senhaAulaV));
    }



    private boolean isEmptyFields(String passAula) {
      if (TextUtils.isEmpty(passAula)) {
            senhaAula.requestFocus(); //seta o foco para o campo password
          senhaAula.setError("Insira a Senha da Aula");
            return true;
        }
        return false;
    }

}

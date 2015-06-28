package com.example.thiago.frequnciafcil;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.UUID;

public class ModuloProfessor extends ActionBarActivity {

    private RequestQueue rq;
    private Map<String, String> params;
    private EditText senhaGerada;
    private TextView status;
    private Button bstatus;
    private Button bSenha;
    private Animation anim;
    private String url;
    private String senhadaAula;
    private String status_inicial;
    private String msg = null;
    private View all;
    private ProgressDialog p_dialog;


    public void alerta(){


    AlertDialog.Builder alertDialogBuilder = new        AlertDialog.Builder(this);
    alertDialogBuilder.setTitle("Problema na Conexão");
    alertDialogBuilder.setMessage("Ocorreu erro na conexão com o servidor ou seu dispositivo encontra-se sem conexão com a internet.\nTente em alguns minutos");
    
    alertDialogBuilder.setPositiveButton("Tentar Novamente",
            new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                    }
                });
            alertDialogBuilder.setNegativeButton("Sair",
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            finish();
                        }
                    });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

}
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modulo_professor);
        bstatus = (Button) findViewById(R.id.bstatus);
        bSenha = (Button) findViewById(R.id.bsenha);
        status = (TextView) findViewById(R.id.status);
        senhaGerada = (EditText) findViewById(R.id.senhaAulaProf);
        rq = Volley.newRequestQueue(ModuloProfessor.this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_modulo_professor, menu);
        return true;
    }
//


    public String getSenhadaAula(){
        UUID uuid = UUID.randomUUID();
        String myRandom = uuid.toString();
        senhadaAula = myRandom.substring(0, 5);
        return senhadaAula;
    }


//

    public void gerarSenha(View v){

        url = MainActivity.urlGeral+"criarFrequencia";
        params = new HashMap<String, String>();


        params.put("password", getSenhadaAula());
//inicio

        CustomJsonObjectResquestProf cjor = new CustomJsonObjectResquestProf(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
            //Função executada quando Houver sucesso


            @Override
            public void onResponse(JSONObject response) {


                Log.i("Teste3", "Sucesso: " + response);

                try {
                    String erro = response.getString("code");
                    msg = response.getString("message");

                    if (erro.equals("14")){
                        senhaGerada.setText(senhadaAula);
                        estilo();

                        Toast.makeText(ModuloProfessor.this,
                                "Professor, a senha foi gerada com sucesso! Informe-a aos alunos.",
                                Toast.LENGTH_LONG)
                                .show();

                    }
                    else if (erro.equals("12")){

                        Toast.makeText(ModuloProfessor.this,
                                "Professor, já existe uma frequência em aberto.",
                                Toast.LENGTH_LONG)
                                .show();

                    }
                    else if (erro.equals("10")){
                        Toast.makeText(ModuloProfessor.this,
                                "Professor, a frequência foi fechada com sucesso.",
                                Toast.LENGTH_LONG)
                                .show();

                    }
                    else if (erro.equals("9")) {
                        Toast.makeText(ModuloProfessor.this,
                                "Professor, neste momento não há nenhuma frequência em aberto.",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                        else {
                        Toast.makeText(ModuloProfessor.this,
                                msg,
                                Toast.LENGTH_LONG)
                                .show();

                    }

                } catch (JSONException e) {

                    e.printStackTrace();

                }

            }
        }, new Response.ErrorListener() {
            //Função executada quando Houver Erro
            @Override
            public void onErrorResponse(VolleyError error) {

                if (error instanceof NoConnectionError) {
                    Toast.makeText(ModuloProfessor.this, "Não foi possível conectar com o servidor, verifique sua conexão de internet.", Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(ModuloProfessor.this, "Problema na conexão com o servidor ou com sua internet, tente mais tarde.", Toast.LENGTH_LONG).show();
                }
            }
        });
        cjor.setTag("tag");
        rq.add(cjor);
//fim
    }

     public void estilo(){



        bstatus.setVisibility(View.VISIBLE);
        bSenha.setVisibility(View.INVISIBLE);
        status.setTextColor(Color.GREEN);
        //status.setBackgroundColor(Color.DKGRAY);
        status.setText("Ligado");

        anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(800); //You can manage the blinking time with this parameter
       // anim.setStartOffset(20);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        status.startAnimation(anim);
//        anim.setRepeatCount(1); para de PISCAR




    }

    public void desligarFrequencia(View V){

//inicio
        url = MainActivity.urlGeral+"fecharFrequencia";
        CustomJsonObjectResquestProf cjor = new CustomJsonObjectResquestProf(Request.Method.GET, url, params, new Response.Listener<JSONObject>() {
            //Função executada quando Houver sucesso


            @Override
            public void onResponse(JSONObject response) {

                String msg = null;

                Log.i("Teste3", "Sucesso: " + response);

                try {
                    String erro = response.getString("code");
                    msg = response.getString("message");

                    if (erro.equals("10")){

                        status.setText("Desligado");
                        anim.setRepeatCount(0);
                        status.setTextColor(Color.RED);
                        bstatus.setVisibility(View.INVISIBLE);
                        bSenha.setVisibility(View.VISIBLE);
                        senhaGerada.setText("");

                        Toast.makeText(ModuloProfessor.this,
                                "Professor, a frequência foi desligada com sucesso.",
                                Toast.LENGTH_LONG)
                                .show();

                    }
                    else if(erro.equals("11")){

                        Toast.makeText(ModuloProfessor.this,
                                "Professor, neste Momento não há frequência em aberto.",
                                Toast.LENGTH_LONG)
                                .show();

                    }
                    else {
                        Toast.makeText(ModuloProfessor.this,
                                msg,
                                Toast.LENGTH_LONG)
                                .show();

                    }

                } catch (JSONException e) {

                    e.printStackTrace();

                }

            }
        }, new Response.ErrorListener() {
            //Função executada quando Houver Erro
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof NoConnectionError) {
                    Toast.makeText(ModuloProfessor.this, "Não foi possível conectar com o servidor, verifique sua conexão de internet.", Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(ModuloProfessor.this, "Problema na conexão com o servidor ou com sua internet, tente mais tarde.", Toast.LENGTH_LONG).show();
                }
            }
        });
        cjor.setTag("tag");
        rq.add(cjor);
//fim

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

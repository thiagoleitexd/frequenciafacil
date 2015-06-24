package com.example.thiago.frequnciafcil;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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
    private Animation anim;
    private String url;
    private String senhadaAula;
    private String status_inicial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modulo_professor);

        params = new HashMap<String, String>();

//inicio
        url = MainActivity.urlGeral+"fecharFrequencia";
        CustomJsonObjectResquestProf cjor = new CustomJsonObjectResquestProf(Request.Method.GET, url, params, new Response.Listener<JSONObject>() {
            //Função executada quando Houver sucesso

            @Override
            public void onResponse(JSONObject response) {

                String msg = null;

                Log.i("Teste3", "Sucesso: " + response);

                try {
                    String erro = response.getString("error");
                    msg = response.getString("message");

                    if (erro.equals("false")){

                        Toast.makeText(ModuloProfessor.this,
                                "Status: Frequência Desligada",
                                Toast.LENGTH_SHORT)
                                .show();
                    }
                    else {

                        Toast.makeText(ModuloProfessor.this,
                                msg,
                                Toast.LENGTH_SHORT)
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
                Toast.makeText(ModuloProfessor.this, "Erro: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        rq = Volley.newRequestQueue(ModuloProfessor.this);
        cjor.setTag("tag");
        rq.add(cjor);
//fim
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_modulo_professor, menu);
        return true;
    }
//
public void statusFrequencia(){
    url = MainActivity.urlGeral+"statusFrequencia";
    params = new HashMap<String, String>();

    CustomJsonObjectResquestProf cjorr = new CustomJsonObjectResquestProf(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {

        public void onResponse(JSONObject response) {

            String msg = null;


            Log.i("Teste3", "Sucesso: " + response);

            try {
                String erro = response.getString("error");
                System.out.println(response);
                msg = response.getString("message");

                if (erro.equals("false")){


                    Toast.makeText(ModuloProfessor.this,
                           msg,
                            Toast.LENGTH_SHORT)
                            .show();

                }
                else {

                    Toast.makeText(ModuloProfessor.this,
                            msg,
                            Toast.LENGTH_SHORT)
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
            Toast.makeText(ModuloProfessor.this, "Erro: " + error.getMessage(), Toast.LENGTH_SHORT).show();
        }
    });
    cjorr.setTag("tag");
    rq.add(cjorr);
//fim
}



//

    public void gerarSenha(View v){

        url = MainActivity.urlGeral+"criarFrequencia";
        params = new HashMap<String, String>();

        senhaGerada = (EditText) findViewById(R.id.senhaAulaProf);
        UUID uuid = UUID.randomUUID();
        String myRandom = uuid.toString();
        senhadaAula = myRandom.substring(0, 5);



        params.put("password", senhadaAula);
//inicio
        CustomJsonObjectResquestProf cjor = new CustomJsonObjectResquestProf(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
            //Função executada quando Houver sucesso


            @Override
            public void onResponse(JSONObject response) {

                String msg = null;

                Log.i("Teste3", "Sucesso: " + response);

                try {
                    String erro = response.getString("error");
                    msg = response.getString("message");

                    if (erro.equals("false")){
                        senhaGerada.setText(senhadaAula);
                        estilo();

                        Toast.makeText(ModuloProfessor.this,
                                "Senha Registrada com Sucesso!",
                                Toast.LENGTH_SHORT)
                                .show();

                    }
                    else {

                        Toast.makeText(ModuloProfessor.this,
                                msg,
                                Toast.LENGTH_SHORT)
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
                Toast.makeText(ModuloProfessor.this, "Erro: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        cjor.setTag("tag");
        rq.add(cjor);
//fim
    }

     public void estilo(){


        bstatus = (Button) findViewById(R.id.bstatus);
        bstatus.setVisibility(View.VISIBLE);
        status = (TextView) findViewById(R.id.status);
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
                    String erro = response.getString("error");
                    msg = response.getString("message");

                    if (erro.equals("false")){

                        status.setText("Desligado");
                        anim.setRepeatCount(0);
                        status.setTextColor(Color.RED);
                        bstatus.setVisibility(View.INVISIBLE);
                        senhaGerada.setText("");

                        Toast.makeText(ModuloProfessor.this,
                                "Frequência Desligada",
                                Toast.LENGTH_SHORT)
                                .show();

                    }
                    else {

                        Toast.makeText(ModuloProfessor.this,
                                msg,
                                Toast.LENGTH_SHORT)
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
                Toast.makeText(ModuloProfessor.this, "Erro: " + error.getMessage(), Toast.LENGTH_SHORT).show();
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

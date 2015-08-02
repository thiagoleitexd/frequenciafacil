package com.example.thiago.frequnciafcil;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
import java.util.Timer;
import java.util.TimerTask;

public class MainActivityProfessor extends ActionBarActivity {

    public static String apikey;
    private int contBack = 0;
    private RequestQueue rq;
    private Map<String, String> params;
    private EditText senhaGerada;
    private TextView status;
    private Button bstatus;
    private Button bSenha;
    private String url;
    private String msg = null;
    private ProgressDialog p_dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity_professor);

        Intent intent = getIntent();
        apikey = intent.getStringExtra("apiKey");

        bstatus = (Button) findViewById(R.id.bstatus);
        bSenha = (Button) findViewById(R.id.bsenha);
        status = (TextView) findViewById(R.id.status);
        senhaGerada = (EditText) findViewById(R.id.senhaAulaProf);
        params = new HashMap<String, String>();
        url = MainActivityAluno.urlGeral+"fecharFrequencia";

        p_dialog = ProgressDialog.show(this, "Conectando ao Servidor", "Aguarde...", false, true);

        CustomJsonObjectResquestProf cjor = new CustomJsonObjectResquestProf(Request.Method.GET, url, params, new Response.Listener<JSONObject>() {
            //Função executada quando Houver sucesso
            @Override
            public void onResponse(JSONObject response) {
                p_dialog.dismiss();

                Log.i("Teste3", "Sucesso: " + response);

                try {
                    String erro = response.getString("code");
                    msg = response.getString("message");

                    if (erro.equals("9") || erro.equals("10")){

                        new Timer().schedule(new TimerTask() {
                            public void run() {
                                Intent intent = new Intent(MainActivityProfessor.this, ModuloProfessor.class);
                                finish();
                                startActivity(intent);
                            }
                        }, 100 /*amount of time in milliseconds before execution*/);

                      }
                    else {

                        Toast.makeText(MainActivityProfessor.this,
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
                p_dialog.dismiss();
                alerta();
                if (error instanceof NoConnectionError) {
                    Toast.makeText(MainActivityProfessor.this, "Não foi possível conectar com o servidor, verifique sua conexão de internet.", Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(MainActivityProfessor.this, "Problema na conexão com o servidor ou com sua internet, tente mais tarde.", Toast.LENGTH_SHORT).show();
                }

            }
        });
        rq = Volley.newRequestQueue(MainActivityProfessor.this);
        cjor.setTag("tag");
        rq.add(cjor);
//fim


    }

    public void alerta() {


        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_activity_professor, menu);
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

package com.example.thiago.frequnciafcil;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modulo_professor);
        url = "http://redesdecomputadores.esy.es/checkin/v1/criarFrequencia";
        rq = Volley.newRequestQueue(ModuloProfessor.this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_modulo_professor, menu);
        return true;
    }

    public void gerarSenha(View v){

        params = new HashMap<String, String>();


        UUID uuid = UUID.randomUUID();
        String myRandom = uuid.toString();
        senhaGerada = (EditText) findViewById(R.id.senhaAulaProf);
        senhaGerada.setText(myRandom.substring(0, 5));

        params.put("password", senhaGerada.getText().toString());
//inicio
        CustomJsonObjectResquest cjor = new CustomJsonObjectResquest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
            //Função executada quando Houver sucesso


            @Override
            public void onResponse(JSONObject response) {

                String msg = "oi";

                Log.i("Teste3", "Sucesso: " + response);

                try {
                    String erro = response.getString("error");
                    msg = response.getString("message");

                    if (erro.equals("false")){
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

        System.out.println("oi");
        status.setText("Desligado");
        anim.setRepeatCount(0);
        status.setTextColor(Color.RED);
        bstatus.setVisibility(View.INVISIBLE);
        senhaGerada.setText("");




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

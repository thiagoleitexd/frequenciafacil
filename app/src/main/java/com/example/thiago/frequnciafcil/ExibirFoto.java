package com.example.thiago.frequnciafcil;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.support.v7.app.ActionBarActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ExibirFoto extends ActionBarActivity {

    private RequestQueue rq;
    private Map<String, String> params;
    private int scale;
    private String fotoWS;
    private ImageView fotoAluno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exibir_foto);
        fotoAluno = (ImageView) findViewById(R.id.fotoAluno);
        //inicio


        String url = MainActivityAluno.urlGeral + "exibirFoto";

        //fim da declaracao de variaveis que serão usadas caso a FOTO FUNCIONE

        params = new HashMap<String, String>();

        Intent ReceberId = getIntent();
        String idDaIntent = ReceberId.getStringExtra("id");

        params.put("id", idDaIntent);


        CustomJsonObjectResquest cjor = new CustomJsonObjectResquest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {

            //Função executada quando Houver sucesso
            @Override
            public void onResponse(JSONObject response) {


                try {

                    //erro = response.getString("error");
                    String msg = response.getString("message");
                    String codigo = response.getString("code");

                    if (codigo.equals("false")) {


                        fotoWS = response.getString("foto"); //essa é a foto em string, este valor que irá para o BANCO DE DADOS
                        Log.i("Teste2", "Sucesso: " + response);

                        //ordem de conversão : Bitmap(imagem) -> byte 64 -> String
                        //conversão da volta: String -> byte 64 -> bitmap(imagem)
                        try {
                            byte[] voltadafoto = Base64.decode(fotoWS, Base64.DEFAULT); //convertendo o fotoWS(valor que está no banco) em byte 64
                            Bitmap bitmap4 = BitmapFactory.decodeByteArray(voltadafoto, 0, voltadafoto.length); //finalmente transformando o byte 64 em bitmap !!
                            fotoAluno.setImageBitmap(bitmap4); //mostrando a imagem !!!
                        }catch (Exception e){
                            System.out.println("Erro : "+e.getMessage());
                        }


                        Toast.makeText(ExibirFoto.this,
                                "Foto Carregada com sucesso",
                                Toast.LENGTH_LONG)
                                .show();





                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
/*


                    if (codigo.equals("1")) {
                        Log.i("Teste2", "Sucesso: " + response);

                        Intent intent = new Intent(Foto.this, MainActivityAluno.class);
                        intent.putExtra("levelacess", "2");
                        Toast.makeText(Foto.this,
                                "Cadastro Efetuado com sucesso",
                                Toast.LENGTH_LONG)
                                .show();
                        startActivity(intent);
                    }
                    else if (codigo.equals("2")) {
                        Toast.makeText(Foto.this,
                                "Esse E-mail já está cadastrado no sistema. Tente outro.",
                                Toast.LENGTH_LONG)
                                .show();

                    }
                    else if (codigo.equals("3")) {
                        Toast.makeText(Foto.this,
                                "Essa Matrícula ja está cadastrada no sistema. Tente outro.",
                                Toast.LENGTH_LONG)
                                .show();

                    }
                    else if (codigo.equals("4")) {
                        Toast.makeText(Foto.this,
                                "Esse E-mail e essa Matrícula já estão cadastrados no sistema.",
                                Toast.LENGTH_LONG)
                                .show();

                    }
                    else{
                        Toast.makeText(Foto.this,
                                "Erro de comunicação com o servidor/Banco de Dados. Tente Mais Tarde.",
                                Toast.LENGTH_LONG)
                                .show();
                    }

                    */

            }
        }, new Response.ErrorListener() {
            //Função executada quando Houver Erro
            @Override
            public void onErrorResponse(VolleyError error) {
                //if (error instanceof NoConnectionError) {
                //    Toast.makeText(Foto.this, "Não foi possível conectar com o servidor, verifique sua conexão de internet.", Toast.LENGTH_LONG).show();
                //} else {
                //    Toast.makeText(Foto.this, "Problema na conexão com o servidor ou com sua internet, tente mais tarde.", Toast.LENGTH_SHORT).show();
                //}
            }
        });

        rq = Volley.newRequestQueue(ExibirFoto.this);
        cjor.setTag("tag");
        rq.add(cjor);



        //fim

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_exibir_foto, menu);
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

    public void decodeUri() {


            //fim da gambi


            //desconverter

            //byte[] fotoemBytes = fotoWS.getBytes(Charset.forName("UTF-8"));
            //Bitmap bitmap3 = BitmapFactory.decodeByteArray(FotoemBytes, 0, FotoemBytes.length);

            //ivSelectedImage.setImageBitmap(bitmap3);
            //fim desconverter

        }
    }



package com.example.thiago.frequnciafcil;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

public class Foto extends ActionBarActivity {
    private int scale;
    private RequestQueue rq;
    private Map<String, String> params;
    private String codigo, msg;
    String nomeIntent, emailIntent, passwordIntent, matriculaIntent;
    private ImageView ivSelectedImage;
    private byte[] foto_escolhida;
    private String fotoWS;
    private Uri uriGeral;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foto);
        ivSelectedImage = (ImageView) findViewById(R.id.ivSelectedImage);


        //System.out.println(email+nome+senha+matricula+levelacess);

        rq = Volley.newRequestQueue(Foto.this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_foto, menu);


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


    public void selecionarFoto(View V) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(intent, "Complete action using"), 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == 1 && null != data) {
            uriGeral = data.getData();
            decodeUri(uriGeral);
        }
    }

    public void decodeUri(Uri uri) {
        ParcelFileDescriptor parcelFD = null;
        try {
            parcelFD = getContentResolver().openFileDescriptor(uri, "r");
            FileDescriptor imageSource = parcelFD.getFileDescriptor();

            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeFileDescriptor(imageSource, null, o);

            // the new size we want to scale to
            final int REQUIRED_SIZE = 1024;

            // Find the correct scale value. It should be the power of 2.
            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            scale = 1;
            while (true) {
                if (width_tmp < REQUIRED_SIZE && height_tmp < REQUIRED_SIZE) {
                    break;
                }
                width_tmp /= 2;
                height_tmp /= 2;
                scale *= 2;
            }

            // decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            final Bitmap bitmap = BitmapFactory.decodeFileDescriptor(imageSource, null, o2);
            //ivSelectedImage.setImageBitmap(bitmap);


            //converter
            Bitmap bitmap2 = bitmap;

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap2.compress(Bitmap.CompressFormat.PNG, 50, stream);
            byte[] foto = stream.toByteArray(); //em tese, isso que mandarei para o banco de dados !!!! detalhe: a variavel foto é um byteArray !!!!!!!
            foto_escolhida  = foto;
            //fim converter


            //gambi


            //estas variaveis vao ser usadas, caso a FOTO funcione!
            String url = MainActivityAluno.urlGeral + "register";

            Intent intent_anterior = getIntent();
            nomeIntent = intent_anterior.getStringExtra("name");
            passwordIntent = intent_anterior.getStringExtra("password");
            matriculaIntent = intent_anterior.getStringExtra("matricula");
            emailIntent = intent_anterior.getStringExtra("email");
            //fim da declaracao de variaveis que serão usadas caso a FOTO FUNCIONE

            params = new HashMap<String, String>();


            params.put("name", nomeIntent);
            params.put("email", emailIntent);
            params.put("password", passwordIntent);
            params.put("matricula", matriculaIntent);

            final String fotoString = new String(foto_escolhida);

            params.put("foto", fotoString);

            System.out.println(nomeIntent);
            System.out.println(matriculaIntent);
            System.out.println(passwordIntent);
            System.out.println(emailIntent);


            CustomJsonObjectResquest cjor = new CustomJsonObjectResquest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {

                //Função executada quando Houver sucesso
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        //erro = response.getString("error");
                        String msg = response.getString("message");
                        String codigo = response.getString("code");
                        fotoWS = response.getString("foto");
                        Log.i("Teste2", "Sucesso: " + response);
                        System.out.println(fotoWS);


                        byte[] fotoemBytes = fotoWS.getBytes(Charset.forName("UTF-8"));

                        Bitmap bitmap3;

                        bitmap3 = BitmapFactory.decodeByteArray(fotoemBytes, 0, fotoemBytes.length);

                        ivSelectedImage.setImageBitmap(bitmap3);

                        //decodeUri2(uriGeral);

                        //byte[] fotodevolta = fotoString.getBytes();
                        //Bitmap bitmap4 = BitmapFactory.decodeByteArray(fotodevolta, 0, fotodevolta.length); //o banco me enviará o byteArray, e eu converterei em imagem ! a variavel foto é um byteArray !!!!!!!
                        //ivSelectedImage.setImageBitmap(bitmap4);

                        //byte[] fotodevolta = fotoString.getBytes();
                        //Bitmap bitmap4 = BitmapFactory.decodeByteArray(fotodevolta, 0, fotodevolta.length); //o banco me enviará o byteArray, e eu converterei em imagem ! a variavel foto é um byteArray !!!!!!!
                        //ivSelectedImage.setImageBitmap(bitmap4);


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

            cjor.setTag("tag");
            rq.add(cjor);


            //fim da gambi


            //desconverter

            //byte[] fotoemBytes = fotoWS.getBytes(Charset.forName("UTF-8"));
            //Bitmap bitmap3 = BitmapFactory.decodeByteArray(FotoemBytes, 0, FotoemBytes.length);

            //ivSelectedImage.setImageBitmap(bitmap3);
            //fim desconverter

        } catch (FileNotFoundException e) {
            // handle errors
        } catch (IOException e) {
            // handle errors
        } finally {
            if (parcelFD != null)
                try {
                    parcelFD.close();
                } catch (IOException e) {
                    // ignored
                }
        }
    }
}


package com.example.thiago.frequnciafcil;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Foto extends ActionBarActivity {

    private ImageView ivSelectedImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foto);
        ivSelectedImage = (ImageView) findViewById(R.id.ivSelectedImage);


        Intent intent = getIntent();
        String email = intent.getStringExtra("login");
        String nome = intent.getStringExtra("name");
        String senha = intent.getStringExtra("senha");
        String matricula = intent.getStringExtra("matricula");
        String levelacess = intent.getStringExtra("levelacess");
        System.out.println(email+nome+senha+matricula+levelacess);


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


    public void selecionarFoto(View V){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(intent, "Complete action using"), 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == 1 && null != data) {
            decodeUri(data.getData());
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
            int scale = 1;
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
            Bitmap bitmap = BitmapFactory.decodeFileDescriptor(imageSource, null, o2);
            //ivSelectedImage.setImageBitmap(bitmap);



            //converter
            Bitmap bitmap2 = bitmap;

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap2.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] foto = stream.toByteArray(); //em tese, isso que mandarei para o banco de dados !!!! detalhe: a variavel foto é um byteArray !!!!!!!
            //fim converter

            //desconverter
            Bitmap bitmap3 = BitmapFactory.decodeByteArray(foto , 0, foto.length); //o banco me enviará o byteArray, e eu converterei em imagem ! a variavel foto é um byteArray !!!!!!!
            ivSelectedImage.setImageBitmap(bitmap3);
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

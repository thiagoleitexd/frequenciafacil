package com.example.thiago.frequnciafcil;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.example.thiago.frequnciafcil.R;

public class Foto extends ActionBarActivity {

    private ImageView ivSelectedImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foto);

        ivSelectedImage = (ImageView) findViewById(R.id.ivSelectedImage);

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
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 5678);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);




        if (requestCode == 5678 && data != null && data.getData() != null) {

            Uri uri = data.getData();



            Cursor cursor = getContentResolver().query
                    (uri, new String[]
                        {
                        android.provider.MediaStore.Images.ImageColumns.DATA
                        }, null, null, null
                    );
            cursor.moveToFirst();

            final String imageFilePath = cursor.getString(0);

            System.out.println(cursor.getString(0));

            cursor.close();

            Bitmap bitmap = BitmapFactory.decodeFile(imageFilePath);
            ivSelectedImage.setImageBitmap(bitmap);
        }
    }



}

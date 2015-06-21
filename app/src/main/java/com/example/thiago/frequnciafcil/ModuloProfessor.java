package com.example.thiago.frequnciafcil;

import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.thiago.frequnciafcil.R;

import java.util.UUID;

public class ModuloProfessor extends ActionBarActivity {

    private EditText senhaGerada;
    private TextView status;
    private Button bstatus;
    private Animation anim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modulo_professor);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_modulo_professor, menu);
        return true;
    }

    public void gerarSenha(View v){

        UUID uuid = UUID.randomUUID();
        String myRandom = uuid.toString();
        senhaGerada = (EditText) findViewById(R.id.senhaAulaProf);
        senhaGerada.setText(myRandom.substring(0, 5));
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

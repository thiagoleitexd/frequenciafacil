package com.example.thiago.frequnciafcil;

import android.content.Intent;
import android.os.SystemClock;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.thiago.frequnciafcil.R;

import java.util.Timer;
import java.util.TimerTask;


public class PagelaVirtual extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagela_virtual);

    //    new Timer().schedule(new TimerTask() {
    //        public void run() {
    //            startActivity(new Intent(PagelaVirtual.this, MainActivityAluno.class));
    //            finish();
    //        }
    //    }, 2000 /*amount of time in milliseconds before execution*/);
    //
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_pagela_virtual, menu);
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


    public void irTelaProfessor(View v){

        Intent intent;
        intent = new Intent(PagelaVirtual.this,MainActivityAluno.class);
        intent.putExtra("levelacess","1");
        startActivity(intent);
    }

    public void irTelaAluno(View v){

        Intent intent;
        intent = new Intent(PagelaVirtual.this,MainActivityAluno.class);
        intent.putExtra("levelacess","2");
        startActivity(intent);

    }

}

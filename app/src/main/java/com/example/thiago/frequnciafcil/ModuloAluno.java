package com.example.thiago.frequnciafcil;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


public class ModuloAluno extends ActionBarActivity {

    private String array_spinner[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modulo_aluno);

        TextView apresentacao = (TextView) findViewById(R.id.apresentacao);


        array_spinner=new String[5];
        array_spinner[0]="Selecione uma Disciplina";
        array_spinner[1]="Redes de computadores";
        array_spinner[2]="Fundamentos de Sistema de Informação";
        array_spinner[3]="Programação Web";
        array_spinner[4]="Empreendedorismo em Informática";
        Spinner s = (Spinner) findViewById(R.id.disciplinas);
        ArrayAdapter adapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item, array_spinner);
        s.setAdapter(adapter);

        Intent intent = getIntent();
        String apikey = intent.getStringExtra("apiKey");
        String nome = intent.getStringExtra("name");
        apresentacao.setText("Olá, " + nome + "!\nSeja bem vindo ao aplicativo Frequência Fácil.");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_modulo_aluno, menu);
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

    public void presente(View view) {

        Toast.makeText(ModuloAluno.this,
                "Presença Registrada com Sucesso!",
                Toast.LENGTH_SHORT)
                .show();


    }
}

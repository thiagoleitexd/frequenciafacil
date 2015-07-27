package com.example.thiago.frequnciafcil;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class MenuAluno extends ActionBarActivity {
    TextView apresentacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_aluno);


        Intent intent = getIntent();
        String nome = intent.getStringExtra("name");
        apresentacao = (TextView) findViewById(R.id.apresentacao);
        apresentacao.setText("Olá, " + nome + "!\nSeja bem vindo ao aplicativo Pagela Virtual.");


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_menu_aluno, menu);
        return true;
    }

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
            case R.id.menuSenhaAluno:
                Intent intent2 = new Intent(MenuAluno.this, MudarSenha.class);
                startActivity(intent2);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }


    public void open(View view){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Pagela Virtual");
        alertDialogBuilder.setMessage("Versão 1.0");
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void marcarPresenca (View V){

        Intent intent = new Intent(MenuAluno.this, ModuloAluno.class);

        startActivity(intent);


    }
    public void dataDasFaltas (View V){

        Intent intent = new Intent(MenuAluno.this, AlunoFaltas.class);

        startActivity(intent);


    }

}

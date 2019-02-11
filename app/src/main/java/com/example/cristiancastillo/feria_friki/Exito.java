package com.example.cristiancastillo.feria_friki;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Exito extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exito);
        getSupportActionBar().hide();
    }

    public void Siguiente (View view){

        Intent Siguiente = new Intent(this, RegistroSocio.class);
        startActivity(Siguiente);

    }

    public void Volver (View view){

        Intent Volver = new Intent(this, IniciarSesion.class);
        startActivity(Volver);

    }
}
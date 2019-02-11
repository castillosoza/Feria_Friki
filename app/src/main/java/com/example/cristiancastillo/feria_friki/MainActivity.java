package com.example.cristiancastillo.feria_friki;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Ocultar ActionBar
        getSupportActionBar().hide();
    }

    //Ir a Activity Iniciar Sesion
    public void IniciarSesion (View view){

        Intent InciarSesion = new Intent(this, IniciarSesion.class);
        startActivity(InciarSesion);

    }
    //Ir a Activity HazteSocio
    public void HazteSocio (View view){

        Intent HazteSocio = new Intent(this, HazteSocio.class);
        startActivity(HazteSocio);

    }
}
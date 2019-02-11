package com.example.cristiancastillo.feria_friki;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class RegistroSocio extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_socio);
        getSupportActionBar().hide();
    }

    public void Siguiente (View view){

        Intent Siguiente = new Intent (this, RegistroSocio2.class);
        startActivity(Siguiente);
    }

}

package com.example.cristiancastillo.feria_friki;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;

public class RegistroSocio2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_socio2);
        getSupportActionBar().hide();
    }

    public void Pagar(View view){

        Intent pagar = new Intent(this,ExitoSocio.class);
        startActivity(pagar);

    }

}

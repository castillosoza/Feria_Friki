package com.example.cristiancastillo.feria_friki;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class IniciarSesion extends AppCompatActivity {

    private EditText correo, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iniciar_sesion);
        getSupportActionBar().hide();

        correo = (EditText)findViewById(R.id.et_correoinisesion);
        password = (EditText)findViewById(R.id.et_pwd);
    }

    public void InicioSesion (View view){

        //Validamos que el campo correo tenga datos
        if(correo.getText().toString().trim().length()>0 && password.getText().toString().trim().length()>0) {

            if (correo.getText().toString().equals("castillosoza@gmail.com")) {

                if (password.getText().toString().equals("123456")){

                    Toast.makeText(this, "Acceso Correcto", Toast.LENGTH_LONG).show();

                }else {Toast.makeText(this,"Acceso invalido, por favor valide su correo/contraseña", Toast.LENGTH_SHORT).show();}



            } else {Toast.makeText(this,"Acceso invalido, por favor valide su correo/contraseña", Toast.LENGTH_SHORT).show();}


        }else {

            Toast.makeText(this, "Por favor complete los campos", Toast.LENGTH_LONG).show();
        }

    }
}

package com.example.cristiancastillo.feria_friki;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class HazteSocio extends AppCompatActivity {

    private EditText password, correo;
    FirebaseAuth firebaseAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hazte_socio);
        getSupportActionBar().hide();

        password = (EditText)findViewById(R.id.et_pwd);
        correo = (EditText)findViewById(R.id.et_correohaztesocio);
        firebaseAuth = FirebaseAuth.getInstance();
    }

    //Ir a Siguiente
    public void Siguiente (View view){



        String correo_string = correo.getText().toString().trim();
        String pwd_string = password.getText().toString().trim();

        firebaseAuth.createUserWithEmailAndPassword(correo_string, pwd_string).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    Toast.makeText(HazteSocio.this, "¡Bien!, registro exitoso", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(HazteSocio.this, "¡Ups!, algo sucedio con tú registro.", Toast.LENGTH_SHORT).show();
                }

            }

        });



        //Validar que campos no esten vacios
        if (correo.getText().toString().trim().length()>0 && password.getText().toString().trim().length()>=6){



            //Validamos que se haya ingresado una dirección de correo
        }else if(correo.getText().toString().trim().length()==0){

            Toast.makeText(this,"Por favor ingrese su correo",Toast.LENGTH_LONG).show();

            //Validamos que se haya ingresado una contraseña
        }else if(password.getText().toString().trim().length()<6){

            Toast.makeText(this,"Por favor ingrese una contraseña con mínimo 6 caracteres",Toast.LENGTH_LONG).show();

        }

    }


}

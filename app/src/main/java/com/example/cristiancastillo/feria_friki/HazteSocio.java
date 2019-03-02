package com.example.cristiancastillo.feria_friki;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;



public class HazteSocio extends AppCompatActivity implements
        View.OnClickListener {

    private static final String TAG = "EmailPassword";

    private EditText password, correo;
    private FirebaseAuth mAuth;
    private TextView estado, detalle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hazte_socio);
        getSupportActionBar().hide();

        password = (EditText)findViewById(R.id.et_pwd);
        correo = (EditText)findViewById(R.id.et_correohaztesocio);
        estado = (TextView)findViewById(R.id.estado);
        detalle = (TextView)findViewById(R.id.detalle);

        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.btn_registro).setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);

    }


    private boolean validateForm() {
        boolean valid = true;

        String email = correo.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            correo.setError("El email es necesario.");
            valid = false;
        } else {

            correo.setError(null);
        }

        String pwd = password.getText().toString().trim();
        if (TextUtils.isEmpty(pwd)) {
            password.setError("La contraseña es necesaria.");
            valid = false;



        } else {
            password.setError(null);

            if(pwd.length() >= 6){

            }else {Toast.makeText(this, "La contraseña debe tener mínimo 6 caracteres", Toast.LENGTH_SHORT).show();}
                   }

        return valid;

    }




    private void createAccount(String correo_string, String pwd_string) {
        Log.d(TAG, "Cuenta exitosa para: " + correo_string);
        if (!validateForm()) {
            Log.d(TAG,"Validación de formulario incorrecta");
            Toast.makeText(HazteSocio.this, "Validación de formulario incorrecta",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(correo_string, pwd_string)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "¡Genial!, ¡Usuario creado con exito!");
                            Toast.makeText(HazteSocio.this, "¡Genial!, ¡Usuario creado con éxito!.",
                                    Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);


                            Intent intent = new Intent(HazteSocio.this, IniciarSesion.class);
                            startActivity(intent);




                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "¡Pucha, tenemos problemas para crear la cuenta!", task.getException());
                            Toast.makeText(HazteSocio.this, "¡Pucha, tenemos problemas para crear la cuenta, por favor revisa tú conexión a internet!.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }



                    }
                });
        // [END create_user_with_email]




    }

    private void updateUI(FirebaseUser user) {

        if (user != null) {
           estado.setText(getString(R.string.emailpassword_status_fmt, user.getEmail(), user.isEmailVerified()));
           detalle.setText(getString(R.string.firebase_status_fmt, user.getUid()));
        } else {
            estado.setText(R.string.signed_out);
            detalle.setText(null);


        }

    }

        @Override
        public void onClick (View v){

            int i = v.getId();
            if (i == R.id.btn_registro) {
                createAccount(correo.getText().toString(), password.getText().toString());
            }



        }
    }

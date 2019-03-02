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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class IniciarSesion extends AppCompatActivity implements
        View.OnClickListener {


    private static final String TAG = "EmailPassword";

    private EditText correo_is, password_is;
    private TextView estado_is, detalle_is;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iniciar_sesion);
        getSupportActionBar().hide();

        //Campos de texto
        correo_is = (EditText) findViewById(R.id.et_correoinisesion);
        password_is = (EditText) findViewById(R.id.et_pwd);

        //Log de registro
        estado_is = (TextView) findViewById(R.id.tv_estado);
        detalle_is = (TextView) findViewById(R.id.tv_detalle);

        //Firebase
        mAuth = FirebaseAuth.getInstance();


        findViewById(R.id.btn_acceder).setOnClickListener(this);


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

        String email = correo_is.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            correo_is.setError("El email es necesario.");
            valid = false;
        } else {

            correo_is.setError(null);
        }

        String pwd = password_is.getText().toString().trim();
        if (TextUtils.isEmpty(pwd)) {
            password_is.setError("La contraseña es necesaria.");
            valid = false;


        } else {
            password_is.setError(null);

            if (pwd.length() >= 6) {

            } else {
                Toast.makeText(this, "La contraseña debe tener mínimo 6 caracteres", Toast.LENGTH_SHORT).show();
            }
        }

        return valid;

    }

    private void signIn(final String correo_string, String password) {
        Log.d(TAG, "Sesión iniciada para: " + correo_string);
        if (!validateForm()) {
            return;
        }

        // [START sign_in_with_email]


        mAuth.signInWithEmailAndPassword(correo_string, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "¡Bienvenido!");
                            Toast.makeText(IniciarSesion.this, "¡Bienvenido " + correo_string + "!",
                                    Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);

                            Intent intent = new Intent(IniciarSesion.this, PerfilUsuario.class);
                            startActivity(intent);





                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "Error en inicio de sesión", task.getException());
                            Toast.makeText(IniciarSesion.this, "Error en inicio de sesión",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // [START_EXCLUDE]
                        if (!task.isSuccessful()) {
                            estado_is.setText(R.string.auth_failed);
                        }

                        // [END_EXCLUDE]
                    }
                });
        // [END sign_in_with_email]


    }


    private void updateUI(FirebaseUser user) {


        if (user != null) {


            estado_is.setText(getString(R.string.emailpassword_status_fmt, user.getEmail(), user.isEmailVerified()));
            detalle_is.setText(getString(R.string.firebase_status_fmt, user.getUid()));


            findViewById(R.id.btn_acceder).setVisibility(View.VISIBLE);


        } else {
            estado_is.setText(R.string.signed_out);
            detalle_is.setText(null);


        }

    }


    @Override
    public void onClick(View v) {

        int i = v.getId();
        if (i == R.id.btn_acceder) {
            signIn(correo_is.getText().toString(), password_is.getText().toString());
        }

    }
}

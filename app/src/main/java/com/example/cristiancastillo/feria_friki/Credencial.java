package com.example.cristiancastillo.feria_friki;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.FirebaseFunctionsException;
import com.google.firebase.functions.HttpsCallableResult;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mercadopago.android.px.core.MercadoPagoCheckout;
import com.mercadopago.android.px.model.Payment;
import com.mercadopago.android.px.model.exceptions.MercadoPagoError;


import java.util.HashMap;
import java.util.Map;

public class Credencial extends AppCompatActivity  {

    private EditText nick, tel, rut, direcccion;
    private TextView UrlIMGFondo, tvPref;
    private Button IMGFondo, compra;
    private static final  int GALLERY = 1;
    private ProgressDialog cargaImagenFondo;
    private StorageReference mStorage;
    private DatabaseReference mRootReference;
    private FirebaseUser mUser;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseFunctions mFunctions;
    private static final int REQUEST_CODE = 1;
    protected Context context;
    protected MercadoPagoCheckout checkout;
    private final String MP_PUBLIC_KEY = "TEST-4680981369947056-122222-fbe23d8a5460c8b500c9a73e93d24a45-26020571";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credencial);
        getSupportActionBar().hide();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        //navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        //BottomNavigationViewHelper.disableShiftMode(navigation);
        Menu menu = navigation.getMenu();
        MenuItem menuItem = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.CUPCAKE) {
            menuItem = menu.getItem(1);
        }
        menuItem.setChecked(true);

        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.navigation_home:

                        Intent PerfilUsuario = new Intent(Credencial.this, PerfilUsuario.class);
                        startActivity(PerfilUsuario);

                        break;
                    case R.id.navigation_dashboard:

                        break;

                    case R.id.navigation_notifications:

                        Intent Eventos = new Intent(Credencial.this, Eventos.class);
                        startActivity(Eventos);
                        break;


                }


                return false;
            }
        });


        nick = (EditText)findViewById(R.id.et_nick);
        tel = (EditText)findViewById(R.id.et_telefono);
        rut = (EditText)findViewById(R.id.et_rut);
        direcccion = (EditText)findViewById(R.id.et_direccion);
        UrlIMGFondo = (TextView) findViewById(R.id.tv_url);
        tvPref = (TextView) findViewById(R.id.tv_preference);
        IMGFondo = (Button)findViewById(R.id.btn_cargaIMGFondo);
        compra = (Button)findViewById(R.id.btn_compra);
        cargaImagenFondo = new ProgressDialog(this);




        solicitarImagenFondo();
        //Firebase
        Firebase();

    }

    private void Firebase() {
        mRootReference = FirebaseDatabase.getInstance().getReference();
        mStorage = FirebaseStorage.getInstance().getReference();
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mFunctions = FirebaseFunctions.getInstance();
        mFunctions.getHttpsCallable("buyCredencial").call(mUser.getEmail());



    }



    private void solicitarImagenFondo() {
        IMGFondo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent cargar_imagen = new Intent(Intent.ACTION_PICK);
                cargar_imagen.setType("image/*");
                startActivityForResult(cargar_imagen, GALLERY);
            }
        });
    }

    public void launchCheckout(View view) {

        FirebaseFunctions.getInstance()
                .getHttpsCallable("buyCredential")
                .call("email"+mUser.getEmail())
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(Credencial.this,"Falla la llamada", Toast.LENGTH_LONG).show();
                    }
                }).addOnSuccessListener(new OnSuccessListener<HttpsCallableResult>() {
                      @Override
                      public void onSuccess(HttpsCallableResult httpsCallableResult) {

                        Toast.makeText(Credencial.this,"Llamada realizada con éxito", Toast.LENGTH_LONG).show();
                        String result = httpsCallableResult.getData().toString();
                          Log.i("Captura preference", "Preference original: "+result);
                        String cambio = result.replace("{preference=", "");
                        String preference = cambio.replace("}","");
                        Log.i("Captura preference", "Preference: "+preference);
                        Toast.makeText(Credencial.this,"Preference: "+preference, Toast.LENGTH_LONG).show();

                        startMercadoPagoCheckout(preference);
                      }
                });

    }

    private void startMercadoPagoCheckout(final String preference) {
       new MercadoPagoCheckout.Builder( MP_PUBLIC_KEY, preference).build()
           .startPayment(Credencial.this, REQUEST_CODE);
        Log.i("Parametros recibidos", "MP_PUBLIC_KEY: "+MP_PUBLIC_KEY+" | Preference: "+preference+" | REQUEST_CODE: "+REQUEST_CODE);


    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data){
        //super.onActivityResult(requestCode, resultCode, data);
        //Carga de imagen
        if(requestCode == GALLERY && resultCode == RESULT_OK){

            cargaImagenFondo.setTitle("Cargando...");
            cargaImagenFondo.setMessage("Cargado imagen para tú credencial");
            cargaImagenFondo.setCancelable(false);
            cargaImagenFondo.show();

            Uri uri = data.getData();
            StorageReference filePath = mStorage.child("Android/credentials").child(uri.getLastPathSegment());
            filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    cargaImagenFondo.dismiss();

                    Uri mostrarImagen = taskSnapshot.getDownloadUrl();
                    String descargarImagen = mostrarImagen.toString();
                    UrlIMGFondo.setText(descargarImagen);

                    Toast.makeText(Credencial.this, "¡Bien! Cargaste tú foto para tú credencial", Toast.LENGTH_SHORT).show();

                }
            });

        }
        //Checkout
        if (requestCode == REQUEST_CODE) {


            if (resultCode == MercadoPagoCheckout.PAYMENT_RESULT_CODE) {

                Toast.makeText(Credencial.this, "¡Entraste el Checkout", Toast.LENGTH_SHORT).show();

                final Payment payment = (Payment) data.getSerializableExtra(MercadoPagoCheckout.EXTRA_PAYMENT_RESULT);
                ((TextView) findViewById(R.id.tv_mPago)).setText("Resultado del pago: " + payment.getPaymentStatus());
                Toast.makeText(Credencial.this, "Pago realizado con éxito", Toast.LENGTH_SHORT).show();

                Log.d("Funcion OK", "Aprobado");

                String name = nick.getText().toString().trim();
                String phone = tel.getText().toString().trim();
                String run = rut.getText().toString().trim();
                String address = direcccion.getText().toString().trim();
                String credentialImageUrl = UrlIMGFondo.getText().toString().trim();
                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                Map<String, Object> datosSocio = new HashMap<>();

                datosSocio.put("name", name);
                datosSocio.put("phone", phone);
                datosSocio.put("rut", run);
                datosSocio.put("address", address);
                datosSocio.put("credentialImageUrl", credentialImageUrl);
                datosSocio.put("isMember", "true");
                datosSocio.put("uid", uid);


                mRootReference.child(uid).setValue(datosSocio);

                Toast.makeText(Credencial.this, "Cuenta de socio creada con éxito", Toast.LENGTH_SHORT).show();


                //Done!
            } else if (resultCode == RESULT_CANCELED) {
                if (data != null && data.getExtras() != null
                        && data.getExtras().containsKey(MercadoPagoCheckout.EXTRA_ERROR)) {
                    final MercadoPagoError mercadoPagoError =
                            (MercadoPagoError) data.getSerializableExtra(MercadoPagoCheckout.EXTRA_ERROR);
                    ((TextView) findViewById(R.id.tv_mPago)).setText("Error: " + mercadoPagoError.getMessage());
                    Toast.makeText(Credencial.this, "Pago cancelado", Toast.LENGTH_SHORT).show();
                    //Resolve error in checkout
                } else {
                    //Resolve canceled checkout
                    Log.d("Funcion No OK", "No Aprobado");
                }
            }


        }

    }



}









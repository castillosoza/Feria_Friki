package com.example.cristiancastillo.feria_friki;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;

public class HazteSocio2 extends AppCompatActivity {


    private EditText  fecNacimiento;
    private Button cargar_imagen;
    private Spinner spinner_sexo;
    private ImageView iv_cargarImagen, calendar;
    private int GALLERY = 1, CAMERA = 2, dia, mes,ano;
    private static final String IMAGE_DIRECTORY = "/demonuts";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hazte_socio2);
        getSupportActionBar().hide();

        //Relacionamos variables declaradas, con los compononentes que se encuentran en el TableLayout


        fecNacimiento = (EditText)findViewById(R.id.et_fecNacimiento);
        spinner_sexo = (Spinner)findViewById(R.id.sp_sexo);
        cargar_imagen = (Button)findViewById(R.id.btn_cargar);
        iv_cargarImagen = (ImageView)findViewById(R.id.cargarImagen);
        calendar = (ImageView) findViewById(R.id.calendar);


        //Escoger Sexo
        String [] sexo = {"Seleccione su sexo","Masculino", "Femenino"};
        ArrayAdapter <String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item_sexo, sexo);
        spinner_sexo.setAdapter(adapter);


        //Funcionalidad botón cargar
        cargar_imagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPictureDialog();
            }
        });

        //Funcionalidad botón calendario

    }

    //Seleccionamos el origen de la imagen
    private void showPictureDialog() {

        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Seleccionar foto desde");
        String[] pictureDialogItems = {
                "Galería"};
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallery();
                                break;
                            case 1:
                                takePhotoFromCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();

    }

    //Metodo para escoger desde la Galeria
    private void choosePhotoFromGallery() {

        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, GALLERY);
    }

    //Metodo para escoger desde la Cámara
    private void takePhotoFromCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);
    }

    //Almacenar la imagen
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == this.RESULT_CANCELED){
            return;
        }
        if (requestCode == GALLERY){

            if (data != null){

                Uri contentUri = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentUri);
                    String path = saveImage(bitmap);
                    Toast.makeText(HazteSocio2.this, "Imagen guardada", Toast.LENGTH_SHORT).show();
                    iv_cargarImagen.setImageBitmap(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(HazteSocio2.this, "Ha ocurrido un error", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == CAMERA) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            iv_cargarImagen.setImageBitmap(thumbnail);
            saveImage(thumbnail);
            Toast.makeText(HazteSocio2.this, "Imagen guardada", Toast.LENGTH_SHORT).show();
        }
    }

    //Convertir ruta a String
    public String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
        }

        try {
            File f = new File(wallpaperDirectory, Calendar.getInstance()
                    .getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(this,
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            Log.d("TAG", "Archivo guardado::--->;" + f.getAbsolutePath());

            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }

    private void  requestMultiplePermissions(){
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            Toast.makeText(getApplicationContext(), "Permisos concedidos. ¡Gracias!", Toast.LENGTH_SHORT).show();
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            //openSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest>permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(getApplicationContext(), "Ha ocurrido un error :( ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }


    //Ir a Siguiente
    public void Siguiente (View view){

        if(iv_cargarImagen.getDrawable() != null) {

            Intent Siguiente = new Intent(this, Exito.class);
            startActivity(Siguiente);

        }else{

            Toast.makeText(this,"Debes cargar un avatar para poder avanzar", Toast.LENGTH_SHORT).show();

        }
    }

    //Calendario
    public void Calendario (View view){

    final Calendar c = Calendar.getInstance();
        dia=c.get(Calendar.DAY_OF_MONTH);
        mes=c.get(Calendar.MONTH);
        ano=c.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                fecNacimiento.setText(dayOfMonth+"/"+(month+1)+"/"+year);

            }
        },dia,mes,ano);

        datePickerDialog.show();

    }

}

package com.example.pm2e10002_0039_1;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pm2e10002_0039_1.Configuration.SQLiteConection;
import com.example.pm2e10002_0039_1.Configuration.Transactions;
import com.example.pm2e10002_0039_1.Models.Pais;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class PrincipalActivity extends AppCompatActivity {
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode  == peticion_captura_imagen)
        {

            try {
                File foto = new File(currentPhotoPath);
                img.setImageURI(Uri.fromFile(foto));
            }
            catch (Exception ex)
            {
                ex.toString();
            }

        }
    }

    static final int peticion_captura_imagen = 101;
    static final int peticion_acceso_camara = 102;
    private SQLiteConection conexion;

    private ImageView img;
    private Button btnGuardar, btnList;
    private Spinner lista;
    private TextView txtNombre, txtTelefono, txtNota;
    private byte[] byteArray;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        conexion= new SQLiteConection(this, Transactions.NameDataBase,null,1);
        getPaises();

        btnGuardar = findViewById(R.id.btnGuardar);
        btnList = findViewById(R.id.btnLista);

        txtNombre = findViewById(R.id.txtNombre);
        txtTelefono = findViewById(R.id.txtTelefono);
        txtNota = findViewById(R.id.txtNota);

        lista= findViewById(R.id.cmbPais);

        img=findViewById(R.id.img);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayPaisString);

        lista.setAdapter(adapter);

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validar()){
                    Toast.makeText(getApplicationContext(), "Todo ok", Toast.LENGTH_LONG).show();
                    Bitmap bitmap = ((BitmapDrawable) img.getDrawable()).getBitmap();
                    byteArray = getBitmapAsByteArray(bitmap);
                    AddContact();


                }
            }
        });

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                permisos();
            }
        });


        btnList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ListaActivity.class);
                startActivity(intent);
            }
        });

    }


    private void AddContact(){
        SQLiteConection conexion = new SQLiteConection(this, Transactions.NameDataBase, null, 1);
        SQLiteDatabase db = conexion.getWritableDatabase();

        ContentValues valores= new ContentValues();
        valores.put(Transactions.nombreC, txtNombre.getText().toString());
        valores.put(Transactions.telefonoC, txtTelefono.getText().toString());
        valores.put(Transactions.notaC, txtNota.getText().toString());
        valores.put(Transactions.paisC, lista.getSelectedItem().toString());
        valores.put(Transactions.imagenC, byteArray);

        try {
            Long result = db.insert(Transactions.TablaContactos, Transactions.idC, valores);
            if(result!=-1){
                Toast.makeText(getApplicationContext(),"Registro insertado: "+result.toString(),Toast.LENGTH_LONG).show();
            }

        }catch (SQLException ex){
            Toast.makeText(getApplicationContext(),"Ocurrio un error: "+ex.getMessage(),Toast.LENGTH_LONG).show();
        }

        db.close();
        CleanScreen();
    }

    private void CleanScreen() {
        txtNombre.setText("");
        txtTelefono.setText("");
        txtNota.setText("");
        lista.setSelection(0);
        img.setImageResource(R.drawable.hombre);
    }


    private static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 0, outputStream);
        return outputStream.toByteArray();

    }


    private ArrayList<Pais> arrayPais;
    private void getPaises(){
        SQLiteDatabase db = conexion.getReadableDatabase();
        Pais pais=null;

        arrayPais=new ArrayList<Pais>();

        Cursor cursor=db.rawQuery(Transactions.SelectTablePaises, null);
        while (cursor.moveToNext()){
            pais=new Pais();
            pais.setId(cursor.getInt(0));
            pais.setNombre(cursor.getString(1));
            pais.setCodigo(cursor.getInt(2));

            arrayPais.add(pais);
        }
        cursor.close();

        fillCmb();
    }

    ArrayList<String> arrayPaisString;
    private void fillCmb(){
        arrayPaisString = new ArrayList<String>();
        for (int i=0;i<arrayPais.size();i++){
            arrayPaisString.add(""+arrayPais.get(i).getNombre() + " (" +arrayPais.get(i).getCodigo()+")");
        }
    }


    private boolean validar(){
        if(txtNombre.getText().toString().isEmpty() || txtNombre.getText().toString().length()==0){
            Toast.makeText(getApplicationContext(),"Debe rellenar el campo Nombre", Toast.LENGTH_LONG).show();
            return false;
        }
        if(txtTelefono.getText().toString().isEmpty() || txtTelefono.getText().toString().length()==0){
            Toast.makeText(getApplicationContext(),"Debe rellenar el campo Telefono", Toast.LENGTH_LONG).show();
            return false;
        }
        if(txtNota.getText().toString().isEmpty() || txtNota.getText().toString().length()==0){
            Toast.makeText(getApplicationContext(),"Debe rellenar el campo Nota", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }


    private void permisos()
    {
        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},peticion_acceso_camara);
        }
        else
        {
            dispatchTakePictureIntent();
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.pm2e10002_0039_1.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, peticion_captura_imagen);
            }
        }
    }

    private String currentPhotoPath;
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }
}
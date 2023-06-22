package com.example.pm2e10002_0039_1;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pm2e10002_0039_1.Configuration.SQLiteConection;
import com.example.pm2e10002_0039_1.Configuration.Transactions;
import com.example.pm2e10002_0039_1.Models.Contacto;

import java.util.ArrayList;

public class ListaActivity extends AppCompatActivity {

    private SQLiteConection conexion;
    private Button btnBack, btnShare, btnViewImage, btnDelete, btnUpdate;
    private ListView listContactos;
    private int valSelected=-1;
    private byte[] blobData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);

        conexion = new SQLiteConection(this, Transactions.NameDataBase, null, 1);
        listContactos=findViewById(R.id.listContactos);

        ObtenerTabla();

        btnShare=findViewById(R.id.btnCompartir);
        btnViewImage=findViewById(R.id.btnVerImagen);
        btnDelete=findViewById(R.id.btnEliminar);
        btnUpdate=findViewById(R.id.btnActualizar);


        btnViewImage.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("Range")
            @Override
            public void onClick(View v) {
                String selectedText= (String) listContactos.getItemAtPosition(valSelected).toString();
                String[] vals= selectedText.split("-");
                String part1=vals[0];
/*
                String where="id=?";
                String[] argumentosWhere = {part1};
                String columnaBlob = "imagen";

                SQLiteDatabase db = conexion.getReadableDatabase();
                Cursor cursor=db.query(Transactions.TablaContactos,new String[]{columnaBlob},where, argumentosWhere,null,null,null);
                if (cursor.moveToFirst()) {
                    blobData = cursor.getBlob(cursor.getColumnIndex(columnaBlob));
                    cursor.close();
                }
*/

                Intent intent = new Intent(getApplicationContext(), ImagenActivity.class);
                intent.putExtra("id",part1);
                startActivity(intent);



            }
        });

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedText= (String) listContactos.getItemAtPosition(valSelected).toString();
                String[] vals= selectedText.split("-");
                String part3=vals[2];

                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:"+part3));
                    startActivity(callIntent);
                } else {
                    requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, 1);
                }

            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(valSelected!=-1){
                    mostrarDialogoSiNo("Eliminar");
                }else{
                    Toast.makeText(getApplicationContext(), "Debe seleccionar un item", Toast.LENGTH_LONG).show();
                }
            }
        });

        btnBack = findViewById(R.id.btnAtras);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PrincipalActivity.class);
                startActivity(intent);
            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayContactos);
        listContactos.setAdapter(adapter);

        listContactos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                valSelected=position;
            }
        });
    }

    private void DeleteContacto(String id){
        SQLiteConection conexion = new SQLiteConection(this, Transactions.NameDataBase, null, 1);
        SQLiteDatabase db = conexion.getWritableDatabase();
        String[] argWhere={String.valueOf(id)};

        try {
            int rowAfectada=db.delete(Transactions.TablaContactos,"id=?",argWhere);
            if(rowAfectada==1){
                Toast.makeText(getApplicationContext(),"Registro eliminado",Toast.LENGTH_LONG).show();

                ObtenerTabla();


            }
        }catch (SQLException ex){

        }
        db.close();
    }


    ArrayList<Contacto> lista;
    private void ObtenerTabla() {
        SQLiteDatabase db = conexion.getReadableDatabase();
        Contacto contacto = null;
        lista = new ArrayList<Contacto>();
        // Cursor de Base de datos
        Cursor cursor = db.rawQuery(Transactions.SelectTableContactos, null);

        // Recorrer el cursor
        while (cursor.moveToNext()){
            contacto = new Contacto();
            //persona.setId(cursor.getInt(0));
            contacto.setId(cursor.getInt(0));
            contacto.setNombre(cursor.getString(1));
            contacto.setTelefono(cursor.getString(2));
            contacto.setNota(cursor.getString(3));
            contacto.setPais(cursor.getString(4));
            contacto.setImg(cursor.getBlob(5));


            lista.add(contacto);
        }
        cursor.close();

        fillList();

    }

    private ArrayList<String> arrayContactos;
    private void fillList() {
        arrayContactos = new ArrayList<String>();
        for (int i=0;i<lista.size();i++){
            arrayContactos.add(lista.get(i).getId()+"-"+lista.get(i).getNombre()+ "-"+lista.get(i).getTelefono());
            //arrayContactos.add(lista.get(i));
        }
    }


    //Alerta de confirmacion
    private void mostrarDialogoSiNo(String text) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmación");
        builder.setMessage("¿Estás seguro de "+text+"?");

        builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(text.equals("Eliminar")){
                    String selectedText= (String) listContactos.getItemAtPosition(valSelected).toString();
                    String[] vals= selectedText.split("-");
                    String part1=vals[0];
                    DeleteContacto(part1);
                }
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Acciones a realizar si se selecciona "No"
                // Aquí puedes agregar el código que se ejecutará cuando se seleccione "No"
            }
        });

        builder.show();
    }
}
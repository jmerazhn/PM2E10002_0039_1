package com.example.pm2e10002_0039_1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.pm2e10002_0039_1.Configuration.SQLiteConection;
import com.example.pm2e10002_0039_1.Configuration.Transactions;
import com.example.pm2e10002_0039_1.Models.Contacto;

import java.util.ArrayList;

public class ListaActivity extends AppCompatActivity {

    private SQLiteConection conexion;
    private Button btnBack;
    private ListView listContactos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);

        conexion = new SQLiteConection(this, Transactions.NameDataBase, null, 1);
        listContactos=findViewById(R.id.listContactos);

        ObtenerTabla();

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
    }


    ArrayList<Contacto> lista;
    private void ObtenerTabla() {
        SQLiteDatabase db = conexion.getReadableDatabase();
        Contacto contacto = null;
        lista = new ArrayList<Contacto>();
        // Cursor de Base de datos
        System.out.println(Transactions.SelectTableContactos);
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
            arrayContactos.add(""+lista.get(i).getNombre()+ " | "+lista.get(i).getTelefono());
        }
    }
}
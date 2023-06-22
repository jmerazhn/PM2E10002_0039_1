package com.example.pm2e10002_0039_1;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.pm2e10002_0039_1.Configuration.SQLiteConection;
import com.example.pm2e10002_0039_1.Configuration.Transactions;

public class ImagenActivity extends AppCompatActivity {

    private SQLiteConection conexion;
    Button btnBack;
    ImageView img;
    String id;
    private byte[] blobData;
    @SuppressLint("Range")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imagen);

        img=findViewById(R.id.imageView2);

        Bundle datos=this.getIntent().getExtras();

        if (datos != null){
            id=datos.getString("id");
        }

        btnBack=findViewById(R.id.btnBack);

        SQLiteConection conexion = new SQLiteConection(this, Transactions.NameDataBase, null, 1);
        SQLiteDatabase db = conexion.getReadableDatabase();


        String where="id=?";
        String[] argumentosWhere = {id};
        String columnaBlob = "imagen";

        Cursor cursor=db.query(Transactions.TablaContactos,new String[]{columnaBlob},where, argumentosWhere,null,null,null);
        if (cursor.moveToFirst()) {
            blobData = cursor.getBlob(cursor.getColumnIndex(columnaBlob));
            cursor.close();
        }

        Bitmap bitmap = BitmapFactory.decodeByteArray(blobData, 0, blobData.length);
        img.setImageBitmap(bitmap);




        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ListaActivity.class);
                startActivity(intent);
            }
        });
    }
}
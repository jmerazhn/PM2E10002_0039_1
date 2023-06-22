package com.example.pm2e10002_0039_1.Configuration;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class SQLiteConection extends SQLiteOpenHelper{


    public SQLiteConection(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Transactions.CreateTablePaises);
        db.execSQL(Transactions.CreateTableContactos);

        ContentValues contentValues = new ContentValues();
        contentValues.put("nombre", "HONDURAS");
        contentValues.put("codigo", "504");
        db.insert(Transactions.TablaPaises, null, contentValues);
        contentValues.put("nombre", "EL SALVADOR");
        contentValues.put("codigo", "503");
        db.insert(Transactions.TablaPaises, null, contentValues);
        contentValues.put("nombre", "COSTA RICA");
        contentValues.put("codigo", "506");
        db.insert(Transactions.TablaPaises, null, contentValues);
        contentValues.put("nombre", "GUATEMALA");
        contentValues.put("codigo", "502");
        db.insert(Transactions.TablaPaises, null, contentValues);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(Transactions.DropTablePaises);
        db.execSQL(Transactions.DropTableContactos);
        onCreate(db);

    }
}

package com.allegra.handyuvisa.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by jsandoval on 17/08/16.
 */
public class UsuarioSQLiteHelper extends SQLiteOpenHelper {

    String sql = "CREATE TABLE Usuario (numid TEXT)";


    public UsuarioSQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXIST Usuario");
        db.execSQL(sql);
    }
}

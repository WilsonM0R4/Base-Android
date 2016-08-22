package com.allegra.handyuvisa.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.allegra.handyuvisa.models.UserDataBase;

/**
 * Created by jsandoval on 17/08/16.
 */
public class UsuarioSQLiteHelper extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "usuario_info.db";

    private static final String TABLE_USER = "user";

    // User Table Columns names
    private static final String KEY_NAME = "nombredb";
    private static final String KEY_SURANAME = "apellidodb";
    private static final String KEY_TYPEID = "typeiddb";
    private static final String KEY_NUMID = "iddb";
    private static final String KEY_NUMMCARD = "nummcarddb";
    private static final String KEY_VALUE1 = "value1";
    private static final String KEY_VALUE2 = "value2";
    private static final String KEY_VALUE3 = "value3";

    private static final String[] COLUMNS = {KEY_NAME,KEY_SURANAME,KEY_TYPEID, KEY_NUMID, KEY_NUMMCARD,
            KEY_VALUE1, KEY_VALUE2, KEY_VALUE3};

    public UsuarioSQLiteHelper(Context ctx){
        super(ctx,DB_NAME, null,DB_VERSION);
        Log.d("Usuario_info: ", "Database Created...!");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USER_TABLE = "CREATE TABLE user ( " + "nombredb TEXT, " + "apellidodb TEXT, " + " typeiddb TEXT, " +
                "iddb TEXT, " + "nummcarddb TEXT, " + "value1 TEXT, " + "value2 TEXT, " + "value3 TEXT )";

        db.execSQL(CREATE_USER_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS user");
        this.onCreate(db);

    }

    public void addUser(UserDataBase userDataBase){

        Log.d("addBook", userDataBase.toString());

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, userDataBase.getNombre());
        values.put(KEY_SURANAME, userDataBase.getApellido());
        values.put(KEY_TYPEID, userDataBase.getTipoid());
        values.put(KEY_NUMID, userDataBase.getId());
        values.put(KEY_NUMMCARD, userDataBase.getNummcard());
        values.put(KEY_VALUE1, userDataBase.getValue1());
        values.put(KEY_VALUE2, userDataBase.getValue2());
        values.put(KEY_VALUE3, userDataBase.getValue3());

        db.insert(TABLE_USER, null,values);

        db.close();
    }

}

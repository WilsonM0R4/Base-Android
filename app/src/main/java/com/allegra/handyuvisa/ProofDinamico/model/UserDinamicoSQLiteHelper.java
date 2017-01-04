package com.allegra.handyuvisa.ProofDinamico.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by jsandoval on 28/11/16.
 */

public class UserDinamicoSQLiteHelper extends SQLiteOpenHelper {


    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "usuario_dinamico.db";

    private static final String TABLE_USER = "user";

    // User Table Columns names
    private static final String KEY_NAME = "nombredb";
    private static final String KEY_SURANAME = "apellidodb";
    private static final String KEY_TYPEID = "typeiddb";
    private static final String KEY_NUMID = "iddb";
    private static final String KEY_NUMMCARD = "nummcarddb";
    private static final String KEY_COBERTURA = "coberturadb";

    public UserDinamicoSQLiteHelper(Context ctx){
        super(ctx,DB_NAME, null,DB_VERSION);
        Log.d("Usuario_info: ", "Database Created...!");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USER_TABLE = "CREATE TABLE user ( " + "nombredb TEXT, " + "apellidodb TEXT, " + " typeiddb TEXT, " +
                "iddb TEXT, " + "nummcarddb TEXT, " + "coberturadb TEXT )";

        db.execSQL(CREATE_USER_TABLE);

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

        db.execSQL("DROP TABLE IF EXISTS user");
        this.onCreate(db);
    }

    public void addUserDinamic(UserDataBaseDinamico userDataBase){

        Log.d("addBook", userDataBase.toString());

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, userDataBase.getNombre());
        values.put(KEY_SURANAME, userDataBase.getApellido());
        values.put(KEY_TYPEID, userDataBase.getTipoid());
        values.put(KEY_NUMID, userDataBase.getId());
        values.put(KEY_NUMMCARD, userDataBase.getNummcard());
        values.put(KEY_COBERTURA, userDataBase.getCobertura().toString());

        db.insert(TABLE_USER, null,values);

        db.close();
    }

    public Cursor getInformationDatabase(SQLiteDatabase db){
        Cursor cursor;
        String[] projections= {KEY_NAME, KEY_SURANAME, KEY_TYPEID, KEY_NUMID, KEY_NUMMCARD, KEY_COBERTURA};
        cursor = db.query(TABLE_USER, projections, null,null,null,null,null);
        return cursor;
    }
}

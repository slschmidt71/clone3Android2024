package com.example.androidlabs;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

public class MyDBHelper extends SQLiteOpenHelper {

    private static final String DBNAME = "mydb.db";
    private static final int VERSION = 1;

    private static final String TABLE_NAME = "todos";
    private static final String ID = "_id";
    private static final String TODO = "todo";
    private static final Boolean URGENT = false;

    SQLiteDatabase myDB;

    public MyDBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DBNAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String queryTable = "CREATE TABLE " + TABLE_NAME + " (" +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TODO + " TEXT NOT NULL, " +
                URGENT + " TEXT NOT NULL " +
                ")";

        db.execSQL(queryTable);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void openDB() {
        myDB = getWritableDatabase();
    }

    public void closeDB() {
        if(myDB != null && myDB.isOpen()) {
            myDB.close();
        }
    }

    public long insert(int id, String toDo, Boolean urgent) {
        ContentValues values = new ContentValues();
        if (id != -1)
            values.put(ID,id);
        values.put(TODO,toDo);
        values.put(String.valueOf(URGENT),urgent);

        return myDB.insert(TABLE_NAME,null,values);
    }

    public long update(int id, String toDo, Boolean urgent) {
        ContentValues values = new ContentValues();
        if (id != -1)
            values.put(ID,id);
        values.put(TODO,toDo);
        values.put(String.valueOf(URGENT),urgent);

        String where = ID + " = " + id;
        return myDB.update(TABLE_NAME,values,where, null);
    }


    public long delete(int id) {

        String where = ID + " = " + id;
        return myDB.delete(TABLE_NAME,where, null);
    }

    public Cursor getAllRecords() {

        String query = "SELECT * FROM " + TABLE_NAME;
        return myDB.rawQuery(query,null);
    }



}

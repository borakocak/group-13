package com.example.joysticktest;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class MyDatabaseHelper extends SQLiteOpenHelper {

    private Context context;
    private static final String DATABASE_NAME = "USER_DATABASE";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "USER_NAME";
    private static final String COLUMN_ID = "ID";
    private static final String COLUMN_USERNAME="USERNAME";
    private static final String COLUMN_PASSWORD = "PASSWORD";

    public MyDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME +
                " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_USERNAME + " TEXT, " +
                COLUMN_PASSWORD + " TEXT" + " )";

        db.execSQL(query);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean addOne(String userName, String passWord){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_USERNAME,userName);
        cv.put(COLUMN_PASSWORD,passWord);

        long insert = db.insert(TABLE_NAME, null, cv);
        if (insert == -1){
            return false;
        }else{
            return true;
        }

    }//This method add the registered information to the database

    public String Search(String userName){
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT " + COLUMN_PASSWORD + " FROM " + TABLE_NAME + " WHERE " + COLUMN_USERNAME + " = " + "\"" +  userName + "\"";

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()){
            String password = cursor.getString(cursor.getColumnIndex(COLUMN_PASSWORD));
            return password;
        }else{
            return null;
        }

    }//This method provide a query system to select the password from database


}

package com.example.mardichim20;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DataBaseHelperPhone extends SQLiteOpenHelper {
    public static final String TABLE = "CUSTOMER_TABLE";
    public static final String COLUMN_ID = "COLUMN_ID";
    public static final String COLUMN_NAME = "COLUMN_NAME";
    public static final String COLUMN_PHONE = "COLUMN_PHONE";
    public static final String COLUMN_NUM = "COLUMN_NUM";



    public DataBaseHelperPhone(@Nullable Context context) {
        super(context, "customer_phone.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement = "CREATE TABLE " + TABLE + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_NAME + " TEXT, " + COLUMN_NUM + " INT, " + COLUMN_PHONE + " INT)";
        db.execSQL(createTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public boolean addOne(Model_Name Model){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        //PUTTING THE VALUES IN THE TABLE OF THE DATA BASE
        cv.put(COLUMN_NAME,Model.getName());
        cv.put(COLUMN_PHONE,Model.getPhone());
        cv.put(COLUMN_NUM,Model.getNumber());


        long insert = db.insert(TABLE, null, cv);
        if(insert == -1){
            return false;
        }else{
            return true;
        }
    }

    public List<Model_Name> GETINFO() {

        List<Model_Name> returnList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE, null);
        if (cursor.moveToFirst()) {
            do {
                String id = cursor.getString(0);
                String name = cursor.getString(1);
                int number = cursor.getInt(2);
                int phone = cursor.getInt(3);

                // on below line we are adding the data from cursor to our array list.
                Model_Name model_name = new Model_Name(name,phone,number,Integer.parseInt(id));

                returnList.add(model_name);

            } while (cursor.moveToNext());
            // moving our cursor to next.
        }else{
            //FUCK MY LIFE
        }
        // at last closing our cursor
        // and returning our array list.
        cursor.close();
        db.close();
        return returnList;
    }
    public boolean deleteTitle(String id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE, COLUMN_ID + "=?", new String[]{id}) > 0;
    }


}

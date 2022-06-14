package com.example.mardichim20;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DataBaseHelperMil extends SQLiteOpenHelper {
    public static final String TABLE = "CUSTOMER_TABLE";
    public static final String COLUMN_ID = "COLUMN_ID";
    public static final String COLUMN_NAME = "COLUMN_NAME";
    public static final String COLUMN_BDATE = "COLUMN_BEGIN_DATE";
    public static final String COLUMN_EDATE = "COLUMN_END_DATE";
    public static final String COLUMN_WHO = "COLUMN_WHO";
    public static final String COLUMN_PHONE = "COLUMN_PHONE";
    public static final String COLUMN_PLACE = "COLUMN_PLACE";
    public static final String COLUMN_NUMBER = "PERSONAL_NUMBER";
    public static final String COLUMN_ARRIVALS = "COLUMN_ARRIVAL";



    public DataBaseHelperMil(@Nullable Context context) {
        super(context, "customer_mil.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String createTableStatement = "CREATE TABLE " + TABLE + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_NAME + " TEXT, " + COLUMN_BDATE + " TEXT," + COLUMN_WHO + " TEXT, " + COLUMN_EDATE + " TEXT, " + COLUMN_PLACE + " TEXT, " + COLUMN_PHONE + " INT,"+COLUMN_NUMBER+" TEXT ,"+COLUMN_ARRIVALS+" INT)";
        db.execSQL(createTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public boolean addOne(Model_mil Model){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        //PUTTING THE VALUES IN THE TABLE OF THE DATA BASE
        cv.put(COLUMN_NAME,Model.getName());
        cv.put(COLUMN_PHONE,Model.getPhoneNum());
        cv.put(COLUMN_BDATE,Model.getBeginDate());
        cv.put(COLUMN_EDATE,Model.getEndDate());
        cv.put(COLUMN_WHO,Model.getWho());
        cv.put(COLUMN_PLACE,Model.getPlace());
        cv.put(COLUMN_NUMBER,Model.getpNum());
        cv.put(COLUMN_ARRIVALS,Model.getArrivals());


        long insert = db.insert(TABLE, null, cv);
        if(insert == -1){
            return false;
        }else{
            return true;
        }
    }

    public List<Model_mil> GETINFO() {

        List<Model_mil> returnList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE, null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                String Bdate = cursor.getString(2);
                String Edate = cursor.getString(4);
                String who = cursor.getString(3);
                String place = cursor.getString(5);
                int phone = cursor.getInt(6);
                int Pnum = cursor.getInt(7);
                int arrivals = cursor.getInt(8);

                Model_mil model_mil = new Model_mil(name,Bdate,Edate,phone,who,place,id,Pnum,arrivals);

                returnList.add(model_mil);

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
    public boolean updateRow(Model_mil Model){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_NAME,Model.getName());
        cv.put(COLUMN_PHONE,Model.getPhoneNum());
        cv.put(COLUMN_BDATE,Model.getBeginDate());
        cv.put(COLUMN_EDATE,Model.getEndDate());
        cv.put(COLUMN_WHO,Model.getWho());
        cv.put(COLUMN_PLACE,Model.getPlace());
        cv.put(COLUMN_NUMBER,Model.getpNum());
        cv.put(COLUMN_ARRIVALS,Model.getArrivals());

        db.update(TABLE,cv,COLUMN_ID + "=?",new String[] {String.valueOf(Model.getIdMil())});
        return true;
    }




}

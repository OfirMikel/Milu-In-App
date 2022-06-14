package com.example.mardichim20;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.ebanx.swipebtn.OnStateChangeListener;
import com.ebanx.swipebtn.SwipeButton;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class Miluin extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Configuration configuration = getResources().getConfiguration();
        fontSizeAdjust(configuration);
        setContentView(R.layout.activity_miluin);
        getSupportActionBar().setTitle(" Milu-In");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#003300")));
        getWindow().setStatusBarColor(Color.parseColor("#003300"));
        SwipeButton swipeButton = findViewById(R.id.Swipe);
        swipeButton.setOnStateChangeListener(new OnStateChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onStateChange(boolean active) {
                RefreshDataBase();
                startActivity(new Intent(getApplicationContext(),Mil.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

    }
    public void fontSizeAdjust(Configuration configuration){
        configuration.fontScale = (float) 1.0;
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(metrics);
        metrics.scaledDensity = configuration.fontScale* metrics.density;
        getBaseContext().getResources().updateConfiguration(configuration,metrics);
    }
    @SuppressLint("ResourceType")
    public void RefreshDataBase(){
        DataBaseHelperMil dataBaseHelperMil = new DataBaseHelperMil(this);
        ArrayList<Model_mil> GETINFORM = (ArrayList<Model_mil>) dataBaseHelperMil.GETINFO();
        for (int i = 0; i < GETINFORM.toArray().length; i++) {
            Model_mil model_mil = GETINFORM.get(i);

            String bdate = model_mil.getBeginDate();

            if(model_mil.getBeginDate().equals(getCurrentDate()) && IsValid(model_mil.getPhoneNum())
                    && model_mil.getArrivals() < 3){

                String ValidArrival = GetSMS(String.valueOf(model_mil.getPhoneNum()),this);
                System.out.println(ValidArrival + " -- " + bdate + " ");
                if(ValidArrival.equals(bdate)){
                    model_mil.setArrivals(1);
                }else if(ValidArrival.equals("2")) {
                    model_mil.setArrivals(0);
                }else{
                    model_mil.setArrivals(2);
                }
                dataBaseHelperMil.updateRow(model_mil);

            }
        }
    }
    @SuppressLint("SimpleDateFormat")
    public String GetSMS(String phoneNumber1, Activity activity) {
        Uri mSmsinboxQueryUri = Uri.parse("content://sms/inbox");
        String[] projection = { "_id", "address", "person", "body", "date", "type"};
        String phoneNumber = "+972" +phoneNumber1;

        Cursor cursor1 = activity.getContentResolver().query(mSmsinboxQueryUri, projection, "address = ?", new String[] {phoneNumber}, "date DESC LIMIT 1");
        if (cursor1 != null && cursor1.moveToFirst()) {
            String body = cursor1.getString(cursor1.getColumnIndex("body"));
            int index_Date = cursor1.getColumnIndex("date");
            String strAddress = cursor1.getString(cursor1.getColumnIndex("address"));
            Date date = new Date(cursor1.getLong(index_Date));
            int intPerson = cursor1.getInt(cursor1.getColumnIndex("person"));
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            String dateF = sdf.format(date.getTime());

            System.out.println(" -------- Address: " + strAddress + "  ---- Date: " +dateF + "  ---- Content: " + body + " --- Person: "+intPerson+"  ---------"+ phoneNumber+ dateF);
            if(body.trim().equals("1")){
                cursor1.close();
                return dateF;
            }
            if (body.trim().equals("2")){
                cursor1.close();
                return "2";
            }

        }
        return "0";
    }
    private String getCurrentDate(){
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Calendar c = Calendar.getInstance();
        return sdf.format(c.getTime());
    }
    private boolean IsValid(int phoneNum) {
        return phoneNum > 50000000;
    }

}
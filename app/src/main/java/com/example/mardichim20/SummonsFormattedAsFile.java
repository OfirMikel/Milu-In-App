package com.example.mardichim20;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class SummonsFormattedAsFile extends AppCompatActivity {
    private ArrayList<Model_mil> participantsInfo = new ArrayList<>();
    TextView Place , PlaceOfOccupation;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summons_formated_as_file);
        Configuration configuration = getResources().getConfiguration();
        fontSizeAdjust(configuration);
        getSupportActionBar().setTitle(" Milu-In");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#003300")));
        getWindow().setStatusBarColor(Color.parseColor("#003300"));

        configuration.setLayoutDirection(new Locale("en"));
        getResources().updateConfiguration(configuration, getResources().getDisplayMetrics());
        //getting all the information from the past activity
        Bundle bundle = getIntent().getExtras();
        participantsInfo = bundle.getParcelableArrayList("info");
        int arrayLength = participantsInfo.size() - 1;
        //initializing the variables of the xml file
        Place = findViewById(R.id.place);
        PlaceOfOccupation = findViewById(R.id.placeOfOccupation);
        Place.setText(participantsInfo.get(arrayLength).getPlace());
        PlaceOfOccupation.setText(participantsInfo.get(arrayLength).getPlace());

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat myFormat = new SimpleDateFormat("dd/MM/yyyy");
        //creating the table
        TableLayout tableLayout = findViewById(R.id.tableLayout);
        System.out.println(arrayLength);
        for (int i = 0 ; i < arrayLength+1 ; i++){
            TableRow tableRow = new TableRow(this);
            tableRow.setLayoutParams(new TableLayout.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            tableRow.setGravity(Gravity.CENTER);

            //חישוב הימ"מ
            Date dateB = null;
            Date dateE = null;
            try {
                 dateB =  myFormat.parse(participantsInfo.get(i).getBeginDate());
                 dateE = myFormat.parse(participantsInfo.get(i).getEndDate());

            } catch (ParseException e) {
                e.printStackTrace();
            }
            assert dateE != null;
            assert dateB != null;
            long diff = dateE.getTime() -dateB.getTime();
            diff = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)+1;

            //הגדרת התצוגה
            TextView serialNum = new TextView(this);
            TextView id = new TextView(this);
            TextView fullName = new TextView(this);
            TextView role = new TextView(this);
            TextView beginDate = new TextView(this);
            TextView endDate = new TextView(this);
            TextView amountOfDays = new TextView(this);

            //קביעת הטקסט
            serialNum.setText(String.valueOf(i+1));
            id.setText(String.valueOf(participantsInfo.get(i).getpNum()));
            fullName.setText(participantsInfo.get(i).getName());
            role.setText("        ");
            beginDate.setText(participantsInfo.get(i).getBeginDate());
            endDate.setText(participantsInfo.get(i).getEndDate());
            amountOfDays.setText(String.valueOf(diff));

            //מיקום הטקסט באמצע
            serialNum.setGravity(Gravity.CENTER);
            id.setGravity(Gravity.CENTER);
            fullName.setGravity(Gravity.CENTER);
            role.setGravity(Gravity.CENTER);
            beginDate.setGravity(Gravity.CENTER);
            endDate.setGravity(Gravity.CENTER);
            amountOfDays.setGravity(Gravity.CENTER);

            //שינוי צבע הטקסט לשחור
            serialNum.setTextColor(Color.BLACK);
            id.setTextColor(Color.BLACK);
            fullName.setTextColor(Color.BLACK);
            role.setTextColor(Color.BLACK);
            beginDate.setTextColor(Color.BLACK);
            endDate.setTextColor(Color.BLACK);
            amountOfDays.setTextColor(Color.BLACK);

            //שינוי גודל הטקסט ל10 פיקסלים דחוסים
            serialNum.setTextSize(10);
            id.setTextSize(10);
            fullName.setTextSize(10);
            role.setTextSize(10);
            beginDate.setTextSize(10);
            endDate.setTextSize(10);
            amountOfDays.setTextSize(10);

            //קביעת "ריפוד" לטקסט
            serialNum.setPadding(5,3,5,3);
            id.setPadding(5,3,5,3);
            fullName.setPadding(5,3,5,3);
            role.setPadding(5,3,5,3);
            beginDate.setPadding(5,3,5,3);
            endDate.setPadding(5,3,5,3);
            amountOfDays.setPadding(5,3,5,3);

            //הוספת קווי מתאר כקובץ רקע
            serialNum.setBackground(ContextCompat.getDrawable(this, R.drawable.border4));
            id.setBackground(ContextCompat.getDrawable(this, R.drawable.border4));
            fullName.setBackground(ContextCompat.getDrawable(this, R.drawable.border4));
            role.setBackground(ContextCompat.getDrawable(this, R.drawable.border4));
            beginDate.setBackground(ContextCompat.getDrawable(this, R.drawable.border4));
            endDate.setBackground(ContextCompat.getDrawable(this, R.drawable.border4));
            endDate.setBackground(ContextCompat.getDrawable(this, R.drawable.border4));
            amountOfDays.setBackground(ContextCompat.getDrawable(this, R.drawable.border4));

            //הגדרת layout של הVIEWS
            serialNum.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            id.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            fullName.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            role.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            beginDate.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            endDate.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            amountOfDays.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

            tableRow.addView(serialNum);
            tableRow.addView(id);
            tableRow.addView(fullName);
            tableRow.addView(role);
            tableRow.addView(beginDate);
            tableRow.addView(endDate);
            tableRow.addView(amountOfDays);

            tableLayout.addView(tableRow, new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
        }



    }

    public void fontSizeAdjust(Configuration configuration){
        configuration.fontScale = (float) 1.0;
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(metrics);
        metrics.scaledDensity = configuration.fontScale* metrics.density;
        getBaseContext().getResources().updateConfiguration(configuration,metrics);
    }
}
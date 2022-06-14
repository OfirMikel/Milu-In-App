package com.example.mardichim20;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class DialogV {



    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    public void showDialog(Activity activity, String msg, Model_mil model_mil){

        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_info);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(true);

        Configuration configuration = activity.getResources().getConfiguration();
        configuration.setLayoutDirection(new Locale("en"));
        activity.getResources().updateConfiguration(configuration, activity.getResources().getDisplayMetrics());


        //initialise the texts
        TextView FullName = dialog.findViewById(R.id.FullName);
        TextView id = dialog.findViewById(R.id.ID);
        TextView Dates = dialog.findViewById(R.id.dateB);
        TextView call = dialog.findViewById(R.id.call);
        TextView IMM = dialog.findViewById(R.id.placeOf4);
        TextView Whatsapp = dialog.findViewById(R.id.Whatsapp);
        TextView Arrived = dialog.findViewById(R.id.arrived);
        TextView didntArrived = dialog.findViewById(R.id.didnt_arrived);
        TextView Comments = dialog.findViewById(R.id.comments);

        String BeDate = model_mil.getBeginDate();
        String EnDate = model_mil.getEndDate();
        String AllDate = EnDate + " - " + BeDate;

        //Resting the Background of the Arrivals section
        Arrived.setBackground(ContextCompat.getDrawable(dialog.getContext(),R.drawable.round_corner_arrived));
        didntArrived.setBackground(ContextCompat.getDrawable(dialog.getContext(),R.drawable.round_corner_arrived));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy", Locale.ENGLISH);

        String CurrentDate = getCurrentDate();
        LocalDate Cdate = LocalDate.parse(CurrentDate, formatter);
        LocalDate Bdate = LocalDate.parse(BeDate, formatter);

        if(Bdate.isBefore(Cdate) || Bdate.isEqual(Cdate)){
            switch(model_mil.getArrivals()){
                case 0:
                    Comments.setText("☒ החייל השיב שלא התייצב");
                    didntArrived.setBackground(ContextCompat.getDrawable(dialog.getContext(),R.drawable.round_corner_arrived_marked));
                    break;
                case 1:
                    Comments.setText("☑ החייל השיב שהתייצב");
                    Arrived.setBackgroundDrawable(ContextCompat.getDrawable(dialog.getContext(), R.drawable.round_corner_arrived_marked));
                    break;
                case 2:
                    Comments.setText("☐ החייל לא השיב להודעה");
                    didntArrived.setBackground(ContextCompat.getDrawable(dialog.getContext(),R.drawable.round_corner_arrived_marked));
                    break;
                case 3:
                    Comments.setText("☒ הצבת שהחייל לא התייצב");
                    didntArrived.setBackground(ContextCompat.getDrawable(dialog.getContext(),R.drawable.round_corner_arrived_marked));
                    break;
                case 4:
                    Arrived.setBackgroundDrawable(ContextCompat.getDrawable(dialog.getContext(), R.drawable.round_corner_arrived_marked));
                    Comments.setText("☑ הצבת שהחייל התייצב");
                    break;
            }

            Arrived.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog alertDialog = new AlertDialog.Builder(activity)
                            .setTitle("הזנת התייבות")
                            .setMessage("אתה בטוח שתרצה להזין שהחייל התייצב?")
                            .setPositiveButton("שמור", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    DataBaseHelperMil dataBaseHelperMil = new DataBaseHelperMil(activity);
                                    model_mil.setArrivals(4);
                                    dataBaseHelperMil.updateRow(model_mil);
                                    Arrived.setBackground(ContextCompat.getDrawable(activity,R.drawable.round_corner_arrived_marked));
                                    didntArrived.setBackground(ContextCompat.getDrawable(activity,R.drawable.round_corner_arrived));
                                    Comments.setText("☑ הצבת שהחייל התייצב");
                                }
                            })
                            .setNegativeButton("בטל", null)
                            .show();

                }
            });

            didntArrived.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog alertDialog = new AlertDialog.Builder(activity)
                            .setTitle("הזנת התייבות")
                            .setMessage("אתה בטוח שתרצה להזין שהחייל לא התייצב?")
                            .setPositiveButton("שמור", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    DataBaseHelperMil dataBaseHelperMil = new DataBaseHelperMil(activity);
                                    model_mil.setArrivals(3);
                                    dataBaseHelperMil.updateRow(model_mil);
                                    Arrived.setBackground(ContextCompat.getDrawable(activity,R.drawable.round_corner_arrived));
                                    didntArrived.setBackground(ContextCompat.getDrawable(activity,R.drawable.round_corner_arrived_marked));
                                    Comments.setText("☒ הצבת שהחייל לא התייצב");

                                }
                            })
                            .setNegativeButton("בטל", null)
                            .show();

                }
            });
        }
        else{
            Comments.setVisibility(View.INVISIBLE);
            Arrived.setVisibility(View.INVISIBLE);
            didntArrived.setText("לא ניתן לדווח התייצבות עדיין");

        }


        SimpleDateFormat myFormat = new SimpleDateFormat("dd/MM/yyyy");
        java.util.Date date1 = null;
        java.util.Date date2 = null;
        long diff = 0;
        try {
            date1 =  myFormat.parse(BeDate);
            date2 = myFormat.parse(EnDate);
            diff = date2.getTime() - date1.getTime();

        } catch (ParseException e) {
            e.printStackTrace();
        }

        TextView WHO = dialog.findViewById(R.id.Whoin);
        TextView Place = dialog.findViewById(R.id.placeOf);
        TextView PhoneNum = dialog.findViewById(R.id.placeOf2);
        TextView Pnum = dialog.findViewById(R.id.placeOf3);


        String Phone = String.valueOf(model_mil.getPhoneNum());

        //setting the model
        if(model_mil.getName().length()>= 15){
            FullName.setTextSize(40);
        }else{
            FullName.setTextSize(50);
        }
        FullName.setText(model_mil.getName());
        if(model_mil.getpNum() == 0 ){
            Pnum.setTextColor(Color.parseColor("#E23737"));
            Pnum.setTextSize(17);
            Pnum.setText("אין מספר אישי במאגר המידע");
        }else{
            Pnum.setText(String.valueOf(model_mil.getpNum()));
        }
        id.setText(String.valueOf(model_mil.getIdMil()));
        Dates.setText(AllDate);
        WHO.setText(model_mil.getWho());
        Place.setText(model_mil.getPlace());
        IMM.setText(String.valueOf(TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)+1));
        System.out.println(Phone.length());
        if(Phone.length() == 9){
            PhoneNum.setText("0" + Phone.substring(0,2) +"-" + Phone.substring(2,5)+"-" + Phone.substring(5));
        }else if(Phone.length() == 1){
            PhoneNum.setText("No Phone Number");
            PhoneNum.setAllCaps(true);
            PhoneNum.setTextColor(Color.parseColor("#E23737"));
        }else{
            PhoneNum.setText("Incorrect Phone Num");
            PhoneNum.setAllCaps(true);
            PhoneNum.setTextColor(Color.parseColor("#E23737"));
        }

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Animation myAnim = AnimationUtils.loadAnimation(activity, R.anim.button_click);
                call.startAnimation(myAnim);

                String caller = "972"+Phone;
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" +caller ));// Initiates the Intent
                activity.startActivity(intent);
            }
        });

        Whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Animation myAnim = AnimationUtils.loadAnimation(activity, R.anim.button_click);
                Whatsapp.startAnimation(myAnim);

                Intent SendW = new Intent(Intent.ACTION_VIEW,Uri.parse("https://api.whatsapp.com/send?phone=9720"+model_mil.getPhoneNum()));
                SendW.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activity.startActivity(SendW);
            }

        });


        dialog.show();

    }
    private String getCurrentDate(){
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Calendar c = Calendar.getInstance();
        return sdf.format(c.getTime());
    }




}

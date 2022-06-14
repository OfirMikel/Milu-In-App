package com.example.mardichim20;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class DialogEvent {


    TextView Dates,Location,Amount,Name,AmountOfParticipants , click1,click2 ;
    Button clickFormat;
    private long counter = 0;
    private long counterArrived = 0;

    private String NamesOfParticipants = "";
    private String Participants_amount = "";
    String location = "0";
    String BeginDate ="0"; // the Variables to save to the text View
    String EndDate ="0";// the Variables to save to the text View

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("SimpleDateFormat")

    public void showDialog(Activity activity, ArrayList<Model_mil> ListModel, int position, String BeginDate , String EndDate, ArrayList<Model_mil> ParticipantsInfo){
       this.BeginDate = BeginDate;
       this.EndDate =EndDate;

        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.header_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(true);

        Configuration configuration = activity.getResources().getConfiguration();
        configuration.setLayoutDirection(new Locale("en"));
        activity.getResources().updateConfiguration(configuration, activity.getResources().getDisplayMetrics());


        SimpleDateFormat myFormat = new SimpleDateFormat("dd/MM/yyyy");
        java.util.Date date1 = null;
        java.util.Date date2 = null;
        long diff;
        int amountOfParticipants = 0;
        int amountOfParticipantsArrived = 0;

        String dates =  BeginDate+ "-" + EndDate;
        Collections.sort(ParticipantsInfo,SortByCommander);



        for (int i =0 ; i<ParticipantsInfo.size() ; i++) {
            Model_mil model_mil = ParticipantsInfo.get(i);
            String BDate = model_mil.getBeginDate();
            String EDate = model_mil.getEndDate();
            location = model_mil.getPlace();
            try {
                date1 =  myFormat.parse(BDate);
                date2 = myFormat.parse(EDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            assert date2 != null;
            assert date1 != null;
            diff = date2.getTime() - date1.getTime();
            diff = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)+1;
            String BtoEdate = model_mil.getBeginDate()+ " - " +model_mil.getEndDate();
            counter = diff + counter;

            amountOfParticipants++;
            if(i>1){
                if(!ParticipantsInfo.get(i).getWho().equals( ParticipantsInfo.get(i-1).getWho()) ){
                    NamesOfParticipants = NamesOfParticipants + "\n" + "מזומנים של " +model_mil.getWho()+ ": \n" ;
                }
            }else{
                if(i == 0){
                    NamesOfParticipants = NamesOfParticipants + "\n" + "מזומנים של " +model_mil.getWho()+ ": \n" ;
                }
            }

            if (ParticipantsInfo.get(i).getArrivals() == 1 || ParticipantsInfo.get(i).getArrivals() == 4) {
                counterArrived = diff + counterArrived;
                amountOfParticipantsArrived++;
                NamesOfParticipants =  NamesOfParticipants +"\n" +amountOfParticipants+ ". " +model_mil.getName() + " | " + model_mil.getpNum() + "\n"+  BtoEdate  +"\n";
                Participants_amount = Participants_amount +"\n" +amountOfParticipants+ ". " +model_mil.getName() +  "  |  " + diff  ;
            }else{
                NamesOfParticipants =  NamesOfParticipants +"\n" +amountOfParticipants+ ". " +model_mil.getName() + " | " + " (לא התייצב/ה)"+"\n";
                Participants_amount = Participants_amount +"\n" +amountOfParticipants+ ". " +model_mil.getName() +  "  |  " + diff + " (לא התייצב/ה)";
            }
        }
        Participants_amount = Participants_amount +"\n\n\n" +"סה\"כ " + counterArrived + " ימ\"מ של המתייצבים" ;


        Dates=dialog.findViewById(R.id.dates);
        Location =dialog.findViewById(R.id.placeOf1);
        Amount = dialog.findViewById(R.id.amount);
        Name = dialog.findViewById(R.id.EventName);
        AmountOfParticipants =dialog.findViewById(R.id.amount_of);
        click1 = dialog.findViewById(R.id.amount_of1);
        click2 = dialog.findViewById(R.id.amount_of2);
        clickFormat = dialog.findViewById(R.id.format);

        String participantsShowMaxAndShow =  amountOfParticipantsArrived+"/" + amountOfParticipants;
        AmountOfParticipants.setText(participantsShowMaxAndShow);
        Name.setText(ListModel.get(position).getName());
        Amount.setText(counterArrived+ "/" + counter);
        Dates.setText(dates);
        Location.setText(location);
        dialog.show();

        click1.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RtlHardcoded")
            @Override
            public void onClick(View v) {

                DialogNames dialogNames = new DialogNames();
                dialogNames.showDialog(activity,NamesOfParticipants);
            }
        });

        click2.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RtlHardcoded")
            @Override
            public void onClick(View v) {
                DialogNames dialogNames = new DialogNames();
                dialogNames.showDialog(activity,Participants_amount);
            }
        });


        clickFormat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, SummonsFormattedAsFile.class);
                intent.putExtra("info",ParticipantsInfo);
                activity.startActivity(intent);
            }
        });




    }



    public static Comparator<Model_mil> SortByCommander =new Comparator<Model_mil>() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public int compare(Model_mil o1, Model_mil o2 ) {
            String String1 = o1.getWho();
            String String2 = o2.getWho();
            return String1.compareTo(String2);

        }
    };

    @RequiresApi(api = Build.VERSION_CODES.O)
    private boolean inRange(LocalDate min, LocalDate max, LocalDate check){
        boolean isAfter = check.isAfter(min);
        boolean isBefore = check.isBefore(max);
        boolean isEq = check.isEqual(min);
        boolean isEq1 = check.isEqual(max);
        if(isAfter && isBefore){
            return true;
        }else if(isEq){
            return true;
        }else if(isEq1){
            return true;
        }else{
            return false;
        }
    }









}

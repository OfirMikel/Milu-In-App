package com.example.mardichim20;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class DialogSMS{



    @SuppressLint("ResourceType")
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void showDialog(Activity activity , ArrayList<Model_mil> model_mils){
        String CurrentDate = getCurrentDate();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.ENGLISH);
        final Dialog dialog = new Dialog(activity);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);

        dialog.setContentView(R.layout.dialog_sms);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(true);


        Configuration configuration = activity.getResources().getConfiguration();
        configuration.setLayoutDirection(new Locale("en"));
        activity.getResources().updateConfiguration(configuration, activity.getResources().getDisplayMetrics());


         dialog.show();

         dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
         dialog.getWindow().setGravity(Gravity.BOTTOM);


         Button sms = (Button) dialog.findViewById(R.id.button_sms);
         Button info = (Button) dialog.findViewById(R.id.button_info);

         sms.setOnClickListener(v -> {
             final Animation myAnim = AnimationUtils.loadAnimation(activity, R.anim.button_click);
             sms.startAnimation(myAnim);
             ArrayList<String> names = new ArrayList<>();
             ArrayList<String> PhoneNum = new ArrayList<>();
             for (Model_mil model_mil : model_mils) {
                 String bdate = model_mil.getBeginDate();
                 LocalDate Cdate = LocalDate.parse(CurrentDate, formatter);
                 LocalDate Bdate = LocalDate.parse(bdate, formatter);
                 if (Bdate.isEqual(Cdate) && !model_mil.getPlace().equals("1")&& !model_mil.getPlace().equals("0") && IsValid(model_mil.getPhoneNum())) {
                     String addName = model_mil.getName();
                     String phone = "+9720" + model_mil.getPhoneNum();
                     PhoneNum.add(phone);
                     names.add(addName);
                 }
             }

             if (names.toArray().length == 0) {
                 Toast.makeText(activity, "אף חייל מיל לא מתחיל היום את השמפ", Toast.LENGTH_SHORT).show();
             }else {
                 if (ContextCompat.checkSelfPermission(activity, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
                     sendSMS(PhoneNum,activity);
                 }else{
                     ActivityCompat.requestPermissions((Activity) activity,new String[]{Manifest.permission.SEND_SMS},100);
                 }
                 Toast.makeText(activity, ""+names, Toast.LENGTH_SHORT).show();
             }
         });

         info.setOnClickListener(v -> {
             final Animation myAnim = AnimationUtils.loadAnimation(activity, R.anim.button_click);
             info.startAnimation(myAnim);

             ArrayList<String> names = new ArrayList<>();

             ArrayList<String> Arrived = new ArrayList<>();
             ArrayList<String> haventArrived = new ArrayList<>();
             for (Model_mil model_mil : model_mils) {

                 String bdate = model_mil.getBeginDate();
                 String edate = model_mil.getEndDate();

                 LocalDate Cdate = LocalDate.parse(CurrentDate, formatter);
                 LocalDate Bdate = LocalDate.parse(bdate, formatter);
                 LocalDate Edate = LocalDate.parse(edate, formatter);

                 if (inRange(Bdate, Edate, Cdate) && !model_mil.getWho().equals("event")) {
                     System.out.println(model_mil.getArrivals() + " - " +model_mil.getName());
                     if(model_mil.getArrivals() == 1 || model_mil.getArrivals() == 4){
                         Arrived.add("\n"+model_mil.getName() +" -- "+ edate);
                     }else {
                         haventArrived.add("\n"+model_mil.getName() +" -- "+ edate);
                     }
                     names.add(model_mil.getName());
                 }
             }
             if (names.toArray().length == 0) {
                 Toast.makeText(activity, "אין חיילי מיל בשמפ היחדתי להיום", Toast.LENGTH_SHORT).show();
             } else {
                 String arrivedF = Arrived.toString()
                         .replace("[", "")
                         .replace("]", "")
                         .replace(",","");//הופך לסטרינג את המידע

                 String NarrivedF =haventArrived.toString()
                         .replace("[", "")
                         .replace("]", "")
                         .replace(",","");//הופך לסטרינג את המידע
                 if(haventArrived.size()==0){
                     ClipboardManager clipboardManager = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
                     ClipData clipData = ClipData.newPlainText("Arrived", " התייצבו: " + arrivedF);
                     clipboardManager.setPrimaryClip(clipData);

                     String text = " התייצבו: " + arrivedF ;
                     DialogNames dialogNames = new DialogNames();
                     dialogNames.showDialog(activity,text);

                 }else{
                     ClipboardManager clipboardManager = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
                     ClipData clipData = ClipData.newPlainText("Arrived", " התייצבו: " + arrivedF+ "\n" + "לא התייצבו: " + NarrivedF);
                     clipboardManager.setPrimaryClip(clipData);

                     String text = " התייצבו: " + arrivedF+ "\n" + "לא התייצבו: " + NarrivedF;

                     DialogNames dialogNames = new DialogNames();
                     dialogNames.showDialog(activity,text);


                 }
             }

         });


    }



    private boolean IsValid(int phoneNum) {
        return phoneNum > 50000000;
    }
    private String getCurrentDate(){
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Calendar c = Calendar.getInstance();
        return sdf.format(c.getTime());
    }
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
        }else return isEq1;
    }
    private void sendSMS(ArrayList<String> phoneNum, Activity activity){

        String sMassage =  "חייל מילואים יקר, זומנת למילואים ליום הנוכחי." + "\n\n"
                +"במידה והתייצבת השב 1 \nבמידה ולא התייצבת השב 2" +"\n"+
                "\n"+ "שים לב!"
                +"\n\n" + "במידה ולא תתקבל תשובה עד השעה 12 יוזן שלא התייצבת.";



        String formattedPhoneNum = phoneNum.toString()
                .replace("[", "")  //remove the right bracket
                .replace("]", "")  //remove the left bracket
                .trim();           //remove trailing spaces from partially initialized arrays

        Intent smsIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:"+formattedPhoneNum));
        smsIntent.putExtra("sms_body", sMassage);
        smsIntent.putExtra("exit_on_sent", true);
        activity.startActivity(smsIntent);
    }













}

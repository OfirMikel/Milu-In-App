package com.example.mardichim20;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import java.util.Locale;

public class DialogNames {

    private String NamesOfParticipants = "";

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("SimpleDateFormat")

    public void showDialog(Activity activity , String namesOfParticipants){

        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.participants);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(true);
        NamesOfParticipants = namesOfParticipants;
        Configuration configuration = activity.getResources().getConfiguration();
        configuration.setLayoutDirection(new Locale("en"));
        activity.getResources().updateConfiguration(configuration, activity.getResources().getDisplayMetrics());


        TextView textView = dialog.findViewById(R.id.par);
        textView.setText(NamesOfParticipants.substring(1));
        textView.setTextSize(14);
        dialog.show();






    }

}

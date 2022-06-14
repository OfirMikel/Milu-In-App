package com.example.mardichim20;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDateTime;

public class Notification_receiver extends BroadcastReceiver {
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {
        LocalDateTime now = LocalDateTime.now();
        if(now.getHour() == 6){
            NotificationHelper notificationHelper = new NotificationHelper(context);
            notificationHelper.createNotification();
        }

    }
}

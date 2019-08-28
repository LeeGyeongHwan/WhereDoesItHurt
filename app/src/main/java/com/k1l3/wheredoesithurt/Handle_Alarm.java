package com.k1l3.wheredoesithurt;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.ArrayList;

public class Handle_Alarm extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        ArrayList<String> alarmTime = intent.getStringArrayListExtra("alarmTime");
        int index = intent.getIntExtra("startIndex",0);
        int hour = Integer.parseInt(alarmTime.get(index).substring(0,2));
        int min = Integer.parseInt(alarmTime.get(index).substring(2,4));
        String ampm = "AM";
        if(hour>12){
            hour=hour-12;
            ampm = "PM";
        }
        else if(hour==0)
            hour = 12;
        String time= hour+":"+min+" "+ampm;

        Log.d("what", "onReceive: " + intent.toString());
        String title = intent.getStringExtra("title");
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder;
        Intent main = new Intent(context, MainActivity.class);
        PendingIntent mainPending = PendingIntent.getActivity(context, 0, main, PendingIntent.FLAG_UPDATE_CURRENT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel("makenoti", "makenotificaition", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(mChannel);
            builder = new NotificationCompat.Builder(context, mChannel.getId());
        } else {
            builder = new NotificationCompat.Builder(context);
        }

        Intent snoozeIntent = new Intent(context, example.class);
        snoozeIntent.setAction("nananan");
        snoozeIntent.putExtra("EXTRA_NOTIFICATION_ID", 0);
        PendingIntent snoozePendingIntent =
                PendingIntent.getActivity(context, 0, snoozeIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.noti_logo));
        builder.setSmallIcon(R.drawable.small_icon);
        builder.setTicker("알람");
        builder.setContentTitle(time);
        builder.setContentText(title + " 복용시간입니다.");
        builder.setWhen(System.currentTimeMillis());
        builder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
        builder.setContentIntent(mainPending);
        builder.addAction(R.drawable.btn_o, "복용", snoozePendingIntent);
        builder.addAction(R.drawable.btn_x, "미복용", snoozePendingIntent);
        builder.setAutoCancel(true);
        builder.setNumber(999);

        notificationManager.notify(0, builder.build());
    }
}

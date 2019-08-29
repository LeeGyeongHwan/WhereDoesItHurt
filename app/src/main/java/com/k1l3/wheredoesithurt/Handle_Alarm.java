package com.k1l3.wheredoesithurt;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
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
        Log.d("what", "onReceive extras: "+intent.getExtras().toString());
        boolean isInexactAlarm = intent.getBooleanExtra("inExactNotification",false);
        boolean smokeOrDrink = intent.getBooleanExtra("smokeOrDrink",false);

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel("makenoti", "makenotificaition", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(mChannel);
            builder = new NotificationCompat.Builder(context, mChannel.getId());
        } else {
            builder = new NotificationCompat.Builder(context);
        }

        //알람 종류에 따라
        Log.d("what", "onReceive: inexactAlarm : "+isInexactAlarm);
        if(isInexactAlarm){ //notify 아이콘과 제목 바꾸기 시간은 현재시간?
            Log.d("what", "onReceive: if문 들어옴");
            String title = intent.getStringExtra("title");


            Intent main = new Intent(context, MainActivity.class);
            PendingIntent mainPending =
                    PendingIntent.getActivity(context, 0, main, PendingIntent.FLAG_UPDATE_CURRENT);

            if(smokeOrDrink){
                builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.no_smoking));
                builder.setContentTitle("흡연을 자제하세요");
            }else{
                builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.no_alcohole));
                builder.setContentTitle("음주를 자제하세요");
            }
            //흠연, 음주에 따른 로고변경

            builder.setSmallIcon(R.drawable.small_icon);
            builder.setTicker("알람");
            builder.setContentText(title + " 복용중입니다.");
            builder.setWhen(System.currentTimeMillis());
            builder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
            builder.setContentIntent(mainPending);
            builder.setAutoCancel(false);
            builder.setNumber(999);

        }else{
            ArrayList<String> alarmTime = intent.getStringArrayListExtra("alarmTime");

            int index = intent.getIntExtra("startIndex",0);
            int hour = Integer.parseInt(alarmTime.get(index).substring(0,2));
            int min = Integer.parseInt(alarmTime.get(index).substring(2,4));
            String ampm = "AM";

            if(hour>12){
                hour=hour-12;
                ampm = "PM";
            } else if(hour==0) {
                hour = 12;
            } else if(hour==1){
                ampm="PM";
            }
            String shour=Integer.toString(hour);
            String smin=Integer.toString(min);
            if(hour<10)
                shour="0"+hour;
            if(min<10)
                smin="0"+min;

            String time= shour+":"+smin+" "+ampm;

            Log.d("what", "onReceive: " + intent.toString());

            String title = intent.getStringExtra("title");


            int numMed = intent.getIntExtra("codeIndex",0);

            Intent main = new Intent(context, MainActivity.class);

            main.putExtra("Fragment",index);
            main.putExtra("FragmentMed",numMed-1);

            main.setAction(Intent.ACTION_MAIN);
            main.addCategory(Intent.CATEGORY_LAUNCHER);

            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);

            stackBuilder.addParentStack(MainActivity.class);
            stackBuilder.addNextIntent(main);

            PendingIntent mainPending = stackBuilder.getPendingIntent(0,  PendingIntent.FLAG_UPDATE_CURRENT);

            Intent snoozeIntent = new Intent(context, MainActivity.class);
            snoozeIntent.setAction("nananan");

            snoozeIntent.putExtra("EXTRA_NOTIFICATION_ID", 0);
            PendingIntent snoozePending =
                    PendingIntent.getActivity(context, 0, snoozeIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.noti_logo));
            builder.setSmallIcon(R.drawable.small_icon);
            builder.setTicker("알람");
            builder.setContentTitle(time);
            builder.setContentText(title + " 복용시간입니다.");
            builder.setWhen(System.currentTimeMillis());
            builder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
            builder.setContentIntent(snoozePending);
            builder.addAction(R.drawable.btn_o, "복용", mainPending);
            builder.addAction(R.drawable.btn_x, "미복용", snoozePending);
            builder.setAutoCancel(true);
            builder.setNumber(999);
        }
        notificationManager.notify(0, builder.build());
    }
}

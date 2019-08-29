package com.k1l3.wheredoesithurt;


import android.app.AlarmManager;
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

import com.k1l3.wheredoesithurt.models.User;

import java.util.ArrayList;
import java.util.GregorianCalendar;

import static android.content.Context.ALARM_SERVICE;


public class Handle_Alarm extends BroadcastReceiver {
    Context context;
    private static final User user = User.getInstance();

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context=context;
        Log.d("what", "onReceive extras: "+intent.getExtras().toString());
        boolean isInexactAlarm = intent.getBooleanExtra("inExactNotification",false);
        boolean smokeOrDrink = intent.getBooleanExtra("smokeOrDrink",false);
        int requestCode = intent.getIntExtra("requestCode",0);
        int codeIndex = intent.getIntExtra("codeIndex",0);


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
        String title = intent.getStringExtra("title");

        //알람 종류에 따라
        Log.d("what", "onReceive: inexactAlarm : "+isInexactAlarm);
        if(isInexactAlarm){ //notify 아이콘과 제목 바꾸기 시간은 현재시간?


            Intent main = new Intent(context, MainActivity.class);
            main.putExtra("requestCode",requestCode);
            PendingIntent mainPending =
                    PendingIntent.getActivity(context,requestCode , main, PendingIntent.FLAG_UPDATE_CURRENT);

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

            Log.d("what", "onReceive: Time : " + time);



            int numMed = intent.getIntExtra("codeIndex",0);

            Intent main = new Intent(context, MainActivity.class);

            main.putExtra("requestCode",requestCode);
            main.putExtra("Fragment",index);
            main.putExtra("FragmentMed",numMed-1);

            main.setAction(Intent.ACTION_MAIN);
            main.addCategory(Intent.CATEGORY_LAUNCHER);

            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);

            stackBuilder.addParentStack(MainActivity.class);
            stackBuilder.addNextIntent(main);

            PendingIntent mainPending = stackBuilder.getPendingIntent(requestCode,  PendingIntent.FLAG_UPDATE_CURRENT);

            Intent snoozeIntent = new Intent(context, cancelNotification.class);
            snoozeIntent.setAction("nananan");
            snoozeIntent.putExtra("requestCode",requestCode);
            snoozeIntent.putExtra("EXTRA_NOTIFICATION_ID", 0);
            PendingIntent snoozePending =
                    PendingIntent.getBroadcast(context, 0, snoozeIntent, PendingIntent.FLAG_UPDATE_CURRENT);

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
            MakeAlarmService(title,requestCode+1,codeIndex);
        }

        notificationManager.notify(requestCode, builder.build());


    }
    public void MakeAlarmService(String title,int requestCodeinHere,int codeIndex) {
        Intent intent = new Intent(context, Handle_Alarm.class);
        intent.putExtra("title", title);

        intent.putExtra("requestCode",requestCodeinHere);
        Log.d("what", "handle make : + request : "+ requestCodeinHere);
        ArrayList<String> preTime = user. getPrescriptions().get(codeIndex-1).getTimes().getTimes();
        Log.d("what", "handle make: pretime get(i)"+ preTime.get(0));


        //시간 구해서 현재시간과 비교 가장 가까운 미래시간 부터 시간차
        GregorianCalendar currentCalendar = (GregorianCalendar) GregorianCalendar.getInstance();
        int currentHourOfDay = currentCalendar.get(GregorianCalendar.HOUR_OF_DAY);
        int currentMinute = currentCalendar.get(GregorianCalendar.MINUTE);
        Log.d("what", "handle make: gregorian hour : " + currentHourOfDay + ", minute : " + currentMinute);

        int index=0;
        boolean visit=false;
        for(int i=0; i<preTime.size();i++){
            String temp = preTime.get(i);
            int hour = Integer.parseInt(temp.substring(0,2));
            int min = Integer.parseInt(temp.substring(2,4));

            if (currentHourOfDay < hour || (currentHourOfDay == hour && currentMinute < min)) {
                if (visit)
                    continue;
                index = i;
                visit = true;
                Log.d("what", "handle make: first index : " + index + ", hour : " + hour + ",min : " + min);
                //처음에 한해서 하는데 나머지 시간은 넘김
            }
        }
        intent.putExtra("codeIndex",codeIndex);
        intent.putExtra("startIndex",index);
        intent.putExtra("visited",visit);
        intent.putStringArrayListExtra("alarmTime",preTime);

        //visit==false -> 내일 처음시간
        //visit == true -> 인덱스 부터 알람시작

        PendingIntent makeAlarm = PendingIntent.getBroadcast(context, requestCodeinHere, intent, 0);

        String time=preTime.get(index);

        setOnceAlarm(Integer.parseInt(time.substring(0,2)),Integer.parseInt(time.substring(2,4)),makeAlarm,!visit);

    }


    public void setOnceAlarm(int hourOfDay, int minute, PendingIntent alarmPendingIntent, boolean tomorrow) {
        Log.d("what", "handle set: alarmpending intent" + alarmPendingIntent.toString());
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, getTimeInMillis(tomorrow, hourOfDay, minute), alarmPendingIntent);
        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, getTimeInMillis(tomorrow, hourOfDay, minute), alarmPendingIntent);
        else
            alarmManager.set(AlarmManager.RTC_WAKEUP, getTimeInMillis(tomorrow, hourOfDay, minute), alarmPendingIntent);
    }


    private long getTimeInMillis(boolean tomorrow, int hourOfDay, int minute) {
        Log.d("what", "handle get: tomorrow " + tomorrow + ", hour : " + hourOfDay + ", minute : " + minute);

        GregorianCalendar calendar = (GregorianCalendar) GregorianCalendar.getInstance();

        if (tomorrow)
            calendar.add(GregorianCalendar.DAY_OF_YEAR, 1);

        calendar.set(GregorianCalendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(GregorianCalendar.MINUTE, minute);
        calendar.set(GregorianCalendar.SECOND, 0);
        calendar.set(GregorianCalendar.MILLISECOND, 0);

        return calendar.getTimeInMillis();

    }
}

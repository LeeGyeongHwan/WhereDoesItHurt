package com.k1l3.wheredoesithurt;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class cancelNotification extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent){

        int requestCode = intent.getIntExtra("requestCode",0);
        Log.d("what", "onReceive: cancel Notifiaction! requestCode + "+requestCode);
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(requestCode);

    }
}

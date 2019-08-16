package com.k1l3.wheredoesithurt;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.util.ArrayList;

public class PermissionUtils {
    public static boolean requestPermission(
            Activity activity, int requestCode, String... permissions) {
        boolean granted = true;
        ArrayList<String> permissionsNeeded = new ArrayList<>();

        for (String s : permissions) {
            Log.d("gallery", "requestPermission: s " + s);
            int permissionCheck = ContextCompat.checkSelfPermission(activity, s);
            Log.d("gallery", "requestPermission: percheck" + permissionCheck);
            Log.d("gallery", "requestPermission: pergranted" + PackageManager.PERMISSION_GRANTED);
            boolean hasPermission = (permissionCheck == PackageManager.PERMISSION_GRANTED);
            Log.d("gallery", "requestPermission: permission " + hasPermission);
            granted &= hasPermission;
            if (!hasPermission) {
                permissionsNeeded.add(s);
                Log.d("gallery", "had list: " + permissionsNeeded);
            }
        }

        if (granted) {
            return true;
        } else {
            Log.d("gallery", "requestPermission: not granted");
            ActivityCompat.requestPermissions(activity,
                    permissionsNeeded.toArray(new String[permissionsNeeded.size()]),
                    requestCode);
            Log.d("gallery", "this is after");
            return false;
        }
    }


    public static boolean permissionGranted(
            int requestCode, int permissionCode, int[] grantResults) {
        return requestCode == permissionCode && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED;
    }
}

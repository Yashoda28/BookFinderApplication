package com.example.yashoda.bookfinderapplication;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

class CommonUtils {


    //public static String userName = "key1";
   // public static String patientID = "key2";

    static void showMessage(final Context context, final String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    static void Log(Exception se) {
        Log.e(se.getClass().getName(), se.getMessage());
    }

    static void handleException(Context context, Exception e, String message) {
        Log(e);
        showMessage(context, message);
    }
}

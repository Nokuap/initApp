package com.dmitryzheltko.initapp.Utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by dmitry.zheltko on 3/25/2015.
 */
public class Utils {
    public static void log(String message) {
        Log.d("debug", message);
    }

    public static void log(int message) {
        Log.d("debug", String.valueOf(message));
    }

    public static void toast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}



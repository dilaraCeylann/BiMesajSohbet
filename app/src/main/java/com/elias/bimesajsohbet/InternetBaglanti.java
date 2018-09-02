package com.elias.bimesajsohbet;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

public class InternetBaglanti {

    static Context context;


    private static InternetBaglanti instance = new InternetBaglanti();
    ConnectivityManager connectivityManager;
    NetworkInfo wifiInfo, mobileInfo;
    boolean connected = false;

    public static InternetBaglanti getInstance(Context ctx) {
        context = ctx.getApplicationContext();
        return instance;
    }

    public boolean isOnline() {
        try {
            connectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            connected = networkInfo != null && networkInfo.isAvailable() &&
                    networkInfo.isConnected();
            if(connected == true){
                Toast.makeText(context, "var", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "yok", Toast.LENGTH_SHORT).show();
            }
            return connected;

        } catch (Exception e) {
            System.out.println("CheckConnectivity Exception: " + e.getMessage());
            Log.v("connectivity", e.toString());
            Toast.makeText(context, "Bug", Toast.LENGTH_SHORT).show();
        }


        return connected;
    }
}

package com.example.kerim.noktakutu;

/**
 * Created by Kerim on 3.5.2017.
 */


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

public class NetworkChangeReceiver extends BroadcastReceiver {
    private static final String LOG_TAG = "Otomatik internet Kontrol¸";
    static boolean isConnected = false;

    @Override
    public void onReceive(final Context context, final Intent intent) {

        isNetworkAvailable(context); //receiver çalıştığı zaman çağırılacak method

    }


    private boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE); //Sistem ağını dinliyor internet var mı yok mu

        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {

                        if (!isConnected) { //internet varsa
                            isConnected = true;
                            Log.v(LOG_TAG, "internete Bağlandınız!");

                        }
                        return true;
                    }
                }
            }
        }
        isConnected = false;
        Toast.makeText(context, "İnternet Yok,İnternet Bağlantınızı Kontrol Ediniz", Toast.LENGTH_LONG).show();

        Log.v(LOG_TAG, "İnternet Yok!");
        return false;
    }
}
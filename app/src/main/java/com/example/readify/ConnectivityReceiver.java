package com.example.readify;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ConnectivityReceiver extends BroadcastReceiver {

    public static ConnectivityReceiverListener connectivityReceiverListener;
    public ConnectivityReceiver(){ super(); }

    @Override
    public void onReceive(Context context, Intent intent) {
        String status = NetworkUtil.getConnectivityStatusString(context);
        if (status.isEmpty()) {
            status = "No internet connection";
        }

        if (connectivityReceiverListener != null)
            connectivityReceiverListener.onNetworkConnectionChanged(status);
    }

    public interface ConnectivityReceiverListener {
        void onNetworkConnectionChanged(String status);
    }
}

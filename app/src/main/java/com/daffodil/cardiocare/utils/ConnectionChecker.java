package com.daffodil.cardiocare.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;

public class ConnectionChecker {

    /**
     * This method check mobile is connected to network.
     * @param context
     * @return true if connected otherwise false.
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager conMan = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(conMan.getActiveNetworkInfo() != null && conMan.getActiveNetworkInfo().isConnected())
            return true;
        else
            return false;
    }

    public static void DisplayingNoNetworkMessage(final Context context){

//        final Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {

                if(!ConnectionChecker.isNetworkAvailable(context)){
                    new AlertDialog.Builder(context)
                            .setTitle("Warning")
                            .setMessage("You have no Internet Connection. Connect to internet and Try again")

                            // Specifying a listener allows you to take an action before dismissing the dialog.
                            // The dialog is automatically dismissed when a dialog button is clicked.
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    ((Activity)context).finish();
                                }
                            })

//                    handler.postDelayed(this, 10000);
                            // A null listener allows the button to dismiss the dialog and take no further action.
//                    .setNegativeButton(android.R.string.no, null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();

                }

//
//            }
//        }, 1000);  //the time is in miliseconds

    }

}

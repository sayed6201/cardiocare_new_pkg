package com.daffodil.cardiocare.utils;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.daffodil.cardiocare.HomeActivity;
import com.daffodil.cardiocare.R;


public class CheckConnectivity extends BroadcastReceiver
    {

        AlertDialog alertDialog;
        @Override
        public void onReceive(final Context context, Intent intent)
        {
            try
            {
                if (!isOnline(context)) {
                    if (alertDialog == null) {
                        alertDialog = new AlertDialog.Builder(context)
                                .setTitle("No Internet")
                                .setMessage("You have no Internet Connection. You can access our offline services")

//                    To prevent dialog box from getting dismissed on back key pressed use this

                                .setCancelable(false)
//                    And to prevent dialog box from getting dismissed on outside touch use this


                                // Specifying a listener allows you to take an action before dismissing the dialog.
                                // The dialog is automatically dismissed when a dialog button is clicked.
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {

                                        Intent intent = new Intent(context.getApplicationContext(), HomeActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        context.startActivity(intent);
                                        ((Activity) context).finish();

                                    }
                                })

//                    handler.postDelayed(this, 10000);
                                // A null listener allows the button to dismiss the dialog and take no further action.
//                    .setNegativeButton(android.R.string.no, null)
                                .setIcon(R.drawable.no_wifi)
                                .show();
                    }
                } else {
                    if(alertDialog != null){
                        alertDialog.dismiss();
                        alertDialog = null;
                    }

//                    Toast.makeText(context, "Connected", Toast.LENGTH_SHORT).show();
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }

        private boolean isOnline(Context context) {
            try {
                ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo netInfo = cm.getActiveNetworkInfo();
                //should check null because in airplane mode it will be null
                return (netInfo != null && netInfo.isConnected());
            } catch (NullPointerException e) {
                e.printStackTrace();
                return false;
            }
        }
    }

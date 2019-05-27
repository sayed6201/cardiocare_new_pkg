package com.daffodil.cardiocare;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.daffodil.cardiocare.Models.LoggedpatientDetail;
import com.daffodil.cardiocare.app.AppController;
import com.daffodil.cardiocare.utils.ApiClass;
import com.daffodil.cardiocare.utils.CheckConnectivity;
import com.daffodil.cardiocare.utils.Datas;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class SplashActivity extends AppCompatActivity {

    ProgressBar progressBar;
    ImageView imageViewMoney;
    TextView imageViewText, msgTextView;
    static SplashActivity splashActivity;
    int tim = 0;

    private CheckConnectivity checkConnectivity;
    LocalBroadcastManager localBroadcastManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        splashActivity = this;

        progressBar = findViewById(R.id.splash_progressbar);
        imageViewMoney = findViewById(R.id.iv_money);
        imageViewText = findViewById(R.id.iv_text);
        msgTextView = findViewById(R.id.msg_tv);

        imageViewMoney.setTranslationY(-1200);
        imageViewText.setTranslationY(1200);

        checkConnectivity = new CheckConnectivity();
        registerNetworkBroadcastForNougat();

        new Thread(new Runnable() {
            @Override
            public void run() {
                doWork();

                startApp();
                //default method to finishthe activity

            }
        }).start();

        imageViewMoney.animate().translationYBy(1200).setDuration(800);
        imageViewText.animate().translationYBy(-1200).setDuration(800);



    }

    private void doWork() {

        for (int i = 0; i < 100; i += 10) {
            try {

                Thread.sleep(200);
                progressBar.setProgress(i);
//                tim++;
//                msgTextView.setText("hello");

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void startApp() {
        SharedPreferences prefs = getSharedPreferences(Datas.spName, MODE_PRIVATE);
        String logStatus = prefs.getString("isLogged", "false");
        if(logStatus.equals("true")){
            String refreshToken = prefs.getString("refresh_token", "hellow--");
            String userId = prefs.getString("logged_id", "null");
            requestForRetrieveLogInData(userId,refreshToken);
        }else {
            Intent i = new Intent(SplashActivity.this,HomeActivity.class);
            startActivity(i);
        }
    }

    public void requestForRetrieveLogInData(String userId, String refreshToken){
        // Post request
        String tag_json_obj = "json_obj_req";
        //"http://103.86.197.83/api.ecure.oauth/
        String url = ApiClass.ApiAuthUrl+"api/token";
        //api.auth.ecure24.com


        JSONObject patient = new JSONObject();

        try {
            patient.put("userId",userId);
            patient.put("password","pass");
            patient.put("grantType","refresh_token");
            patient.put("refreshToken",refreshToken);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url, patient,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            if(response.getJSONObject("tokens").getString("accessToken") != null){

                                String token = response.getJSONObject("tokens").getString("accessToken");
                                String refreshToken = response.getJSONObject("tokens").getString("refreshToken");
                                String userId = response.getJSONObject("user").getString("id");
                                String phoneNumber = response.getJSONObject("user").getString("phoneNumber");
                                String em = response.getJSONObject("user").getString("email");
                                String userName = response.getJSONObject("user").getString("userName");

                                LoggedpatientDetail loggedpatientDetail = new LoggedpatientDetail(token,userId,em,phoneNumber,userName);

                                SharedPreferences.Editor editor = getSharedPreferences(Datas.spName, MODE_PRIVATE).edit();
                                editor.putString("isLogged","true");
                                editor.putString("refresh_token", refreshToken);
                                editor.putString("logged_id", userId);
                                editor.apply();

                                Intent i = new Intent(SplashActivity.this, PatientProfileActivity.class);
                                i.putExtra("patientObj",loggedpatientDetail);
                                startActivity(i);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                // As of f605da3 the following should work
                NetworkResponse response = error.networkResponse;


                if (error instanceof ServerError && response != null) {
                    try {
                        String res = new String(response.data,
                                HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                        // Now you can use any deserializer to make sense of data
                        JSONObject obj = new JSONObject(res);
                        Toast.makeText(SplashActivity.this, obj.getJSONArray("messages").get(0)+"", Toast.LENGTH_SHORT).show();

                        SharedPreferences.Editor editor = getSharedPreferences(Datas.spName, MODE_PRIVATE).edit();
                        editor.putString("isLogged","false");
                        editor.apply();

                        dialogMessage("Your login Token expired. You Have to login.");


                    } catch (UnsupportedEncodingException e1) {
                        // Couldn't properly decode data to string
                        e1.printStackTrace();
                    } catch (JSONException e2) {
                        // returned data is not JSONObject?
                        e2.printStackTrace();
                    }
                }

                if (error instanceof TimeoutError) {
//                    Toast.makeText(SplashActivity.this,
//                            "Our server is busy, try later", Toast.LENGTH_LONG).show();
                    dialogMessage("Our server is currently busy, You can access our offline services");

                }


            }

        })
        {
            /**
             * Passing some request headers
             * */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

    }

    public void dialogMessage(String msg){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(SplashActivity.this);

        builder1.setMessage(msg);
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        startActivity(new Intent(SplashActivity.this,HomeActivity.class));
                        finish();
                        dialog.cancel();
                    }
                });

//                            builder1.setNegativeButton(
//                                    "No",
//                                    new DialogInterface.OnClickListener() {
//                                        public void onClick(DialogInterface dialog, int id) {
//                                            dialog.cancel();
//                                        }
//                                    });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterNetworkChanges();
    }

    protected void unregisterNetworkChanges() {
        try {
            unregisterReceiver(checkConnectivity);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    private void registerNetworkBroadcastForNougat() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            registerReceiver(checkConnectivity, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            registerReceiver(checkConnectivity, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
    }
}
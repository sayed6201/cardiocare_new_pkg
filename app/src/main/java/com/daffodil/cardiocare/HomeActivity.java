package com.daffodil.cardiocare;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.daffodil.cardiocare.Models.LoggedpatientDetail;
import com.daffodil.cardiocare.app.AppController;
import com.daffodil.cardiocare.utils.ApiClass;
import com.daffodil.cardiocare.utils.Datas;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {

    static HomeActivity homeActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        homeActivity = this;

        SplashActivity.splashActivity.finish();

    }

    public void clicker(View v){
        if(v.getId() == R.id.login_cv){
            Intent intent = new Intent(HomeActivity.this,LoginActivity.class);
            startActivity(intent);

        }else if(v.getId() == R.id.appointment_cv){
            Intent intent = new Intent(HomeActivity.this,MainActivity.class);
            intent.putExtra("home_msg","Please select your Doctor for the Appointment.");
            startActivity(intent);

        }else if(v.getId() == R.id.contact_us_cv){
//            Intent intent = new Intent(HomeActivity.this,LoginActivity.class);
//
//            SharedPreferences prefs = getSharedPreferences(Datas.spName, MODE_PRIVATE);
//            String logStatus = prefs.getString("isLogged", "false");
//            if(logStatus.equals("true")){
//                String refreshToken = prefs.getString("refresh_token", "hellow--");
//                String userId = prefs.getString("logged_id", "null");
//                requestForRetrieveLogInData(userId,refreshToken);
//            }else{
//                Toast.makeText(this, "Log in First", Toast.LENGTH_SHORT).show();
//            }
//            Toast.makeText(homeActivity, "Contactus", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(HomeActivity.this,ContactActivity.class);
            startActivity(intent);

        }else if(v.getId() == R.id.doctor_cv){
            Intent intent = new Intent(HomeActivity.this,MainActivity.class);
            startActivity(intent);

        }else if(v.getId() == R.id.about_company){
            Intent intent = new Intent(HomeActivity.this,DSLActivity.class);
            startActivity(intent);
        }else if(v.getId() == R.id.share_fab){
            final String appPackageName = this.getPackageName();
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "Check out Cardio Care App at: https://play.google.com/store/apps/details?id=com.sayed.cardiocare");
            sendIntent.setType("text/plain");
            this.startActivity(sendIntent);
        }else if(v.getId() == R.id.login_btn){
            Intent intent = new Intent(HomeActivity.this,LoginActivity.class);
            startActivity(intent);
        }else if(v.getId() == R.id.share_btm_fab){
            final String appPackageName = this.getPackageName();
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "Check out Cardio Care App at: https://play.google.com/store/apps/details?id=com.sayed.cardiocare");
            sendIntent.setType("text/plain");
            this.startActivity(sendIntent);
        }else if(v.getId() == R.id.home_pharma_cv){
            Intent i = new Intent(HomeActivity.this, WebActivity.class);
            i.putExtra("url","https://pharmacy.cardiocarebd.com/");
            startActivity(i);
        }else if(v.getId() == R.id.getAmbulance_cv){
            Intent i = new Intent(HomeActivity.this, WebActivity.class);
            i.putExtra("url","http://cardiocarebd.com/ambulance/en/auth");
            startActivity(i);
        }else if(v.getId() == R.id.helpline){
            String phone = "+8801747333314";
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
            startActivity(intent);
        }else if(v.getId() == R.id.package_cv){
            Intent i = new Intent(HomeActivity.this,PackageActivity.class);
            startActivity(i);
        }
    }

    public void log_reg_homeClick(View v){
        Intent intent = new Intent(HomeActivity.this,LoginActivity.class);
        startActivity(intent);
    }


    public void requestForRetrieveLogInData(String userId, String refreshToken){
        // Post request
        String tag_json_obj = "json_obj_req";
        //"http://203.190.9.108/api.auth.ecure24.com/
        String url = ApiClass.ApiAuthUrl+"api/token";
/*
{
    "appointment_Schedule_Day_Slot_MappingId": "636711476987797174",
    "doctorId": "E000024",
    "visitedDate": "2019-03-19",
    "timeSlot": "18:00:00",
    "patientInfo": {
        "sex": "male",
        "patientName": "test by siddik",
        "age2": 35,
        "addressInfo": {
            "mobile": "01847140114"
        }
    },
    "consultationMainServiceCharges": "636711476987797168"
}
*/

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

                                Intent i = new Intent(HomeActivity.this, PatientProfileActivity.class);
                                i.putExtra("patientObj",loggedpatientDetail);
                                startActivity(i);
                                finish();
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
                        Toast.makeText(HomeActivity.this, obj.getJSONArray("messages").get(0)+"", Toast.LENGTH_SHORT).show();

                    } catch (UnsupportedEncodingException e1) {
                        // Couldn't properly decode data to string
                        e1.printStackTrace();
                    } catch (JSONException e2) {
                        // returned data is not JSONObject?
                        e2.printStackTrace();
                    }
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

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setMessage("Are you sure you want to exit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }

}

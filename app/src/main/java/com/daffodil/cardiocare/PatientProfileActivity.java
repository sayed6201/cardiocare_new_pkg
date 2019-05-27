package com.daffodil.cardiocare;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.daffodil.cardiocare.Adapter.PatientLabAdapter;
import com.daffodil.cardiocare.Models.LabReportModel;
import com.daffodil.cardiocare.Models.LoggedpatientDetail;
import com.daffodil.cardiocare.app.AppController;
import com.daffodil.cardiocare.utils.ApiClass;
import com.daffodil.cardiocare.utils.CheckConnectivity;
import com.daffodil.cardiocare.utils.Datas;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PatientProfileActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TextView pName,pId,pPhone,tv,navName,navId,userEmail;
    Gson gson;
    ArrayList<LabReportModel> labLists;
    RecyclerView recyclerView;
    PatientLabAdapter patientLabAdapter;
    LinearLayoutManager linearLayoutManager;
    CheckConnectivity checkConnectivity;
    static  LoggedpatientDetail patient;
    Dialog myDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setIcon(R.mipmap.ic_launcher_cardiocare);
        actionBar.setTitle("");

        myDialog = new Dialog(this);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        gson = new Gson();
        labLists = new ArrayList<>();

        pName = findViewById(R.id.pname_et);
        pId = findViewById(R.id.pid_et);
        pPhone = findViewById(R.id.phone_et);
        tv = findViewById(R.id.tv);
        userEmail = findViewById(R.id.email_et);
        recyclerView = findViewById(R.id.recycleListView);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        navId = headerView.findViewById(R.id.nav_id);
        navName = headerView.findViewById(R.id.nav_name);

        Intent i = getIntent();
        patient = (LoggedpatientDetail)i.getSerializableExtra("patientObj");

        SharedPreferences prefs = getSharedPreferences(Datas.spName, MODE_PRIVATE);
        String logStatus = prefs.getString("isLogged", "false");

        if(logStatus.equals("true")){

            pName.setText("Name: "+patient.getUserName());
            pId.setText("Id: "+patient.getUserId());
            pPhone.setText("Phone: "+patient.getPhoneNumber());
            navName.setText(patient.getUserName()+"");
            navId.setText(patient.getUserId()+"");
            userEmail.setText("Email: "+patient.getEmail()+"");
            requestForLabs(patient.getToken(),patient);
        }


        checkConnectivity = new CheckConnectivity();
        registerNetworkBroadcastForNougat();



        navigationView.setNavigationItemSelectedListener(this);

        if (SplashActivity.splashActivity != null){
            SplashActivity.splashActivity.finish();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
//            super.onBackPressed();
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





    public void clicker(View v){
        if(v.getId() == R.id.login_cv){
            Intent intent = new Intent(PatientProfileActivity.this,LoginActivity.class);
            startActivity(intent);

        }else if(v.getId() == R.id.appointment_cv){
            Intent intent = new Intent(PatientProfileActivity.this,MainActivity.class);
            intent.putExtra("home_msg","Please select your Doctor for the Appointment.");
            startActivity(intent);

        }else if(v.getId() == R.id.contact_us_cv){
            Intent intent = new Intent(PatientProfileActivity.this,ContactActivity.class);
            startActivity(intent);

        }else if(v.getId() == R.id.doctor_cv){
            Intent intent = new Intent(PatientProfileActivity.this,MainActivity.class);
            startActivity(intent);

        }else if(v.getId() == R.id.about_company){
            Intent intent = new Intent(PatientProfileActivity.this,DSLActivity.class);
            startActivity(intent);
        }else if(v.getId() == R.id.share_fab){
            final String appPackageName = this.getPackageName();
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "Check out Cardio Care App at: https://play.google.com/store/apps/details?id=com.sayed.cardiocare");
            sendIntent.setType("text/plain");
            this.startActivity(sendIntent);
        }else if(v.getId() == R.id.login_btn){
            Intent intent = new Intent(PatientProfileActivity.this,LoginActivity.class);
            startActivity(intent);
        }else if(v.getId() == R.id.share_btm_fab){
            final String appPackageName = this.getPackageName();
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "Check out Cardio Care App at: https://play.google.com/store/apps/details?id=com.sayed.cardiocare");
            sendIntent.setType("text/plain");
            this.startActivity(sendIntent);
        }else if(v.getId() == R.id.home_pharma_cv){
            Intent i = new Intent(PatientProfileActivity.this, WebActivity.class);
            i.putExtra("url","https://pharmacy.cardiocarebd.com/");
            startActivity(i);
        }else if(v.getId() == R.id.getAmbulance_cv){
            Intent i = new Intent(PatientProfileActivity.this, WebActivity.class);
            i.putExtra("url","http://cardiocarebd.com/ambulance/en/auth");
            startActivity(i);
        }else if(v.getId() == R.id.helpline){
            String phone = "+8801747333314";
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
            startActivity(intent);
        }else if(v.getId() == R.id.package_cv){
            Intent i = new Intent(PatientProfileActivity.this, PackageActivity.class);
            startActivity(i);
        }else if(v.getId() == R.id.consult_hist){
            Intent intent = new Intent(PatientProfileActivity.this,ReceiptActivity.class);
            startActivity(intent);
        }else if(v.getId() == R.id.storage_prescription){
            Intent intent = new Intent(PatientProfileActivity.this,ImgaeUploadActivity.class);
            startActivity(intent);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_logout) {

            SharedPreferences.Editor editor = getSharedPreferences(Datas.spName, MODE_PRIVATE).edit();
            editor.putString("isLogged","false");
            editor.apply();

            Intent i =new Intent(PatientProfileActivity.this,HomeActivity.class);
            startActivity(i);
            finish();

        } else if (id == R.id.nav_appointment) {

            Intent intent = new Intent(PatientProfileActivity.this,MainActivity.class);
            intent.putExtra("home_msg","Please select your Doctor for the Appointment.");
            startActivity(intent);

        } else if (id == R.id.nav_doc_info) {

            Intent intent = new Intent(PatientProfileActivity.this,MainActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_contact) {

//            Toast.makeText(this, "contact us", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(PatientProfileActivity.this,ContactActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_share) {

            final String appPackageName = this.getPackageName();
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "Check out Cardio Care App at: https://play.google.com/store/apps/details?id=com.sayed.cardiocare");
            sendIntent.setType("text/plain");
            this.startActivity(sendIntent);

        } else if (id == R.id.nav_company) {
            Intent i = new Intent(this, DSLActivity.class);
            startActivity(i);
        }else if(id == R.id.nav_invoice){
            Intent intent = new Intent(PatientProfileActivity.this,ReceiptActivity.class);
            startActivity(intent);
        }else if(id == R.id.nav_pass_change){
            dialogPopUp();
//            requestForPasswordChange();
        }else if(id == R.id.nav_prescription){
            Intent i = new Intent(PatientProfileActivity.this, ImgaeUploadActivity.class);
            startActivity(i);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void dialogPopUp(){
        TextView txtclose;
        final EditText  newPassEt,cnfrmPassEt,currPassEt;
        myDialog.setContentView(R.layout.dialog_passowrd_change);
        Button changePassBtn = (Button) myDialog.findViewById(R.id.change_pass);
        txtclose = myDialog.findViewById(R.id.txtclose);
        newPassEt = myDialog.findViewById(R.id.new_pass_et);
        cnfrmPassEt = myDialog.findViewById(R.id.cnfrm_new_pass_et);
        currPassEt = myDialog.findViewById(R.id.curr_pass_et);
        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
//        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        changePassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestForPasswordChange(currPassEt.getText().toString().trim(), newPassEt.getText().toString().trim(), cnfrmPassEt.getText().toString().trim());
            }
        });

        myDialog.show();
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterNetworkChanges();
    }

    private void registerNetworkBroadcastForNougat() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            registerReceiver(checkConnectivity, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            registerReceiver(checkConnectivity, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
    }

    protected void unregisterNetworkChanges() {
        try {
            unregisterReceiver(checkConnectivity);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }


    public void requestForLabs(final String accesstoken, final LoggedpatientDetail patientObj){
        // Post request
        String tag_json_obj = "json_obj_req";
        //http://103.86.197.83/api.paitent.ecure24.com
        String url =  ApiClass.ApiPatientUrl+"api/Labs";

        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        pDialog.hide();
                        pDialog.dismiss();

                        for(int i=0; i<response.length(); i++){
                            try {
                                LabReportModel labReportModel = (LabReportModel) gson.fromJson(response.getJSONObject(i).toString(),LabReportModel.class);
                                labLists.add(labReportModel);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        patientLabAdapter = new PatientLabAdapter(PatientProfileActivity.this,labLists,patientObj);
                        linearLayoutManager = new LinearLayoutManager(PatientProfileActivity.this,LinearLayoutManager.HORIZONTAL,false);
                        recyclerView.setLayoutManager(linearLayoutManager);
                        recyclerView.setAdapter(patientLabAdapter);
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                pDialog.hide();
                pDialog.dismiss();
                // As of f605da3 the following should work
                NetworkResponse response = error.networkResponse;
                if (error instanceof ServerError && response != null) {
                    try {
                        String res = new String(response.data,
                                HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                        // Now you can use any deserializer to make sense of data
                        JSONObject obj = new JSONObject(res);

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
                headers.put("Authorization", "Bearer " + accesstoken);
                return headers;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(req, tag_json_obj);

    }

    public void requestForPasswordChange(String currPass, String newPass, String cnfrmPass){

        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        // Post request
        String tag_json_obj = "json_obj_req";
        //"http://203.190.9.108/api.auth.ecure24.com/
        String url =ApiClass.ApiAuthUrl+"api/password";

        final JSONObject patient = new JSONObject();
/*
        {
            "currentPassword": "string",
                "newPassword": "string",
                "confirmNewPassword": "string"
        }
*/
        try {
            patient.put("currentPassword",currPass);
            patient.put("newPassword",newPass);
            patient.put("confirmNewPassword",cnfrmPass);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url, patient,
                new Response.Listener<JSONObject>(){

                    @Override
                    public void onResponse(JSONObject response) {

                        pDialog.hide();
                        pDialog.dismiss();

                        Toast.makeText(PatientProfileActivity.this, " Changed ", Toast.LENGTH_SHORT).show();

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                pDialog.hide();
                pDialog.dismiss();

                // As of f605da3 the following should work
                NetworkResponse response = error.networkResponse;


                if (error instanceof ServerError && response != null) {
                    try {
                        String res = new String(response.data,
                                HttpHeaderParser.parseCharset(response.headers, "utf-8"));

//                        if (res == null || res.length() == 0) {
//                            Toast.makeText(PatientProfileActivity.this, "Password Changed", Toast.LENGTH_SHORT).show();
//                        }else{
                            // Now you can use any deserializer to make sense of data
                            JSONObject obj = new JSONObject(res);

                            Toast.makeText(PatientProfileActivity.this, ""+obj.getJSONArray("messages").get(0), Toast.LENGTH_LONG).show();
//                        }


                    } catch (UnsupportedEncodingException e1) {
                        // Couldn't properly decode data to string
                        e1.printStackTrace();
                    } catch (JSONException e2) {
                        // returned data is not JSONObject?
                        e2.printStackTrace();
                    }
                }
                  else if(error instanceof TimeoutError){
                    // Take action when timeout happens
                    Toast.makeText(PatientProfileActivity.this, "Time out", Toast.LENGTH_SHORT).show();
                }
                    else if(response == null){
                    // Take action when timeout happens
                    Toast.makeText(PatientProfileActivity.this, "Password Changed", Toast.LENGTH_SHORT).show();
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
                headers.put("Authorization", "Bearer " + PatientProfileActivity.patient.getToken());
                return headers;
            }


        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

//    public class StrImplRequest extends StringRequest {
//        @Override
//        protected Response<String> parseNetworkResponse(NetworkResponse response) {
//            // take the statusCode here.
//            response.statusCode;
//            return super.parseNetworkResponse(response);
//        }
//    }



}

package com.daffodil.cardiocare;

import android.app.ProgressDialog;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
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

public class ReceiptActivity extends AppCompatActivity {

    TextView pName,pId,pPhone,tv,navName,navId;
    Gson gson;
    ArrayList<LabReportModel> labLists;
    RecyclerView recyclerView;
    PatientLabAdapter patientLabAdapter;
    LinearLayoutManager linearLayoutManager;
    CheckConnectivity checkConnectivity;
    ImageView emptyBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setIcon(R.mipmap.ic_launcher_cardiocare);
        actionBar.setTitle("");

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        gson = new Gson();
        labLists = new ArrayList<>();

        pName = findViewById(R.id.pname_et);
        pId = findViewById(R.id.pid_et);
        pPhone = findViewById(R.id.phone_et);
        tv = findViewById(R.id.tv_rec);
        recyclerView = findViewById(R.id.recycleListView);
        emptyBox = findViewById(R.id.empty_box);

        SharedPreferences prefs = getSharedPreferences(Datas.spName, MODE_PRIVATE);
        String logStatus = prefs.getString("isLogged", "false");

        if(logStatus.equals("true")){

//            pName.setText("Name: "+PatientProfileActivity.patient.getUserName());
//            pId.setText("Id: "+PatientProfileActivity.patient.getUserId());
//            pPhone.setText("Phone: "+PatientProfileActivity.patient.getPhoneNumber());
//            navName.setText(patient.getUserName()+"");
//            navId.setText(patient.getUserId()+"");
            requestForLabs(PatientProfileActivity.patient.getToken(),PatientProfileActivity.patient);
        }




        checkConnectivity = new CheckConnectivity();
        registerNetworkBroadcastForNougat();

        if (SplashActivity.splashActivity != null){
            SplashActivity.splashActivity.finish();
        }
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
        //"http://203.190.9.108/api.paitent.ecure24.com/
        String url = ApiClass.ApiPatientUrl+"api/Labs";

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

                        if(labLists.size()==0){
                            tv.setText("You have no record Yet");
                            emptyBox.setVisibility(View.VISIBLE);
                        }
                        patientLabAdapter = new PatientLabAdapter(ReceiptActivity.this,labLists,patientObj);
                        linearLayoutManager = new LinearLayoutManager(ReceiptActivity.this,LinearLayoutManager.VERTICAL,false);
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

}

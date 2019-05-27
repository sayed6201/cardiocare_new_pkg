package com.daffodil.cardiocare;

import android.app.ProgressDialog;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.daffodil.cardiocare.Adapter.ListAdapterWithRecycleView;
import com.daffodil.cardiocare.Models.Doctors;
import com.daffodil.cardiocare.app.AppController;
import com.daffodil.cardiocare.utils.ApiClass;
import com.daffodil.cardiocare.utils.CheckConnectivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    TextView tv;
    Spinner customSpinner;
    HashMap<String,String> speciality;
    ArrayList<String> specialityName;
    ArrayList<Doctors> doctorsList;
    ListAdapterWithRecycleView listAdapterWithRecycleView;
    LinearLayoutManager linearLayoutManager;
    RecyclerView recyclerView;
    CheckConnectivity checkConnectivity;



    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setIcon(R.mipmap.ic_launcher_cardiocare);
        actionBar.setTitle("");

        setContentView(R.layout.activity_main);
//        tv = findViewById(R.id.display);
        customSpinner = (Spinner) findViewById(R.id.speciality_spinner);
        recyclerView =(RecyclerView) findViewById(R.id.recycleListView);

        specialityName = new ArrayList<>();
        speciality = new HashMap<>();
        doctorsList = new ArrayList<>();

        getAllSpeciality();

        customSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String value = parent.getItemAtPosition(position).toString();
                clear();
                if(value.equals("--Select Speciality--")){
                    getAllDoctor();
                }else {
                    getDoctorsBySpeciality(speciality.get(value));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if(getIntent().getStringExtra("home_msg")!=null){
            Snackbar.make(findViewById(android.R.id.content), getIntent().getStringExtra("home_msg"), Snackbar.LENGTH_INDEFINITE)
                    .setAction("Ok", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    })
                    .setActionTextColor(getResources().getColor(android.R.color.holo_red_light ))
                    .show();
        }

        checkConnectivity = new CheckConnectivity();
        registerNetworkBroadcastForNougat();

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

    public void clear() {
        final int size = doctorsList.size();
        doctorsList.clear();
    }

    public void getAllSpeciality(){
        //getting all the posts
        // Tag used to cancel the request
        String tag_json_obj = "json_array_req";
        //"http://103.86.197.83/api.appointment.ecure24.com/
        String url =ApiClass.ApiAppointment+"api/specialities";

        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        final JsonArrayRequest jsonObjReq = new JsonArrayRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONArray>() {


                    @Override
                    public void onResponse(JSONArray response) {

                        Log.d("Response", response.toString());
                        pDialog.hide();
                        pDialog.dismiss();
                        specialityName.add("--Select Speciality--");
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                speciality.put(response.getJSONObject(i).getString("specialityFullName"),response.getJSONObject(i).getString("id"));
                                specialityName.add(response.getJSONObject(i).getString("specialityFullName"));

                                Log.i("haha",response.getJSONObject(i).getString("specialityFullName")+"");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        ArrayAdapter<String> customSpinAdap = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_item, specialityName);
                        customSpinAdap.setDropDownViewResource(R.layout.custom_dropdown);
                        customSpinner.setAdapter(customSpinAdap);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d("response", "Error: " + error.getMessage());
                        Log.i("haha",error.getMessage()+"");
                        // hide the progress dialog
                        pDialog.hide();
                        pDialog.dismiss();

                        // As of f605da3 the following should work
                        NetworkResponse response = error.networkResponse;

                        if (error instanceof TimeoutError) {
                            Toast.makeText(MainActivity.this,
                                    "Our server is busy, try later", Toast.LENGTH_LONG).show();
                        }

                        if (error instanceof ServerError && response != null) {
                            try {
                                String res = new String(response.data,
                                        HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                                // Now you can use any deserializer to make sense of data
                                JSONObject obj = new JSONObject(res);
                                Toast.makeText(MainActivity.this, ""+obj, Toast.LENGTH_SHORT).show();

                            } catch (UnsupportedEncodingException e1) {
                                // Couldn't properly decode data to string
                                e1.printStackTrace();
                            } catch (JSONException e2) {
                                // returned data is not JSONObject?
                                e2.printStackTrace();
                            }
                        }
                    }
                });

// Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

    public void getDoctorsBySpeciality(String id){

        // Tag used to cancel the request
        String tag_json_obj = "json_array_req";
        //"http://103.86.197.83/api.appointment.ecure24.com/
        String url =ApiClass.ApiAppointment+"api/doctors/speciality/"+id;

        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        final JsonArrayRequest jsonObjReq = new JsonArrayRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONArray>() {


                    @Override
                    public void onResponse(JSONArray response) {

                        Log.d("Response", response.toString());
                        pDialog.hide();
                        pDialog.dismiss();

                        for (int i = 0; i < response.length(); i++) {

                            try {
                                Doctors doctor = new Doctors(
                                        response.getJSONObject(i).getString("employeeId"),
                                        response.getJSONObject(i).getJSONObject("hrM_EmployeeInfo").getString("employeeFullName"),
                                        response.getJSONObject(i).getString("shortBio"),
                                        response.getJSONObject(i).getString("specialityId"),
                                        response.getJSONObject(i).getJSONObject("doctorSpeciality").getString("specialityFullName")
                                );
                                doctorsList.add(doctor);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        listAdapterWithRecycleView = new ListAdapterWithRecycleView(MainActivity.this,doctorsList);
                        linearLayoutManager = new LinearLayoutManager(MainActivity.this,LinearLayoutManager.VERTICAL,false);
                        LinearLayoutManager layoutManager=new LinearLayoutManager(MainActivity.this);
                        recyclerView.setLayoutManager(linearLayoutManager);
                        recyclerView.setAdapter(listAdapterWithRecycleView);



                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("response", "Error: " + error.getMessage());
                // hide the progress dialog
                pDialog.hide();
                pDialog.dismiss();


                // As of f605da3 the following should work
                NetworkResponse response = error.networkResponse;

                if (error instanceof TimeoutError) {
                    Toast.makeText(MainActivity.this,
                            "Our server is busy, try later", Toast.LENGTH_LONG).show();
                }

                if (error instanceof ServerError && response != null) {
                    try {
                        String res = new String(response.data,
                                HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                        // Now you can use any deserializer to make sense of data
                        JSONObject obj = new JSONObject(res);
                        Toast.makeText(MainActivity.this, ""+obj, Toast.LENGTH_SHORT).show();
                    } catch (UnsupportedEncodingException e1) {
                        // Couldn't properly decode data to string
                        e1.printStackTrace();
                    } catch (JSONException e2) {
                        // returned data is not JSONObject?
                        e2.printStackTrace();
                    }
                }

            }
        });

// Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

    public void getAllDoctor(){
        //getting all the posts
        // Tag used to cancel the request
        String tag_json_obj = "json_array_req";
        //http://103.86.197.83/api.appointment.ecure24.com/
        String url = ApiClass.ApiAppointment+"api/doctors";

        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        final JsonArrayRequest jsonObjReq = new JsonArrayRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONArray>() {


                    @Override
                    public void onResponse(JSONArray response) {

                        Log.d("Response", response.toString());
                        pDialog.hide();
                        pDialog.dismiss();

                        for (int i = 0; i < response.length(); i++) {

                            try {
                                Doctors doctor = new Doctors(
                                        response.getJSONObject(i).getString("employeeId"),
                                        response.getJSONObject(i).getJSONObject("hrM_EmployeeInfo").getString("employeeFullName"),
                                        response.getJSONObject(i).getString("shortBio"),
                                        response.getJSONObject(i).getString("specialityId"),
                                        response.getJSONObject(i).getJSONObject("doctorSpeciality").getString("specialityFullName")
                                );
                                doctorsList.add(doctor);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                        listAdapterWithRecycleView = new ListAdapterWithRecycleView(MainActivity.this,doctorsList);
                        linearLayoutManager = new LinearLayoutManager(MainActivity.this,LinearLayoutManager.VERTICAL,false);
                        LinearLayoutManager layoutManager=new LinearLayoutManager(MainActivity.this);
                        recyclerView.setLayoutManager(linearLayoutManager);
                        recyclerView.setAdapter(listAdapterWithRecycleView);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("response", "Error: " + error.getMessage());
                // hide the progress dialog
                pDialog.hide();
                pDialog.dismiss();

                // As of f605da3 the following should work
                NetworkResponse response = error.networkResponse;

                if (error instanceof TimeoutError) {
                    Toast.makeText(MainActivity.this,
                            "Our server is busy, try later", Toast.LENGTH_LONG).show();
                }

                if (error instanceof ServerError && response != null) {
                    try {
                        String res = new String(response.data,
                                HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                        // Now you can use any deserializer to make sense of data
                        JSONObject obj = new JSONObject(res);
                        Toast.makeText(MainActivity.this, ""+obj, Toast.LENGTH_SHORT).show();

                    } catch (UnsupportedEncodingException e1) {
                        // Couldn't properly decode data to string
                        e1.printStackTrace();
                    } catch (JSONException e2) {
                        // returned data is not JSONObject?
                        e2.printStackTrace();
                    }
                }
            }
        });

// Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }


}

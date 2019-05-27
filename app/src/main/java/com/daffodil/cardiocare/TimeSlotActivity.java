package com.daffodil.cardiocare;

import android.app.ProgressDialog;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Build;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.gson.Gson;
import com.daffodil.cardiocare.Adapter.TimeSlotAdapter;
import com.daffodil.cardiocare.Models.AddressInfo;
import com.daffodil.cardiocare.Models.AppointmentRequest;
import com.daffodil.cardiocare.Models.PatientInfo;
import com.daffodil.cardiocare.Models.TimeSlot;
import com.daffodil.cardiocare.app.AppController;
import com.daffodil.cardiocare.utils.ApiClass;
import com.daffodil.cardiocare.utils.CheckConnectivity;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class TimeSlotActivity extends AppCompatActivity implements DatePickerFragment.OnCompleteListener{

    Button showFragment;
    TextView date, dr_name, dr_sp, dr_id;
    TimeSlotAdapter timeSlotAdapter;
    LinearLayoutManager linearLayoutManager;
    RecyclerView recyclerView;
    static String drId;
    static String drName;
    static String drSp;
    static String selectedDate;
    Gson gson;
    CheckConnectivity checkConnectivity;

    static String visitDate;
    static String consultationMainServiceCharges;

    ArrayList<TimeSlot> slotLists;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setIcon(R.mipmap.ic_launcher_cardiocare);
            actionBar.setTitle("");


            setContentView(R.layout.activity_time_slot);
            date = findViewById(R.id.date);
            dr_name = findViewById(R.id.dr_name);
            dr_id = findViewById(R.id.dr_id);
            dr_sp = findViewById(R.id.dr_sp);

//            showFragment = (Button) findViewById(R.id.btnDatePicker);
            recyclerView = findViewById(R.id.recycleListView);

            slotLists = new ArrayList<>();

            drId = getIntent().getStringExtra("dr_id");
            drName = getIntent().getStringExtra("dr_name");
            drSp = getIntent().getStringExtra("dr_sp");

            dr_name.setText("Name: "+drName);
            dr_sp.setText("Speciality: "+drSp);
            dr_id.setText("Id: "+drId);

            selectedDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
            getDoctorsSchedule(drId,selectedDate);


            gson = new Gson();

            /*
{
        "appointment_Schedule_Day_Slot_MappingId": "636561291401410817",
        "doctorId": "E000018",
        "visitedDate": "2019-04-13",
        "timeSlot": "18:00:00",
        "patientInfo": {
        "sex": "male",
        "patientName": "test by siddik",
        "age2": 35,
        "addressInfo": {
        "mobile": "01847140114"
        }
        },
        "consultationMainServiceCharges": "19"
        }
*/

            PatientInfo patientInfo = new PatientInfo("male","sayed","35",new AddressInfo("012232333"));

            AppointmentRequest appointmentRequest = new AppointmentRequest("123212","E0012","19-03-292","18:00:00",patientInfo,"8927391");

            String json = gson.toJson(appointmentRequest);

            Log.i("json",json);

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

        public void showFragment(View view) {
            /**Show DialogFragment By Calling DatePickerFragment Class**/
            DialogFragment newFragment = new DatePickerFragment();
            /**Call show() of DialogFragment class**/
            newFragment.show(getSupportFragmentManager(), "date picker");
        }

    @Override
    public void onComplete(String date){
        clear();
//        this.date.setText(date);
        selectedDate = date;
        getDoctorsSchedule(drId,selectedDate);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        clear();
        getDoctorsSchedule(drId,selectedDate);
    }

    public void  clear() {
        slotLists.clear();
    }

    public void getDoctorsSchedule(String id, final String date){

        // Tag used to cancel the request
        String tag_json_obj = "json_array_req";
        //"http://103.86.197.83/api.appointment.ecure24.com/
        String url = ApiClass.ApiAppointment+"api/scheduleSlots/doctor/"+id+"/fromDate/"+date+"/next/0";

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
                        try {
                            String slotId = (String) response.getJSONObject(0).getString("slotMppingId");
                            String slotDate = (String) response.getJSONObject(0).getString("date");
                            TimeSlotActivity.this.date.setText("Showing Appointment time for "+slotDate);
                            for (int i = 0; i < response.getJSONObject(0).getJSONArray("slots").length(); i++) {

                                TimeSlot timeSlot = new TimeSlot(
                                        response.getJSONObject(0).getJSONArray("slots").getJSONObject(i).getString("timeSlot"),
                                        response.getJSONObject(0).getJSONArray("slots").getJSONObject(i).getString("isBooked"),
                                        response.getJSONObject(0).getJSONArray("slots").getJSONObject(i).getString("serialNo"),
                                        slotId,
                                        drId,
                                        date
                                );
                                slotLists.add(timeSlot);
                            }

                            timeSlotAdapter = new TimeSlotAdapter(TimeSlotActivity.this,slotLists);
                            linearLayoutManager = new LinearLayoutManager(TimeSlotActivity.this,LinearLayoutManager.VERTICAL,false);
                            LinearLayoutManager layoutManager=new LinearLayoutManager(TimeSlotActivity.this);
                            recyclerView.setLayoutManager(linearLayoutManager);
                            recyclerView.setAdapter(timeSlotAdapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("response", "Error: " + error.getMessage());
                // hide the progress dialog
                pDialog.hide();
                pDialog.dismiss();
            }
        });

// Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

}

package com.daffodil.cardiocare;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.daffodil.cardiocare.app.AppController;
import com.daffodil.cardiocare.utils.ApiClass;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity {

    TextView tv;

    EditText mobileEt, idEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        tv = findViewById(R.id.tv);
        mobileEt = findViewById(R.id.mobile_num_et);
        idEt = findViewById(R.id.id_et);
    }

    public void registrationClick(View v){
        requestForAppointment();
    }

    public void requestForAppointment(){
        // Post request
        String tag_json_obj = "json_obj_req";
        //"http://203.190.9.108/api.auth.ecure24.com/api
        String url = ApiClass.ApiAuthUrl+"PatientUserRegistrations";
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
            patient.put("mobileNo",mobileEt.getText().toString().trim());
            patient.put("id",idEt.getText().toString().trim());
        } catch (JSONException e) {
            e.printStackTrace();
        }



        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url, patient,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        Log.i("Response",response.toString());
                        tv.setText(response.toString());

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
                        tv.setText(res);
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
}

package com.daffodil.cardiocare;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.daffodil.cardiocare.Adapter.TestReportAdapter;
import com.daffodil.cardiocare.Models.LabReportModel;
import com.daffodil.cardiocare.Models.LoggedpatientDetail;
import com.daffodil.cardiocare.Models.TestReportModel;
import com.daffodil.cardiocare.app.AppController;
import com.daffodil.cardiocare.utils.ApiClass;
import com.daffodil.cardiocare.utils.CheckConnectivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;



    public class TestActivity extends AppCompatActivity {

        TextView receiptNotv,registrationID,receiptDateTime,referredByName,testBillAmount,receivableAmount,totalReceivedAmount,currentDueAmount,tv;

        ArrayList<TestReportModel> testList;
        RecyclerView recyclerView;
        TestReportAdapter adapter;
        LinearLayoutManager linearLayoutManager;
        Gson gson;
        CheckConnectivity checkConnectivity;
        ImageView boxEmpty;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_test);

            testList = new ArrayList<>();

//            receiptNotv = findViewById(R.id.receiptNo);
//            registrationID = findViewById(R.id.registrationID);
//            receiptDateTime = findViewById(R.id.receiptDateTime);
//            referredByName = findViewById(R.id.referredByName);
//            testBillAmount = findViewById(R.id.testBillAmount);
//            receivableAmount = findViewById(R.id.receivableAmount);
//            totalReceivedAmount = findViewById(R.id.totalReceivedAmount);
//            currentDueAmount = findViewById(R.id.currentDueAmount);
            recyclerView = findViewById(R.id.recycleListView);
            tv = findViewById(R.id.tv);
            boxEmpty = findViewById(R.id.empty_box_test);
            gson = new Gson();


            Intent i = getIntent();
            LoggedpatientDetail loggedpatientDetail = (LoggedpatientDetail)i.getSerializableExtra("patientObj");
            LabReportModel labReportModel = (LabReportModel) i.getSerializableExtra("reportObj");
            String reportNo = i.getStringExtra("reportNo");
//            Toast.makeText(this, "hi "+reportNo+" "+loggedpatientDetail.getToken()+labReportModel.getReceiptNo(), Toast.LENGTH_SHORT).show();
            requestForTestReport(loggedpatientDetail.getToken(),labReportModel.getReceiptNo(),reportNo);

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

        public void requestForTestReport(final String accesstoken, final String receiptNo, final String reportNo){
            // Post request
            String tag_json_obj = "json_obj_req";
            //"http://203.190.9.108/api.paitent.ecure24.com/
            String url = ApiClass.ApiPatientUrl+"api/Labs/"+receiptNo;

            final ProgressDialog pDialog = new ProgressDialog(this);
            pDialog.setMessage("Loading...");
            pDialog.show();

            JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET,
                    url, null,
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {

                            pDialog.hide();
                            pDialog.dismiss();

                            try {
//                                receiptNotv.setText("Receipt No: "+response.getString("receiptNo"));
//                                registrationID.setText("Registration Id: "+response.getString("registrationID"));
//                                receiptDateTime.setText("Receipt Date: "+response.getString("receiptDateTime"));
//                                referredByName.setText("Referred By: "+response.getString("referredByName"));
//                                testBillAmount.setText("Test Bill Amount: "+response.getString("testBillAmount"));
//                                receivableAmount.setText("Receivable Amount: "+response.getString("receivableAmount"));
//                                totalReceivedAmount.setText("TotalReceived Amount: "+response.getString("totalReceivedAmount"));
//                                totalReceivedAmount.setText("Current Due Amount: "+response.getString("currentDueAmount"));

                                for(int i=0 ;i<response.getJSONArray("labTestReportInfos").length();i++){

                                    String data = response.getJSONArray("labTestReportInfos").getJSONObject(i).getString("reportNo");


                                    if (data.equals(reportNo)){
//                                        tv.setText(data+": "+response.getJSONArray("labTestReportInfos").getJSONObject(i).getJSONArray("labTestReportServices")+"");

                                        for(int j =0; j<response.getJSONArray("labTestReportInfos").getJSONObject(i).getJSONArray("labTestReportServices").length();j++){

                                            TestReportModel obj = new TestReportModel(
                                                    response.getJSONArray("labTestReportInfos").getJSONObject(i).getJSONArray("labTestReportServices").getJSONObject(j).getString("result"),
                                                    response.getJSONArray("labTestReportInfos").getJSONObject(i).getJSONArray("labTestReportServices").getJSONObject(j).getJSONObject("labTestServiceItemInfo").getString("reportingServiceName"),
                                                    response.getJSONArray("labTestReportInfos").getJSONObject(i).getJSONArray("labTestReportServices").getJSONObject(j).getJSONObject("labTestServiceItemInfo").getString("unit"),
                                                    response.getJSONArray("labTestReportInfos").getJSONObject(i).getJSONArray("labTestReportServices").getJSONObject(j).getJSONObject("labTestServiceItemInfo").getString("defaultResult"),
                                                    response.getJSONArray("labTestReportInfos").getJSONObject(i).getJSONArray("labTestReportServices").getJSONObject(j).getJSONObject("labTestServiceItemInfo").getString("refferenceRange")
                                            );

                                            testList.add(obj);

                                        }
                                        break;
                                    }
//                                    DetailLabReport obj = new DetailLabReport(
//                                            response.getJSONArray("labTestReportInfos").getJSONObject(i).getString("reportNo"),
//                                            response.getJSONArray("labTestReportInfos").getJSONObject(i).getString("receiptNo"),
//                                            response.getJSONArray("labTestReportInfos").getJSONObject(i).getString("charge"),
//                                            response.getJSONArray("labTestReportInfos").getJSONObject(i).getString("deliveryDateTime"),
//                                            response.getJSONArray("labTestReportInfos").getJSONObject(i).getString("reportDateTime"),
//                                            response.getJSONArray("labTestReportInfos").getJSONObject(i).getJSONObject("testServiceLabTestServiceInfo").getJSONObject("labReportTitle").getString("reportTitleName"),
//                                            response.getJSONArray("labTestReportInfos").getJSONObject(i).getJSONObject("testServiceLabTestServiceInfo").getJSONObject("labSpecimen").getString("specimenName")
//
//                                    );
//                                response.getJSONArray("labTestReportInfos").getJSONObject(i).getJSONArray("labTestReportServices").getJSONObject(0).getString("result")
//                                response.getJSONArray("labTestReportInfos").getJSONObject(i).getJSONArray("labTestReportServices").getJSONObject(0).getJSONObject("labTestServiceItemInfo").getString("reportingServiceName"),
//                                        response.getJSONArray("labTestReportInfos").getJSONObject(i).getJSONArray("labTestReportServices").getJSONObject(0).getJSONObject("labTestServiceItemInfo").getString("unit"),
//                                        response.getJSONArray("labTestReportInfos").getJSONObject(i).getJSONArray("labTestReportServices").getJSONObject(0).getJSONObject("labTestServiceItemInfo").getString("defaultResult"),
//                                        response.getJSONArray("labTestReportInfos").getJSONObject(i).getJSONArray("labTestReportServices").getJSONObject(0).getJSONObject("labTestServiceItemInfo").getString("refferenceRange")
//                                    reportList.add(obj);
                                }

                                if(testList.size() == 0){
                                    tv.setVisibility(View.VISIBLE);
                                    tv.setText("Test Report is not Available Yet");
                                    boxEmpty.setVisibility(View.VISIBLE);

                                }
                                adapter = new TestReportAdapter(TestActivity.this,testList);
                                linearLayoutManager = new LinearLayoutManager(TestActivity.this,LinearLayoutManager.VERTICAL,false);
                                recyclerView.setLayoutManager(linearLayoutManager);
                                recyclerView.setAdapter(adapter);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {

                    pDialog.hide();
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


        public void passChangeTester(){


        }
    }

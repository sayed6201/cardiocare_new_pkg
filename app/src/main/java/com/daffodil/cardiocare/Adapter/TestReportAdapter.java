package com.daffodil.cardiocare.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.daffodil.cardiocare.Models.TestReportModel;
import com.daffodil.cardiocare.R;

import java.util.List;



/**
 * Created by sayed on 8/19/17.
 */

public class TestReportAdapter extends RecyclerView.Adapter<TestReportAdapter.ViewHolder> {

    private static final String TAG = ListAdapterWithRecycleView.class.getSimpleName();

    private List<TestReportModel> list;

    private Context context;

    public TestReportAdapter(Context context, List<TestReportModel> list){
        this.list = list;
        this.context=context;

    }


    //LAyout inflater
    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.test_report_view_layout,parent,false);
        return new ViewHolder(view);
    }


    //DATABINDING
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        TestReportModel obj = list.get(position);

//        holder.reportTitleName.setText("Report Title: "+obj.getReportTitleName());
//        holder.specimenName.setText("Specimen Name: "+obj.getSpecimenName());
//        holder.reportNo.setText("Report No: "+obj.getReportNo());
        holder.result.setText("Result: "+obj.getResult());
        holder.reportingServiceName.setText("Reporting Service Name: "+obj.getReportingServiceName());
        holder.unit.setText("Unit: "+obj.getUnit());
        holder.defaultResult.setText("Default Result: "+obj.getDefaultResult());
        holder.refferenceRange.setText("Refference Range: "+obj.getRefferenceRange());

        //if u use arrayList
        //holder.fundTextView.setText(fund.get(position).toString());
        //holder.nameTextView.setText(name.get(position));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder{

//        public TextView reportNo;
//        public TextView receiptNo;
//        public TextView charge;
//        public TextView reportDateTime;
//        public TextView reportTitleName;
//        public TextView specimenName;
//        public TextView deliveryDateTime;
        public TextView result;
        public TextView reportingServiceName;
        public TextView unit;
        public TextView defaultResult;
        public TextView refferenceRange;



        public ViewHolder(View view){
            super(view);
//            reportNo = (TextView)view.findViewById(R.id.reportNo);
//            receiptNo = (TextView)view.findViewById(R.id.receiptNo);
//            charge = (TextView)view.findViewById(R.id.charge);
//            reportDateTime = (TextView)view.findViewById(R.id.reportDateTime);
//            reportTitleName = (TextView)view.findViewById(R.id.reportTitleName);
//            specimenName = (TextView)view.findViewById(R.id.specimenName);
//            deliveryDateTime = (TextView)view.findViewById(R.id.deliveryDateTime);
            result = (TextView)view.findViewById(R.id.result);
            reportingServiceName = (TextView)view.findViewById(R.id.reportingServiceName);
            unit = (TextView)view.findViewById(R.id.unit);
            defaultResult = (TextView)view.findViewById(R.id.defaultResult);
            refferenceRange = (TextView)view.findViewById(R.id.refferenceRange);

            if(list.size() > 0){
                // on item click
                view.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        // get position
                        int pos = getAdapterPosition();
//                    // check if item still exists
                        if(pos != RecyclerView.NO_POSITION){

//                            Intent intent = new Intent(context, LabReportDetailActivity.class);
//                            intent.putExtra("reportObj",list.get(pos));
//                            intent.putExtra("patientObj",patientObj);
//
//                            context.startActivity(intent);

                            Toast.makeText(context, "selected "+pos, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        }
    }
}


package com.daffodil.cardiocare.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.daffodil.cardiocare.LabReportDetailActivity;
import com.daffodil.cardiocare.Models.LabReportModel;
import com.daffodil.cardiocare.Models.LoggedpatientDetail;
import com.daffodil.cardiocare.R;

import java.util.List;

/**
 * Created by sayed on 8/19/17.
 */

public class PatientLabAdapter extends RecyclerView.Adapter<PatientLabAdapter.ViewHolder> {

    private static final String TAG = ListAdapterWithRecycleView.class.getSimpleName();

    private List<LabReportModel> list;
    private LoggedpatientDetail patientObj;
    private Context context;

    public PatientLabAdapter(Context context, List<LabReportModel> list, LoggedpatientDetail patientObj){
        this.list = list;
        this.context=context;
        this.patientObj = patientObj;
    }


    //LAyout inflater
    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.labreport_view_layout,parent,false);
        return new ViewHolder(view);
    }


    //DATABINDING
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        LabReportModel obj = list.get(position);

        holder.receiptIdTv.setText("Receipt Id: "+obj.getReceiptNo());
        holder.reportDateTv.setText("Receipt Date:"+obj.getReceiptDateTime());
        holder.reportRegIdTv.setText("Registration Id: "+obj.getRegistrationID());


        //if u use arrayList
        //holder.fundTextView.setText(fund.get(position).toString());
        //holder.nameTextView.setText(name.get(position));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        public TextView receiptIdTv;
        public TextView reportRegIdTv;
        public TextView reportDateTv;


        public ViewHolder(View view){
            super(view);
            receiptIdTv = (TextView)view.findViewById(R.id.receipt_no_tv);
            reportDateTv = (TextView)view.findViewById(R.id.report_date_tv);
            reportRegIdTv = (TextView)view.findViewById(R.id.reg_no_tv);

            if(list.size() > 0){
                // on item click
                view.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        // get position
                        int pos = getAdapterPosition();
//                    // check if item still exists
                        if(pos != RecyclerView.NO_POSITION){

                            Intent intent = new Intent(context, LabReportDetailActivity.class);
                            intent.putExtra("reportObj",list.get(pos));
                            intent.putExtra("patientObj",patientObj);

                            context.startActivity(intent);

//                            Toast.makeText(context, "selected "+pos, Toast.LENGTH_SHORT).show();
                        }
                    }
                });


            }
        }
    }
}

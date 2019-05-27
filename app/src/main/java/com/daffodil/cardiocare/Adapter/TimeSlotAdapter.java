package com.daffodil.cardiocare.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.daffodil.cardiocare.AppointmentActivity;
import com.daffodil.cardiocare.Models.TimeSlot;
import com.daffodil.cardiocare.R;

import java.util.List;

/**
 * Created by sayed on 8/19/17.
 */

public class TimeSlotAdapter extends RecyclerView.Adapter<TimeSlotAdapter.PersonViewHolder> {

    private static final String TAG = ListAdapterWithRecycleView.class.getSimpleName();

    private List<TimeSlot> list;
    private Context context;

    public TimeSlotAdapter(Context context, List<TimeSlot> list){
        this.list = list;
        this.context=context;
    }


    //LAyout inflater
    @Override
    public PersonViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.slot_view_layout,parent,false);
        return new PersonViewHolder(view);
    }


    //DATABINDING
    @Override
    public void onBindViewHolder(final PersonViewHolder holder, int position) {

        TimeSlot slot = list.get(position);

        holder.tv_time_slot.setText(slot.getTimeSlot());
        if(slot.getIsBooked().equals("true")){
            holder.tv_booked.setText("Booked");
            holder.tv_booked.setBackgroundColor(Color.parseColor("#E71C23"));
        }else{
            holder.tv_booked.setText("Available");
            holder.tv_booked.setBackgroundColor(Color.parseColor("#10A881"));
        }

        holder.tv_serial_num.setText("Serial no: "+slot.getSerialNo());

        //if u use arrayList
        //holder.fundTextView.setText(fund.get(position).toString());
        //holder.nameTextView.setText(name.get(position));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class PersonViewHolder extends RecyclerView.ViewHolder{
        public TextView tv_time_slot;
        public TextView tv_booked;
        public TextView tv_serial_num;

        public PersonViewHolder(View view){
            super(view);
            tv_time_slot = (TextView)view.findViewById(R.id.time_slot);
            tv_booked = (TextView)view.findViewById(R.id.booked);
            tv_serial_num = (TextView)view.findViewById(R.id.serial_num);

            if(list.size() > 0){

                // on item click
                view.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        // get position
                        int pos = getAdapterPosition();
                        if(pos != RecyclerView.NO_POSITION){
                            if (list.get(pos).getIsBooked().equals("true")){
                                Toast.makeText(context, "The Slot is booked", Toast.LENGTH_SHORT).show();
                            }else{
                                Intent intent = new Intent(context, AppointmentActivity.class);
                                intent.putExtra("slot_map_id",list.get(pos).getSlotId());
                                intent.putExtra("time_slot",list.get(pos).getTimeSlot());
                                intent.putExtra("dr_id",list.get(pos).getDrId());
                                intent.putExtra("visit_date",list.get(pos).getVisitDate());

                                context.startActivity(intent);

    //                            Toast.makeText(context, "selected "+list.get(pos).getDrId()+" "+list.get(pos).getVisitDate(), Toast.LENGTH_SHORT).show();
                            }


                        }
                    }
                });


            }
        }
    }
}

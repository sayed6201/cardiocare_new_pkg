package com.daffodil.cardiocare.Adapter;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.daffodil.cardiocare.Models.Doctors;
import com.daffodil.cardiocare.R;
import com.daffodil.cardiocare.TimeSlotActivity;

import java.util.List;

/**
 * Created by Created by sayed on 8/19/17.
 */

public class ListAdapterWithRecycleView extends RecyclerView.Adapter<ListAdapterWithRecycleView.ViewHolder> {

    private static final String TAG = ListAdapterWithRecycleView.class.getSimpleName();

    private List<Doctors> DoctorsList;
    private Context context;

    public ListAdapterWithRecycleView(Context context, List<Doctors> list){
        this.DoctorsList = list;
        this.context=context;
    }


    //LAyout inflater
    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.post_view_layout,parent,false);
        final ViewHolder viewHolder=new ViewHolder(view);

//        view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int position= getAdapterPosition();
//                Toast.makeText(context,"Item at position "+position+" "+DoctorsList.get(position).getEmployeeFullName(), Toast.LENGTH_SHORT).show();
////                notifyDataSetChanged();
////                if(personModifier!=null){personModifier.onPersonDeleted(position);}
//            }
//        });

//        view.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View view) {
//                int position= getAdapterPosition();
//                Toast.makeText(context,"Item at position "+position, Toast.LENGTH_SHORT).show();
////                notifyDataSetChanged();
////                if(personModifier!=null){personModifier.onPersonDeleted(position);}
//                return true;
//            }
//        });

        return new ViewHolder(view);
    }


    //DATABINDING
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        Doctors doctors = DoctorsList.get(position);

        holder.tv_dr_name.setText(doctors.getEmployeeFullName());
        holder.tv_dr_id.setText("Id: "+doctors.getEmployeeId());
        holder.tv_dr_speciality.setText("Speciality: "+doctors.getSpecialityFullName());
        holder.tv_dr_bio.setText("Bio: "+doctors.getShortBio());

        //if u use arrayList
        //holder.fundTextView.setText(fund.get(position).toString());
        //holder.nameTextView.setText(name.get(position));

    }

    @Override
    public int getItemCount() {
        return DoctorsList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        public TextView tv_dr_name;
        public TextView tv_dr_speciality;
        public TextView tv_dr_id;
        public TextView tv_dr_bio;

        public ViewHolder(View view){
            super(view);
            tv_dr_name = (TextView)view.findViewById(R.id.dr_name);
            tv_dr_speciality=(TextView)view.findViewById(R.id.dr_speciality);
            tv_dr_id=(TextView)view.findViewById(R.id.dr_employeeId);
            tv_dr_bio=(TextView)view.findViewById(R.id.dr_bio);

            if(DoctorsList.size() > 0){

                // on item click
                view.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        // get position
                        int pos = getAdapterPosition();
//                    // check if item still exists
                        if(pos != RecyclerView.NO_POSITION){
                            Intent intent = new Intent(context, TimeSlotActivity.class);
                            intent.putExtra("dr_id",DoctorsList.get(pos).getEmployeeId());
                            intent.putExtra("dr_name",DoctorsList.get(pos).getEmployeeFullName());
                            intent.putExtra("dr_sp",DoctorsList.get(pos).getSpecialityFullName());
                            context.startActivity(intent);
                        }
                    }
                });

            }


        }
    }
}

package com.daffodil.cardiocare.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.daffodil.cardiocare.ImgaeUploadActivity;
import com.daffodil.cardiocare.Models.Prescription;
import com.daffodil.cardiocare.R;

import java.util.List;


/**
 * Created by Created by sayed on 8/19/17.
 */

public class PrescriptionRecyclerViewAdapter extends RecyclerView.Adapter<PrescriptionRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = ListAdapterWithRecycleView.class.getSimpleName();

    private List<Prescription> list;
    private Context context;

    public PrescriptionRecyclerViewAdapter(Context context, List<Prescription> list){
        this.list = list;
        this.context = context;
    }


    //LAyout inflater
    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.prescription_view_layout,parent,false);
        final ViewHolder viewHolder=new ViewHolder(view);
        return new ViewHolder(view);
    }


    //DATABINDING
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        Prescription prescription = list.get(position);


//        holder.prescriptionIv.setImageResource(prescription.getResource());

        Glide.with(context).load(prescription.getImgUrl()).into(
                holder.prescriptionIv
        );



        //if u use arrayList
        //holder.fundTextView.setText(fund.get(position).toString());
        //holder.nameTextView.setText(name.get(position));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView prescriptionIv;
        ImageView button;

        public ViewHolder(View view){
            super(view);

            prescriptionIv = view.findViewById(R.id.prescription_iv);
            button = view.findViewById(R.id.delete_prescription);


            if(list.size() > 0){

                // on item click
                button.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        new AlertDialog.Builder(context)
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setMessage("Are you sure you want to delete?")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                                {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        // get position
                                        final ProgressDialog pDialog = new ProgressDialog(context);
                                        pDialog.setMessage("Deleting...");
                                        pDialog.show();

                                        int pos = getAdapterPosition();
                                        String nameOfImg = list.get(pos).getImgName();
                                        // Create a reference to the file to delete
                                        StorageReference desertRef = ImgaeUploadActivity.storageRef.child(nameOfImg);
                                        // Delete the file
                                        desertRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                // File deleted successfully

                                                DatabaseReference ref = ImgaeUploadActivity.myRef;
                                                Query applesQuery = ref.orderByChild("imgName").equalTo(nameOfImg);

                                                applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                                                            appleSnapshot.getRef().removeValue();

                                                            pDialog.hide();
                                                            pDialog.dismiss();
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {
                                                        Toast.makeText(context, ""+databaseError.toException(), Toast.LENGTH_SHORT).show();
                                                    }
                                                });

                                                Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception exception) {
                                                // Uh-oh, an error occurred!
                                                pDialog.hide();
                                                pDialog.dismiss();
                                                Toast.makeText(context, "sorry try later", Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                    }

                                })
                                .setNegativeButton("No", null)
                                .show();

                    }
                });

                prescriptionIv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder mBuilder = new AlertDialog.Builder(context);
                        LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View mView = li.inflate(R.layout.image_zoom_layout, null);
                        PhotoView photoView = mView.findViewById(R.id.photo_view);

                        final ProgressDialog pDialog = new ProgressDialog(context);
                        pDialog.setMessage("Deleting...");
                        pDialog.show();

                        Glide.with(context)
                                .load(list.get(getAdapterPosition()).getImgUrl())
                                .listener(new RequestListener<Drawable>() {
                                    @Override
                                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                        pDialog.hide();
                                        pDialog.dismiss();
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                        pDialog.hide();
                                        pDialog.dismiss();
                                        return false;
                                    }
                                })
                                .into(photoView);

//                        Glide.with(context).load(list.get(getAdapterPosition()).getImgUrl()).into(photoView);
                        mBuilder.setView(mView);
                        AlertDialog mDialog = mBuilder.create();
                        mDialog.show();
                    }
                });

            }
        }
    }
}

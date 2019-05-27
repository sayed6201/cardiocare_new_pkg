package com.daffodil.cardiocare;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.daffodil.cardiocare.Adapter.PrescriptionRecyclerViewAdapter;
import com.daffodil.cardiocare.Models.Prescription;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ImgaeUploadActivity extends AppCompatActivity {

    TextView textView;
    public static FirebaseDatabase database;
    public static FirebaseStorage storage;
    public static StorageReference storageRef;
    public static DatabaseReference myRef;
    String myId;

    RecyclerView prescriptionRecyclerView;
    PrescriptionRecyclerViewAdapter prescriptionRecyclerViewAdapter;

    private static final String TAG = MainActivity.class.getSimpleName();
    public static final int REQUEST_IMAGE = 100;

    ImageView imgProfile;

    List<Prescription> list;

    LinearLayout emptyMessage;

    FloatingActionButton fbUpload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imgae_upload);
        textView =findViewById(R.id.textView);
        imgProfile = findViewById(R.id.imageView);
        emptyMessage = findViewById(R.id.empty_storage_layout);
        prescriptionRecyclerView = findViewById(R.id.prescription_rv);
        fbUpload = findViewById(R.id.floatingActionButtonUpload);

        myId = PatientProfileActivity.patient.getPhoneNumber();

        // Write a message to the database
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference(myId);

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        list = new ArrayList<>();

        prescriptionRecyclerViewAdapter = new PrescriptionRecyclerViewAdapter(ImgaeUploadActivity.this,list);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        prescriptionRecyclerView.setLayoutManager(staggeredGridLayoutManager);
        prescriptionRecyclerView.setAdapter(prescriptionRecyclerViewAdapter);
        readFromDb();



//        list.add(new Prescription(R.drawable.bgdr));
//        list.add(new Prescription(R.drawable.bgdr));
//        list.add(new Prescription(R.drawable.bgdr));
//        list.add(new Prescription(R.drawable.bgdr));
//        list.add(new Prescription(R.drawable.bgdr));
//        list.add(new Prescription(R.drawable.bgdr));
//        list.add(new Prescription(R.drawable.bgdr));
//        list.add(new Prescription(R.drawable.bgdr));
//        list.add(new Prescription(R.drawable.bgdr));
//        list.add(new Prescription(R.drawable.bgdr));
//        list.add(new Prescription(R.drawable.bgdr));
//        list.add(new Prescription(R.drawable.bgdr));

        fbUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onProfileImageClick();
            }
        });


    }

    public void readFromDb(){
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                list.clear();

                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Prescription obj = postSnapshot.getValue(Prescription.class);
                    list.add(obj);
                }
                pDialog.hide();
                pDialog.dismiss();

                if (list.size() == 0){
                    emptyMessage.setVisibility(View.VISIBLE);
                }else {
                    emptyMessage.setVisibility(View.GONE);
                }
                prescriptionRecyclerViewAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("error response", "Failed to read value.", error.toException());
            }
        });
    }


    // my button click function
    void onProfileImageClick() {

        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            showImagePickerOptions();
                        } else {
                            // TODO - handle permission denied case
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    private void showImagePickerOptions() {
        ImagePickerActivity.showImagePickerOptions(this, new ImagePickerActivity.PickerOptionListener() {
            @Override
            public void onTakeCameraSelected() {
                launchCameraIntent();
            }

            @Override
            public void onChooseGallerySelected() {
                launchGalleryIntent();
            }
        });
    }

    private void launchCameraIntent() {
        Intent intent = new Intent(ImgaeUploadActivity.this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_IMAGE_CAPTURE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 3); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 4);

        // setting maximum bitmap width and height
        intent.putExtra(ImagePickerActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_WIDTH, 1000);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_HEIGHT, 1000);

        startActivityForResult(intent, REQUEST_IMAGE);
    }

    private void launchGalleryIntent() {
        Intent intent = new Intent(ImgaeUploadActivity.this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_GALLERY_IMAGE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 3); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 4);
        startActivityForResult(intent, REQUEST_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = data.getParcelableExtra("path");
                try {
                    // You can update this bitmap to your server
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);

                    // loading profile image from local cache
//                    loadProfile(uri.toString());
                    imgProfile.setImageURI(uri);

//                    Toast.makeText(this, uri+"", Toast.LENGTH_SHORT).show();
                    uploadPrescription(uri);

                }   catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void uploadPrescription(Uri uri){

        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Uploading...");
        pDialog.show();

        String imgName = System.currentTimeMillis()+"."+getExtensionOfPhoto(uri);
//        Uri file = Uri.fromFile(new File("path/to/images/rivers.jpg"));
        Uri file = uri;
        StorageReference riversRef = storageRef.child(imgName);
        UploadTask uploadTask = riversRef.putFile(file);

// Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                pDialog.hide();
                pDialog.dismiss();
                Toast.makeText(ImgaeUploadActivity.this, "oops", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
                pDialog.hide();
                pDialog.dismiss();
                Toast.makeText(ImgaeUploadActivity.this, "uploaded", Toast.LENGTH_SHORT).show();

            }
        });

        uploadTask = riversRef.putFile(file);
        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return riversRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();

                    String id = myRef.push().getKey();
                    Prescription obj = new Prescription(downloadUri+"",imgName);
                    myRef.child(id).setValue(obj);

                } else {
                    // Handle failures
                    // ...
                }
            }
        });
    }

    public void deletePrescription(String imgName){

// Create a reference to the file to delete
        StorageReference desertRef = storageRef.child(imgName);

// Delete the file
        desertRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // File deleted successfully
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Uh-oh, an error occurred!
            }
        });
    }

    private String getExtensionOfPhoto(Uri uri) {

        String extension;

        //Check uri format to avoid null
        if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            //If scheme is a content
            final MimeTypeMap mime = MimeTypeMap.getSingleton();
            extension = mime.getExtensionFromMimeType(getContentResolver().getType(uri));
        } else {
            //If scheme is a File
            //This will replace white spaces with %20 and also other special characters. This will avoid returning null values on file name with spaces and special characters.
            extension = MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(new File(uri.getPath())).toString());

        }

        return extension;

    }

    public void deletePrescription(View v){

    }

}

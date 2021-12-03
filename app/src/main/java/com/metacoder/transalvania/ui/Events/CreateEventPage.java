package com.metacoder.transalvania.ui.Events;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.metacoder.transalvania.databinding.ActivityCreateEventPageBinding;
import com.metacoder.transalvania.models.EventModel;
import com.metacoder.transalvania.utils.Utils;

import java.io.File;

public class CreateEventPage extends AppCompatActivity {
    boolean isEdit = false;
    String downloadURL = "";
    EventModel model;
    private ActivityCreateEventPageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateEventPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.eventImage.setOnClickListener(view -> ImagePicker.with(CreateEventPage.this)
                .crop()
                .start());

        binding.signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = binding.title.getText().toString();
                String location = binding.location.getText().toString();
                String details = binding.placeDetails.getText().toString();
                if (title.isEmpty() || location.isEmpty() || details.isEmpty() || downloadURL.isEmpty()) {

                } else {
                    DatabaseReference mref = FirebaseDatabase.getInstance().getReference("Events");
                    // int stock, String priceDetails, String placeDetails, String name,
                    // String current_rating, int max_ppl, int id, String banner_image, String location
                    EventModel model = new EventModel(100, details, details, title, "0.0", 100, downloadURL.length(), downloadURL, location);
                    mref.child(downloadURL.length() + "").setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_LONG).show();
                            finish();
                        }
                    }).addOnFailureListener(e -> {
                        Toast.makeText(getApplicationContext(), "Error : " + e.getMessage(), Toast.LENGTH_LONG).show();
                    });
                }

            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && data != null) {
            Uri uri = data.getData();

            File file = new File(String.valueOf(uri));
            binding.eventImage.setImageURI(uri);
            String mimetype = Utils.getMimeType(getApplicationContext(), uri);
            try {
                FileUpload(uri, mimetype);
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }

        }

    }

    public void FileUpload(Uri file, String mimetype) {
        ProgressDialog dialog = Utils.createDialogue(CreateEventPage.this, "");
        dialog.setMessage("Uploading Media...");
        dialog.setCancelable(false);
        dialog.show();

        StorageReference storageReference = FirebaseStorage.getInstance().getReference("uploads").child("" + System.currentTimeMillis() + "." + mimetype);


        UploadTask uploadTask = storageReference.putFile(file);

        // Listen for state changes, errors, and completion of the upload.
        uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                // Log.d(TAG, "Upload is " + progress + "% done");
            }
        }).addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onPaused(UploadTask.TaskSnapshot taskSnapshot) {
                //  Log.d(TAG, "Upload is paused");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // Handle successful uploads on complete
                // ...
            }
        });


// Register observers to listen for when the download is done or if it fails
        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                // Continue with the task to get the download URL
                downloadURL = storageReference.getDownloadUrl().toString();

                return storageReference.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    downloadURL = downloadUri.toString();
                    dialog.dismiss();
                } else {
                    // Handle failures
                    // ...
                    dialog.dismiss();
                }
            }
        });
    }

}
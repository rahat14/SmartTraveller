package com.metacoder.transalvania.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.metacoder.transalvania.databinding.ActivityRegisterBinding;
import com.metacoder.transalvania.models.ProfileModel;
import com.metacoder.transalvania.utils.Utils;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.HashMap;

public class Register extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    boolean isEdit = false;
    String downloadURL = "";
    ProfileModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        isEdit = getIntent().getBooleanExtra("is_edit", false);

        if (!isEdit) {
            getSupportActionBar().setTitle("Register");
            binding.ppContianer.setVisibility(View.GONE);
            binding.PersonPass.setVisibility(View.VISIBLE);
            binding.signUp.setText("Sign Up");
        } else {
            getSupportActionBar().setTitle("Edit Profile");
            model = (ProfileModel) getIntent().getSerializableExtra("MODEL");
            binding.PersonPass.setVisibility(View.GONE);
            binding.ppContianer.setVisibility(View.VISIBLE);
            binding.container.setVisibility(View.GONE);
            binding.signUp.setText("Update");

            try {
                downloadURL = model.getPp();

                Glide.with(getApplicationContext())
                        .load(downloadURL)
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                        .into(binding.pp);
            } catch (Exception e) {

            }
            binding.Personname.setText(model.getName());
            binding.PersonEmail.setText(model.getMail());
            binding.PersonEmail.setFocusable(false);
            binding.PersonEmail.setEnabled(false);

        }


        binding.signinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent p = new Intent(getApplicationContext(), SignIn.class);
                startActivity(p);

            }
        });


        binding.pp.setOnClickListener(view -> ImagePicker.with(Register.this)
                .crop()
                .start());

        binding.signUp.setOnClickListener(v -> {
            String password = binding.PersonPass.getText().toString();
            String mail = binding.PersonEmail.getText().toString();
            String username = binding.Personname.getText().toString();

            if (!isEdit) {

                if (password.isEmpty()) {
                    binding.PersonPass.setText("Can't Be Empty");
                } else if (mail.isEmpty()) {
                    binding.PersonEmail.setText("Can't Be Empty");
                } else if (username.isEmpty()) {
                    binding.Personname.setText("Can't Be Empty");
                } else {
                    registerUser(mail, username, password);
                }

            } else {
                // update profile
                ProgressDialog dialog = Utils.createDialogue(Register.this, "Updating User....");
                dialog.show();
                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                DatabaseReference mref = FirebaseDatabase.getInstance().getReference("users");
                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("name", username);
                map.put("mail", mail);
                map.put("pp", downloadURL);
                map.put("user_id", uid);

                mref.child(uid).updateChildren(map).addOnCompleteListener(task -> {
                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Profile Updated", Toast.LENGTH_LONG).show();
                    finish();
                });
            }

        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void registerUser(String mail, String username, String password) {

        ProgressDialog dialog = Utils.createDialogue(Register.this, "Creating User....");
        dialog.show();

        FirebaseAuth mauth = FirebaseAuth.getInstance();
        mauth.createUserWithEmailAndPassword(mail, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        DatabaseReference mref = FirebaseDatabase.getInstance().getReference("users");

                        HashMap<String, Object> map = new HashMap<String, Object>();


                        map.put("name", username);
                        map.put("mail", mail);
                        map.put("pp", "");
                        map.put("user_id", uid);

                        mref.child(uid).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull @NotNull Task<Void> task) {
                                dialog.dismiss();
                                Intent p = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(p);
                                finish();
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Eorror : " + e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && data != null) {
            Uri uri = data.getData();

            File file = new File(String.valueOf(uri));
            binding.pp.setImageURI(uri);
            String mimetype = Utils.getMimeType(getApplicationContext(), uri);
            try {
                FileUpload(uri, mimetype);
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }

        }

    }

    public void FileUpload(Uri file, String mimetype) {
        ProgressDialog dialog = Utils.createDialogue(Register.this, "");
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
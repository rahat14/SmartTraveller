package com.metacoder.transalvania.ui.timeline;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.metacoder.transalvania.R;
import com.metacoder.transalvania.models.ProfileModel;
import com.metacoder.transalvania.models.counterModel;
import com.metacoder.transalvania.models.postModel;
import com.metacoder.transalvania.utils.Utils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class CreatePostActivity extends AppCompatActivity {
    public final String[] EXTERNAL_PERMS = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
    public final int EXTERNAL_REQUEST = 138;
    EditText statusIn;
    ImageView imageView;
    String mimetype, filePath, uid;
    FirebaseAuth mauth;
    String userName, userProfilePic, downloadURL = "";
    ImageView closeBtn;
    TextView nextBTn;
    DatabaseReference postRef, counterRef;
    ProgressDialog progressDialog;
    String category = "all";
    String stringToApped = "file://";

    public static String getPath(Context ctx, Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = ctx.getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) return null;
        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String s = cursor.getString(columnIndex);
        cursor.close();
        return s;
    }

    public static String getMimeType(Context context, Uri uri) {
        String extension;

        //Check uri format to avoid null
        if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            //If scheme is a content
            final MimeTypeMap mime = MimeTypeMap.getSingleton();
            extension = mime.getExtensionFromMimeType(context.getContentResolver().getType(uri));
        } else {
            //If scheme is a File
            //This will replace white spaces with %20 and also other special characters. This will avoid returning null values on file name with spaces and special characters.
            extension = MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(new File(uri.getPath())).toString());

        }

        return extension;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        statusIn = findViewById(R.id.statusBar);
        statusIn.requestFocus();
        imageView = findViewById(R.id.mediaViewer);
        nextBTn = findViewById(R.id.nextBtn);
        closeBtn = findViewById(R.id.closerBtn);

        progressDialog = new ProgressDialog(CreatePostActivity.this);
        progressDialog.setCancelable(false);
        mauth = FirebaseAuth.getInstance();
        uid = mauth.getUid();

        counterRef = FirebaseDatabase.getInstance().getReference("counterRef").child(uid);


        Intent post = getIntent();
        mimetype = post.getStringExtra("type");
        filePath = post.getStringExtra("path");
        category = post.getStringExtra("category");


        postRef = FirebaseDatabase.getInstance().getReference("post");


        Log.d("TAG", "onCreate:  " + mimetype + "  PATH " + filePath);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });

        //  Uri uri = Uri.parse(filePath);

//
//        Glide.with(this)
//                .load(uri)
//                .thumbnail(0.25f)
//                .fitCenter()
//                .into(imageView);


        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        nextBTn.setOnClickListener(v -> {
            createPost("true", "d");
            //    if (mimetype.contains("jpg") || mimetype.contains("jpg")) {
            //uploadImage(uri);


            //     } else {

//                if (getDuration(CreatePostActivity.this, uri.toString()) >= 21) {
//                    Toast.makeText(CreatePostActivity.this, "MORE THAN 20 SECONDS ", Toast.LENGTH_LONG)
//                            .show();
//                } else {
//                    //  uploadVideoWithName(uri);
//                }


            // }


        });


    }

    public boolean requestForPermission() {

        boolean isPermissionOn = true;
        final int version = Build.VERSION.SDK_INT;
        if (version >= 23) {
            if (!canAccessExternalSd()) {
                isPermissionOn = false;
                requestPermissions(EXTERNAL_PERMS, EXTERNAL_REQUEST);
            }
        }

        return isPermissionOn;
    }

    public boolean canAccessExternalSd() {
        return (hasPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE));
    }

    private boolean hasPermission(String perm) {
        return (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(this, perm));

    }

    public long getDuration(Context context, String LINK) {


        MediaMetadataRetriever retriever = new MediaMetadataRetriever();

// There are other variations of setDataSource(), if you have a different input
        retriever.setDataSource(context, Uri.parse(LINK));
        String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        long durationMs = Long.parseLong(time);
        retriever.release();


        return durationMs / 1000;
    }

    public void createPost(String isImage, String mediaLink) {

        progressDialog.setTitle("Creating Post...");

        String postTextt = statusIn.getText().toString().trim();

        // create the post model heres

        String key = postRef.push().getKey();

        Map<String, Boolean> likedUser = new HashMap<>();
        likedUser.put("test", false);
        postModel postModel = new postModel(key, uid, postTextt
                , downloadURL, isImage, userProfilePic, userName, System.currentTimeMillis(), 0,
                0, likedUser, category);


        postRef.child(key).setValue(postModel)
                .addOnSuccessListener(aVoid -> {

                    //   Toast.makeText(getApplicationContext() , "Post Updated", Toast.LENGTH_LONG ).show();

                    increaseThePostCounter();

                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Error : " + e.getMessage(), Toast.LENGTH_LONG).show();

            }
        });

    }

    @Override
    protected void onDestroy() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        super.onDestroy();
    }

    public void goHome() {
        // progressDialog.dismiss();
        finish();
    }

    public void FileUpload(Uri file) {
        ProgressDialog dialog = Utils.createDialogue(CreatePostActivity.this, "");
        dialog.setMessage("Uploading Media...");
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

    public void getOwnData() {
        DatabaseReference OwnRef = FirebaseDatabase.getInstance().getReference("users").child(uid);
        OwnRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                ProfileModel user = dataSnapshot.getValue(ProfileModel.class);

                if (user != null) {
                    userName = user.getName();
                    // userProfilePic = user.get();

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void increaseThePostCounter() {


        counterRef.runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                counterModel p = mutableData.getValue(counterModel.class);
                if (p == null) {
                    return Transaction.success(mutableData);
                }

                // Star the post and add self to stars
                p.postCounter = p.postCounter + 1;
                // Set value and report transaction success

                mutableData.setValue(p);

                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean committed,
                                   DataSnapshot currentData) {

                // Transaction completed


                goHome();

            }


        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        getOwnData();
    }

    private void openGallery() {

        ImagePicker.with(this)
                .start();
//        Intent intent = new Intent(
//                Intent.ACTION_PICK,
//                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
//        );
//        intent.setType("*/*");
//        String[] mimetypes = {
//                "image/*",
//                "application/pdf",
//                "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
//                "application/msword"
//        };
//        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimetypes);
//        startActivityForResult(intent, 123);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && data != null) {
            Uri uri = data.getData();

            File file = new File(String.valueOf(uri));
            mimetype = getMimeType(getApplicationContext(), uri);

            imageView.setImageURI(uri);

            try {
                FileUpload(uri);
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }

        }

    }
}
package com.metacoder.smart_traveler.ui.auth;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.metacoder.smart_traveler.databinding.ActivitySignInBinding;
import com.metacoder.smart_traveler.ui.MainActivity;
import com.metacoder.smart_traveler.ui.resetPassword.ResetPassword;
import com.metacoder.smart_traveler.utils.Utils;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class SignIn extends AppCompatActivity {

    private static final int RC_SIGN_IN = 100;
    private ActivitySignInBinding binding;
    GoogleSignInOptions gso = new GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("853338194045-d9sq1dsvda2i8gkllgts2rsnl6ab1eir.apps.googleusercontent.com")
            .requestEmail()
            .requestProfile()
            .build();
    FirebaseAuth mauth;
    GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        mauth = FirebaseAuth.getInstance();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        binding.forgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent p = new Intent(getApplicationContext(), ResetPassword.class);
                startActivity(p);

            }
        });

        binding.googleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

        binding.signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent p = new Intent(getApplicationContext(), Register.class);
                startActivity(p);
                finish();
            }
        });

        binding.signinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String emailStr = binding.email.getText().toString();
                String passwordStr = binding.password.getText().toString();

                if (emailStr.isEmpty()) {
                    binding.email.setError("Can't Be Empty");
                }
                if (passwordStr.isEmpty()) {
                    binding.password.setError("Can't Be Empty");
                }

                if (!emailStr.isEmpty() && !passwordStr.isEmpty()) {
                    userSignIn(emailStr, passwordStr);
                }

            }
        });


    }

    private void userSignIn(String emailStr, String passwordStr) {
        ProgressDialog dialog = Utils.createDialogue(SignIn.this, "Signing In...");
        dialog.show();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signInWithEmailAndPassword(emailStr, passwordStr)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        dialog.dismiss();
                        Intent p = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(p);
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss();

                new MaterialAlertDialogBuilder(SignIn.this)
                        .setTitle("Error")
                        .setMessage("Email Or Password Does Not Match.")
                        .setPositiveButton(android.R.string.ok, (dialog1, which) -> dialog1.dismiss())
                        .show();
                // Toast.makeText(getApplicationContext(), "Error: " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d("TAG", "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.d("TAG", "Google sign in failed" + e.getLocalizedMessage());
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mauth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mauth.getCurrentUser();
                            checkUser(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithCredential:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Error : " + task.getException(), Toast.LENGTH_LONG).show();

                        }
                    }
                });
    }

    private void checkUser(FirebaseUser user) {
        DatabaseReference mref = FirebaseDatabase.getInstance().getReference();
        String uid = user.getUid();
        Log.d("TAG", "loadProfileData: " + uid);
        mref.child("users").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                if (snapshot.exists()) {

                    // user exists
                    Intent p = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(p);
                    finish();

                } else {

                    // cretae user

                    DatabaseReference mref = FirebaseDatabase.getInstance().getReference("users");
                    HashMap<String, Object> map = new HashMap<String, Object>();
                    map.put("name", user.getDisplayName());
                    map.put("mail", user.getEmail());
                    map.put("pp", "");
                    map.put("user_id", uid);
                    map.put("ph", "");
                    mref.child(uid).updateChildren(map).addOnCompleteListener(task -> {
                        Intent p = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(p);
                        finish();
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

                Toast.makeText(getApplicationContext(), "Error : " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

}
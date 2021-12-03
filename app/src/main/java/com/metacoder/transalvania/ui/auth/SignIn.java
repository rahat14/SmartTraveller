package com.metacoder.transalvania.ui.auth;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.metacoder.transalvania.databinding.ActivitySignInBinding;
import com.metacoder.transalvania.ui.MainActivity;
import com.metacoder.transalvania.ui.resetPassword.ResetPassword;
import com.metacoder.transalvania.utils.Utils;

public class SignIn extends AppCompatActivity {

    private ActivitySignInBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        binding.forgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent p = new Intent(getApplicationContext(), ResetPassword.class);
                startActivity(p);

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
                } else if (passwordStr.isEmpty()) {
                    binding.password.setError("Can't Be Empty");
                } else {
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


}
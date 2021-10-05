package com.metacoder.transalvania.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.metacoder.transalvania.databinding.ActivitySignInBinding;
import com.metacoder.transalvania.utils.Utils;

public class SignIn extends AppCompatActivity {

    private ActivitySignInBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent p   = new Intent(getApplicationContext() ,  Register.class );
                startActivity(p);
                finish();
            }
        });

        binding.signinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String emailStr = binding.email.getText().toString();
                String passwordStr = binding.password.getText().toString() ;

                if(emailStr.isEmpty()){
                    binding.email.setError("Can't Be Empty");
                }else if( passwordStr.isEmpty()){
                    binding.password.setError("Can't Be Empty");
                }else {
                    userSignIn(emailStr , passwordStr) ;
                }

            }
        });




    }

    private void userSignIn(String emailStr, String passwordStr) {
        ProgressDialog dialog = Utils.createDialogue(SignIn.this , "Signing In...") ;
        dialog.show();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signInWithEmailAndPassword(emailStr , passwordStr)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        dialog.dismiss();
                        Intent p   = new Intent(getApplicationContext() ,  MainActivity.class );
                        startActivity(p);
                        finish();
                    }
                }).addOnFailureListener(e -> {
            dialog.dismiss();
            Toast.makeText(getApplicationContext() , "Error: " + e.getLocalizedMessage() , Toast.LENGTH_SHORT).show();
        });

    }





}
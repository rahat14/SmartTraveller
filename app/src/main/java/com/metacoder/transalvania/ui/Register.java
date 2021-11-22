package com.metacoder.transalvania.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.metacoder.transalvania.databinding.ActivityRegisterBinding;
import com.metacoder.transalvania.utils.Utils;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class Register extends AppCompatActivity {

    private ActivityRegisterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setTitle("Register");

        binding.signinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent p = new Intent(getApplicationContext(), SignIn.class);
                startActivity(p);

            }
        });

        binding.signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = binding.PersonPass.getText().toString();
                String mail = binding.PersonEmail.getText().toString();
                String username = binding.Personname.getText().toString();


                if (password.isEmpty()) {
                    binding.PersonPass.setText("Can't Be Empty");
                } else if (mail.isEmpty()) {
                    binding.PersonEmail.setText("Can't Be Empty");
                } else if (username.isEmpty()) {
                    binding.Personname.setText("Can't Be Empty");
                } else {
                    registerUser(mail, username, password);
                }


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
}
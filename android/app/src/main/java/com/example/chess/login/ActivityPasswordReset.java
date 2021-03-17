package com.example.chess.login;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chess.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class ActivityPasswordReset extends AppCompatActivity {

    EditText userEmail;
    Button sendM;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset);

        //get views
        sendM = findViewById(R.id.sendm);
        userEmail = findViewById(R.id.emailrec);
        mAuth = FirebaseAuth.getInstance();

        //back button
        ImageButton back = findViewById(R.id.backbtn1);
        back.setOnClickListener(v -> onBackPressed());

        //send email query
        sendM.setOnClickListener(v -> mAuth.sendPasswordResetEmail(userEmail.getText().toString()).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                Toast.makeText(ActivityPasswordReset.this, R.string.email_sent, Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(ActivityPasswordReset.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
            }
        }));




    }
}
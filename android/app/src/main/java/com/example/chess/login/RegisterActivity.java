package com.example.chess.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chess.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {

    EditText passText;
    EditText emailText;
    Button btn;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //get views
        passText = findViewById(R.id.passtext);
        emailText = findViewById(R.id.emailtext);
        btn = findViewById(R.id.create);
        passText.setInputType( InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD );
        passText.setTransformationMethod(PasswordTransformationMethod.getInstance());

        //back button
        ImageButton back = findViewById(R.id.backbtn1);
        back.setOnClickListener(v -> onBackPressed());

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        /*if (mAuth.getCurrentUser()!=null){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }*/


        //create account button onClickListener
        btn.setOnClickListener(v -> {
            String email = emailText.getText().toString().trim();
            String password = passText.getText().toString().trim();

            //check if fields are empty
            if(TextUtils.isEmpty(email)){
                emailText.setError(getResources().getString(R.string.email_req));
                return;

            }

            if(TextUtils.isEmpty(password)){
                passText.setError(getResources().getString(R.string.pass_req));
                return;
            }

            //check password length
            if(password.length()<6){
                passText.setError(getResources().getString(R.string.pass_longer));
                return;
            }

            //register the user in the firebase
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(RegisterActivity.this, R.string.acc_create, Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));

                        } else {
                            // If sign in fails, display a message to the user.

                            Toast.makeText(RegisterActivity.this, R.string.acc_fail, Toast.LENGTH_SHORT).show();

                        }

                    });


        });
    }






}



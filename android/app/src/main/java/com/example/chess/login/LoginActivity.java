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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.example.chess.MainActivity;
import com.example.chess.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    Button btnIn;
    Button createBtn;
    EditText passIn;
    EditText emailIn;
    Button resetBtn;
    ImageButton exitBtn;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //get views from layout
        createBtn = findViewById(R.id.createbtn);
        btnIn = findViewById(R.id.logbtn);
        passIn = findViewById(R.id.passin);
        emailIn = findViewById(R.id.emailin);
        resetBtn = findViewById(R.id.resetbtn);
        exitBtn = findViewById(R.id.exitButton);
        //get Firebase authentication instance
        mAuth = FirebaseAuth.getInstance();
        //set input type to password
        passIn.setInputType( InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD );
        passIn.setTransformationMethod(PasswordTransformationMethod.getInstance());


        exitBtn.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this, R.style.AlertDialogStyle);
            builder.setMessage(R.string.areYouSure)
                    .setTitle(R.string.leavelog)
                    .setPositiveButton("Ok", (dialog, id) -> {
                        this.finishAffinity();

                    })
                    .setNegativeButton(R.string.cancel, (dialog, id) -> {

                    });
            builder.create().show();
        });
        //login button onClickListener
        btnIn.setOnClickListener(v -> {
            String email = emailIn.getText().toString().trim();
            String password = passIn.getText().toString().trim();

            //check if fields are empty
            if (TextUtils.isEmpty(email)) {
                emailIn.setError(getResources().getString(R.string.email_req));
                return;
            }

            if (TextUtils.isEmpty(password)) {
                passIn.setError(getResources().getString(R.string.pass_req));
                return;
            }

            //check password length
            if (password.length() < 6) {
                passIn.setError(getResources().getString(R.string.pass_longer));
                return;
            }

            //sign in
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            if(user.isEmailVerified()){
                                // Sign in success, update UI with the signed-in user's information
                                Toast.makeText(LoginActivity.this, R.string.log_success, Toast.LENGTH_LONG).show();
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                finish();
                            } else {
                                Toast.makeText(LoginActivity.this, R.string.emailNotVerified, Toast.LENGTH_LONG).show();
                                user.sendEmailVerification().addOnSuccessListener(aVoid -> Toast.makeText(LoginActivity.this, R.string.sentNewMail, Toast.LENGTH_SHORT).show()).addOnFailureListener(e -> {

                                });
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginActivity.this, R.string.error + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_LONG).show();
                            Toast.makeText(LoginActivity.this, R.string.log_success, Toast.LENGTH_LONG).show();
                        }
                    });
        });

        //register account intent to RegisterActivity
        createBtn.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(),RegisterActivity.class)));

        //reset password intent to ActivityPasswordReset
        resetBtn.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), ActivityPasswordReset.class)));

        }
    }

package com.example.chess.login;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.chess.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    public static final String TAG = "RegisterActivity";
    EditText passText;
    EditText emailText;
    EditText usernameText;
    ImageView image_profile;
    private Uri imageUri;
    private String imageLink = "";
    Button btn;
    private FirebaseAuth mAuth;
    FirebaseFirestore fStore;
    StorageReference fStorage;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //get views
        image_profile = findViewById(R.id.image_profile);
        passText = findViewById(R.id.passText);
        emailText = findViewById(R.id.emailText);
        usernameText = findViewById(R.id.userNameText);
        btn = findViewById(R.id.create);
        passText.setInputType( InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD );
        passText.setTransformationMethod(PasswordTransformationMethod.getInstance());
        fStore = FirebaseFirestore.getInstance();
        fStorage = FirebaseStorage.getInstance().getReference();

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
            String username = usernameText.getText().toString();

            //check if fields are empty
            if(TextUtils.isEmpty(email)){
                emailText.setError(getResources().getString(R.string.email_req));
                return;

            }

            if(TextUtils.isEmpty(password)){
                passText.setError(getResources().getString(R.string.pass_req));
                return;
            }

            if(TextUtils.isEmpty(username)){
                usernameText.setError(getResources().getString(R.string.pass_req));
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
                            user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(RegisterActivity.this, "O Email de Verificação foi enviado!", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                }
                            });
                            userID = mAuth.getCurrentUser().getUid();
                            DocumentReference documentReference = fStore.collection("profile").document(userID);
                            Map<String, Object> profile = new HashMap<>();
                            profile.put("email", email);
                            profile.put("username", username);
                            profile.put("rating", 800);
                            profile.put("userUID", userID);
                            documentReference.set(profile).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "onSuccess: user profile is created for " + userID);
                                }
                            });
                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));

                        } else {
                            // If sign in fails, display a message to the user.

                            Toast.makeText(RegisterActivity.this, R.string.acc_fail, Toast.LENGTH_SHORT).show();

                        }

                    });


        });
    }






}



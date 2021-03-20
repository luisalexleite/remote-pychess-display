package com.example.chess;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.chess.login.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    private static final String TAG = "MainActivity";
    private static final int ERROR_DIALOG_REQUEST = 9001;
    private static ArrayList<String> ids = new ArrayList<>();
    //FireStore instance
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    //boolean to check if user is a PetSitter
    public static Boolean petsitter = true;
    private final CollectionReference petsRef = db.collection("Pet");
    private final CollectionReference perfilRef = db.collection("Perfil");
    Button button_out;
    Button button_perfil;

    //Bottom navigation listener
    @SuppressLint("NonConstantResourceId")


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //get userID
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        final String userID = user.getUid();

        //log out button
        button_out = findViewById(R.id.logout);
        button_out.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(MainActivity.this, "Sign Out Successful", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));

        });

        button_perfil = findViewById(R.id.perfil);

        button_perfil.setOnClickListener(v -> {

            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);

        });


        }
}
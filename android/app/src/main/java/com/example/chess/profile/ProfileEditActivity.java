package com.example.chess.profile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.chess.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileEditActivity extends AppCompatActivity {

    //FireStore instance
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference petsitterRef = db.collection("Petsitter");
    private String TAG = "ProfileActivity";
    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

        //back button
        ImageButton back = findViewById(R.id.backBtnProfileEdit);
        back.setOnClickListener(v -> onBackPressed());


    }
}

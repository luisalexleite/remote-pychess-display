package com.example.chess.profile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.example.chess.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class ProfileActivity extends AppCompatActivity {

    public static final String TAG = "ProfileActivity";

    TextView userName, email, rating;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;
    ImageButton edit_button;
    //FireStore instance
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        userName = findViewById(R.id.profile_name);
        email = findViewById(R.id.email_profile);
        rating = findViewById(R.id.rating);
        edit_button = findViewById(R.id.editButton);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        userID = fAuth.getCurrentUser().getUid();

        DocumentReference documentReference = fStore.collection("profile").document(userID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                        userName.setText(documentSnapshot.getString("username"));
                        email.setText(documentSnapshot.getString("email"));
                        long rating2 = documentSnapshot.getLong("rating");
                        String s = String.valueOf(rating2);
                        rating.setText(s);
                    }
                });
        //back button
        ImageButton back = findViewById(R.id.backBtnProfile);
        back.setOnClickListener(v -> onBackPressed());

        edit_button.setOnClickListener(v -> {
                Intent intent = new Intent(this, ProfileEditActivity.class);
                startActivity(intent);

        });
    }
}

package com.example.chess.profile;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.chess.MainActivity;
import com.example.chess.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class ProfileActivity extends AppCompatActivity {


    TextView userName, email, rating, name;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    StorageReference fStorage;
    String userID;
    ImageButton edit_button;
    ImageView profileImage;
    //FireStore instance
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        userName = findViewById(R.id.profile_name);
        email = findViewById(R.id.email_profile);
        rating = findViewById(R.id.rating);
        name = findViewById(R.id.name_profile);
        edit_button = findViewById(R.id.editButton);
        profileImage = findViewById(R.id.image_profile);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        fStorage = FirebaseStorage.getInstance().getReference();
        userID = fAuth.getCurrentUser().getUid();

        StorageReference profileRef = fStorage.child(userID + ".jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(uri -> Picasso.get().load(uri).into(profileImage));


        DocumentReference documentReference = fStore.collection("profile").document(userID);
        documentReference.addSnapshotListener(this, (documentSnapshot, e) -> {
            String fName = documentSnapshot.getString("fName");
            String lName = documentSnapshot.getString("lName");
            String fullName = fName + " " + lName;
            if(fName != null && lName != null) {
                name.setText(fullName);
            }else if (fName != null) {
                name.setText(fName);
            }else if (lName != null) {
                name.setText(lName);
            }

                userName.setText(documentSnapshot.getString("username"));
                email.setText(documentSnapshot.getString("email"));
                long rating2 = documentSnapshot.getLong("rating");
                String s = String.valueOf(rating2);
                rating.setText(s);
            //}
        });

        //back button
        ImageButton back = findViewById(R.id.backBtnProfile);
        back.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });

        edit_button.setOnClickListener(v -> {
                Intent intent = new Intent(this, ProfileEditActivity.class);
                startActivity(intent);

        });
    }
}

package com.example.chess.profile;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.chess.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import java.util.HashMap;
import java.util.Map;

public class ProfileEditActivity extends AppCompatActivity {

    //EditProfileValues

    private EditText usernameText, fNameText, lNameText;
    private ImageView profileImg;
    private TextView emailText;

    //FireStore instance
    private String imageLink = "";

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    StorageReference fStorage;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        fStorage = FirebaseStorage.getInstance().getReference();

        //Find Views
        emailText = findViewById(R.id.email_edit);
        usernameText = findViewById(R.id.username_edit_profile);
        fNameText = findViewById(R.id.first_name);
        lNameText = findViewById(R.id.lName_profile);
        profileImg = findViewById(R.id.image_profile_edit);
        userID = fAuth.getCurrentUser().getUid();

        StorageReference profileRef = fStorage.child(userID + ".jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(uri -> Picasso.get().load(uri).into(profileImg));

        //back button
        ImageButton back = findViewById(R.id.backBtnProfileEdit);
        ImageButton update = findViewById(R.id.submitButton);
        back.setOnClickListener(v -> onBackPressed());

        DocumentReference dR = fStore.collection("profile").document(userID);
        dR.addSnapshotListener(this, (dR1, error) -> {
            assert dR1 != null;
            emailText.setText(dR1.getString("email"));
            usernameText.setText(dR1.getString("username"));
            fNameText.setText(dR1.getString("fName"));
            lNameText.setText(dR1.getString("lName"));
        });

        profileImg.setOnClickListener(v -> {

            Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(openGalleryIntent, 1000);
        });



        update.setOnClickListener(v -> {

                String email = emailText.getText().toString();
                String username = usernameText.getText().toString();
                String fName = fNameText.getText().toString();
                String lName = lNameText.getText().toString();

                userID = fAuth.getCurrentUser().getUid();
                DocumentReference documentReference = fStore.collection("profile").document(userID);
                if((!email.equals("")) || (!username.equals("")) || (!fName.equals("")) || (!lName.equals(""))){
                    Map<String, Object> profile = new HashMap<>();
                    if(!email.equals("")) {
                        profile.put("email", email);
                    }
                    if(!username.equals("") && username.length() <20) {
                        profile.put("username", username);
                    } else {
                        Toast.makeText(this, R.string.username_long, Toast.LENGTH_SHORT).show();
                    }
                    if(!fName.equals("")) {
                        profile.put("fName", fName);
                    }
                    if(!lName.equals("")) {
                        profile.put("lName", lName);
                    }
                    if(!imageLink.equals("")) {
                        profile.put("image", imageLink);
                    }
                    if( username.length() <20) {
                        documentReference.update(profile);
                    }

                }

                if( username.length() <20) {
                    Intent intent = new Intent(this, ProfileActivity.class);
                    startActivity(intent);
                }
            });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1000){
            if(resultCode == Activity.RESULT_OK && data!=null && data.getData()!=null){
                Uri imageUri = data.getData();
                profileImg.setImageURI(imageUri);
                uploadImageToFirebase(imageUri);

            }
        }
    }

    //Fire base storage
    private void uploadImageToFirebase(Uri imageUri) {
        // upload image to firebase storage
        StorageReference fileRef = fStorage.child(userID + ".jpg");
        fileRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> fileRef.getDownloadUrl().addOnSuccessListener(uri -> imageLink = uri.toString()));
    }
}

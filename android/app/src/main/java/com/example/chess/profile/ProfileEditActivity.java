package com.example.chess.profile;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.chess.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class ProfileEditActivity extends AppCompatActivity {

    //EditProfileValues

    EditText usernameText, emailText, fNameText, lNameText;
    ImageView profileImg;

    //FireStore instance
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference petsitterRef = db.collection("Petsitter");
    private String TAG = "ProfileEditActivity";
    private View view;
    private Uri imageUri;
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
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profileImg);
            }
        });

        //back button
        ImageButton back = findViewById(R.id.backBtnProfileEdit);
        ImageButton update = findViewById(R.id.submitButton);
        back.setOnClickListener(v -> onBackPressed());

        DocumentReference dR = fStore.collection("profile").document(userID);
        dR.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot dR, @Nullable FirebaseFirestoreException error) {
                emailText.setText(dR.getString("email"));
                usernameText.setText(dR.getString("username"));
                fNameText.setText(dR.getString("fName"));
                lNameText.setText(dR.getString("lName"));
            }
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

                FirebaseUser user = fAuth.getCurrentUser();
                userID = fAuth.getCurrentUser().getUid();
                DocumentReference documentReference = fStore.collection("profile").document(userID);
                if((!email.equals("")) || (!username.equals("")) || (!fName.equals("")) || (!lName.equals(""))){
                    Map<String, Object> profile = new HashMap<>();
                    if(!email.equals("")) {
                        profile.put("email", email);
                    }
                    if(!username.equals("")) {
                        profile.put("username", username);
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
                    documentReference.update(profile);
                }

                Intent intent = new Intent(this, ProfileActivity.class);
                startActivity(intent);
            });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1000){
            if(resultCode == Activity.RESULT_OK && data!=null && data.getData()!=null){
                imageUri = data.getData();
                profileImg.setImageURI(imageUri);
                uploadImageToFirebase(imageUri);

            }
        }
    }

    private void uploadImageToFirebase(Uri imageUri) {
        // upload image to firebase storage
        userID = fAuth.getCurrentUser().getUid();
        StorageReference fileRef = fStorage.child(userID + ".jpg");
        fileRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    imageLink = uri.toString();
                });
        });
    }
}

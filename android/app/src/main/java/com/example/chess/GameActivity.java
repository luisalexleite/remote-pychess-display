package com.example.chess;

import android.os.Bundle;
import android.provider.Settings;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.charset.Charset;
import java.util.Random;

public class GameActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    StorageReference fStorage;
    String userID;
    EditText move;
    String username = "a";
    private int i = 0;

    Button teste;
    Button teste2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        fStorage = FirebaseStorage.getInstance().getReference();
        userID = fAuth.getCurrentUser().getUid();




        // Random String
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        int length = 15;
        for(int i = 0; i < length; i++) {
            int index = random.nextInt(alphabet.length());
            char randomChar = alphabet.charAt(index);
            sb.append(randomChar);
        }
        String randomString = sb.toString();

        DocumentReference documentReference = fStore.collection("profile").document(userID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                String username = documentSnapshot.getString("username");

                teste = findViewById(R.id.teste);
                teste.setOnClickListener(v -> {
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference mDatabase = database.getInstance().getReference();

                    //Game
                    mDatabase.child("games").child(randomString).child("state").setValue(0);
                    mDatabase.child("games").child(randomString).child("whites").setValue(userID);
                    mDatabase.child("games").child(randomString).child("type").setValue(0);
                    mDatabase.child("games").child(randomString).child("blacks").setValue("8D038Y1kAVNNcjKuxSJRmdBbRRc2");


                });

            }
            });

        //Moves
        teste2 = findViewById(R.id.teste2);
        teste2.setOnClickListener(v -> {
            move = findViewById(R.id.movein);

            String move2 = move.getText().toString().trim();

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference mDatabase = database.getInstance().getReference();

            mDatabase.child("movements").child(randomString).child(String.valueOf(i = i + 1)).child("move").setValue(move2);
            mDatabase.child("movements").child(randomString).child(String.valueOf(i)).child("state").setValue(0);

        });

        }
    }

package com.example.chess;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Random;

public class GameJoinActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    StorageReference fStorage;
    String userID;
    String username = "a";
    private int i = 0;

    Button teste;
    Button teste2;
    EditText play;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_game);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        fStorage = FirebaseStorage.getInstance().getReference();
        userID = fAuth.getCurrentUser().getUid();

        




        DocumentReference documentReference = fStore.collection("profile").document(userID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                String username = documentSnapshot.getString("username");

                teste = findViewById(R.id.join);
                teste.setOnClickListener(v -> {
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference mDatabase = database.getInstance().getReference();

                    //Game
                    String ola1 = String.valueOf(mDatabase.child("games").get());
                    //String ola = String.valueOf(mDatabase.child("games").get().getResult().getValue());
                    Toast.makeText(GameJoinActivity.this, ola1, Toast.LENGTH_SHORT).show();




                });

            }
            });



        //Moves
        play = findViewById(R.id.play);
        teste2 = findViewById(R.id.teste2);
        teste2.setOnClickListener(v -> {

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference mDatabase = database.getInstance().getReference();
            String plays = play.getText().toString();


        });

        }
    }

package com.example.chess;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Random;

public class JoinActivity extends AppCompatActivity {

    private int i = 0;
    private static final int RESULT_SPEECH = 1;
    ImageButton btnSpeak2;
    TextView tvText2, rating1, rating2;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    StorageReference fStorage;
    FirebaseDatabase database;
    DatabaseReference reference;
    String userID, gameID = "", blacks;
    Button joinGame2, addJogada2, giveup2;
    ImageView profileImageGame, profileImageGame2;


    //Chess Stuff
    String[] pieces = {"K", "N", "Q", "R", "B", "O-O", "O-O-O"
            , "a1", "a2", "a3", "a4", "a5", "a6", "a7", "a8"
            , "b1", "b2", "b3", "b4", "b5", "b6", "b7", "b8"
            , "c1", "c2", "c3", "c4", "c5", "c6", "c7", "c8"
            , "d1", "d2", "d3", "d4", "d5", "d6", "d7", "d8"
            , "e1", "e2", "e3", "e4", "e5", "e6", "e7", "e8"
            , "f1", "f2", "f3", "f4", "f5", "f6", "f7", "f8"
            , "g1", "g2", "g3", "g4", "g5", "g6", "g7", "g8"
            , "h1", "h2", "h3", "h4", "h5", "h6", "h7", "h8" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        fStorage = FirebaseStorage.getInstance().getReference();
        userID = fAuth.getCurrentUser().getUid();

        profileImageGame = findViewById(R.id.image_profile_game);
        profileImageGame2 = findViewById(R.id.image_profile_game2);
        rating1 = findViewById(R.id.rating_game);
        rating2 = findViewById(R.id.rating_game2);

        tvText2 = findViewById(R.id.tvText2);
        btnSpeak2 = findViewById(R.id.btnSpeak2);
        btnSpeak2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "pt-pt");
                try {
                    startActivityForResult(intent, RESULT_SPEECH);
                    tvText2.setText("");
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(getApplicationContext(), "Your device dont support this", Toast.LENGTH_SHORT);
                    e.printStackTrace();
                }
            }
        });

        giveUp();

        ImageButton back = findViewById(R.id.backBtnProfile);
        back.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });

        reference = database.getInstance().getReference().child("game_waiting");
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {


            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                System.out.println(snapshot.getKey());
                gameID = snapshot.getKey();
                joinGame2 = findViewById(R.id.joinGame2);
                joinGame2.setOnClickListener(v -> {
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference mDatabase = database.getInstance().getReference();
                    mDatabase.child("game_waiting").child(gameID).child("state").setValue(0);
                    mDatabase.child("game_waiting").child(gameID).child("blacks").setValue(userID);
                    joinGame2.setVisibility(View.GONE);

                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                    DatabaseReference getid = ref.child("game_waiting").child(gameID).child("whites");
                    getid.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            blacks = dataSnapshot.getValue(String.class);
                            System.out.println("whites: "+ blacks);

                            StorageReference profileRef2 = fStorage.child(blacks + ".jpg");
                            profileRef2.getDownloadUrl().addOnSuccessListener(uri -> {
                                Picasso.get().load(uri).into(profileImageGame2);
                                System.out.println();
                            });
                            Toast.makeText(JoinActivity.this, "Jogo Encontrado, podes dar join!", Toast.LENGTH_SHORT).show();
                            mDatabase.child("games").child(gameID).child("state").setValue(0);
                            mDatabase.child("games").child(gameID).child("type").setValue(0);
                            mDatabase.child("games").child(gameID).child("whites").setValue(blacks);
                            mDatabase.child("games").child(gameID).child("blacks").setValue(userID);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });



                });
                System.out.println(gameID);



            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        StorageReference profileRef = fStorage.child(userID + ".jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(uri -> {
            Picasso.get().load(uri).into(profileImageGame);

        });
    }
    private void giveUp() {
            giveup2 = findViewById(R.id.giveUp2);
            giveup2.setOnClickListener(v -> {

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference mDatabase = database.getInstance().getReference();

                mDatabase.child("games").child(gameID).child("method").setValue(7);
                mDatabase.child("games").child(gameID).child("result").setValue(1);
                mDatabase.child("games").child(gameID).child("state").setValue(2);
            });
        }
    private String addPlay (String result){
            addJogada2 = findViewById(R.id.addJogada2);
            String finalResult = result;
            addJogada2.setOnClickListener(v -> {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference mDatabase = database.getInstance().getReference();
                mDatabase.child("movements").child(gameID).child(String.valueOf(i = i + 2)).child("move").setValue(finalResult);
                mDatabase.child("movements").child(gameID).child(String.valueOf(i)).child("state").setValue(0);

            });
            result = "";
            return result;
        }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode){
            case RESULT_SPEECH:
                if(resultCode == RESULT_OK && data != null){
                    ArrayList<String> text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    String result = text.get(0);
                    result = result.toLowerCase();

                    if (result.contains("rainha")) {
                        result = result.replace("rainha", "Q");
                    } else if (result.contains("dama")) {
                        result = result.replace("dama", "Q");
                    } else if (result.contains("rei")) {
                        result = result.replace("rei", "K");
                    } else if (result.contains("bispo")) {
                        result = result.replace("bispo", "B");
                    } else if (result.contains("cavalo")) {
                        result = result.replace("cavalo", "N");
                    } else if (result.contains("torre")) {
                        result = result.replace("torre", "R");
                    } else if (result.contains("pião")) {
                        result = result.replace("pião", "");
                    } else if (result.contains("peao")) {
                        result = result.replace("peao", "");
                    } else if (result.contains("come")) {
                        result = result.replace("come", "");
                    } else if (result.contains("captura")) {
                        result = result.replace("captura", "");
                    } else if (result.contains("rock rei")) {
                        result = result.replace("rock", "O-O");
                    } if (result.contains("é")) {
                        result = result.replace("é", "e");
                    }
                    if (result.contains("rock Q")) {
                        result = result.replace("rock Q", "O-O-O");
                    }
                    if (result.contains("rock K")) {
                        result = result.replace("rock K", "O-O");
                    }
                    if (result.contains("para")) {
                        result = result.replace("para", "");
                    }

                    result = result.replaceAll("\\s+","");
                    tvText2.setText(text.get(0)  + " -> " + result);

                    for(int j = 0; j < pieces.length; j++) {

                        if(result.contains(pieces[j])){
                            addPlay(result);

                        }
                    }
                }
                break;
        }
    }
}
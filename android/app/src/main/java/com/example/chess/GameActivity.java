package com.example.chess;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.chess.login.LoginActivity;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Random;

public class GameActivity extends AppCompatActivity {

    private static final int RESULT_SPEECH = 1;
    ImageButton btnSpeak;
    TextView tvText;
    private DatabaseReference mDatabase;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    StorageReference fStorage;
    FirebaseDatabase database;
    DatabaseReference reference;
    String userID, randomString, gameID;
    EditText move;
    String username = "a";
    private int i = -1;
    String blacks;
    Button createGame, addJogada, giveup;
    ImageView profileImageGame, profileImageGame2;
    //CheckBox
    CheckBox cbwhite, cbblack;

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
        setContentView(R.layout.activity_game);


        //FireBase
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        fStorage = FirebaseStorage.getInstance().getReference();
        userID = fAuth.getCurrentUser().getUid();
        profileImageGame = findViewById(R.id.image_profile_game);
        profileImageGame2 = findViewById(R.id.image_profile_game2);

        //CheckBox
        cbwhite = findViewById(R.id.checkWhite);
        cbblack = findViewById(R.id.checkBlack);

        // Voice recognition
        tvText = findViewById(R.id.tvText);
        btnSpeak = findViewById(R.id.btnSpeak);
        btnSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "pt-pt");
                try {
                    startActivityForResult(intent, RESULT_SPEECH);
                    tvText.setText("");
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(getApplicationContext(), "Your device dont support this", Toast.LENGTH_SHORT);
                    e.printStackTrace();
                }
            }
        });
        randomString = randomString();
        createGame();
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

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        DatabaseReference getid = ref.child("game_waiting").child(randomString).child("blacks");
        getid.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                blacks = dataSnapshot.getValue(String.class);
                System.out.println("blacks: "+ blacks);

                StorageReference profileRef2 = fStorage.child(blacks + ".jpg");
                profileRef2.getDownloadUrl().addOnSuccessListener(uri -> {
                    Picasso.get().load(uri).into(profileImageGame2);

                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        StorageReference profileRef = fStorage.child(userID + ".jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(uri -> {
            Picasso.get().load(uri).into(profileImageGame);

        });



        }

    //Chess Methods
    private void createGame(){

        createGame = findViewById(R.id.createGame);
        createGame.setOnClickListener(v -> {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference mDatabase = database.getInstance().getReference();

            //Create Game
            if(cbwhite.isChecked() && !cbblack.isChecked()){
                mDatabase.child("game_waiting").child(randomString).child("whites").setValue(userID);
                mDatabase.child("game_waiting").child(randomString).child("type").setValue(0);
                Toast.makeText(GameActivity.this, "Jogo criado com sucesso!", Toast.LENGTH_SHORT).show();
                createGame.setVisibility(View.GONE);
                cbblack.setVisibility(View.GONE);
                cbwhite.setVisibility(View.GONE);
                btnSpeak.setVisibility(View.VISIBLE);
                giveup.setVisibility(View.VISIBLE);
                //addJogada.setVisibility(View.VISIBLE);

            }

            if(cbblack.isChecked() && !cbwhite.isChecked()){
                mDatabase.child("game_waiting").child(randomString).child("blacks").setValue(userID);
                mDatabase.child("game_waiting").child(randomString).child("type").setValue(0);
                Toast.makeText(GameActivity.this, "Jogo criado com sucesso!", Toast.LENGTH_SHORT).show();
                createGame.setVisibility(View.GONE);
                cbblack.setVisibility(View.GONE);
                cbwhite.setVisibility(View.GONE);
                btnSpeak.setVisibility(View.VISIBLE);
                giveup.setVisibility(View.VISIBLE);
                //addJogada.setVisibility(View.VISIBLE);
            }
            if(cbblack.isChecked() && cbwhite.isChecked()){
                Toast.makeText(GameActivity.this, "Escolhe só uma das opções", Toast.LENGTH_SHORT).show();
            }

            if(!cbblack.isChecked() && !cbwhite.isChecked()){
                Toast.makeText(GameActivity.this, "Escolhe uma das opções", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void giveUp(){
        giveup = findViewById(R.id.giveUp);
        giveup.setOnClickListener(v -> {

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference mDatabase = database.getInstance().getReference();

            mDatabase.child("games").child(randomString).child("method").setValue(7);
            mDatabase.child("games").child(randomString).child("result").setValue(1);
            mDatabase.child("games").child(randomString).child("state").setValue(2);
        });
    }
    private String addPlay(String result){
        addJogada = findViewById(R.id.addJogada);
        String finalResult = result;
        addJogada.setOnClickListener(v -> {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference mDatabase = database.getInstance().getReference();
            mDatabase.child("movements").child(randomString).child(String.valueOf(i = i + 2)).child("move").setValue(finalResult);
            mDatabase.child("movements").child(randomString).child(String.valueOf(i)).child("state").setValue(0);

        });
        result = "";
        return result;
    }
    private String randomString(){
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
        randomString = sb.toString();
        return randomString;
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
                    tvText.setText(text.get(0)  + " -> " + result);

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

package com.example.chess;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class JoinActivity extends AppCompatActivity {

    //Initialization
    private int i = 0, moveCount, j = 0, h = 0, m = 0, m2 = 0, state;
    private String userID, gameID = "", blacks, color, color2 = "", ratingIni, ratingFinal;
    private static final int RESULT_SPEECH = 1;

    //Reference Data Base
    private FirebaseFirestore fStore;
    private StorageReference fStorage;
    private DatabaseReference reference;

    //Views
    private Button joinGame2, addJogada2, giveup2, leave;
    private ImageButton btnSpeak2;
    private TextView tvText2, rating1, rating2, ratingJoin1, ratingJoin2, moves2, lista_jogos, lost, win, waitingGame;
    private ImageView profileImageGame, profileImageGame2, cavaloBrancas1, cavaloBrancas2, cavaloPretas1, cavaloPretas2;
    private LinearLayout vs;

    //Chess
    private final String[] pieces = {"K", "N", "Q", "R", "B", "O-O", "O-O-O"
            , "a1", "a2", "a3", "a4", "a5", "a6", "a7", "a8"
            , "b1", "b2", "b3", "b4", "b5", "b6", "b7", "b8"
            , "c1", "c2", "c3", "c4", "c5", "c6", "c7", "c8"
            , "d1", "d2", "d3", "d4", "d5", "d6", "d7", "d8"
            , "e1", "e2", "e3", "e4", "e5", "e6", "e7", "e8"
            , "f1", "f2", "f3", "f4", "f5", "f6", "f7", "f8"
            , "g1", "g2", "g3", "g4", "g5", "g6", "g7", "g8"
            , "h1", "h2", "h3", "h4", "h5", "h6", "h7", "h8" };

    //Main Function
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        //Data Base
        FirebaseAuth fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        fStorage = FirebaseStorage.getInstance().getReference();
        userID = fAuth.getCurrentUser().getUid();

        //Reference of views
        profileImageGame = findViewById(R.id.image_profile_game);
        profileImageGame2 = findViewById(R.id.image_profile_game2);
        giveup2 = findViewById(R.id.giveUp2);
        btnSpeak2 = findViewById(R.id.btnSpeak2);
        addJogada2 = findViewById(R.id.addJogada2);
        joinGame2 = findViewById(R.id.joinGame2);
        ratingJoin1 = findViewById(R.id.rating_join);
        ratingJoin2 = findViewById(R.id.rating_join2);
        rating1 = findViewById(R.id.rating_game);
        rating2 = findViewById(R.id.rating_game2);
        tvText2 = findViewById(R.id.tvText2);
        waitingGame = findViewById(R.id.waiting_game);
        lost = findViewById(R.id.lost);
        win = findViewById(R.id.win);
        vs = findViewById(R.id.layout_v2);
        leave = findViewById(R.id.leave);
        moves2 = findViewById(R.id.movesText2);
        cavaloBrancas1 = findViewById(R.id.cavalo_brancas);
        cavaloBrancas2 = findViewById(R.id.cavalo_brancas2);
        cavaloPretas1 = findViewById(R.id.cavalo_pretas);
        cavaloPretas2 = findViewById(R.id.cavalo_pretas2);
        lista_jogos = findViewById(R.id.lista_jogos);

        //Button Voice recognition
        speak();
        //Give Up Method
        giveUp();
        //Go Back Method
        back();
        //Method Listeners
        listener();
        leave();
    }
    private void listener(){
        reference = FirebaseDatabase.getInstance().getReference().child("game_waiting");
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                gameID = snapshot.getKey();

                if(gameID != null && j == 0){
                    joinGame();
                    movements();
                    j++;
                    joinGame2.setVisibility(View.VISIBLE);
                    vs.setVisibility(View.VISIBLE);
                    waitingGame.setVisibility(View.GONE);
                }
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


    }
    private void movements(){
        reference = FirebaseDatabase.getInstance().getReference().child("movements").child(gameID);
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                moveCount = Integer.parseInt(snapshot.getKey());
                state = Integer.parseInt(snapshot.child("state").getValue().toString());
                if (state == 2) {
                    Toast.makeText(JoinActivity.this, R.string.invalidPlay, Toast.LENGTH_SHORT).show();
                }
                if (state == 1) {
                    //WHITES
                    m++;
                    if(m % 2 == 0 || m == 0){
                        moves2.append(snapshot.child("move").getValue() + " ");
                    } else {
                        m2++;
                        moves2.append(m2 + ". " + snapshot.child("move").getValue() + " ");
                    }
                    if (color2.equals("blacks") && moveCount % 2 == 0) {
                        addJogada2.setVisibility(View.VISIBLE);
                    }
                    if (color2.equals("blacks") && moveCount % 2 != 0) {
                        addJogada2.setVisibility(View.INVISIBLE);
                    }

                    //BLACKS
                    if (color2.equals("whites") && moveCount % 2 != 0) {
                        addJogada2.setVisibility(View.VISIBLE);
                    }
                    if (color2.equals("whites") && moveCount % 2 == 0) {
                        addJogada2.setVisibility(View.INVISIBLE);
                    }
                }
            }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {}
            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }
    private  void oppRatingImage() {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("game_waiting").child(gameID).child("state").setValue(0);
        mDatabase.child("game_waiting").child(gameID).child(color).setValue(userID);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        DatabaseReference getid = ref.child("game_waiting").child(gameID).child(color2);
        getid.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                blacks = dataSnapshot.getValue(String.class);
                if(blacks != null) {

                    StorageReference profileRef2 = fStorage.child(blacks + ".jpg");
                    profileRef2.getDownloadUrl().addOnSuccessListener(uri -> Picasso.get().load(uri).into(profileImageGame2));
                    rating2();
                    vs.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
    private void joinGame(){
        DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference();
        DatabaseReference getid2 = ref2.child("game_waiting").child(gameID);
        getid2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot a : dataSnapshot.getChildren()){
                    if(Objects.equals(a.getKey(), "whites")){
                        color = "blacks";
                        color2 ="whites";
                        endGame();
                        oppRatingImage();

                    }
                    if(Objects.equals(a.getKey(), "blacks")){
                        color = "whites";
                        color2 = "blacks";
                        endGame();
                        oppRatingImage();
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        joinGame2.setOnClickListener(v -> {
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
            joinGame2.setVisibility(View.GONE);
            btnSpeak2.setVisibility(View.VISIBLE);
            giveup2.setVisibility(View.VISIBLE);
            lista_jogos.setVisibility(View.GONE);
            StorageReference profileRef = fStorage.child(userID + ".jpg");
            profileRef.getDownloadUrl().addOnSuccessListener(uri -> Picasso.get().load(uri).into(profileImageGame));
            mDatabase.child("games").child(gameID).child("state").setValue(0);
            mDatabase.child("games").child(gameID).child("type").setValue(0);
            mDatabase.child("games").child(gameID).child(color2).setValue(blacks);
            mDatabase.child("games").child(gameID).child(color).setValue(userID);
            rating();
            if(color2.equals("whites")){
                addJogada2.setVisibility(View.INVISIBLE);
                cavaloPretas1.setVisibility(View.VISIBLE);
                cavaloBrancas2.setVisibility(View.VISIBLE);
            }

            if(color2.equals("blacks")){
                addJogada2.setVisibility(View.VISIBLE);
                cavaloBrancas1.setVisibility(View.VISIBLE);
                cavaloPretas2.setVisibility(View.VISIBLE);
            }
        });
    }
    private void addPlay (String result){

        addJogada2.setOnClickListener(v -> {
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
            //Whites
            if((color2.equals("blacks"))) {
                if(i == 1){
                    mDatabase.child("movements").child(gameID).child(String.valueOf(i + 1)).child("move").setValue(result);
                    mDatabase.child("movements").child(gameID).child(String.valueOf(i + 1)).child("state").setValue(0);
                    addJogada2.setVisibility(View.INVISIBLE);
                    i++;
                } else {
                    mDatabase.child("movements").child(gameID).child(String.valueOf(moveCount + 1)).child("move").setValue(result);
                    mDatabase.child("movements").child(gameID).child(String.valueOf(moveCount + 1)).child("state").setValue(0);
                }
            }
            //Blacks
            if ((color2.equals("whites"))) {
                if(i == 1){
                    addJogada2.setVisibility(View.INVISIBLE);
                    i++;
                } else {
                    mDatabase.child("movements").child(gameID).child(String.valueOf(moveCount + 1)).child("move").setValue(result);
                    mDatabase.child("movements").child(gameID).child(String.valueOf(moveCount + 1)).child("state").setValue(0);
                }
            }
        });
    }
    //Less trouble methods
    private void rating(){
        DocumentReference documentReference = fStore.collection("profile").document(userID);
        documentReference.addSnapshotListener(this, (documentSnapshot, e) -> {

            ratingJoin1.setText(documentSnapshot.getString("username"));
            long rating3 = documentSnapshot.getLong("rating");
            ratingIni = String.valueOf(rating3);
            rating1.setText(ratingIni);
            if(h == 0) {
                ratingFinal = ratingIni;
                h++;
            }
        });
    }
    private void rating2(){
        DocumentReference documentReference = fStore.collection("profile").document(blacks);
        documentReference.addSnapshotListener(this, (documentSnapshot, e) -> {

            ratingJoin2.setText(documentSnapshot.getString("username"));
            long rating3 = documentSnapshot.getLong("rating");
            String s = String.valueOf(rating3);
            rating2.setText(s);
        });
    }
    private void back(){
        ImageButton back = findViewById(R.id.backBtnProfile);
        back.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            finish();
            startActivity(intent);
        });
    }
    private void speak(){
        btnSpeak2.setOnClickListener(v -> {
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "pt-pt");
            try {
                startActivityForResult(intent, RESULT_SPEECH);
                tvText2.setText("");
            } catch (ActivityNotFoundException e) {
                Toast.makeText(getApplicationContext(), R.string.NotSuport, Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        });
    }
    private void giveUp() {
            giveup2.setOnClickListener(v -> {
                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                //blacks
                if(color2.equals("whites")){
                    mDatabase.child("games").child(gameID).child("method").setValue(7);
                    mDatabase.child("games").child(gameID).child("result").setValue(1);
                    mDatabase.child("games").child(gameID).child("state").setValue(2);
                }
                //whites
                if(color2.equals("blacks")){
                    mDatabase.child("games").child(gameID).child("method").setValue(7);
                    mDatabase.child("games").child(gameID).child("result").setValue(3);
                    mDatabase.child("games").child(gameID).child("state").setValue(2);
                }
            });
        }
    private void leave(){
        leave.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });
    }
    private void endGame() {
        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference().child("games").child(gameID);
        reference1.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String prevChildKey) {
                if (!color2.equals("") && dataSnapshot.getValue() != null) {
                    if (color2.equals("blacks")) {
                        if (dataSnapshot.getValue().toString().equals("3")) {
                            endGameRating(getString(R.string.rating_down), getString(R.string.lost));
                        }
                        if (dataSnapshot.getValue().toString().equals("1")) {
                            endGameRating(getString(R.string.rating_up), getString(R.string.won));
                        }
                    }
                    if (color2.equals("whites")) {
                        if (dataSnapshot.getValue().toString().equals("1")) {
                            endGameRating(getString(R.string.rating_down), getString(R.string.lost));

                        }
                        if (dataSnapshot.getValue().toString().equals("3")) {
                            endGameRating(getString(R.string.rating_up), getString(R.string.won));
                        }
                    }
                }
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, String prevChildKey) {}
            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {}
            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, String prevChildKey) {}
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }
    private void endGameRating(String rating, String won) {
            win.setText(won);
            vs.setVisibility(View.GONE);
            addJogada2.setVisibility(View.GONE);
            btnSpeak2.setVisibility(View.GONE);
            giveup2.setVisibility(View.GONE);
            tvText2.setVisibility(View.GONE);
            moves2.setVisibility(View.GONE);
            leave.setVisibility(View.VISIBLE);
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
            DocumentReference docRef= fStore.collection("profile").document(userID);
            docRef.get().addOnCompleteListener(task -> {
                String ratingFim = task.getResult().getLong("rating").toString();
                lost.setText(String.format("%s%s --> %s", rating, ratingFinal, ratingFim));
            });
            mDatabase.child("game_waiting").child(gameID).removeValue();
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }

            docRef.get().addOnCompleteListener(task -> {
                String ratingFim = task.getResult().getLong("rating").toString();
                lost.setText(String.format("%s%s --> %s", rating, ratingFinal, ratingFim));
            });
        }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_SPEECH) {
            if (resultCode == RESULT_OK && data != null) {
                ArrayList<String> text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                String result = text.get(0);
                result = result.toLowerCase();

                String[] moves = new String[]{"para", "um", "dois", "tres", "quatro", "cinco", "seis", "sete", "oito", "rainha", "dama", "dame", "dá-me", "rei", "ruca", "hey", "rock Q", "rock K",
                        "bispo", "cavalo", "torre", "pião", "peao", "come", "captura", "rock", "é", "s", "gt", "ver", "de", "há", "vê", "v", "cenas", "se", "sê",
                        "serie", "cr7", "de", "dia", "gp", "guê", "gt", "jean", "gta", "já", "set", "je", "gê", "hahaha", "noite", "quarto", "guia", "h2o"};

                String[] movesReplace = new String[]{"", "1", "2", "3", "4", "5", "6", "7", "8", "Q", "Q", "Q", "Q", "K", "K", "K", "O-O-O", "O-O",
                        "B", "N", "R", "", "", "", "", "O-O", "e", "e", "g", "d", "d", "a", "b", "b", "c2", "c", "c",
                        "c", "c7", "d", "g", "g ", "g  ", "g", "g1", "g  ", "g ", "7  ", "g ", "g ", "h     ", "8", "4", "g", "h2"};
                for (int g = 0; g < moves.length; g++) {
                    if (result.contains(moves[g])) {
                        result = result.replace(moves[g], movesReplace[g]);
                    }
                }
                result = result.replaceAll("\\s+", "");
                tvText2.setText(result);
                for (String piece : pieces) {
                    if (result.contains(piece)) {
                        addPlay(result);
                    }
                }
            }
        }
    }
}

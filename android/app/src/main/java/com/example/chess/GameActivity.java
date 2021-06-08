package com.example.chess;

import android.app.Activity;
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
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.chess.login.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class GameActivity extends AppCompatActivity {

    //Inicialização de variáveis
    private static final int RESULT_SPEECH = 1;
    String userID, randomString, gameID, blacks, color2="", ratingFinal, ratingIni;
    int i = 0, j = 0, h = 0, m = 0, m2 = 0, state;
    int count = 0, moveCount;

    //Base de dados
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    StorageReference fStorage;
    FirebaseDatabase database;
    DatabaseReference reference;


    //Inicialização de views
    ImageButton btnSpeak;
    TextView tvText, rating1, rating2, username1, username2, espera, moves, lost, win;
    Button createGame, addJogada, giveup, leave;
    ImageView profileImageGame, profileImageGame2, cavaloBrancas1, cavaloBrancas2, cavaloPretas1, cavaloPretas2;
    RadioButton cbwhite, cbblack, cbrandom;
    LinearLayout vs;

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

        //Imagens
        profileImageGame = findViewById(R.id.image_profile_game);
        profileImageGame2 = findViewById(R.id.image_profile_game2);

        //CheckBox
        cbwhite = findViewById(R.id.checkWhite);
        cbblack = findViewById(R.id.checkBlack);
        cbrandom = findViewById(R.id.checkRandom);

        btnSpeak = findViewById(R.id.btnSpeak);
        addJogada = findViewById(R.id.addJogada);
        rating1 = findViewById(R.id.rating_game12);
        rating2 = findViewById(R.id.rating_game22);
        username1 = findViewById(R.id.rating_game11);
        username2 = findViewById(R.id.rating_game21);
        espera = findViewById(R.id.espera);
        vs = findViewById(R.id.layout_vs1);
        tvText = findViewById(R.id.tvText);
        lost = findViewById(R.id.lost);
        win = findViewById(R.id.win);
        leave = findViewById(R.id.leave);
        moves = findViewById(R.id.movesText);
        cavaloBrancas1 = findViewById(R.id.cavalo_brancas);
        cavaloBrancas2 = findViewById(R.id.cavalo_brancas2);
        cavaloPretas1 = findViewById(R.id.cavalo_pretas);
        cavaloPretas2 = findViewById(R.id.cavalo_pretas2);

        randomString = randomString();
        createGame();
        listener();
        speak();
        movements();
        giveUp();
        back();
        rating();
        endGame();
        leave();


    }

    private void listener(){
        reference = database.getInstance().getReference().child("game_waiting");
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                System.out.println(snapshot.getKey());
                gameID = snapshot.getKey();
                j = 1;
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                DatabaseReference getid = ref.child("game_waiting").child(randomString).child(color2);;
                getid.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        blacks = dataSnapshot.getValue(String.class);

                        if(blacks != null){
                            System.out.println("GameActivity: "+ blacks);
                            StorageReference profileRef = fStorage.child(blacks + ".jpg");
                            profileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                                Picasso.get().load(uri).into(profileImageGame2);
                            });

                            if(color2.equals("whites")){
                                cavaloPretas1.setVisibility(View.VISIBLE);
                                cavaloBrancas2.setVisibility(View.VISIBLE);
                            }
                            if(color2.equals("blacks")){
                                cavaloBrancas1.setVisibility(View.VISIBLE);
                                cavaloPretas2.setVisibility(View.VISIBLE);
                                addJogada.setVisibility(View.VISIBLE);
                            }

                            rating2();

                            vs.setVisibility(View.VISIBLE);

                            btnSpeak.setVisibility(View.VISIBLE);
                            giveup.setVisibility(View.VISIBLE);
                            espera.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
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

    private void rating(){
        DocumentReference documentReference = fStore.collection("profile").document(userID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                username1.setText(documentSnapshot.getString("username"));
                long rating3 = documentSnapshot.getLong("rating");
                ratingIni = String.valueOf(rating3);
                rating1.setText(ratingIni);
                if (h == 0){
                    ratingFinal = ratingIni;
                    h++;
                }
            }
        });

    }
    private void rating2(){
        DocumentReference documentReference = fStore.collection("profile").document(blacks);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                username2.setText(documentSnapshot.getString("username"));
                long rating3 = documentSnapshot.getLong("rating");
                String s = String.valueOf(rating3);
                rating2.setText(s);
                //}
            }
        });

    }
    private void back(){
        ImageButton back = findViewById(R.id.backBtnProfile);
        back.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);

        });

    }
    private void speak(){
        // Voice recognition


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
    }
    //Chess Methods
    private void createGame(){

        createGame = findViewById(R.id.createGame);
        createGame.setOnClickListener(v -> {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference mDatabase = database.getInstance().getReference();

            //Create Game
            if(cbwhite.isChecked()){
                mDatabase.child("game_waiting").child(randomString).child("whites").setValue(userID);
                mDatabase.child("game_waiting").child(randomString).child("type").setValue(0);
                Toast.makeText(GameActivity.this, "Jogo criado com sucesso!", Toast.LENGTH_SHORT).show();
                color2 = "blacks";
                cbwhite.setVisibility(View.GONE);
                cbblack.setVisibility(View.GONE);
                cbrandom.setVisibility(View.GONE);
                createGame.setVisibility(View.GONE);
                espera.setVisibility(View.VISIBLE);
                vs.setVisibility(View.VISIBLE);
                addJogada.setVisibility(View.GONE);
            }

            if(cbblack.isChecked()){
                mDatabase.child("game_waiting").child(randomString).child("blacks").setValue(userID);
                mDatabase.child("game_waiting").child(randomString).child("type").setValue(0);
                Toast.makeText(GameActivity.this, "Jogo criado com sucesso!", Toast.LENGTH_SHORT).show();
                color2 = "whites";
                cbwhite.setVisibility(View.GONE);
                cbblack.setVisibility(View.GONE);
                cbrandom.setVisibility(View.GONE);
                createGame.setVisibility(View.GONE);
                espera.setVisibility(View.VISIBLE);
                vs.setVisibility(View.VISIBLE);
                addJogada.setVisibility(View.GONE);
            }

            if(cbrandom.isChecked()){
                Toast.makeText(GameActivity.this, "Jogo criado com sucesso!", Toast.LENGTH_SHORT).show();
                Random cor = new Random();
                int rnd = cor.nextInt(2);
                if(rnd == 1){
                    color2 = "whites";
                    mDatabase.child("game_waiting").child(randomString).child("blacks").setValue(userID);
                    mDatabase.child("game_waiting").child(randomString).child("type").setValue(0);
                    addJogada.setVisibility(View.GONE);
                } else {
                    color2 = "blacks";
                    mDatabase.child("game_waiting").child(randomString).child("whites").setValue(userID);
                    mDatabase.child("game_waiting").child(randomString).child("type").setValue(0);
                    addJogada.setVisibility(View.GONE);
                }

                cbwhite.setVisibility(View.GONE);
                cbblack.setVisibility(View.GONE);
                cbrandom.setVisibility(View.GONE);
                createGame.setVisibility(View.GONE);
                espera.setVisibility(View.VISIBLE);
                vs.setVisibility(View.VISIBLE);
            }


        });
    }
    private void giveUp() {
        giveup = findViewById(R.id.giveUp);
        giveup.setOnClickListener(v -> {

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference mDatabase = database.getInstance().getReference();


            //blacks
            if (color2.equals("whites")) {
                mDatabase.child("games").child(randomString).child("method").setValue(7);
                mDatabase.child("games").child(randomString).child("result").setValue(1);
                mDatabase.child("games").child(randomString).child("state").setValue(2);
            }

            //whites
            if (color2.equals("blacks")) {
                mDatabase.child("games").child(randomString).child("method").setValue(7);
                mDatabase.child("games").child(randomString).child("result").setValue(3);
                mDatabase.child("games").child(randomString).child("state").setValue(2);

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
        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference().child("games").child(randomString);
        reference1.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                if (!color2.equals("") && dataSnapshot.getValue() != null) {

                    if (color2.equals("blacks")) {
                        if (dataSnapshot.getValue().toString().equals("3")) {
                            win.setText("Perdeste");
                            vs.setVisibility(View.GONE);
                            addJogada.setVisibility(View.GONE);
                            btnSpeak.setVisibility(View.GONE);
                            giveup.setVisibility(View.GONE);
                            tvText.setVisibility(View.GONE);
                            moves.setVisibility(View.GONE);
                            leave.setVisibility(View.VISIBLE);

                            try {
                                TimeUnit.SECONDS.sleep(2);
                            } catch (InterruptedException interruptedException) {
                                interruptedException.printStackTrace();
                            }

                            DocumentReference docRef= fStore.collection("profile").document(userID);
                            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    String ratingFim = task.getResult().getLong("rating").toString();
                                    System.out.println(ratingFim);
                                    lost.setText("Rating desceu de " + ratingFinal+ " --> " + ratingFim);
                                }
                            });


                            mDatabase.child("game_waiting").child(gameID).removeValue();
                            //mDatabase.child("games").child(gameID).removeValue();

                        }
                        if (dataSnapshot.getValue().toString().equals("1")) {
                            win.setText("Ganhaste");
                            vs.setVisibility(View.GONE);
                            addJogada.setVisibility(View.GONE);
                            btnSpeak.setVisibility(View.GONE);
                            giveup.setVisibility(View.GONE);
                            tvText.setVisibility(View.GONE);
                            moves.setVisibility(View.GONE);
                            leave.setVisibility(View.VISIBLE);

                            try {
                                TimeUnit.SECONDS.sleep(2);
                            } catch (InterruptedException interruptedException) {
                                interruptedException.printStackTrace();
                            }
                            DocumentReference docRef= fStore.collection("profile").document(userID);
                            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    String ratingFim = task.getResult().getLong("rating").toString();
                                    System.out.println(ratingFim);
                                    lost.setText("Rating subiu de " + ratingFinal+ " --> " + ratingFim);
                                }
                            });
                            mDatabase.child("game_waiting").child(gameID).removeValue();
                            //mDatabase.child("games").child(gameID).removeValue();
                        }

                    }


                    if (color2.equals("whites")) {
                        if (dataSnapshot.getValue().toString().equals("1")) {
                            win.setText("Perdeste");
                            vs.setVisibility(View.GONE);
                            addJogada.setVisibility(View.GONE);
                            btnSpeak.setVisibility(View.GONE);
                            giveup.setVisibility(View.GONE);
                            tvText.setVisibility(View.GONE);
                            moves.setVisibility(View.GONE);
                            leave.setVisibility(View.VISIBLE);

                            try {
                                TimeUnit.SECONDS.sleep(2);
                            } catch (InterruptedException interruptedException) {
                                interruptedException.printStackTrace();
                            }
                            DocumentReference docRef= fStore.collection("profile").document(userID);
                            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    String ratingFim = task.getResult().getLong("rating").toString();
                                    System.out.println(ratingFim);
                                    lost.setText("Rating desceu de " + ratingFinal+ " --> " + ratingFim);
                                }
                            });

                            mDatabase.child("game_waiting").child(gameID).removeValue();
                            //mDatabase.child("games").child(gameID).removeValue();

                        }
                        if (dataSnapshot.getValue().toString().equals("3")) {
                            win.setText("Ganhaste");
                            vs.setVisibility(View.GONE);
                            addJogada.setVisibility(View.GONE);
                            btnSpeak.setVisibility(View.GONE);
                            giveup.setVisibility(View.GONE);
                            tvText.setVisibility(View.GONE);
                            moves.setVisibility(View.GONE);
                            leave.setVisibility(View.VISIBLE);

                            try {
                                TimeUnit.SECONDS.sleep(2);
                            } catch (InterruptedException interruptedException) {
                                interruptedException.printStackTrace();
                            }
                            DocumentReference docRef= fStore.collection("profile").document(userID);
                            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    String ratingFim = task.getResult().getLong("rating").toString();
                                    System.out.println(ratingFim);
                                    lost.setText("Rating subiu de " + ratingFinal+ " --> " + ratingFim);
                                }
                            });
                            mDatabase.child("game_waiting").child(gameID).removeValue();
                            //mDatabase.child("games").child(gameID).removeValue();
                        }
                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String prevChildKey) {}

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

    }
    private void movements(){
        reference = FirebaseDatabase.getInstance().getReference().child("movements").child(randomString);
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {



            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                moveCount = Integer.parseInt(snapshot.getKey());
                System.out.println("move" + moveCount);

                state = Integer.parseInt(snapshot.child("state").getValue().toString());
                String move = snapshot.child("move").getValue().toString();
                if (state == 2 && !move.equals("err")) {
                    Toast.makeText(GameActivity.this, "Jogada Inválida!", Toast.LENGTH_SHORT).show();
                    DatabaseReference mDatabase = database.getInstance().getReference();
                    mDatabase.child("movements").child(randomString).child(String.valueOf(moveCount + 1)).child("move").setValue("err");
                    mDatabase.child("movements").child(randomString).child(String.valueOf(moveCount + 1)).child("state").setValue(0);
                }
                if (state == 1) {
                    //WHITES
                    m++;
                    if(m % 2 == 0 || m == 0){
                        moves.append(snapshot.child("move").getValue() + " ");
                    } else {
                        m2++;
                        moves.append(m2 + ". " + snapshot.child("move").getValue() + " ");

                    }
                    if (color2.equals("blacks") && moveCount % 2 == 0) {
                        addJogada.setVisibility(View.VISIBLE);
                    }
                    if (color2.equals("blacks") && moveCount % 2 != 0) {
                        addJogada.setVisibility(View.INVISIBLE);
                    }

                    //BLACKS
                    if (color2.equals("whites") && moveCount % 2 != 0) {
                        addJogada.setVisibility(View.VISIBLE);
                    }
                    if (color2.equals("whites") && moveCount % 2 == 0) {
                        addJogada.setVisibility(View.INVISIBLE);
                    }
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
    private String addPlay(String result){

        String finalResult = result;


        addJogada.setOnClickListener(v -> {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference mDatabase = database.getInstance().getReference();

            //Whites

            if ((color2.equals("blacks"))) {
                if(i == 1){
                    mDatabase.child("movements").child(randomString).child(String.valueOf(i + 1)).child("move").setValue(finalResult);
                    mDatabase.child("movements").child(randomString).child(String.valueOf(i + 1)).child("state").setValue(0);
                    addJogada.setVisibility(View.INVISIBLE);
                    i++;
                } else {
                    mDatabase.child("movements").child(randomString).child(String.valueOf(moveCount + 1)).child("move").setValue(finalResult);
                    mDatabase.child("movements").child(randomString).child(String.valueOf(moveCount + 1)).child("state").setValue(0);
                }
            }

                //Blacks
            if((color2.equals("whites"))) {
                mDatabase.child("movements").child(randomString).child(String.valueOf(moveCount + 1)).child("move").setValue(finalResult);
                mDatabase.child("movements").child(randomString).child(String.valueOf(moveCount + 1)).child("state").setValue(0);

            }
        });
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
                    } if (result.contains("hey")) {
                        result = result.replace("hey", "K");
                    } if (result.contains("rock Q")) {
                        result = result.replace("rock Q", "O-O-O");
                    } if (result.contains("rock K")) {
                        result = result.replace("rock K", "O-O");
                    } if (result.contains("para")) {
                        result = result.replace("para", "");
                    } if (result.contains("hi5")) {
                        result = result.replace("hi5", "h5");
                    }


                    result = result.replaceAll("\\s+","");
                    tvText.setText(result);

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

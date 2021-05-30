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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class JoinActivity extends AppCompatActivity {

    //Inicialização de variáveis
    int i = 0, moveCount, j = 0, state;
    String userID;
    String gameID = "";
    String blacks;
    String whites;
    String color;
    String color2;
    View moves;
    private static final int RESULT_SPEECH = 1;

    //Base de dados
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    StorageReference fStorage;
    FirebaseDatabase database;
    DatabaseReference reference;

    //Views
    Button joinGame2, addJogada2, giveup2;
    ImageButton btnSpeak2;
    TextView tvText2, rating1, rating2, ratingJoin1, ratingJoin2, moves2, lista_jogos;
    ImageView profileImageGame, profileImageGame2, cavaloBrancas1, cavaloBrancas2, cavaloPretas1, cavaloPretas2;
    LinearLayout vs;

    //Chess
    String[] pieces = {"K", "N", "Q", "R", "B", "O-O", "O-O-O"
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

        //Referencias da Base de dados
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        fStorage = FirebaseStorage.getInstance().getReference();
        userID = fAuth.getCurrentUser().getUid();

        //Referencias de views
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
        vs = findViewById(R.id.layout_v2);
        moves2 = findViewById(R.id.movesText2);
        cavaloBrancas1 = findViewById(R.id.cavalo_brancas);
        cavaloBrancas2 = findViewById(R.id.cavalo_brancas2);
        cavaloPretas1 = findViewById(R.id.cavalo_pretas);
        cavaloPretas2 = findViewById(R.id.cavalo_pretas2);
        lista_jogos = findViewById(R.id.lista_jogos);

        //Método ao clicar botão Voice recognition
        speak();
        //Método ao Clicar botão Desistir
        giveUp();
        //Metodo ao Clicar para trás
        back();
        rating();
        //Metodo para os listeners
        listener();

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

                if(j == 0){
                    joinGame(gameID);
                    joinGame2.setVisibility(View.VISIBLE);
                    movements();
                    j++;
                }
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
    private void movements(){
        reference = FirebaseDatabase.getInstance().getReference().child("movements").child(gameID);
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                moveCount = Integer.parseInt(snapshot.getKey());
                System.out.println("move" + moveCount);

                state = Integer.parseInt(snapshot.child("state").getValue().toString());
                if (state == 2) {
                    Toast.makeText(JoinActivity.this, "Jogada Inválida!", Toast.LENGTH_SHORT).show();
                }
                if (state == 1) {
                    //WHITES
                    moves2.append(moveCount + ". " + snapshot.child("move").getValue() + ", ");
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
    private void joinGame(String GameID){

        DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference();
        DatabaseReference getid2 = ref2.child("game_waiting").child(gameID);
        getid2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot a : dataSnapshot.getChildren()){
                    System.out.println("valor: " + a.getKey());
                    if(Objects.equals(a.getKey(), "whites")){
                        color = "blacks";
                        color2 ="whites";
                    }
                    if(Objects.equals(a.getKey(), "blacks")){
                        color = "whites";
                        color2 = "blacks";
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        joinGame2.setOnClickListener(v -> {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference mDatabase = database.getInstance().getReference();
            mDatabase.child("game_waiting").child(gameID).child("state").setValue(0);
            mDatabase.child("game_waiting").child(gameID).child(color).setValue(userID);
            joinGame2.setVisibility(View.GONE);
            btnSpeak2.setVisibility(View.VISIBLE);
            giveup2.setVisibility(View.VISIBLE);
            lista_jogos.setVisibility(View.GONE);
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

            DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
            DatabaseReference getid = ref.child("game_waiting").child(gameID).child(color2);
            getid.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    blacks = dataSnapshot.getValue(String.class);
                    System.out.println("joinActivity: "+ blacks);

                    StorageReference profileRef2 = fStorage.child(blacks + ".jpg");
                    profileRef2.getDownloadUrl().addOnSuccessListener(uri -> {
                        Picasso.get().load(uri).into(profileImageGame2);
                        System.out.println();
                    });
                    Toast.makeText(JoinActivity.this, "Jogo Encontrado!", Toast.LENGTH_SHORT).show();
                    mDatabase.child("games").child(gameID).child("state").setValue(0);
                    mDatabase.child("games").child(gameID).child("type").setValue(0);
                    mDatabase.child("games").child(gameID).child(color2).setValue(blacks);
                    mDatabase.child("games").child(gameID).child(color).setValue(userID);

                    rating2();
                    vs.setVisibility(View.VISIBLE);
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        });

    }
    private String addPlay (String result){

        String finalResult = result;
        addJogada2.setOnClickListener(v -> {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference mDatabase = database.getInstance().getReference();

            //Whites
            if((color2.equals("blacks"))) {
                if(i == 1){
                    mDatabase.child("movements").child(gameID).child(String.valueOf(i + 1)).child("move").setValue(finalResult);
                    mDatabase.child("movements").child(gameID).child(String.valueOf(i + 1)).child("state").setValue(0);
                    addJogada2.setVisibility(View.INVISIBLE);
                    i++;
                } else {
                    mDatabase.child("movements").child(gameID).child(String.valueOf(moveCount + 1)).child("move").setValue(finalResult);
                    mDatabase.child("movements").child(gameID).child(String.valueOf(moveCount + 1)).child("state").setValue(0);
                }

            }
            //Blacks
            if ((color2.equals("whites"))) {
                if(i == 1){
                    addJogada2.setVisibility(View.INVISIBLE);
                    i++;
                } else {
                    mDatabase.child("movements").child(gameID).child(String.valueOf(moveCount + 1)).child("move").setValue(finalResult);
                    mDatabase.child("movements").child(gameID).child(String.valueOf(moveCount + 1)).child("state").setValue(0);
                }

            }


        });
        return result;
    }

    //Less trouble methods
    private void rating(){
        DocumentReference documentReference = fStore.collection("profile").document(userID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                ratingJoin1.setText(documentSnapshot.getString("username"));
                long rating3 = documentSnapshot.getLong("rating");
                String s = String.valueOf(rating3);
                rating1.setText(s);
                //}
            }
        });

    }
    private void rating2(){
        DocumentReference documentReference = fStore.collection("profile").document(blacks);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                ratingJoin2.setText(documentSnapshot.getString("username"));
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
    }
    private void giveUp() {

            giveup2.setOnClickListener(v -> {

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference mDatabase = database.getInstance().getReference();

                //blacks
                if(color2.equals("whites")){
                    mDatabase.child("games").child(gameID).child("method").setValue(7);
                    mDatabase.child("games").child(gameID).child("result").setValue(3);
                    mDatabase.child("games").child(gameID).child("state").setValue(2);
                }

                //whites
                if(color2.equals("blacks")){
                    mDatabase.child("games").child(gameID).child("method").setValue(7);
                    mDatabase.child("games").child(gameID).child("result").setValue(1);
                    mDatabase.child("games").child(gameID).child("state").setValue(2);
                }
            });
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

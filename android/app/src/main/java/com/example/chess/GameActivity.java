package com.example.chess;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
    String userID, randomString;
    EditText move;
    String username = "a";
    private int i = 0;

    Button createGame, addJogada, giveup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        fStorage = FirebaseStorage.getInstance().getReference();
        userID = fAuth.getCurrentUser().getUid();

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



        createGame = findViewById(R.id.createGame);
        createGame.setOnClickListener(v -> {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference mDatabase = database.getInstance().getReference();

            //Create Game
            mDatabase.child("games").child(randomString).child("state").setValue(0);
            mDatabase.child("games").child(randomString).child("whites").setValue(userID);
            mDatabase.child("games").child(randomString).child("type").setValue(0);
            mDatabase.child("games").child(randomString).child("blacks").setValue("8D038Y1kAVNNcjKuxSJRmdBbRRc2");
            });

        giveup = findViewById(R.id.giveUp);
        giveup.setOnClickListener(v -> {

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference mDatabase = database.getInstance().getReference();

            mDatabase.child("games").child(randomString).child("method").setValue(7);
            mDatabase.child("games").child(randomString).child("result").setValue(1);
            mDatabase.child("games").child(randomString).child("state").setValue(2);
            });


        ImageButton back = findViewById(R.id.backBtnProfile);
        back.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });

        }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode){
            case RESULT_SPEECH:
                if(resultCode == RESULT_OK && data != null){
                    ArrayList<String> text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    tvText.setText(text.get(0));
                    String result = text.get(0);

                    addJogada = findViewById(R.id.addJogada);
                    addJogada.setOnClickListener(v -> {
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference mDatabase = database.getInstance().getReference();
                        mDatabase.child("movements").child(randomString).child(String.valueOf(i = i + 1)).child("move").setValue(result);
                        mDatabase.child("movements").child(randomString).child(String.valueOf(i)).child("state").setValue(0);

                    });
                }
                break;
        }
    }
}

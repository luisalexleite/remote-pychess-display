package com.example.chess;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class GameOptionsActivity extends AppCompatActivity {

    String cbCheck = "";
    RadioButton cbwhite, cbblack, cbrandom;
    Button createGame;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gameoptions);

        back();
        //Switch
        cbwhite = findViewById(R.id.checkWhite);
        cbblack = findViewById(R.id.checkBlack);
        cbrandom = findViewById(R.id.checkRandom);
        createGame = findViewById(R.id.createGame);
            createGame.setOnClickListener(v -> {
                if(cbwhite.isChecked() || cbblack.isChecked() || cbrandom.isChecked()) {
                    cbCheck = isChecked();
                    Intent intent = new Intent(this, GameActivity.class);
                    intent.putExtra("checked", cbCheck);
                    finish();
                    startActivity(intent);
                }
            });
        }




    public String isChecked () {
        if(cbwhite.isChecked()){
            cbCheck = "cbWhite";
        }
        if(cbblack.isChecked()){
            cbCheck = "cbBlack";
        }
        if(cbrandom.isChecked()){
            cbCheck = "cbRandom";
        }
        return cbCheck;
    }
    public void back(){
        ImageButton back = findViewById(R.id.backBtnProfile);
        back.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            finish();
            startActivity(intent);

        });

    }
}

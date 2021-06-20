package com.example.chess;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class GameOptionsActivity extends AppCompatActivity {

    String cbCheckColor = "", cbCheckTime = "";
    RadioButton cbwhite, cbblack, cbrandom, cbblitz, cbnormal, cbrapido;
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

        cbblitz = findViewById(R.id.checkBlitz);
        cbrapido = findViewById(R.id.checkRapido);
        cbnormal = findViewById(R.id.checkNormal);

        createGame = findViewById(R.id.createGame);
            createGame.setOnClickListener(v -> {
                cbCheckColor = isCheckedColor();
                cbCheckTime = isCheckedTime();
                Intent intent = new Intent(this, GameActivity.class);
                intent.putExtra("checkedColor", cbCheckColor);
                intent.putExtra("checkedTime", cbCheckTime);
                finish();
                startActivity(intent);

            });
        }




    public String isCheckedColor () {
        if(cbwhite.isChecked()){
            cbCheckColor = "cbWhite";
        }
        if(cbblack.isChecked()){
            cbCheckColor = "cbBlack";
        }
        if(cbrandom.isChecked()){
            cbCheckColor = "cbRandom";
        }
        return cbCheckColor;
    }

    public String isCheckedTime () {
        if(cbblitz.isChecked()){
            cbCheckTime = "cbBlitz";
        }
        if(cbnormal.isChecked()){
            cbCheckTime = "cbNormal";
        }
        if(cbrapido.isChecked()){
            cbCheckTime = "cbRapido";
        }
        return cbCheckTime;
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

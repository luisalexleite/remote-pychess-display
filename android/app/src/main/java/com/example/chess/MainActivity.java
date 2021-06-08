package com.example.chess;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.chess.login.LoginActivity;
import com.example.chess.profile.ProfileActivity;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //Menu
    private Toolbar toolbar;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private NavigationView navigationView;




    private static final String TAG = "MainActivity";
    private static final int ERROR_DIALOG_REQUEST = 9001;
    private static ArrayList<String> ids = new ArrayList<>();
    //FireStore instance
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    //boolean to check if user is a PetSitter
    public static Boolean petsitter = true;
    private final CollectionReference petsRef = db.collection("Pet");
    private final CollectionReference perfilRef = db.collection("Perfil");
    Button button_out;
    Button button_perfil;
    Button button_match, joinGame;

    //Bottom navigation listener
    @SuppressLint("NonConstantResourceId")


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Menu
        toolbar = findViewById(R.id.menu_toolbar);
        setSupportActionBar(toolbar);

        mDrawerLayout = findViewById(R.id.container);
        navigationView = findViewById(R.id.menu);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                toolbar,
                R.string.open,
                R.string.close
        );

        mDrawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);


        //Game Start
        button_match = findViewById(R.id.button_match);
        button_match.setOnClickListener(v -> {
            Intent game = new Intent(this, GameActivity.class);
            startActivity(game);
        });

        //Join Start
        joinGame = findViewById(R.id.button_join);
        joinGame.setOnClickListener(v -> {
            Intent join = new Intent(this, JoinActivity.class);
            startActivity(join);
        });

        //get userID
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String userID = user.getUid();




    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        switch (id){
            case R.id.menu_profile:
                Intent intent = new Intent(this, ProfileActivity.class);
                startActivity(intent);
                break;
            case R.id.menu_settings:
                Intent intent2 = new Intent(this, ProfileActivity.class);
                startActivity(intent2);
                break;
            case R.id.menu_logout:
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(MainActivity.this, "Sign Out Successful", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                break;
            default:
                break;

        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
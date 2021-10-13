package com.example.coronaattack;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class GameOverActivity extends AppCompatActivity {

    int score;
    String name, scoretxt;
    Button playAgain_btn,quit_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gameover);
        TextView score_tv = findViewById(R.id.score_tv);
        Intent intent = getIntent();
        score = intent.getIntExtra("score", 0);
        scoretxt = score+"";
        name = intent.getStringExtra("name");
        score_tv.setText(score + "");

        playAgain_btn = findViewById(R.id.play_again_btn);
        quit_btn = findViewById(R.id.quit_btn);

        playAgain_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GameOverActivity.this, GameActivity.class);
                intent.putExtra("name" , name);
                startActivity(intent);
            }
        });

        quit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GameOverActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();
        mAuth.signInAnonymously();

        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance("https://corona-attack-d8bdd-default-rtdb.europe-west1.firebasedatabase.app/").getReference().child("scores");
        mDatabase.push().setValue(name +" " + scoretxt);

        mAuth.signOut();
    }
}
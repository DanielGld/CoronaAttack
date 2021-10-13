package com.example.coronaattack;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Boolean isMute;
    private ImageView volume;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EditText playerName_et = findViewById(R.id.playerName);

        Button play_btn = findViewById(R.id.play_btn);
        play_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(playerName_et.getText().length() != 0) {
                    Intent intent = new Intent(MainActivity.this, GameActivity.class);
                    intent.putExtra("name", playerName_et.getText().toString());
                    startActivity(intent);
                }
            else {
                    Toast.makeText(MainActivity.this,R.string.emptyNameText,Toast.LENGTH_LONG).show();
                }
            }
        });
        TextView score_tv = findViewById(R.id.highestScore_tv);

        SharedPreferences preferences = getSharedPreferences("game",MODE_PRIVATE);
        score_tv.setText(preferences.getInt("highestScore", 0)+ "");

        isMute = preferences.getBoolean("isMute", false);
        volume = findViewById(R.id.volume_img);


        if(isMute){
            volume.setImageResource(R.drawable.mute_img);
        }
        else{
            volume.setImageResource(R.drawable.volume_up_img);
        }
        volume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isMute = !isMute;
                if(isMute){
                    volume.setImageResource(R.drawable.mute_img);
                }
                else{
                    volume.setImageResource(R.drawable.volume_up_img);
                }
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("isMute" ,isMute);
                editor.apply();
            }
        });

        Button results_btn = findViewById(R.id.results);
        results_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ResultsActivity.class);
                startActivity(intent);
            }
        });
    }
}
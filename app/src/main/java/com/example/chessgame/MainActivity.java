package com.example.chessgame;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.chessgame.data.*;


public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Button start = (Button)findViewById(R.id.startButton);
        final Button playback = (Button)findViewById(R.id.playbackButton);
        final Button quit = (Button)findViewById(R.id.exitButton);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        savedGames.context = getApplicationContext();
        savedGames.readData();

        if(start != null){
            start.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    Bundle bundle = new Bundle();
                    Intent i = new Intent(MainActivity.this, Game.class);
                    i.putExtras(bundle);
                    startActivity(i);
                }
            });
        }
        if(playback != null){
            playback.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    Bundle bundle = new Bundle();
                    Intent i = new Intent(MainActivity.this, Games.class);
                    i.putExtras(bundle);
                    startActivity(i);
                }
            });
        }
        if(quit != null){
            quit.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    finishAffinity();
                    System.exit(0);
                }
            });
        }
    }
}
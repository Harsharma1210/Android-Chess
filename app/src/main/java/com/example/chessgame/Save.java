package com.example.chessgame;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chessgame.data.*;

import java.util.ArrayList;


public class Save extends AppCompatActivity {

    private ArrayList<String> saveMoves = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.savescreen);

        final Button save = (Button)findViewById(R.id.saveButton);
        final Button cancel = (Button)findViewById(R.id.cancelButton);

        ArrayList<String> moves = (ArrayList<String>) getIntent().getSerializableExtra("finishedGame");

        if(save!= null){
            save.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    String saveTitle = ((TextView)findViewById(R.id.saveTitle)).getText().toString();
                    if(saveTitle == null){
                        Toast.makeText(Save.this, "Title must not be empty", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    savedGames.writeData(new gameData(saveTitle, moves));
                    Toast.makeText(Save.this, "Game Saved", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(Save.this, MainActivity.class);
                    startActivity(i);
                }
            });
        }
        if (cancel!=null) {
            cancel.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    Intent i = new Intent(Save.this,MainActivity.class);
                    startActivity(i);
                }
            });
        }

    }

}

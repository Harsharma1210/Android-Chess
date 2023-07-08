package com.example.chessgame;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.chessgame.data.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class Games extends AppCompatActivity {


    private ListView listView;

    private ArrayList<gameData> games = savedGames.games;
    private String[] gameTitles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gameslist);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);

        // Set title
        ab.setTitle("Recorded Games");

        final Button sortTitle = (Button)findViewById(R.id.sortTitleButton);
        final Button sortDate = (Button)findViewById(R.id.sortDateButton);

        showList();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {

                showPlayback(pos);
            }
        });

        if(sortTitle!= null){
            sortTitle.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    if (games!=null) {
                        Collections.sort(games,new Comparator<gameData>(){
                            public int compare(gameData s1,gameData s2){
                                if(s1 != null && s2 != null)
                                {
                                    String title1 = s1.toString().substring(7, s1.toString().indexOf(","));
                                    String title2 = s2.toString().substring(7, s2.toString().indexOf(","));
                                    return title1.compareToIgnoreCase(title2);
                                }
                                return 0;
                            }});

                    }

                    showList();
                }
            });
        }
        if (sortDate!=null) {
            sortDate.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    if (games!=null) {
                        Collections.sort(games,new Comparator<gameData>(){
                            public int compare(gameData s1,gameData s2){
                                if(s1 != null && s2 != null)
                                {
                                    String title1 = s1.toString().substring(s1.toString().indexOf("Date: ")+6);
                                    String title2 = s2.toString().substring(s2.toString().indexOf("Date: ")+6);
                                    return title1.compareToIgnoreCase(title2);
                                }
                                return 0;
                            }});

                    }

                    showList();
                }
            });
        }

    }

    private void showList() {
        listView = findViewById(R.id.gameslist);

        if (games!=null) {
            gameTitles = new String[games.size()];

            for (int i = 0; i < games.size(); i++) {
                gameTitles[i] = games.get(i).toString();
            }

            ArrayAdapter<String> adapter =
                    new ArrayAdapter<>(this, R.layout.recordedgame, gameTitles);


            listView.setAdapter(adapter);
        }
    }

    private void showPlayback(int pos) {
        gameData playbackGame = games.get(pos);
        Field f = null;
        ArrayList<String> playbackMoves = new ArrayList<>();
        try {
            f = playbackGame.getClass().getDeclaredField("moves");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        f.setAccessible(true);

        try {
            playbackMoves = (ArrayList<String>) f.get(playbackGame);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }


        Bundle bundle = new Bundle();

        Intent i = new Intent(this, Playback.class);

        i.putExtra("playbackMoves", playbackMoves);
        i.putExtras(bundle);
        startActivity(i);

    }

}

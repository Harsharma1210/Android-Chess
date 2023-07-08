package com.example.chessgame;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.chessgame.app.*;
import com.example.chessgame.pieces.*;

import java.util.ArrayList;


public class Playback extends AppCompatActivity {

    Chess chess = new Chess();
    String start, end;
    ArrayList<String> playbackMoves;
    int ptr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playback);
        update();

        ArrayList<String> playbackMoves =(ArrayList<String>) getIntent().getStringArrayListExtra("playbackMoves");
        ptr=0;
        final Button next = (Button)findViewById(R.id.nextButton);


        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);

        // Set title
        ab.setTitle("Playback");

        if (next!=null) {
            next.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    if (ptr<playbackMoves.size()-1) {
                        String move = playbackMoves.get(ptr);
                        start = move.substring(0, 2);
                        end = move.substring(3);
                        Toast toast = Toast.makeText(Playback.this, move, Toast.LENGTH_SHORT);
                        toast.setMargin(10,10);
                        toast.show();
                        ptr++;
                        executeMove();
                    } else {
                        TextView tt = (TextView)findViewById(R.id.turnText);
                        String outcome = "";
                        if (playbackMoves.get(ptr).equals("resign") || playbackMoves.get(ptr).equals("draw")) {
                            String turn = (chess.isWhiteTurn) ? "White" : "Black";
                            String reason = (playbackMoves.get(ptr).equals("resign")) ? " resigned" : "draw";
                            outcome = (reason.equals("draw")) ? "DRAW" : turn + reason;
                        } else {
                            outcome = playbackMoves.get(ptr).equals("w") ? "WHITE WON" : "BLACK WON";
                        }
                        tt.setText("Game over: " + outcome);
                    }
                }
            });
        }
    }


    public void executeMove() {

        if (chess.executeMove(start+" "+end)) {
            ImageView square = (ImageView) findViewById(getResources().getIdentifier(start, "id",getPackageName()));
            square.setColorFilter(Color.TRANSPARENT);
            start=null;
            end = null;
            update();
        } else {
            Toast toast = Toast.makeText(Playback.this, start+" "+end + " doesnt work", Toast.LENGTH_LONG);
            toast.setMargin(10,10);
            toast.show();
        }
    }

    public void update() {
        ImageView tempView;
        for (int y=0; y<8; y++) {
            for (int x=0; x<8; x++) {
                Piece p = chess.b.getPiece(x,y);
                tempView = (ImageView) findViewById(getResources().getIdentifier(String.valueOf((char)(x+97))+(8-y), "id", getPackageName()));

                if (p!=null) {
                    int imgID = getResources().getIdentifier(p.getPiecename().toLowerCase(), "drawable", getPackageName());
                    tempView.setImageResource(imgID);
                } else {
                    tempView.setImageResource(android.R.color.transparent);
                }
            }
        }
        if (chess.isWinner) {
            TextView tt = (TextView)findViewById(R.id.turnText);
            String turn = (chess.isWhiteTurn) ? "WHITE WON" : "BLACK WON";
            tt.setText("Game over: " + turn);
        } else {
            TextView tt = (TextView) findViewById(R.id.turnText);
            String turn = (chess.isWhiteTurn) ? "White" : "Black";
            tt.setText("Turn: " + turn);
        }
    }

    public void pieceSelected(View v) {

    }

}
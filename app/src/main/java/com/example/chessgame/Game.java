package com.example.chessgame;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
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


public class Game extends AppCompatActivity {

    Chess chess = new Chess();
    String start, end;
    boolean canUndo = true;
    public static ArrayList<String> moves = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);
        update();
        final Button ai = (Button)findViewById(R.id.aiButton);
        final Button undo = (Button)findViewById(R.id.undoButton);
        final Button draw = (Button)findViewById(R.id.drawButton);
        final Button resign = (Button)findViewById(R.id.resignButton);


        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        //ab.setDisplayHomeAsUpEnabled(true);

        // Set title
        ab.setTitle("Player vs Player");

        if(draw!= null){
            draw.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    AlertDialog.Builder builder = new AlertDialog.Builder(Game.this);
                    builder.setMessage("Game has ended in a draw")
                            .setTitle("Draw!");
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            moves.add("draw");
                            Bundle bundle = new Bundle();
                            Intent i = new Intent(Game.this, Save.class);
                            i.putExtra("finishedGame", moves);
                            i.putExtras(bundle);
                            startActivity(i);
                            moves = new ArrayList<>();
                            finish();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            });
        }
        if (resign!=null) {
            resign.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    AlertDialog.Builder builder = new AlertDialog.Builder(Game.this);
                    builder.setMessage(chess.isWhiteTurn ? "White has resigned!" : "Black has resigned!")
                            .setTitle(chess.isWhiteTurn ? "Black wins!" : "White wins!");
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            moves.add("resign");
                            Bundle bundle = new Bundle();
                            Intent i = new Intent(Game.this, Save.class);
                            i.putExtra("finishedGame", moves);
                            i.putExtras(bundle);
                            startActivity(i);
                            moves = new ArrayList<>();
                            finish();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            });
        }

        if (ai!=null) {
            ai.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    moves.add(chess.randomMove(chess.isWhiteTurn));
                    update();
                }
            });
        }

        if (undo!=null) {
            undo.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    undo();
                }
            });
        }
    }

    public void pieceSelected(View v) {
        int currID = v.getId();
        ImageView square = (ImageView) findViewById(currID);
        String currSquare = v.getResources().getResourceName(currID);
        currSquare = currSquare.substring(currSquare.length()-2);

        if (start==null) {
            start = currSquare;
            square.setColorFilter(Color.RED, PorterDuff.Mode.OVERLAY);
        } else if (start.equals(currSquare)) {
            start = null;
            square.setColorFilter(Color.TRANSPARENT);
        } else {
            end = currSquare;
            executeMove();
        }

        if (start!=null && !isValidSelection()) {
            start = null;
            square.setColorFilter(Color.TRANSPARENT);
            Toast toast = Toast.makeText(this, "Invalid Selection!", Toast.LENGTH_SHORT);
            toast.setMargin(10,10);
            toast.show();
        }
    }

    public boolean isValidSelection() {
        int[] coord = chess.getPosition(start);
        int x = coord[0];
        int y = coord[1];
        Piece p = chess.b.getPiece(x,y);

        if (p==null) return false;

        return (chess.b.getPiece(x, y).isWhite()==chess.isWhiteTurn);
    }

    public void executeMove() {
        if (start==null || end==null) return;
        if (chess.executeMove(start+" "+end)) {
            ImageView square = (ImageView) findViewById(getResources().getIdentifier(start, "id",getPackageName()));
            square.setColorFilter(Color.TRANSPARENT);
            moves.add(start+" "+end);
            start=null;
            end = null;
            canUndo = true;
            update();
        } else {
            if (chess.isCheck(chess.b, chess.wKPosf, chess.bKPosf)[0]) {
                if (chess.isWhiteTurn) {
                    Toast toast = Toast.makeText(this, "White King is in check!", Toast.LENGTH_SHORT);
                    toast.setMargin(10,10);
                    toast.show();
                }
                else {
                    Toast toast = Toast.makeText(this, "Black King is in check!", Toast.LENGTH_SHORT);
                    toast.setMargin(10,10);
                    toast.show();
                }
            }else {
                Toast toast = Toast.makeText(this, "Invalid Move!", Toast.LENGTH_SHORT);
                toast.setMargin(10,10);
                toast.show();
            }
        }

        if (chess.isWinner) {
            moves.add(chess.isWhiteTurn ? "w" : "b");
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(chess.isWhiteTurn ? "White is the winner!" : "Black is the winner!")
                    .setTitle("Checkmate!");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Bundle bundle = new Bundle();
                    Intent i = new Intent(Game.this, Save.class);
                    i.putExtra("finishedGame", moves);
                    i.putExtras(bundle);
                    startActivity(i);
                    moves = new ArrayList<>();
                    finish();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
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

        TextView tt = (TextView)findViewById(R.id.turnText);
        String turn = (chess.isWhiteTurn) ? "White" : "Black";
        tt.setText("Turn: " + turn);
    }

    public void undo() {
        if (chess.canUndo) {
            chess.undo();
            moves.remove(moves.size()-1);
            start = null;
            end = null;
            update();
        } else {
            Toast toast = Toast.makeText(this, "Cannot undo!", Toast.LENGTH_SHORT);
            toast.setMargin(10,10);
            toast.show();
        }
    }


}
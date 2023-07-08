package com.example.chessgame.data;

import android.content.Context;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class savedGames implements Serializable {
    public static ArrayList<gameData> games;
    public static Context context;


    public static void readData() {
        File f = new File(context.getFilesDir(), "data.dat");
        ObjectInputStream savedGames;

        if (f.exists()) {
            try {
                savedGames = new ObjectInputStream(context.openFileInput("data.dat"));
                games = (ArrayList<gameData>) savedGames.readObject();
                savedGames.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            games = new ArrayList<gameData>();
        }

    }

    public static void writeData(gameData game) {
        games.add(game);
        try {
            ObjectOutput saveGame = new ObjectOutputStream(context.openFileOutput("data.dat",  Context.MODE_PRIVATE));
            saveGame.writeObject(games);
            saveGame.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

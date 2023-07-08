package com.example.chessgame.data;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class gameData implements Serializable {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private Calendar date;
    private String title;
    private ArrayList<String> moves;

    public gameData(String title, ArrayList<String> moves) {
        this.title = title;
        this.moves = moves;
        this.date = Calendar.getInstance();
    }

    public String toString() {
        return "Title: " + title + ", Date: " + DATE_FORMAT.format(date.getTime());
    }



}

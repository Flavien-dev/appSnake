package com.chapfla.appsnake.Models;

import android.database.Cursor;

public class player {

    private String namePlayer;
    private int score;

    public player(String nom, int score) {
        this.namePlayer = nom;
        this.score = score;
    }

    public player(Cursor cursor) {
        namePlayer = cursor.getString(cursor.getColumnIndexOrThrow("namePlayer"));
        score = cursor.getInt(cursor.getColumnIndexOrThrow("score"));
    }

    public String getNamePlayer() {
        return namePlayer;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}

package com.chapfla.appsnake.Models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;

public class SnakeDB extends SQLiteOpenHelper {

    private static final String TABLE_PLAYER = "player";
    static String DB_NAME="snake.db";
    static int DB_VERSION=1;

    public SnakeDB(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlCreateDatatablePlayer = "CREATE TABLE " + TABLE_PLAYER + "(idPlayer INTEGER PRIMARY KEY AUTOINCREMENT, namePlayer TEXT, score INTEGER);";
        db.execSQL(sqlCreateDatatablePlayer);

        db.execSQL("INSERT INTO " + TABLE_PLAYER + " VALUES(" + null + ",\"Flavien\",10)");
        db.execSQL("INSERT INTO " + TABLE_PLAYER + " VALUES(" + null + ",\"Auguste\",7)");
        db.execSQL("INSERT INTO " + TABLE_PLAYER + " VALUES(" + null + ",\"Karim\",12)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void addNewPlayer(String nom, int score) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("namePlayer", nom);
        values.put("score", score);

        db.insert(TABLE_PLAYER, null, values);

        db.close();
    }
}

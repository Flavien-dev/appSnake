package com.chapfla.appsnake.Controllers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.chapfla.appsnake.Models.SnakeDB;
import com.chapfla.appsnake.Models.player;

import java.util.ArrayList;

public class PlayerManager {
    public ArrayList<player> listPlayers;
    private SQLiteDatabase db;

    public ArrayList<player> getListPlayer(){
        return listPlayers;
    }

    public PlayerManager(Context context) {
        listPlayers = initListPlayer(context);
    }

    private ArrayList<player> initListPlayer(Context context) {
        ArrayList<player> listPlayer = new ArrayList<>();
        SnakeDB helper = new SnakeDB(context);
        db = helper.getReadableDatabase();

        Cursor cursor = db.query(true,"player",new String[]{"idPlayer","namePlayer","score"},null,null,null,null,"score DESC",null);

        // ajoute les joueurs à la liste
        while(cursor.moveToNext()){
            listPlayer.add(new player(cursor));
        }

        cursor.close();
        db.close();

        // retourne la liste contenant les questions
        return listPlayer;
    }
}

package com.chapfla.appsnake;

import static android.widget.Toast.LENGTH_SHORT;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.chapfla.appsnake.Controllers.PlayerManager;
import com.chapfla.appsnake.Models.SnakeDB;
import com.chapfla.appsnake.Models.player;

import java.util.ArrayList;

public class ScoreActivity extends AppCompatActivity {
    private SnakeDB maDB;
    private ListView lv_players;
    private Button bt_rejouer;
    public ArrayList<player> playerList;

    String nameNewPlayer;
    int scoreNewPlayer;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_table);

        Intent SnakeActivity = getIntent();

        nameNewPlayer = SnakeActivity.getStringExtra("name");
        scoreNewPlayer = SnakeActivity.getIntExtra("score",0);

        maDB = new SnakeDB(ScoreActivity.this);
        maDB.addNewPlayer(nameNewPlayer,scoreNewPlayer);

        lv_players = (ListView) findViewById(R.id.lv_score_player);
        bt_rejouer = findViewById(R.id.bt_rejouer);
    }

    protected void onStart() {

        super.onStart();
        PlayerManager pm = new PlayerManager(this);
        playerList = pm.getListPlayer();

        PlayerAdapter playerArrayAdapter = new PlayerAdapter(this,playerList);
        lv_players.setAdapter(playerArrayAdapter);

        bt_rejouer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent MainActivity = new Intent(ScoreActivity.this, MainActivity.class);
                startActivity(MainActivity);
            }
        });
    }
}

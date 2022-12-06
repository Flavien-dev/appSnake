package com.chapfla.appsnake;

import static android.widget.Toast.LENGTH_SHORT;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.chapfla.appsnake.Controllers.PlayerManager;
import com.chapfla.appsnake.Models.player;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Button bt_jouer;
    private EditText et_name_player;
    public ArrayList<player> playerList;
    private String nameNewPlayer;
    private int scoreNewPlayer;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bt_jouer = findViewById(R.id.btn_jouer);
        et_name_player = findViewById(R.id.et_player_name);
    }

    protected void onStart() {

        super.onStart();
        PlayerManager pm = new PlayerManager(this);
        playerList = pm.getListPlayer();

        bt_jouer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (!et_name_player.getText().toString().equals("")) {
                    nameNewPlayer = et_name_player.getText().toString();
                    scoreNewPlayer = 0;
                    Intent SnakeActivity = new Intent(MainActivity.this, SnakeActivity.class);
                    SnakeActivity.putExtra("name",nameNewPlayer);
                    SnakeActivity.putExtra("score",scoreNewPlayer);
                    et_name_player.setText("");
                    startActivity(SnakeActivity);
                } else {
                    Toast.makeText(getApplicationContext(),"Vous devez écrire votre nom avant de jouer", (int) 1000).show();
                }
            }
        });
    }
}
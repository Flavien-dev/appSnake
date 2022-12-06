package com.chapfla.appsnake;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.SyncStateContract;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.Random;

public class SnakeActivity extends AppCompatActivity implements SensorEventListener {

    // valeurs relatives à la grille
    static final int NOMBRE_CASES = 448;
    static final int NOMBRE_COLONNES = 32;

    // délais de la partie
    static final int DELAI_COLLISION = 1000000;
    static final int DELAI_FIN_PARTIE = 1000;

    // valeurs relatives au sensor
    static final int PRODUIT_PUISSANCE_SENSOR = 10;
    static final int LIMITE_PUISSANCE_SENSOR = 15;

    // valeurs des vitesses
    static final int SPEED_MIN = 200;
    static final int SPEED_REMOVE = 50;

    // limite des bordures
    static final int LIMITE_INF_BORDURE = 32;
    static final int BORDURE_MOD_0 = 0;
    static final int BORDURE_MOD_1 = 1;
    static final int BORDURE_MOD_30 = 30;
    static final int BORDURE_MOD_31 = 31;
    static final int LIMITE_SUP_BORDURE = 384;

    // position initiale du serpent
    static final int INIT_POS_SNAKE = 140;

    private TextView tv_score;
    private TextView tv_player_name;
    private ImageView iv_message;
    private LinearLayout ll_param;

    // gravity sensor
    private SensorManager sensorManager;
    private Sensor gravSensor;

    // grille
    GridView myGV;

    // permet de faire bouger le jeu
    Runnable GameRunnable = null;

    // différentes directions du serpent
    String direction = "RIGHT";
    String oldPosition;

    // différentes listes
    ArrayList<CaseGridView> casesGV;
    ArrayList<Integer> snakeBodyPositions;
    ArrayList<Integer> borderPositions;

    // différentes cases
    CaseGridView snake;
    CaseGridView apple1;
    CaseGridView apple2;

    // vitesse du serpent
    long snakeSpeed;

    // positions du serpent et des deux pommes
    int snakeHeadPosition;
    int applePosition1;
    int applePosition2;

    public String namePlayer;
    public int scorePlayer;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main_land);

        Intent MainActivity = getIntent();

        namePlayer = MainActivity.getStringExtra("name");
        scorePlayer = MainActivity.getIntExtra("score",0);

        Log.wtf("nom",namePlayer);

        // initialiser du sensor
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        gravSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);

        // initialiser les objets
        tv_score = findViewById(R.id.tv_score);
        tv_player_name = findViewById(R.id.tv_playerName);
        ll_param = findViewById(R.id.ll_param);
        iv_message = findViewById(R.id.iv_game_over);
        iv_message.setVisibility(View.INVISIBLE);

        // initialiser la grille
        myGV = findViewById(R.id.GVsnake);

        // initialiser les listes
        casesGV = new ArrayList<>();
        snakeBodyPositions = new ArrayList<>();
        borderPositions = new ArrayList<>();

        // initialiser la vitesse
        snakeSpeed = 900;

        // initialiser la direction
        oldPosition = "RIGHT";

        // initialiser les valeurs des objets
        tv_player_name.setText(namePlayer);

        // trouver toutes les bordures
        initBorderPositions();

        // afficher les cases jouables
        ajouterCases();

        // afficher les bordures
        displayBorders();

        // placer les pommes et le serpent dans la grille
        initSnakeApple();

        // rafraichir la grille
        actualiserGridView();

        // prise de valeur du capteur rapide
        sensorManager.registerListener(this, gravSensor, SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onStart() {
        super.onStart();

        // démarre le jeu
        Handler handler = new Handler();

        GameRunnable = new Runnable() {
            @Override
            public void run() {

                if (borderPositions.contains(snakeHeadPosition)) {
                    // si oui, le jeu s'arrête, sinon, il continue
                    tv_player_name.setVisibility(View.VISIBLE);
                    scorePlayer = snakeBodyPositions.size();
                    snakeBodyPositions.clear();
                    ll_param.setVisibility(View.INVISIBLE);
                    GVsnake.setVisiblitiy(View.INVISIBLE);
                    iv_message.setVisibility(View.VISIBLE);
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    handler.postDelayed(this, DELAI_COLLISION);
                    ajouterTableauScore();
                } else {
                    // détecte si le serpent mange la pomme 1
                    if (snakeHeadPosition == applePosition1) {
                        // si oui, le serpent s'agrandit, accélère et affiche une nouvelle pomme
                        snakeBodyPositions.add(snakeHeadPosition);
                        if (snakeSpeed > SPEED_MIN) {
                            snakeSpeed -= SPEED_REMOVE;
                        }
                        displayApple1();
                    }
                    // détecte si le serpent mange la pomme 2
                    if (snakeHeadPosition == applePosition2) {
                        // si oui, le serpent s'agrandit, accélère et affiche une nouvelle pomme
                        snakeBodyPositions.add(snakeHeadPosition);
                        if (snakeSpeed > SPEED_MIN) {
                            snakeSpeed -= SPEED_REMOVE;
                        }
                        displayApple2();
                    }

                    // efface la dernière case sur laquelle le serpent est passé
                    casesGV.get(snakeBodyPositions.get(snakeBodyPositions.size() - 1)).setMonImage(R.drawable.case_blanche);

                    // selon la direction, le serpent se déplace en conséquence
                    switch (direction) {
                        // si c'est à droite, le serpent ira à droite
                        case "RIGHT":
                            // vérifie que le serpent ne se rampe pas dessus
                            if (oldPosition.equals("LEFT")) {
                                snakeHeadPosition--;
                            } else {
                                snakeHeadPosition++;
                                oldPosition = direction;
                            }
                            break;
                        // si c'est à gauche, le serpent ira à gauche
                        case "LEFT":
                            // vérifie que le serpent ne se rampe pas dessus
                            if (oldPosition.equals("RIGHT")) {
                                snakeHeadPosition++;
                            } else {
                                snakeHeadPosition--;
                                oldPosition = direction;
                            }
                            break;
                        // si c'est en haut, le serpent ira à en haut
                        case "TOP":
                            // vérifie que le serpent ne se rampe pas dessus
                            if (oldPosition.equals("BOTTOM")) {
                                snakeHeadPosition += NOMBRE_COLONNES;
                            } else {
                                snakeHeadPosition -= NOMBRE_COLONNES;
                                oldPosition = direction;
                            }
                            break;
                        // si c'est en bas, le serpent ira à en bas
                        case "BOTTOM":
                            // vérifie que le serpent ne se rampe pas dessus
                            if (oldPosition.equals("TOP")) {
                                snakeHeadPosition -= NOMBRE_COLONNES;
                            } else {
                                snakeHeadPosition += NOMBRE_COLONNES;
                                oldPosition = direction;
                            }
                            break;
                    }

                    // vérifie que le serpent se rampe pas dessus
                    if (snakeBodyPositions.contains(snakeHeadPosition)) {
                        tv_player_name.setVisibility(View.VISIBLE);
                        scorePlayer = snakeBodyPositions.size();
                        snakeBodyPositions.clear();
                        ll_param.setVisibility(View.INVISIBLE);
                        iv_message.setVisibility(View.VISIBLE);
                        handler.postDelayed(this, DELAI_COLLISION);
                        ajouterTableauScore();
                    } else {
                        // affiche la taille du serpent
                        tv_score.setText("Score : " + snakeBodyPositions.size());

                        // actualise les différentes positions du corps du serpent
                        actualiserSnakeBodyPosition();

                        // affiche le serpent avec les nouvelles positions
                        displaySnake();

                        // actualiser la grille
                        actualiserGridView();

                        // effectue la boucle dans un laps de temps varialbes
                        handler.postDelayed(this,snakeSpeed);
                    }
                }
            }
        };
        handler.postDelayed(GameRunnable,(long) DELAI_FIN_PARTIE);
    }

    /**
     * actualise la position du serpent
     */
    public void actualiserSnakeBodyPosition() {
        for (int i = snakeBodyPositions.size()-1;i >= 0; i--) {
            if (i == 0) {
                snakeBodyPositions.set(0, snakeHeadPosition);
            } else {
                snakeBodyPositions.set(i,snakeBodyPositions.get(i-1));
            }
        }
    }

    /**
     * affiche et ajoute les case dans la grille
     */
    public void ajouterCases() {
        for (int i = 0; i < NOMBRE_CASES; i++) {
            casesGV.add(new CaseGridView(R.drawable.case_blanche,i));
        }
    }


    /**
     * affiche la bordure
     */
    public void displayBorders() {
        for (int i = 0; i < borderPositions.size()-1;i++) {
            casesGV.get(borderPositions.get(i)).setMonImage(R.drawable.bord);
        }
    }

    /**
     * affiche le serpent
     */
    public void displaySnake() {
        for (int i = 0; i < snakeBodyPositions.size();i++) {
            if (i == 0) {
                // affiche la tête
                casesGV.get(snakeBodyPositions.get(0)).setMonImage(R.drawable.tete_snake);
            } else {
                // affiche le corps
                casesGV.get(snakeBodyPositions.get(i)).setMonImage(R.drawable.corps_snake);
            }
        }
    }

    /**
     * affiche la première pomme
     */
    public void displayApple1() {
        applePosition1 = applePlace();
        casesGV.get(applePosition1).setMonImage(apple1.getMonImage());
    }

    /**
     * affiche la seconde pomme
     */
    public void displayApple2() {
        applePosition2 = applePlace();
        casesGV.get(applePosition2).setMonImage(apple2.getMonImage());
    }

    /**
     * actualise la grille
     */
    public void actualiserGridView() {
        CaseGVAdapter adapter = new CaseGVAdapter(this, casesGV);
        myGV.setAdapter(adapter);
    }

    /**
     * détecte le côté duquel le téléphone s'incline
     * @param sensorEvent
     */
    public void onSensorChanged(SensorEvent sensorEvent) {
        // détecte le haut
        if (sensorEvent.values[0]*PRODUIT_PUISSANCE_SENSOR < -LIMITE_PUISSANCE_SENSOR) {
            direction = "TOP";
        // détecte le bas
        } else if (sensorEvent.values[0]*PRODUIT_PUISSANCE_SENSOR >= LIMITE_PUISSANCE_SENSOR) {
            direction = "BOTTOM";
        // détecte la gauche
        } else if (sensorEvent.values[1]*PRODUIT_PUISSANCE_SENSOR < -LIMITE_PUISSANCE_SENSOR) {
            direction = "LEFT";
        // détecte la droite
        } else if (sensorEvent.values[1]*PRODUIT_PUISSANCE_SENSOR >= LIMITE_PUISSANCE_SENSOR) {
            direction = "RIGHT";
        }
        Log.wtf("direction",direction);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    /**
     * cherche une position pour la pomme
     * @return la nouvelle position
     */
    public int applePlace() {
        Random rand = new Random();

        int applePosition = rand.nextInt(NOMBRE_CASES);

        while (borderPositions.contains(applePosition) || snakeBodyPositions.contains(applePosition)) {
            applePosition = rand.nextInt(NOMBRE_CASES);
        }

        return applePosition;
    }

    /**
     * définit les bords de l'application
     */
    public void initBorderPositions() {
        for (int i = 0; i <= NOMBRE_CASES; i ++) {
            if (i <= LIMITE_INF_BORDURE
                    || (i % NOMBRE_COLONNES) == BORDURE_MOD_0
                    || (i % NOMBRE_COLONNES) == BORDURE_MOD_1
                    || (i % NOMBRE_COLONNES) == BORDURE_MOD_30
                    || (i % NOMBRE_COLONNES) == BORDURE_MOD_31
                    || i >= LIMITE_SUP_BORDURE) {
                borderPositions.add(i);
            }
        }
    }

    /**
     * affiche les 2 pommes ainsi que le serpent
     */
    public void initSnakeApple() {
        snakeHeadPosition = INIT_POS_SNAKE;
        applePosition1 = applePlace();
        applePosition2 = applePlace();

        snakeBodyPositions.add(snakeHeadPosition);

        snake = new CaseGridView(R.drawable.tete_snake, snakeHeadPosition);
        apple1 = new CaseGridView(R.drawable.pomme_snake, applePosition1);
        apple2 = new CaseGridView(R.drawable.pomme_snake, applePosition2);

        casesGV.get(snakeHeadPosition).setMonImage(snake.getMonImage());
        casesGV.get(applePosition1).setMonImage(apple1.getMonImage());
        casesGV.get(applePosition2).setMonImage(apple2.getMonImage());
    }

    public void ajouterTableauScore() {
        Intent ScoreActivity = new Intent(SnakeActivity.this, ScoreActivity.class);
        ScoreActivity.putExtra("name",namePlayer);
        ScoreActivity.putExtra("score",scorePlayer);
        startActivity(ScoreActivity);
    }
}

package com.example.dylan.checkers;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Board board;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);

        board = new Board();

        // Play Game
        Button play = (Button) findViewById(R.id.playButton);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playGame();
            }
        }); // End Play Game button

        // Load Saved Game
        Button load = (Button) findViewById(R.id.loadButton);
        load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadGame();
            }
        }); // End Load Saved Game button


        //  Game Options button
        final Button options = (Button) findViewById(R.id.optionsButton);
        options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                optionsMenu();
            }
        });
        //End Options button

        //  Settings button
        final Button settings = (Button) findViewById(R.id.settingsButton);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingsMenu();
            }
        });


    }


    private void playGame() {
        Intent intent = new Intent(this, ButtonBoard.class);
        startActivity(intent);
    }

    // TODO: If there is no saved game (look for saved.dat file), make this un-clickable
    private void loadGame() {
        Intent intent = new Intent(this, ButtonBoard.class);
        intent.putExtra("LOAD", true);
        startActivity(intent);
    }

    private void optionsMenu() {
        Intent intent = new Intent(this, GameOptions.class);
        startActivity(intent);
    }

    private void settingsMenu() {
        Intent intent = new Intent(this, Settings.class);
        startActivity(intent);
    }

}

package com.uno.dbbc.checkers;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
/*
 * MainActivity.java - Controls the main menu that is displayed when the application is launched
 *                   - Allows a user to Play the game or Load the game
 */
public class MainActivity extends AppCompatActivity {

    /*
     * Method that deals with the creation of MainActivity activity
     * Handles when player clicks a button to perform an action
     * @param Bundle savedInstanceState - Saves the instance of the main menu activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);

        // Play Game button and listener
        Button play = (Button) findViewById(R.id.playButton);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ButtonBoard.class);
                startActivity(intent);
            }
        }); // End Play Game button

        // Load Saved Game button and listener
        Button load = (Button) findViewById(R.id.loadButton);
        load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ButtonBoard.class);
                intent.putExtra("LOAD", true);
                startActivity(intent);
            }
        }); // End Load Saved Game button

        // Exit application button and listener
        Button exit = (Button) findViewById(R.id.exitButton);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }); // End Exit application button

    }



}

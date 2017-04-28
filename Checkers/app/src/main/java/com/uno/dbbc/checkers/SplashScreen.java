package com.uno.dbbc.checkers;

import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;

/*
 * SplashScreen.java - Creates a popup splash screen when starting Checkers application
 */
public class SplashScreen extends AppCompatActivity {

    /*
     * Method that deals with the creation of SplashScreen activity
     * Timer controls time the screen is displayed for
     * @param Bundle savedInstanceState - Saves the instance of the splash screen activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        Thread timerThread = new Thread(){
            public void run(){
                try{
                    sleep(3000);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }finally{
                    Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };
        timerThread.start();
    }
}



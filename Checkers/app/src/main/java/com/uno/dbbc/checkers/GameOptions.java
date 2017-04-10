package com.uno.dbbc.checkers;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.view.View;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class GameOptions extends AppCompatActivity {

    int color_selected = 0;


        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.game_options_menu);
        }

        public void selectBoardColor(View view){
            registerForContextMenu(view);
            openContextMenu(view);
        }
        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
            super.onCreateContextMenu(menu, v, menuInfo);
            MenuInflater menuInflater = getMenuInflater();
            menuInflater.inflate(R.menu.game_options_menu, menu);
            MenuItem color_brown = menu.findItem(R.id.brown_board_button);
            MenuItem color_black = menu.findItem(R.id.black_board_button);

            if(color_selected == 1){
                color_brown.setChecked(true);
            }
            else if(color_selected == 2){
                color_black.setChecked(true);
            }

        }

        @Override
        public boolean onContextItemSelected(MenuItem item){
            switch(item.getItemId()){
                case R.id.brown_board_button:
                    Toast.makeText(getApplicationContext(), "Brown Selected.", Toast.LENGTH_SHORT).show();
                    item.setChecked(true);
                    color_selected = 1;
                    return true;

                case R.id.black_board_button:
                    Toast.makeText(getApplicationContext(), "Black Selected.", Toast.LENGTH_SHORT).show();
                    item.setChecked(true);
                    color_selected = 2;
                    return true;
            }
            return super.onContextItemSelected(item);
        }


}

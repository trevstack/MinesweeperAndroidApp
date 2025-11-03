package com.trevc.minesweeper;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Arrays;


public class TitlePage extends AppCompatActivity {

    final int SETTINGS_ACTIVITY_CODE = 123;
    private int[] colorCodes = {0, 1, 2, 3};
    private int rowSize = 5, colSize = 5, minePercent = 15;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_title_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        Button playButton = findViewById(R.id.playButton);
        playButton.setOnClickListener((view) -> {
            Intent playIntent = new Intent(this, Game.class);
            playIntent.putExtra("rowSize", rowSize);
            playIntent.putExtra("colSize", colSize);
            playIntent.putExtra("minePercent", minePercent);
            playIntent.putExtra("cellColors", colorCodes);
            startActivity(playIntent);
        });

        Button settingsButton = findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener((view) -> {
            Intent playIntent = new Intent(this, Settings.class);
            playIntent.putExtra("rowSize", rowSize);
            playIntent.putExtra("colSize", colSize);
            playIntent.putExtra("minePercent", minePercent);
            playIntent.putExtra("cellColors", colorCodes);
            startActivityForResult(playIntent, SETTINGS_ACTIVITY_CODE);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SETTINGS_ACTIVITY_CODE) {
            if (resultCode == RESULT_OK) {
                int[] returnedColorCodes = data.getIntArrayExtra("cellColors");
                if (returnedColorCodes != null) {
                    Log.i("ACT-RET", "Returning activity data: " + Arrays.toString(returnedColorCodes));
                    colorCodes = returnedColorCodes;
                }

                colSize = data.getIntExtra("colSize", 5);
                rowSize = data.getIntExtra("rowSize", 5);
                minePercent = data.getIntExtra("minePercent", 10);
            }
        }
    }
}
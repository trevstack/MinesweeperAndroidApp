package com.trevc.minesweeper;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Arrays;

public class Settings extends AppCompatActivity {


    int[] colorIndex = {0, 1, 2, 3};
    int rowSize, colSize, minePercent;
    private enum cellType {
        COVERED, UNCOVERED, SUSPECTED, REVEALED
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_settings);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Get passed values from TitlePage
        Intent passedIntent = getIntent();
        rowSize = passedIntent.getIntExtra("rowSize", 5);
        colSize = passedIntent.getIntExtra("colSize", 5);
        minePercent = passedIntent.getIntExtra("minePercent", 15);
        int[] cellColors = passedIntent.getIntArrayExtra("cellColors");
        if (cellColors != null) {
            colorIndex = cellColors;
        }
        Log.i("REC-INTENT", "RS: " + rowSize + ", CS: " + colSize + ", MP: " + minePercent);


        // Sliders (Seekbars)
        SeekBar rowSlider = findViewById(R.id.rowSlider);
        SeekBar columnSlider = findViewById(R.id.colSlider);
        SeekBar minePercentSlider = findViewById(R.id.percentSlider);

        // Slider Values (TextViews)
        TextView rowTextValue = findViewById(R.id.rowTextValue);
        TextView columTextValue = findViewById(R.id.colTextValue);
        TextView percentTextValue = findViewById(R.id.percentTextValue);

        // Slider Functions
        rowSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                rowSize = 5 + progress;
                rowTextValue.setText(String.valueOf(rowSize));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
        columnSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                colSize = 5 + progress;
                columTextValue.setText(String.valueOf(colSize));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
        minePercentSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                minePercent = 10 + progress * 5;
                percentTextValue.setText(String.valueOf(minePercent) + "%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        // Set slider values
        rowSlider.setProgress(rowSize - 5, true);
        rowTextValue.setText(String.valueOf(rowSize));
        columnSlider.setProgress(colSize - 5, true);
        columTextValue.setText(String.valueOf(colSize));
        minePercentSlider.setProgress((minePercent - 10) / 5, true);
        percentTextValue.setText(String.valueOf(minePercent) + "%");


        // Return Button
        Button returnButton = findViewById(R.id.returnButton);
        returnButton.setOnClickListener((view) -> {
            Log.i("ACT-END", "Finishing Activity. Colors: " + Arrays.toString(colorIndex));
            Intent resultIntent = new Intent();
            resultIntent.putExtra("cellColors", colorIndex);
            resultIntent.putExtra("rowSize", rowSize);
            resultIntent.putExtra("colSize", colSize);
            resultIntent.putExtra("minePercent", minePercent);
            Log.i("SEND-INTENT", "RS: " + rowSize + ", CS: " + colSize + ", MP: " + minePercent);

            setResult(Settings.RESULT_OK, resultIntent);

            finish();
        });


        // Dropdowns (Spinners)
        Spinner coveredCellColor = findViewById(R.id.coveredCellSpinner);
        Spinner uncoveredCellColor = findViewById(R.id.uncoveredCellSpinner);
        Spinner suspectedCellColor = findViewById(R.id.suspectedCellSpinner);
        Spinner revealedCellColor = findViewById(R.id.revealedCellSpinner);

        populateSpinner(coveredCellColor, cellType.COVERED.ordinal(), colorIndex[0]);
        populateSpinner(uncoveredCellColor, cellType.UNCOVERED.ordinal(), colorIndex[1]);
        populateSpinner(suspectedCellColor, cellType.SUSPECTED.ordinal(), colorIndex[2]);
        populateSpinner(revealedCellColor, cellType.REVEALED.ordinal(), colorIndex[3]);
    }

    private void populateSpinner(Spinner dropdown, int cellTypeIndex, int defaultSelection) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.cellColors, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdown.setAdapter(adapter);

        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedColor = parent.getSelectedItem().toString();
//                Toast.makeText(getApplicationContext(), "Selected: " + selectedColor + " for " + key, Toast.LENGTH_SHORT).show();
//                colors.add(cellTypeIndex, selectedColor);
//                colors.set(cellTypeIndex, selectedColor);
                Log.i("CellColorSelector", "Color: " + selectedColor);
                colorIndex[cellTypeIndex] = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        dropdown.setSelection(defaultSelection);
    }
}
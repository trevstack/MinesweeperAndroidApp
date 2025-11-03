package com.trevc.minesweeper;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Random;

public class Game extends AppCompatActivity {

    private int rowSize, colSize, minePercent, totalMines;
    private int covered, uncovered, suspected, revealed;
    private int[] cellColors;
    private GridView cellGrid;
    private TextView mineCounter;
    private ArrayList<ArrayList<Cell>> cellData;
    private GridViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_game);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button returnButton = findViewById(R.id.returnButton);
        returnButton.setOnClickListener((view) -> {
            finish();
        });

        cellGrid = findViewById(R.id.cellGrid);
        mineCounter = findViewById(R.id.remainingMines);

        extractPreferences();
        createBoard();
    }

    public void updateMineCounter(int newMineCount) {
        mineCounter.setText(String.valueOf(newMineCount));
    }

    private void extractPreferences() {
        Intent passedIntent = getIntent();
        rowSize = passedIntent.getIntExtra("rowSize", 5);
        colSize = passedIntent.getIntExtra("colSize", 5);
        minePercent = passedIntent.getIntExtra("minePercent", 15);

//        covered = passedIntent.getIntExtra("covered", 0);
//        uncovered = passedIntent.getIntExtra("uncovered", 1);
//        suspected = passedIntent.getIntExtra("suspected", 2);
//        revealed = passedIntent.getIntExtra("revealed", 3);
        cellColors = passedIntent.getIntArrayExtra("cellColors");
        if (cellColors == null) {
            Log.i("GAME-PREF", "Cell colors not found");
            cellColors = new int[]{0, 1, 2, 3};
        }
    }

    public void checkWinCondition() {
        // Loop through every cell
        for (int r = 0; r < rowSize; r++) {
            for (int c = 0; c < colSize; c++) {
                Cell currentCell = cellData.get(r).get(c);


                // Cell isn't a bomb, and it's hidden, then game's not over
                if (currentCell.getBombCount() != -1 && currentCell.getCellState() == 0) {
                    return;
                }
            }
        }

        // all non-bomb cells have been revealed
        endGame(true);
    }

    public void endGame(boolean didWin) {
        // disable the grid
        cellGrid.setEnabled(false);

        String statusMessage;
        if (didWin) {
            statusMessage = "Congrats you win!";
        } else {
            statusMessage = "You lost... L.";
        }
        Toast.makeText(getApplicationContext(), statusMessage, Toast.LENGTH_SHORT).show();
        cellGrid.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 2500);
    }

    private ArrayList<ArrayList<Cell>> generateBoard() {
        final int MINE = -1;

        //Create the board
        int[][] tempBoard = new int[rowSize][colSize];
        Random random = new Random();

        // Calculate number of mines to place
        double mineProbability = minePercent / 100.0;
        totalMines = (int) (rowSize * colSize * mineProbability);

        // Place mines and update neighbor counts
        int minesPlaced = 0;
        while (minesPlaced < totalMines) {
            int r = random.nextInt(rowSize);
            int c = random.nextInt(colSize);

            // Check if this cell is NOT already a mine
            if (tempBoard[r][c] == MINE) continue;;

            // Place the mine
            tempBoard[r][c] = MINE;
            minesPlaced++;

            // Update neighbor tiles
            for (int dr = -1; dr <= 1; dr++) { // delta row
                for (int dc = -1; dc <= 1; dc++) { // delta col

                    // Skip the cell itself
                    if (dr == 0 && dc == 0) {
                        continue;
                    }

                    int nr = r + dr; // neighbor row
                    int nc = c + dc; // neighbor col

                    // Check if the neighbor is within the board boundaries
                    if (nr >= 0 && nr < rowSize && nc >= 0 && nc < colSize) {
                        // Increment the neighbor's count ONLY if it's NOT a mine
                        if (tempBoard[nr][nc] != MINE) {
                            tempBoard[nr][nc]++;
                        }
                    }
                }
            }
        }

        ArrayList<ArrayList<Cell>> board = new ArrayList<>();
        for (int r = 0; r < rowSize; r++) {
            ArrayList<Cell> row = new ArrayList<>();
            for (int c = 0; c < colSize; c++) {

                int bombCount = tempBoard[r][c];
                row.add(new Cell(bombCount));
            }
            board.add(row);
        }

        return board;
    }
    private void createBoard() {
        this.cellData = generateBoard();
        this.adapter = new GridViewAdapter(Game.this, cellData, rowSize, colSize, totalMines, cellColors);
        cellGrid.setNumColumns(colSize);
        cellGrid.setAdapter(adapter);
        updateMineCounter(totalMines);
    }
}
package com.trevc.minesweeper;

import java.util.ArrayList;

public class GameBoard {

    private int rowSize, colSize, minePercent, totalMines;
    private ArrayList<ArrayList<Cell>> cellData;

    public GameBoard(int rSize, int cSize, int mPercent) {
        this.rowSize = rSize;
        this.colSize = cSize;
        this.minePercent = mPercent;
    }

    public ArrayList<ArrayList<Cell>> getCellData() {
        return cellData;
    }
    public void setCellData(ArrayList<ArrayList<Cell>> cellData) {
        this.cellData = cellData;
    }

    public void setRowSize(int rowSize) {
        this.rowSize = rowSize;
    }

    public void setColSize(int colSize) {
        this.colSize = colSize;
    }
    public void setMinePercent(int minePercent) {
        this.minePercent = minePercent;
        double mineProbability = minePercent / 100.0;
        totalMines = (int) (rowSize * colSize * mineProbability);
    }

}

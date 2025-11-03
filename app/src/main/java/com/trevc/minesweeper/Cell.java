package com.trevc.minesweeper;

public class Cell {
    private int bombCount;
    private int cellState;

    public Cell(int surroundingBombs) {
        this.bombCount = surroundingBombs;
        this.cellState = 0;
    }

    public int getBombCount() {
        return bombCount;
    }

    public int getCellState() {
        return cellState;
    }

    public void setCellState(int newCellState) {
        cellState = newCellState;
    }
}

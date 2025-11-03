package com.trevc.minesweeper;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class GridViewAdapter_OLD extends BaseAdapter {
    ArrayList<ArrayList<Cell>> cellData;
    Context context;
    int cellRows, cellCols;
    public GridViewAdapter_OLD(Context context, ArrayList<ArrayList<Cell>> cellData, int cellRows, int cellCols) {
        this.context = context;
        this.cellData = cellData;
        this.cellRows = cellRows;
        this.cellCols = cellCols;
    }

    @Override
    public int getCount() {
        return cellRows * cellCols;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private int getColorCode(Resources resources, int colorIndex) {
        int[] colorArray = resources.getIntArray(R.array.cellColorCodes);
        return colorArray[colorIndex];
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflator = LayoutInflater.from(parent.getContext());
        View root = inflator.inflate(R.layout.cell, parent, false);
        FrameLayout frameLayout = root.findViewById(R.id.grid_cell);

        int row = position / cellRows;
        int col = position % cellCols;
        Cell cellItem = cellData.get(row).get(col);
        Button cellButton = root.findViewById(R.id.cellButton);
        TextView cellBombCountText = root.findViewById(R.id.cellBombCount);
        cellBombCountText.setText(
                cellItem.getCellState() == 0 // 0: hidden, 1: revealed, 2: flagged
                        ? ""
                        : String.valueOf(cellItem.getBombCount()
                )
        );
        cellButton.setOnClickListener((view) -> {
            if (cellItem.getBombCount() == -1) { // bombCount is -1 if bomb in cell
                // somehow end game
            } else {
                // reveal bomb count
                cellBombCountText.setText(String.valueOf(cellItem.getBombCount()));
            }
        });
        cellButton.setOnLongClickListener(new View.OnLongClickListener() {
              @Override
              public boolean onLongClick(View v) {
                  int cellState = cellItem.getCellState();
                  if (cellState == 1) { // dont handle already revealed cell
                    return false;
                  }

                  cellItem.setCellState(cellState == 0 ? 2 : 0);
//                  cellButton.getBackground().setTint(getColorCode(parent.getResources(), cellItem.getColor()));
                  return true;
              }
          }
        );
        return convertView;
    }
}

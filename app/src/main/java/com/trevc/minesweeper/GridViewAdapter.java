package com.trevc.minesweeper;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color; // Import for color
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
// FrameLayout is no longer needed in getView, but keep if you use it elsewhere
// import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class GridViewAdapter extends BaseAdapter {
    ArrayList<ArrayList<Cell>> cellData;
    Context context;
    int cellRows, cellCols, totalMines;
    int[] cellColors;

    public GridViewAdapter(Context context, ArrayList<ArrayList<Cell>> cellData, int cellRows, int cellCols, int totalMines,  int[] cellColors) {
        this.context = context;
        this.cellData = cellData;
        this.cellRows = cellRows;
        this.cellCols = cellCols;
        this.totalMines = totalMines;
        this.cellColors = cellColors;
    }

    @Override
    public int getCount() {
        return cellRows * cellCols;
    }

    @Override
    public Object getItem(int position) {
        // Correctly get the item instead of returning null
        int row = position / cellCols;
        int col = position % cellCols;
        return cellData.get(row).get(col);
    }

    @Override
    public long getItemId(int position) {
        return position; // Use position as a unique ID
    }

    private int getColorCode(Resources resources, int colorIndex) {
        int[] colorArray = resources.getIntArray(R.array.cellColorCodes);
        return colorArray[colorIndex];
    }

    private static class ViewHolder {
        Button cellButton;
        TextView cellBombCountText;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        // --- 1. ViewHolder Pattern Implementation ---
        if (convertView == null) {
            // If the view is new, inflate it
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.cell, parent, false);

            // Create a ViewHolder to store references
            holder = new ViewHolder();
            holder.cellButton = convertView.findViewById(R.id.cellButton);
            holder.cellBombCountText = convertView.findViewById(R.id.cellBombCount);

            // "Tag" the view with its holder
            convertView.setTag(holder);
        } else {
            // If the view is being recycled, get its holder
            holder = (ViewHolder) convertView.getTag();
        }

        // Row/Col Calculation
        int row = position / cellCols; // Must divide by number of columns
        int col = position % cellCols;
        Cell cellItem = cellData.get(row).get(col);

        holder.cellButton.setOnClickListener(null);
        holder.cellButton.setOnLongClickListener(null);

        switch (cellItem.getCellState()) {
            case 1: // 1: REVEALED
                holder.cellButton.setText(""); // Clear flag text

                if (cellItem.getBombCount() == -1) {
                    holder.cellBombCountText.setText("B"); // Show "B" for Bomb
                    holder.cellButton.getBackground().setTint(getColorCode(parent.getResources(), cellColors[3]));

                } else if (cellItem.getBombCount() > 0) {
                    holder.cellBombCountText.setText(String.valueOf(cellItem.getBombCount()));
                    holder.cellButton.getBackground().setTint(getColorCode(parent.getResources(), cellColors[1]));

                } else {
                    holder.cellBombCountText.setText(""); // Empty for "0" cells
                    holder.cellButton.getBackground().setTint(getColorCode(parent.getResources(), cellColors[1]));
                }
                break;

            case 2: // 2: FLAGGED
                holder.cellButton.getBackground().setTint(getColorCode(parent.getResources(), cellColors[2]));
                holder.cellButton.setText("F"); // Show "F" for Flag
                holder.cellBombCountText.setText(""); // Hide count
                break;

            case 0: // 0: HIDDEN
            default:
                holder.cellButton.getBackground().setTint(getColorCode(parent.getResources(), cellColors[0]));
                holder.cellButton.setText(""); // Clear flag text
                holder.cellBombCountText.setText(""); // Hide count
                break;
        }

        // Set New Listeners
        holder.cellButton.setOnClickListener((view) -> {
            // Only allow clicking on a hidden, unflagged cell
            if (cellItem.getCellState() == 0) {
                // UPDATE THE STATE
                cellItem.setCellState(1);

                if (cellItem.getBombCount() == -1) {

                    ((Game) context).endGame(false);
                } else {
                    // Player hit a safe cell
                    ((Game) context).checkWinCondition();
                }

                // Refresh grid
                notifyDataSetChanged();
            }
        });

        holder.cellButton.setOnLongClickListener((view) -> {
            if (cellItem.getCellState() == 1) { // Don't flag revealed cells
                return true; // Consume the click
            }

            int newState = 0;
            if (cellItem.getCellState() == 0) {
                newState = 2;
                totalMines--;
            } else {
                newState = 0;
                totalMines++;
            }
            cellItem.setCellState(newState);
            ((Game) context).updateMineCounter(totalMines);

            // Refresh grid
            notifyDataSetChanged();

            return true; // Consume the long click
        });

        return convertView;
    }
}
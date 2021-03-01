package dao;

import java.io.Serializable;

public class Location implements Serializable {
    private int row;
    private int col;

    public Location() {
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    @Override
    public String toString() {
        return  "row=" + row +
                ", col=" + col;
    }
}

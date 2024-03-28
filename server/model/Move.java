package server.model;

import java.io.Serializable;

public class Move implements Serializable  {
    private int row;
    private int col;

        public Move( int row, int col) {
            this.row = row;
            this.col = col;
        }

        public int getRow() {
            return this.row;
        }

        public int getCol() {
            return this.col;
        }
}

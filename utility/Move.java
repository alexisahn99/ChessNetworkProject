package utility;

import java.io.Serializable;

public class Move implements Serializable  {
    private int row;
    private int col;
    private int port;

        public Move( int row, int col, int portNumber) {
            this.row = row;
            this.col = col;
            this.port = portNumber;
        }

        public int getRow() {
            return this.row;
        }

        public int getCol() {
            return this.col;
        }

        public int getPortNum() {
            return this.port;
        }
}

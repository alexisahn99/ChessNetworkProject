package server;

import server.controller.*;
import server.model.ChessPieces.*;
import utility.Move;
import utility.Tuple;


public class GameLogic {
    private Controller controller = new Controller();

    public GameLogic() {}

    Tuple checkMove(ChessPieceColor playerColor, Move move, int centralPortNum) {

        if (playerColor == controller.getCurrentPlayer()) {

            System.out.println("Data read");

            Tuple moveResponse = controller.userPressed(move.getRow(), move.getCol(), move.getPortNum()); 

            moveResponse.setCentralPortNum(centralPortNum);
            
            if (playerColor != moveResponse.getCurrentPlayerColor()) {
                // Logic to repaint the board
            }
            
            return moveResponse;
        }
        return null;
    }
}

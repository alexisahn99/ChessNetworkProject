package server;

import server.model.Move;
import server.controller.*;
import server.model.ChessPieces.*;


public class GameLogic {
    private ChessPieceColor curTurn;
    private ChessPieceColor prevTurn;
    private Controller controller = new Controller(null);

    public GameLogic() {
        this.prevTurn = ChessPieceColor.W;
    }

 

    Tuple checkMove(ChessPieceColor playerColor, Move move) {

        if (prevTurn == playerColor && (playerColor == ChessPieceColor.W || playerColor == ChessPieceColor.B)) {

            System.out.println("Data read");

            Tuple moveResponse = controller.userPressed(move.getRow(), move.getCol());
            curTurn = moveResponse.getCurrentPlayerColor();

            if (curTurn != prevTurn) {
                prevTurn = curTurn;
            }    
            return moveResponse;
        }
        return null;
    }
}

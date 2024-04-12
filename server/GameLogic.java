package server;

import server.controller.*;
import server.model.ChessPieces.*;
import utility.Move;
import utility.Tuple;


public class GameLogic {
    private Controller controller;

    public GameLogic() {
        this.controller = new Controller();
    }
    
    public Tuple checkMove(ChessPieceColor playerColor, Move move, int centralPortNum) {
        if (playerColor == controller.getCurrentPlayer()) {
            // System.out.println("GameLogic: Data read");
            Tuple moveResponse = controller.userPressed(move.getRow(), move.getCol(), move.getPortNum());
            moveResponse.setCentralPortNum(centralPortNum);
            return moveResponse;
        }
        else {
            return null;
        }
    }

    public ChessPieceColor getCurrentPlayer() {
        return controller.getCurrentPlayer();
    }


    public Tuple getAllChessPieces() {
        return controller.getAllChessPieces();
    }

    public Tuple getPlayerChessPieces() {
        return controller.getPlayerChessPieces();
    }

    public boolean isCheck() {
        return controller.isCheck();
    }

    public boolean isCheckMate() {
        return controller.isCheckMate();
    }
    
}

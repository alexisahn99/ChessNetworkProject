package server.controller;

import java.util.ArrayList;

import server.model.ChessBoard;
import server.model.ChessPieces.ChessPiece;

public class Controller {
    private ChessBoard board;
    private ChessPiece currentChessPiece;

    public Controller(ChessBoard board) {
        this.board = board;
        ArrayList<int[]> allCurrentPieces = this.board.getPiecesLocation(this.board.getCurrentPlayer());
    }
}

package server.controller;

import java.util.ArrayList;
import server.model.ChessPieces.ChessPieceColor;

public class Tuple {
    private boolean function_success;
    private ArrayList<int[]> chess_pieces;
    private boolean game_over;
    private ChessPieceColor current_player;

    public Tuple(boolean function_success, ArrayList<int[]> chess_pieces, boolean game_over, ChessPieceColor current_player) {
        this.function_success = function_success;
        this.chess_pieces = chess_pieces;
        this.game_over = game_over;
        this.current_player = current_player;
    }

    public boolean getFunctionSuccess() {
        return function_success;
    }

    public ArrayList<int[]> getChessPieces() {
        return chess_pieces;
    }

    public boolean getGameOver() {
        return game_over;
    }

    public ChessPieceColor getCurrentPlayerColor() {
        return current_player;
    }
}

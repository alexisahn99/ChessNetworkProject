package server.model.ChessPieces;

public enum ChessPieceColor {
    W("white"),
    B("black"),
    R("red");   // Used to indicate non-player users observing the game

    ChessPieceColor(String s)
    {
        //System.out.println("ChessPieceColor constructor is called");
    }
}



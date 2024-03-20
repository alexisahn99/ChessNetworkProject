package server.model.ChessPieces;

import server.model.ChessBoard;
import server.UnicodeMap;

import java.util.ArrayList;

public class Queen extends ChessPiece
{
    private ChessPieceColor color = ChessPieceColor.B;
    private ArrayList<ChessPiece> pieces;

    public Queen(int row, int col, ChessBoard board, ChessPieceColor color)
    {
        super(row, col, board, color);
        pieces = new ArrayList<ChessPiece>();
        pieces.add(new Bishop(row, col, board, color));
        pieces.add(new Rook(row, col, board, color));
    }

    public ArrayList<ArrayList<int[]>> getLegalSquares()
    {
        ArrayList<ArrayList<int[]>> legalSquares = new ArrayList<ArrayList<int[]>>();
        for (ChessPiece piece: this.pieces)
        {
            legalSquares.addAll(piece.getLegalSquares());
        }
        return legalSquares;
    }
    
    public ArrayList<int[]> getMovableSquares()
    {
        ArrayList<int[]> movableSquares = new ArrayList<int[]>();
        for (ChessPiece piece: this.pieces)
        {
            movableSquares.addAll(piece.getMovableSquares());
        }
        return movableSquares;
    }

    @Override
    public void move(int newRow, int newCol)
    {
        super.move(newRow, newCol);
        for (ChessPiece piece: this.pieces)
        {
            piece.move(newRow, newCol);
        }
    }

    public String getLabel() {
        String result;
        if (super.color == ChessPieceColor.B)
        {
            result = UnicodeMap.bQueen;
        }
        else
        {
            result = UnicodeMap.wQueen;
        }
        return result;
    }
}


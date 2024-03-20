package server.model.ChessPieces;

import server.model.ChessBoard;

import java.io.Serializable;
import java.util.ArrayList;

public abstract class ChessPiece implements Serializable
{
    protected int row;
    protected int col;
    protected boolean hasNotMoved;
    protected boolean ableToEnPassant;
    protected ChessBoard board;
    protected ChessPieceColor color;

    public ChessPiece(int row, int col, ChessBoard board, ChessPieceColor color)
    {
        this.row = row;
        this.col = col;
        this.hasNotMoved = true;
        this.ableToEnPassant = false;
        this.board = board;
        this.color = color;
    }

    public int getCurrentRow()
    {
        return this.row;
    }
    public int getCurrentCol()
    {
        return this.col;
    }

    public ChessPieceColor getColor()
    {
        return this.color;
    }

    public void move(int newRow, int newCol)
    {
        this.row = newRow;
        this.col = newCol;
        if (this.hasNotMoved)
        {
            this.hasNotMoved = false;
        }
    }

    public void resetEnPassant()
    {
        this.ableToEnPassant = false;
    }

    public ArrayList<int[]> getMovableSquares()
    {
        ArrayList<ArrayList<int[]>> legalSquares = this.getLegalSquares();

        ArrayList<int[]> movableSquares = new ArrayList<int[]>();
        for (ArrayList<int[]> list : legalSquares)
        {
            for (int[] location : list)
            {
                int row = location[0];
                int col = location[1];
                ChessPiece piece = this.board.getChessPiece(row, col);

                if (piece == null)
                {
                    movableSquares.add(location);
                }
                else
                {
                    if (piece.getColor() != this.color)
                    {
                        movableSquares.add(location);
                    }
                    break;
                }
            }
        }
        return movableSquares;
    }

    public abstract ArrayList<ArrayList<int[]>> getLegalSquares();
    public abstract String getLabel();
}

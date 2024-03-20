package server.model.ChessPieces;

import server.model.ChessBoard;
import server.UnicodeMap;

import java.util.ArrayList;
import java.lang.Math;

public class Knight extends ChessPiece
{
    private ArrayList<int[]> squares;

    public Knight(int row, int col, ChessBoard board, ChessPieceColor color)
    {
        super(row, col, board, color);
        
        squares = new ArrayList<int[]>();
                        squares.add(new int[]{-1, 2}); squares.add(new int[]{1, 2});
        squares.add(new int[]{-2, 1});                              squares.add(new int[]{2, 1});
        squares.add(new int[]{-2, -1});                             squares.add(new int[]{2, -1});
                        squares.add(new int[]{-1, -2}); squares.add(new int[]{1, -2});
    }

    // white pawn goes up the array from row index 6 to 5, 4...
    public ArrayList<ArrayList<int[]>> getLegalSquares()
    {
        ArrayList<ArrayList<int[]>> finalResult = new ArrayList<ArrayList<int[]>>();
        ArrayList<int[]> result = new ArrayList<int[]>();
        for (int[] square : this.squares)
        {
            if (super.row + square[0] > -1 & super.row + square[0] < 8 & super.col + square[1] > -1 & super.col + square[1] < 8)
            {
                result.add(new int[]{super.row + square[0], super.col + square[1]});
            }
        }
        finalResult.add(result);
        return finalResult;
    }

    @Override
    public ArrayList<int[]> getMovableSquares()
    {
        ArrayList<ArrayList<int[]>> legalSquares = this.getLegalSquares();
        ArrayList<int[]> movableSquares = new ArrayList<int[]>();
        for (int[] location : legalSquares.get(0))
        {
            int row = location[0];
            int col = location[1];
            ChessPiece piece = this.board.getChessPiece(row, col);
            if (piece != null)
            {
                if (piece.getColor() != super.color)
                {
                    movableSquares.add(location);
                }
            }
            else
            {
                movableSquares.add(location);
            }
        }
        return movableSquares;
    }

    public String getLabel() {
        String result;
        if (super.color == ChessPieceColor.B)
        {
            result = UnicodeMap.bKnight;
        }
        else
        {
            result = UnicodeMap.wKnight;
        }
        return result;
    }

}


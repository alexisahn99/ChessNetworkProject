package server.model.ChessPieces;

import server.model.ChessBoard;
import server.UnicodeMap;

import java.util.ArrayList;

public class Bishop extends ChessPiece
{
    public Bishop(int row, int col, ChessBoard board, ChessPieceColor color)
    {
        super(row, col, board, color);
    }
    
    public ArrayList<ArrayList<int[]>> getLegalSquares()
    {
        ArrayList<ArrayList<int[]>> finalResult = new ArrayList<ArrayList<int[]>>();

        int i = 1;
        ArrayList<int[]> result1 = new ArrayList<int[]>();
        while (super.row + i < 8 & super.col + i < 8)
        {
            result1.add(new int[]{super.row + i, super.col + i});
            i++;
        }
        finalResult.add(result1);

        i = 1;
        ArrayList<int[]> result2 = new ArrayList<int[]>();
        while (super.row + i < 8 & super.col - i > -1)
        {
            result2.add(new int[]{super.row + i, super.col - i});
            i++;
        }
        finalResult.add(result2);

        i = 1;
        ArrayList<int[]> result3 = new ArrayList<int[]>();
        while (super.row - i > -1 & super.col + i < 8)
        {
            result3.add(new int[]{super.row - i, super.col + i});
            i++;
        }
        finalResult.add(result3);

        i = 1;
        ArrayList<int[]> result4 = new ArrayList<int[]>();
        while (super.row - i > -1 & super.col - i > -1)
        {
            result4.add(new int[]{super.row - i, super.col - i});
            i++;
        }
        finalResult.add(result4);
        return finalResult;
    }

    public String getLabel() {
        String result;
        if (super.color == ChessPieceColor.B)
        {
            result = UnicodeMap.bBishop;
        }
        else
        {
            result = UnicodeMap.wBishop;
        }
        return result;
    }

}


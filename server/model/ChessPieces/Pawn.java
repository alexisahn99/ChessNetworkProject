package server.model.ChessPieces;

import server.model.ChessBoard;
import server.UnicodeMap;

import java.util.ArrayList;

public class Pawn extends ChessPiece
{
    private ArrayList<int[]> squares = new ArrayList<int[]>();
    private int hasNotMovedForward;
    private int rowForward;

    public Pawn(int row, int col, ChessBoard board, ChessPieceColor color)
    {
        super(row, col, board, color);
        if (super.color == ChessPieceColor.B)
        {
            rowForward = 1;
            hasNotMovedForward = 2;
        }
        else
        {
            rowForward = -1;
            hasNotMovedForward = -2;
        }            
        squares.add(new int[]{rowForward, -1});
        squares.add(new int[]{rowForward, 1});
        squares.add(new int[]{rowForward, 0});
    }

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
        if (super.hasNotMoved)
        {
            result.add(new int[]{super.row + hasNotMovedForward, super.col});
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
            ChessPiece piece = super.board.getChessPiece(row, col);

            if (isDiagonal(row, col))
            {
                if (piece != null)
                {
                    if (piece.getColor() != super.color)
                    {
                        movableSquares.add(location);
                    }
                }
                // Check for En Passant
                ChessPiece potentialPawn = super.board.getChessPiece(row - rowForward, col);
                if (this.canEnPassant(potentialPawn))
                {
                    movableSquares.add(location);
                }
            }
            else
            {
                if (piece == null)
                {
                    movableSquares.add(location);
                }
                else
                {
                    break;
                }
            }
        }
        return movableSquares;
    }

    private boolean isPassing(int newRow, int newCol)
    {
        boolean result = false;
        if (newRow == super.row)
        {
            if (newCol == super.col - 1 | newCol == super.col + 1)
            {
                result = true;
            }
        }
        return result;
    }

    private boolean canEnPassant(ChessPiece potentialPassingPawn)
    {
        boolean result = false;
        if (potentialPassingPawn != null)
        {
            if (potentialPassingPawn.ableToEnPassant & potentialPassingPawn.getColor() != super.color)
            {
                result = true;
            }
        }
        return result;
    }

    private boolean isDiagonal(int newRow, int newCol)
    {
        boolean result = false;
        if (newRow == super.row + rowForward)
        {
            if (newCol == super.col - 1 | newCol == super.col + 1)
            {
                result = true;
            }
        }
        return result;
    }

    @Override
    public void move(int newRow, int newCol)
    {
        if (this.hasNotMoved)
        {
            this.hasNotMoved = false;
            if (super.row - newRow == 2 | super.row - newRow == -2)
            {
                super.ableToEnPassant = true;
            }
        }
        if (isDiagonal(newRow, newCol))
        {
            ChessPiece potentialPassingPawn = super.board.getChessPiece(newRow - rowForward, newCol);
            if (canEnPassant(potentialPassingPawn))
            {
                super.board.removePiece(newRow - rowForward, newCol, potentialPassingPawn);
            }
        }
        super.row = newRow;
        super.col = newCol;
    }
    
    public String getLabel() {
        String result;
        if (super.color == ChessPieceColor.B)
        {
            result = UnicodeMap.bPawn;
        }
        else
        {
            result = UnicodeMap.wPawn;
        }
        return result;
    }
}

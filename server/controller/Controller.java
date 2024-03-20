package server.controller;

import java.util.ArrayList;

import server.model.ChessBoard;
import server.model.ChessPieces.ChessPiece;
import server.model.ChessPieces.ChessPieceColor;

public class Controller {
    private ChessBoard board;
    private ChessPiece currentChessPiece;

    public Controller(ChessBoard board) {
        this.board = new ChessBoard();
    }

    public ChessPieceColor getCurrentPlayer()
    {
        return this.board.getCurrentPlayer();
    }

    public void switchPlayers() {
        if (this.board.getCurrentPlayer() == ChessPieceColor.W) 
        {
            this.board.setCurrentPlayer(ChessPieceColor.B);
        } 
        else 
        {
            this.board.setCurrentPlayer(ChessPieceColor.W);
        }
    }

    public ArrayList<int[]> selectPiece(int fromRow, int fromCol)
    // always returns movableSquares that a piece can move to. 
    // It may also return an empty movable squares. 
    // TODO: what happens when movableSquares.size() == 0
    {
        this.currentChessPiece = this.board.getChessPiece(fromRow, fromCol);
        ArrayList<int[]> movableSquares = this.board.getMovableSquares(this.currentChessPiece);
        if (movableSquares.size() == 0)
        {
            this.board.setClickCount(0);
            ArrayList<int[]> allCurrentPieces = this.board.getPiecesLocation(this.board.getCurrentPlayer());
        }
        return movableSquares;
    }

    public void selectDestination(int toRow, int toCol)
    {
        this.board.placeChessPiece(toRow, toCol, this.currentChessPiece);

        // Check if a pawn has reached the opposite end of the board
        if (this.board.isPawnAtEnd(this.currentChessPiece)) 
        {
            // TODO: convert pawn to new piece
        }
        else 
        {
            this.endOfTurn();
        }
    }

    public void userPressed(int row, int col)
    {
        int clickCount = this.board.getClickCount();
        clickCount++;
        this.board.setClickCount(clickCount);

        if (clickCount == 1)
        {
            this.selectPiece(row, col);

        }
        else if (clickCount == 2)
        {
            this.selectDestination(row, col);
            this.board.setClickCount(0);
        }
    }

    public void convertPawn(String unicode, ChessPieceColor color)
    {
        this.board.addNewPiece(this.currentChessPiece.getCurrentRow(), this.currentChessPiece.getCurrentCol(), unicode, color);
        this.endOfTurn();
    }

    public void endOfTurn()
    {
        this.switchPlayers();
        this.view.updateDisplay();

        // Check for game over
        if (this.board.isGameOver()) 
        {
            WinLossData writeData = new WinLossData();
            String winner;
            if (this.getCurrentPlayer() == ChessPieceColor.B)
            {
                winner = "White";
                writeData.addWinLossData("1");
            }
            else
            {
                winner = "Black";
                writeData.addWinLossData("0");
            }
            this.view.displayWinner(winner);
            System.out.println("Game over!");
        }
        else
        {
            ArrayList<int[]> allCurrentPieces = this.board.getPiecesLocation(this.board.getCurrentPlayer());
            this.view.enableSquares(allCurrentPieces);
        }
    }

}

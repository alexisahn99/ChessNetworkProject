package server.controller;

import java.util.ArrayList;
import server.model.ChessBoard;
import server.model.ChessPieces.ChessPiece;
import server.model.ChessPieces.ChessPieceColor;
import utility.FunctionFlag;
import utility.Tuple;

// TODO: removing pawn at the end option for now in selectDestination()
// TODO: when REPAINT we need to send info of what piece is where

public class Controller {
    private ChessBoard board;
    private ChessPiece currentChessPiece;

    public Controller() {
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

    public Tuple userPressed(int row, int col, int portNum)
    {
        int clickCount = this.board.incrementClickCount();

        if (clickCount == 1)
        {
            return this.selectPiece(row, col, portNum);
        }
        else if (clickCount == 0) {
            return this.getPlayerChessPieces();
        }
        else 
        {
            this.board.setClickCount(0); // if invalidMove, you reset selecting a piece. else, validMove
            return this.selectDestination(row, col, portNum);
        }
    }

    // getPlayerChessPieces is to enable current client's chess pieces when click = 0
    public Tuple getPlayerChessPieces() {
        FunctionFlag functionFlag = FunctionFlag.SOURCE;
        ArrayList<int[]> movableSquares = this.board.getPiecesLocation(this.getCurrentPlayer());
        boolean isCheck = this.board.isCheck();
        boolean isCheckMate = this.board.isCheckMate();
        if (isCheckMate) {
            functionFlag = FunctionFlag.CHECKMATE;
        }
        return new Tuple(functionFlag, movableSquares, null, isCheck, isCheckMate, this.getCurrentPlayer(), 0);
    }

    public Tuple getAllChessPieces() {
        FunctionFlag functionFlag = FunctionFlag.REPAINT;
        ArrayList<int[]> allCurrentPieces = this.board.getPiecesLocation();;
        ArrayList<String> allCurrentPieceUnicodes = new ArrayList<>();
        boolean isCheck = this.board.isCheck();
        boolean isCheckMate = this.board.isCheckMate();
        if (isCheckMate) {
            functionFlag = FunctionFlag.CHECKMATE;
        }

        for (int[] piece : allCurrentPieces) {
            int row = piece[0];
            int col = piece[1];
            String label = this.board.getChessPiece(row, col).getLabel();
            allCurrentPieceUnicodes.add(label);
        }

        return new Tuple(functionFlag, allCurrentPieces, allCurrentPieceUnicodes, isCheck, isCheckMate, this.getCurrentPlayer(), 0);
    }

    public Tuple selectPiece(int fromRow, int fromCol, int portNum) {
        FunctionFlag functionFlag = FunctionFlag.DESTINATION;
        this.currentChessPiece = this.board.getChessPiece(fromRow, fromCol);
        ArrayList<int[]> movableSquares = this.board.getMovableSquares(this.currentChessPiece);
        
        // if you click a piece that you cannot move
        if (movableSquares.size() == 0)
        {
            this.board.setClickCount(0);
            return this.getPlayerChessPieces();
        }
        else {
            return new Tuple(functionFlag, movableSquares, null, false, false, this.getCurrentPlayer(), portNum);
        }
        
    }

    public Tuple selectDestination(int toRow, int toCol, int portNum)
    {
        // 1. Determine whether it is a legal destination
        ArrayList<int[]> movableSquares = this.board.getMovableSquares(this.currentChessPiece);
        for (int[] square : movableSquares) {
            if (square[0] == toRow && square[1] == toCol) {
                // 1-1. Move is valid, so we return a tuple indicating this
                this.board.placeChessPiece(toRow, toCol, this.currentChessPiece);
                this.switchPlayers();
                return this.getAllChessPieces();
            }
        }
        // 1-2. If no valid move was found, we consider the move invalid
        this.board.setClickCount(0);
        return this.getPlayerChessPieces();
    }

    public boolean isCheck() {
        return this.board.isCheck();
    }

    public boolean isCheckMate() {
        return this.board.isCheckMate();
    }

    // TODO: never used
    // public void convertPawn(String unicode, ChessPieceColor color)
    // {
    //     this.board.addNewPiece(this.currentChessPiece.getCurrentRow(), this.currentChessPiece.getCurrentCol(), unicode, color);
    //     this.endOfTurn();
    // }

    // TODO: never used; need to implement for proper game server function
    // public boolean endOfTurn()
    // {
    //     this.switchPlayers();
    //     // Check for game over
    //     if (this.board.isGameOver()) 
    //     {
    //         System.out.println("Game over!");
    //     }
    //     else
    //     {
    //         ArrayList<int[]> allCurrentPieces = this.board.getPiecesLocation(this.board.getCurrentPlayer());
    //         // this.view.enableSquares(allCurrentPieces);
    //         return true;
    //     }
    //     return false;
    // }

}

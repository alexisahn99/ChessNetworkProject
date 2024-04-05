package server.controller;

import java.io.Serializable;
import java.util.ArrayList;
import server.model.ChessPieces.ChessPieceColor;

public class Tuple implements Serializable {
    private FunctionFlag functionFlag;
    private boolean isValidMove;
    private ArrayList<int[]> chessPieces;
    private boolean isGameOver;
    private ChessPieceColor currentPlayer;
    private int centralPortNumber;

    public Tuple(FunctionFlag functionFlag, 
                 boolean isValidMove, 
                 ArrayList<int[]> chessPieces, 
                 boolean isGameOver, 
                 ChessPieceColor currentPlayer,
                 int centralPortNumber) 
    {
        this.functionFlag = functionFlag;
        this.isValidMove = isValidMove;
        this.chessPieces = chessPieces;
        this.isGameOver = isGameOver;
        this.currentPlayer = currentPlayer;
        this.centralPortNumber = centralPortNumber;
    }

    public FunctionFlag getFunctionFlag() {
        return this.functionFlag;
    }

    public boolean getFunctionSuccess() {
        return this.isValidMove;
    }

    public ArrayList<int[]> getChessPieces() {
        return this.chessPieces;
    }

    public boolean getGameOver() {
        return this.isGameOver;
    }

    public ChessPieceColor getCurrentPlayerColor() {
        return this.currentPlayer;
    }

    public int getCentralPortNum() {
        return this.centralPortNumber;
    }

    public void setCentralPortNum(int portNum) {
        this.centralPortNumber = portNum;
    }
}

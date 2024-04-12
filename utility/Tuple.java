package utility;

import java.io.Serializable;
import java.util.ArrayList;

import server.model.ChessPieces.ChessPieceColor;

public class Tuple implements Serializable {
    private FunctionFlag functionFlag;
    private ArrayList<int[]> chessPieceLocations;
    private ArrayList<String> chessPieceUnicodes;
    private boolean isCheck;
    private boolean isCheckMate;
    private ChessPieceColor currentPlayer;
    private int centralPortNumber;

    public Tuple(FunctionFlag functionFlag, 
                 ArrayList<int[]> chessPieceLocations, 
                 ArrayList<String> chessPieceUnicodes,
                 boolean isCheck, 
                 boolean isCheckMate, 
                 ChessPieceColor currentPlayer,
                 int centralPortNumber) 
    {
        this.functionFlag = functionFlag;
        this.chessPieceLocations = chessPieceLocations;
        this.chessPieceUnicodes = chessPieceUnicodes;
        this.isCheck = isCheck;
        this.isCheckMate = isCheckMate;
        this.currentPlayer = currentPlayer;
        this.centralPortNumber = centralPortNumber;
    }

    public FunctionFlag getFunctionFlag() {
        return this.functionFlag;
    }

    public ArrayList<int[]> getChessPieces() {
        return this.chessPieceLocations;
    }

    public ArrayList<String> getChessPieceUnicode() {
        return this.chessPieceUnicodes;
    }

    public boolean isCheck() {
        return this.isCheck;
    }

    public boolean isCheckMate() {
        return this.isCheckMate;
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

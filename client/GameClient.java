package client;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

import server.model.ChessPieces.ChessPieceColor;
import utility.FunctionFlag;
import utility.Tuple;


public class GameClient {
    private static ObjectOutputStream out;
    private static ObjectInputStream in;
    private static GameView gameView;

    private static int centralPortNum;

    public static void main(String[] args) {
        int selfPortNum;
        String userName;
        if (args.length == 2) {
            selfPortNum = Integer.parseInt(args[0]);
            userName = args[1];
        }
        else {
            System.out.println("Usage: java GameClient <selfPortNum> <userName>");
            return;
        }
    
        String hostname = "127.0.0.1"; //args[0];
        int gameServerPort = 21001; // Integer.parseInt(args[1]);

        try {
            Socket clientSocket = new Socket(hostname, gameServerPort);
            // System.out.println("Connected to server.");

            out = new ObjectOutputStream(clientSocket.getOutputStream());
            in = new ObjectInputStream(clientSocket.getInputStream());
            gameView = new GameView(selfPortNum, userName, out);
            gameView.initializeDisplay();

            while(true){
                try{
                    Object input = in.readObject();
                    handleInput(input);
                } catch(ClassNotFoundException err){
                    System.out.println("ERROR in GameClient: Couldn't read from action input stream");
                }
            }
        } catch (IOException err) {
            System.out.println("ERROR in GameClient: I/O error creating socket - " + err.getMessage());
        }
    }

    private static void handleInput(Object input){
        if(input instanceof Tuple){
            Tuple tuple = (Tuple) input;
            FunctionFlag flag = tuple.getFunctionFlag();
            ArrayList<int[]> pieceLocations = tuple.getChessPieces();
            ChessPieceColor currPlayer = tuple.getCurrentPlayerColor();
            ArrayList<String> unicodes = tuple.getChessPieceUnicode();

            gameView.displayCurrentPlayer(currPlayer);
            
            if(tuple.isCheck()){
                gameView.displayCheckStatus();
            }
            switch(flag){
                case DESTINATION:
                    //Outline all possible moves for player
                    gameView.drawPossibleMoves(pieceLocations);
                    break;
                case SOURCE:
                    //Enable the squares for player to move
                    gameView.enableSquares(pieceLocations);
                    break;
                case REPAINT:
                    //Update the screen for the next players turn
                    gameView.update(pieceLocations, unicodes);
                    break;
                case DISABLE:
                    gameView.disableBoard();
                    break;
                case CHECKMATE:
                    gameView.disableBoard();
                    gameView.update(pieceLocations, unicodes);
                    gameView.displayCheckMateStatus(currPlayer);
                    break;
                case PORT:
                    // set port number
                    gameView.initPeerToPeer(tuple.getCentralPortNum());
                    break;
                default:
                    //incorrect flag recieved ?
                    break;
            }
        }
    }
}

package client;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

import server.model.ChessPieces.ChessPieceColor;
import utility.FunctionFlag;
import utility.Tuple;
import peer_to_peer.Peer;


public class GameClient {
    private static ObjectOutputStream out;
    private static ObjectInputStream in;
    private static GameView gameView;
    private static int clientID;
    private static String userName;
    private static Peer peer;

    public static void main(String[] args) {
        /* 
        if (args.length < 2) {
            System.out.println("Usage: java TestClient <server IP> <port number>");
            return;
        }
        */
        String hostname = "127.0.0.1"; //args[0];
        int port = 21001; // Integer.parseInt(args[1]);

        try {
            Socket clientSocket = new Socket(hostname, port);
            // System.out.println("Connected to server.");

            out = new ObjectOutputStream(clientSocket.getOutputStream());
            in = new ObjectInputStream(clientSocket.getInputStream());
            clientID = 10; //TODO: Hardcoded for now
            userName = "Brandon";
            gameView = new GameView(clientID, out);
            gameView.setInitDisplay();

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

            gameView.updateDisplay(currPlayer);
            //if king is not in checkmate (game still going)
            if(!tuple.isCheckMate()){
                if(tuple.isCheck() == true){
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
                    default:
                        //incorrect flag recieved ?
                        break;
                }
            }
            else {
                //TODO: Handle Game Over (CHECK MATE) Here
                // 1. display current player color
                // 2. is check
                // 3. is game over, who is winner?
                // gameView.update(pieceLocations, unicodes);
                // System.out.println(pieceLocations.size());
                // System.out.println(unicodes.size());
                gameView.displayCheckMateStatus(currPlayer);
            }
        }
    }
}

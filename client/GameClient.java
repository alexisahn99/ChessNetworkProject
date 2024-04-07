package client;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import javax.swing.*;

import client.ChessPieces;
import client.Chat;
import client.GameView;
import peer_to_peer.Connection;
import peer_to_peer.Peer;
import server.controller.FunctionFlag;
import server.controller.Tuple;
import server.model.Move;
import java.awt.*;
import java.awt.event.*;

public class GameClient {
    private static ObjectOutputStream out;
    private static ObjectInputStream in;
    private static ChatGUI chatGUI;
    private static GameView gameView;
    private static int clientID;

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
            System.out.println("Connected to server.");

            out = new ObjectOutputStream(clientSocket.getOutputStream());
            in = new ObjectInputStream(clientSocket.getInputStream());
            clientID = 10; //Hardcoded for now
            gameView = new GameView(clientID, out);

            while(true){
                try{
                    Object input = in.readObject();
                    handleInput(input);
                } catch(ClassNotFoundException err){
                    System.out.println("Error: Couldnt read from action input stream");
                }
            }
        } catch (IOException err) {
            System.out.println("I/O error creating socket: " + err.getMessage());
        }
    }

    private static void handleInput(Object input){
        if(input instanceof Tuple){
            Tuple tuple = (Tuple) input;
            FunctionFlag flag = tuple.getFunctionFlag();
            ArrayList<int[]> pieceLocations = tuple.getChessPieces();
            if(!tuple.getFunctionSuccess()){
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
                        ArrayList<String> unicodes = tuple.getChessPieceUnicode();
                        gameView.update(pieceLocations, unicodes);
                        break;
                    default:
                        //incorrect flag recieved ?
                        break;
                }
                if(tuple.getGameOver()){
                    //TODO
                }
            }
            else{
                System.out.println("ERROR: UNSUCCESSFUL FUNCTION");
            }
        }
    }
}

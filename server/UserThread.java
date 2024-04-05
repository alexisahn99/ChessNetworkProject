package server;

import java.io.*;
import java.net.*;

import server.model.Move;
import server.controller.*;
import server.model.ChessPieces.*;
 
/**
 * This thread handles connection for each connected client, so the server
 * can handle multiple clients at the same time.
 */
public class UserThread extends Thread {
    private Socket socket;
    private GameServer server;
    private GameLogic gameLogic;
    private ObjectOutputStream out;
    private ChessPieceColor curTurn;
    private ChessPieceColor prevTurn;
    private ChessPieceColor playerColor;
 
    public UserThread(Socket socket, GameServer server, GameLogic gameLogic) {
        this.socket = socket;
        this.server = server;
        this.gameLogic = gameLogic;
        prevTurn = ChessPieceColor.W;
    }
 
    public void run() {
        try {
            System.out.println("UserThread Running");

            out = new ObjectOutputStream(socket.getOutputStream());

            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
 
            System.out.println("Input and Output streams setup");
  
            while(true) {
                try {
                    
                        Move curMove = (Move) in.readObject();

                        Tuple logicCheck = gameLogic.checkMove(playerColor, curMove);

                        if(logicCheck != null) {
                            server.broadcast(logicCheck, this);
                        }                 

                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (EOFException e) {}
            }
 
        } catch (IOException ex) {
            System.out.println("Error in UserThread: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
 
 
    /**
     * Sends a data to the client.
     */
    void sendMove(Tuple move) {
        try {
            out.writeObject(move);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void setPlayerColor(ChessPieceColor color) {
        playerColor = color;
    }
}
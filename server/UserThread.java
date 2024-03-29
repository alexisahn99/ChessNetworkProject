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
    private ServerTest2 server;
    private ObjectOutputStream out;
    private ChessPieceColor curTurn;
    private ChessPieceColor prevTurn;
    private ChessPieceColor playerColor;
 
    public UserThread(Socket socket, ServerTest2 server) {
        this.socket = socket;
        this.server = server;
        prevTurn = ChessPieceColor.W;
    }
 
    public void run() {
        try {
            System.out.println("UserThread Running");

            out = new ObjectOutputStream(socket.getOutputStream());

            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
 
            System.out.println("Input and Output streams setup");
 
            Controller controller = new Controller(null);
 
            while(true) {
                try {
                    
                    if (prevTurn == playerColor) {
                        Move curMove = (Move) in.readObject();

                        System.out.println("Data read");
    
                        Tuple moveResponse = controller.userPressed(curMove.getRow(), curMove.getCol());
                        curTurn = moveResponse.getCurrentPlayerColor();
    
                        server.broadcast(moveResponse, this);
    
    
                        if (curTurn != prevTurn) {
                            prevTurn = curTurn;
                            // Send message to repaint the board. 
                        }    
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
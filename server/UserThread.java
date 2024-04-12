package server;

import java.io.*;
import java.net.*;

import server.model.ChessPieces.*;
import utility.Move;
import utility.Tuple;
import utility.FunctionFlag;
 
/**
 * This thread handles connection for each connected client, so the server
 * can handle multiple clients at the same time.
 */
public class UserThread extends Thread {
    private Socket socket;
    private GameServer server;
    private GameLogic gameLogic;
    private ObjectOutputStream out;
    private ChessPieceColor playerColor;
    private int playerPortNumber;
 
    public UserThread(Socket socket, GameServer server, GameLogic gameLogic) {
        this.socket = socket;
        this.server = server;
        this.gameLogic = gameLogic;
    }
 
    public void run() {
        try {
            // System.out.println("UserThread: Running");
            out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            // System.out.println("UserThread: Input and Output streams setup");

            // initialize board view when you first join
            this.sendMove(gameLogic.getAllChessPieces());
            Tuple temp = new Tuple(FunctionFlag.PORT, null, null, false, false, null, server.getCentralPortNum());
            this.sendMove(temp);
            if (this.playerColor != gameLogic.getCurrentPlayer()) {
                this.disableBoard(gameLogic.getCurrentPlayer());
            }
            else {
                this.sendMove(gameLogic.getPlayerChessPieces());
            }
            
  
            while(true) {
                try {
                    
                    // Move contains [row, col, port#]
                    Move curMove = (Move) in.readObject();
                    playerPortNumber = curMove.getPortNum();
                    
                    Tuple tuple = gameLogic.checkMove(playerColor, curMove, playerPortNumber);

                    if (tuple == null) {
                        // do nothing, wrong turn
                    }
                    else {
                        if(tuple.getFunctionFlag() == FunctionFlag.CHECKMATE) {
                            server.endGameBroadcast(tuple);
                        }
                        else if(tuple.getFunctionFlag() == FunctionFlag.REPAINT) {
                            // end of turn, everybody needs to repaint
                            ChessPieceColor currentPlayerColor = tuple.getCurrentPlayerColor();
                            server.broadcast(tuple, currentPlayerColor);
                            this.disableBoard(currentPlayerColor);
                        } else {
                            // YOUR turn is not over yet
                            this.sendMove(tuple);
                        }
                    }
                                

                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (EOFException e) {}
            }
 
        } catch (IOException ex) {
            System.out.println("ERROR in UserThread: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void disableBoard(ChessPieceColor currentPlayerColor) {
        // your turn is over, disable the board
        this.sendMove(new Tuple(FunctionFlag.DISABLE, null, null, gameLogic.isCheck(), gameLogic.isCheckMate(), currentPlayerColor, playerPortNumber));
    }
 
 
    /**
     * Sends a data to the client.
     */
    public void sendMove(Tuple move) {
        try {
            out.writeObject(move);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ChessPieceColor getPlayerColor() {
        return this.playerColor;
    }

    public void setPlayerColor(ChessPieceColor color) {
        this.playerColor = color;
        // System.out.println("UserThread: Player pieces set to "+ color.toString());
    }

    public int getPortNumber() {
        return this.playerPortNumber;
    }

}
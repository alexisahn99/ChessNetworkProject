package server;

import java.io.*;
import java.net.*;
import java.util.*;

import server.model.ChessPieces.ChessPieceColor;
import utility.Tuple;
import peer_to_peer.*;

public class GameServer {
    private int selfPortNum;
    private Set<UserThread> userThreads;
    private int userNum;
    private int chatPortNum;
    private GameLogic gameLogic;
    private Peer peer;
    private PrintWriter headOut;
 
    public GameServer(int selfPortNum, int chatPortNum) {
        this.selfPortNum = selfPortNum;
        this.chatPortNum = chatPortNum;
        this.userThreads = new HashSet<>();
        this.userNum = 0;
        this.gameLogic = new GameLogic();
        this.initPeerToPeer();
      
        String hostname = "127.0.0.1";
        int headport = 32156; // Integer.parseInt(args[1]);
 
        // Connect to the head server
        try  {
            Socket clientSocket = new Socket(hostname, headport);
            System.out.println("Game Server: Connected to head server.");
            this.headOut = new PrintWriter(clientSocket.getOutputStream(), true);
            this.headOut.println("server");

        } catch (IOException err) {
            System.out.println("ERROR in Game Server: I/O error creating socket with head server: " + err.getMessage());
        }
    }
    
 
    public void execute() {
        try (ServerSocket serverSocket = new ServerSocket(selfPortNum)) {
 
            // System.out.println("GameServer: listening on selfPortNum " + selfPortNum);
 
            while (true) {
            
                Socket socket = serverSocket.accept();
                // System.out.println("GameServer: New user connected");

                userNum++;
                UserThread newUser = new UserThread(socket, this, this.gameLogic);
                userThreads.add(newUser);
                if (userNum == 1) {
                    newUser.setPlayerColor(ChessPieceColor.W);
                } else if (userNum == 2) {
                    newUser.setPlayerColor(ChessPieceColor.B);
                } else {
                    newUser.setPlayerColor(ChessPieceColor.R);
                }

                newUser.start();
                
            }
 
        } catch (IOException ex) {
            System.out.println("ERROR in Game Server: error in the server: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
       
    /**
     * Delivers data from one user to others (broadcasting)
     */
    public void broadcast(Tuple result, ChessPieceColor playerColor) {
        for (UserThread aUser : userThreads) {
                aUser.sendMove(result);
                if (aUser.getPlayerColor() == playerColor) {
                    // enable your squares
                    aUser.sendMove(this.gameLogic.getPlayerChessPieces());
                }
            }
    }

    public void endGameBroadcast(Tuple result) {
        for (UserThread aUser : userThreads) {
                aUser.sendMove(result);
            }
    }

    public int getCentralPortNum() {
        return this.chatPortNum;
    }

    public void initPeerToPeer(){
        this.peer = new Peer(this.chatPortNum, "server");
        this.peer.start();
    }
}

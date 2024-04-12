package server;

import java.io.*;
import java.net.*;
import java.util.*;

import client.GameClient;
import server.model.ChessPieces.ChessPieceColor;
import utility.Tuple;

public class GameServer {
    private int port;
    private Set<UserThread> userThreads = new HashSet<>();
    private int userNum = 0;
    private int centralPortNum = 0;
    //private static OutputStream out;
    private PrintWriter out;
 
    public GameServer(int port) {
        this.port = port;
        String hostname = "127.0.0.1";
        int headport = 32156; // Integer.parseInt(args[1]);
 
        // Connect to the head server
        try  {
            Socket clientSocket = new Socket(hostname, headport);
            System.out.println("Game Server: Connected to head server.");
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            out.println("server");

        } catch (IOException err) {
            System.out.println("ERROR in Game Server: I/O error creating socket with head server: " + err.getMessage());
        }
        execute();
    }
 
    public void execute() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
 
            System.out.println("Game Server: listening on port " + port);
 
            while (true) {
            
                Socket socket = serverSocket.accept();
                System.out.println("Game Server: New user connected");

                userNum++;
                GameLogic gameLogic = new GameLogic();
                UserThread newUser = new UserThread(socket, this, gameLogic);
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
    public void broadcast(Tuple result, UserThread excludeUser) {
        for (UserThread aUser : userThreads) {
                aUser.sendMove(result);
            }
    }

    public void setCentralPortNum(int portNum) {
        centralPortNum = portNum;
    }

    public int getCentralPortNum() {
        return centralPortNum;
    }
}

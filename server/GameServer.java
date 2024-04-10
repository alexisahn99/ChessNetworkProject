package server;

import java.io.*;
import java.net.*;
import java.util.*;

import client.GameClient;
import server.model.ChessPieces.ChessPieceColor;
import utility.Tuple;

public class GameServer {
    private int port;
    private Set<UserThread> userThreads;
    private int userNum;
    private int centralPortNum;
    private static OutputStream out;
    private GameLogic gameLogic;
 
    public GameServer(int port) {
        this.port = port;
        this.userThreads = new HashSet<>();
        this.userNum = 0;
        this.centralPortNum = 0;
        this.gameLogic = new GameLogic();
    }
 
    public void execute() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
 
            System.out.println("Game Server: listening on port " + port);
 
            while (true) {
            
                Socket socket = serverSocket.accept();
                System.out.println("Game Server: New user connected");

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
 
    public static void main(String[] args) {
        /*
        if (args.length < 1) {
            System.out.println("Syntax: java GameServer <port-number>");
            System.exit(0);
        }
        */


        // TODO change so that this number is not static. Will otherwise throw an error when connecting
        // multiple GameServers to the head server
        int port = 21001;  //Integer.parseInt(args[0]);

        String hostname = "127.0.0.1";
        int headport = 32156; // Integer.parseInt(args[1]);
 
        // Connect to the head server
        try  {
            Socket clientSocket = new Socket(hostname, headport);
            System.out.println("Game Server: Connected to head server.");
            out = clientSocket.getOutputStream();
            DataOutputStream dataOut = new DataOutputStream(out);
            dataOut.writeUTF("Server");

        } catch (IOException err) {
            System.out.println("ERROR in Game Server: I/O error creating socket with head server: " + err.getMessage());
        }

        // Create the game server and run it
        GameServer server = new GameServer(port);
        server.execute();
    }
 
    /**
     * Delivers data from one user to others (broadcasting)
     */
    public void broadcast(Tuple result, ChessPieceColor playerColor) {
        for (UserThread aUser : userThreads) {
                aUser.sendMove(result);
                if(aUser.getPlayerColor() == playerColor) {
                    // enable your squares
                    aUser.sendMove(this.gameLogic.currentPlayerPieces());
                }
            }
    }

    public void setCentralPortNum(int portNum) {
        centralPortNum = portNum;
    }

    public int getCentralPortNum() {
        return centralPortNum;
    }
}

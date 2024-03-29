package server;

import java.io.*;
import java.net.*;
import java.util.*;
import server.controller.Tuple;
import server.model.ChessPieces.ChessPieceColor;


public class ServerTest2 {
    private int port;
    private Set<UserThread> userThreads = new HashSet<>();
    private int userNum = 0;
 
    public ServerTest2(int port) {
        this.port = port;
    }
 
    public void execute() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
 
            System.out.println("Game Server is listening on port " + port);
 
            while (true) {
            
                Socket socket = serverSocket.accept();
                System.out.println("New user connected");

                userNum++;
 
                UserThread newUser = new UserThread(socket, this);
                userThreads.add(newUser);
                if (userNum == 1) {
                    newUser.setPlayerColor(ChessPieceColor.W);
                } else if (userNum == 1) {
                    newUser.setPlayerColor(ChessPieceColor.B);
                } else {
                    newUser.setPlayerColor(null);
                }
                
                newUser.start();
            }
 
        } catch (IOException ex) {
            System.out.println("Error in the server: " + ex.getMessage());
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
 
        int port = 21001;  //Integer.parseInt(args[0]);
 
        ServerTest2 server = new ServerTest2(port);
        server.execute();
    }
 
    /**
     * Delivers data from one user to others (broadcasting)
     */
    void broadcast(Tuple result, UserThread excludeUser) {
        for (UserThread aUser : userThreads) {
            if (aUser != excludeUser) {
                aUser.sendMove(result);
            }
        }

    }
}
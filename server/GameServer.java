package server;

import java.io.*;
import java.net.*;
import java.util.*;

import javax.swing.SwingUtilities;

import client.Client;
import server.controller.Tuple;
import server.model.ChessPieces.ChessPieceColor;


public class GameServer {
    private int port;
    private Set<UserThread> userThreads = new HashSet<>();
    private int userNum = 0;
 
    public GameServer(int port) {
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
                    newUser.setPlayerColor(ChessPieceColor.R);
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


        // TODO change so that this number is not static. Will otherwise throw an error when connecting
        // multiple GameServers to the head server
        int port = 21001;  //Integer.parseInt(args[0]);

        String hostname = "127.0.0.1";
        int headport = 32156; // Integer.parseInt(args[1]);
 
        // Connect to the head server
        try  {
            Socket clientSocket = new Socket(hostname, headport);
            System.out.println("Connected to head server.");

        } catch (IOException err) {
            System.out.println("I/O error creating socket with head server: " + err.getMessage());
        }

        // Create the game server and run it
        GameServer server = new GameServer(port);
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

package head_server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

import head_server.GameThread;;

public class HeadServer {
    private int port;
    private Set<GameThread> gameThreads = new HashSet<>();
 
    public HeadServer(int port) {
        this.port = port;
    }


    public void execute() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
 
            System.out.println("Head Server is listening on port " + port);
 
            while (true) {
            
                Socket socket = serverSocket.accept();
                System.out.println("New Game Server connected");
 
                GameThread newGame = new GameThread(socket, this);
                gameThreads.add(newGame);

                newGame.start();
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
 
        int port = 32156;  //Integer.parseInt(args[0]);
 
        HeadServer server = new HeadServer(port);
        server.execute();
    }
 
}


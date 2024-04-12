package peer_to_peer;

import java.io.*;
import java.net.*;
import java.util.*;

public class Peer {
    private final int selfPortNum;
    private final Set<Connection> connections = Collections.synchronizedSet(new HashSet<>());
    private ServerSocket serverSocket;
    private PeerGUI gui;
    private String userName;

    public Peer(int selfPortNum, String userName) {
        this.selfPortNum = selfPortNum;
        this.userName = userName;
    }

    public void start() {
        try {
            serverSocket = new ServerSocket(selfPortNum);
            //Create GUI by passing Peer and UserName
            if (userName != "server") {
                gui = new PeerGUI(this, this.userName);
            }
            
            new Thread(this::acceptConnections).start();
            new Thread(this::handleUserInput).start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void acceptConnections() {
        try {
            while (!serverSocket.isClosed()) {
                Socket clientSocket = serverSocket.accept();
                Connection connection = new Connection(clientSocket, this);
                connections.add(connection);
                new Thread(connection).start();
            }
        } catch (IOException e) {
            close();
        }
    }

    private void handleUserInput() {
        try (Scanner scanner = new Scanner(System.in)) {
            while (scanner.hasNextLine()) {
                String input = scanner.nextLine();
                sendMessage(input);
            }
        }
    }

    public void sendMessage(String message) {
        String newMessage = this.userName + ": " + message;
        connections.forEach(connection -> connection.sendMessage(newMessage));
        if (userName != "server") {
            gui.appendMessage(newMessage);
        }
        
    }

    public void receiveMessage(String message, Connection sourceConnection) {
        // System.out.println(userName + ": " + message);
        if (userName != "server") {
            gui.appendMessage(message);
        }
        // Forward the message to other peers, excluding the source
        forwardMessage(message, sourceConnection);
    }

    public String getUserName(){
        return this.userName;
    }

    private void forwardMessage(String message, Connection sourceConnection) {
        for (Connection connection : connections) {
            if (connection != sourceConnection) { // Avoid sending back to the source
                connection.sendMessage(message);
            }
        }
    }

    public void close() {
        try {
            serverSocket.close();
            connections.forEach(Connection::close);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void connectToPeer(int centralPortNum) {
        try {
            String host = "localhost"; // TODO: may need to change
            Socket peerSocket = new Socket(host, centralPortNum);
            Connection connection = new Connection(peerSocket, this);
            connections.add(connection);
            new Thread(connection).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

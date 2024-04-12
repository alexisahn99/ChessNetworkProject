package peer_to_peer;

import java.io.*;
import java.net.*;
import java.util.*;

public class Peer {
    private final int port;
    private final Set<Connection> connections = Collections.synchronizedSet(new HashSet<>());
    private ServerSocket serverSocket;
    private PeerGUI gui;
    private String userName;

    public Peer(int port, String userName) {
        this.port = port;
        this.userName = userName;
    }

    public void start() {
        try {
            serverSocket = new ServerSocket(port);
            //Create GUI by passing Peer and UserName
            gui = new PeerGUI(this, this.userName);
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
        connections.forEach(connection -> connection.sendMessage(message));
        gui.appendMessage(this.userName + ": " + message);
    }

    public void receiveMessage(String message, String userName, Connection sourceConnection) {
        System.out.println(userName + ": " + message);
        gui.appendMessage(userName + ": " + message);
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

    public void connectToPeer(String host, int port) {
        try {
            Socket peerSocket = new Socket(host, port);
            Connection connection = new Connection(peerSocket, this);
            connections.add(connection);
            new Thread(connection).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: java Peer <port> [peerHost] [peerPort]");
            return;
        }
        int port = Integer.parseInt(args[0]);
        String userName = "Brandon";
        Peer peer = new Peer(port, userName);
        peer.start();
    
        if (args.length == 3) {
            String peerHost = args[1];
            int peerPort = Integer.parseInt(args[2]);
            peer.connectToPeer(peerHost, peerPort);
        }
    }
}

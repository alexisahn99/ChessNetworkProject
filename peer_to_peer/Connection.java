package peer_to_peer;

import java.io.*;
import java.net.*;

import client.P2PGUI;

public class Connection implements Runnable {
    private final Socket socket;
    private final Peer peer;
    private P2PGUI gui;
    private PrintWriter out;
    private BufferedReader in;

    public Connection(Socket socket, Peer peer) {
        this.socket = socket;
        this.peer = peer;
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            close();
        }
    }

    @Override
    public void run() {
        try {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                // Updated to pass 'this' as the source connection
                gui.displayData();
                //peer.receiveMessage(inputLine, this);
            }
        } catch (IOException e) {
            close();
        }
    }

    public void sendMessage(String message) {
        out.println(message);
    }

    public void close() {
        try {
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

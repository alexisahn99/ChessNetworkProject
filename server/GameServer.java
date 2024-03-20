package server;

import java.io.*;
import java.net.*;
import server.model.ChessPieces.ChessPieceColor;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;


class Node {
    int client;
    String uid;
    String name;
    Node next;

    //Constructor for a new node
    ClientNode(int client, String player, String name) {
        client = client;
        uid = player;
        name = name;
        next = null;
    }
}

public class GameServer {
    private static Node head;
    private static Node sender;
    private static Node prevTurn;

    private static Node findLastNode() {
        Node cur = head;
        Node next;
        while (cur != null) {
            next = cur.next;
            if (next == null) {
                return cur;
            }
            cur = cur.next;
        }
        return null;
    }

    private static Node removeNode(int clientID) {
        Node cur = head;
        Node prev = head;
        if (head.client == clientID) {
            head = head.next;
            return head;
        } else {
            while (cur != null) {
                if (cur.client == clientID) {
                    prev.next = cur.next;
                    return prev;
                }
                prev = cur;
                cur = cur.next;
            }
        }
        return null;
    }

    // Send the move to all of the clients 
    private static void sendMove(byte[] move) {
        Node sendNode = head;
        while (sendNode != null) {
            if (sendNode.client != sender.client) {
                try {
                    OutputStream outputStream = sendNode.client.getOutputStream();
                    outputStream.write(move);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            sendNode = sendNode.next;
        }
    }

    private static void playGame() {
        Node curNode = head;
        byte[] input = new byte[1024];
        while (curNode != null) {
            try {
                InputStream inputStream = curNode.client.getInputStream();
                int ret = inputStream.read(input);
                if (ret == -1) {
                    // Handle disconnection
                    curNode = removeNode(curNode.client);
                } else if (ret > 0) {
                    sender = curNode;
                    // Determine whose move it is and send it
                    if (prevTurn != curNode && (curNode.uid == "1" || curNode.uid == "2")) {
                        sendMessage(input);
                        // TODO send to controller
                        prevTurn = curNode;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            curNode = curNode.next;
        }
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java Game Server <port number>");
            System.exit(-1);
        }
        int PORT_NUM = Integer.parseInt(args[0]);

        Node curNode;
        Node nextNode;
        int curClient;
        int users = 0;

        try {
            ServerSocket serverSocket = new ServerSocket(PORT_NUM);
            serverSocket.setSoTimeout(100);

            System.out.println("Establishing connections");
            while (true) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    clientSocket.setSoTimeout(100);

                    users++;
                    String message;
                    if (head == null) {
                        head = new Node(clientSocket, users, "Player " + users);
                        head.next = null;
                        sender = head;
                        prevTurn = head;
                    } else {
                        curNode = findLastNode();
                        nextNode = new Node(clientSocket, users, "Player " + users);
                        curNode.next = nextNode;
                        nextNode.next = null;
                        sender = nextNode;
                    }

                    message = "User " + users + " has connected\n";
                    System.out.println("Player " + users + " connected");
                } catch (SocketException se) {
                    // Ignore timeout exceptions
                }
                playGame();
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
 
package server;

import java.io.*;
//import com.google.gson.Gson;

import server.controller.Controller;
import server.controller.Tuple;
import server.model.ChessPieces.ChessPieceColor;

import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

class Node {
    Socket client;
    int uid;
    String name;
    Node next;

    public Node() {}

    //Constructor for a new node
    Node(Socket clientID, int player_num, String player_name) {
        client = clientID;
        uid = player_num;
        name = player_name;
        next = null;
    }
}

public class GameServer {
    private static Node head;
    private static Node sender;
    private static ChessPieceColor prevTurn;
    private static ChessPieceColor curTurn;
    private static int curUid;
    private static int prevUid;

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

    private static Node removeNode(Socket clientID) {
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
    private static void sendMove(Tuple move) {
        Node sendNode = head;
        while (sendNode != null) {
            if (sendNode.client != sender.client) {
                try {
                    // Uncomment if using Gson
                    /* 
                    OutputStream outputStream = sendNode.client.getOutputStream();
                    PrintWriter out = new PrintWriter(outputStream, true);
                    out.println(move);
                    */

                    // Serialize complex data to bytes
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    ObjectOutputStream out = new ObjectOutputStream(bos);
                    out.writeObject(move);
                    out.flush();
                    byte[] bytes = bos.toByteArray();

                    OutputStream outputStream = sendNode.client.getOutputStream();
                    outputStream.write(bytes);

                    // Close resources
                    out.close();
                    bos.close();
                    outputStream.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            sendNode = sendNode.next;
        }
    }

    private static void playGame(Controller controller) {
        Node curNode = head;
        byte[] input = new byte[16];
        while (curNode != null) {
            try {
                InputStream inputStream = curNode.client.getInputStream();
                int ret = inputStream.read(input);

                if (ret == -1) {
                    // Handle disconnection
                    curNode = removeNode(curNode.client);
                    System.out.println("Client " + curNode.uid + " disconnected");
                } else if (ret > 0) {

                    sender = curNode;
                    curUid = curNode.uid;
                    System.out.println("Received a valid input stream");
                    System.out.println("Prev Turn: " + prevTurn + " Cur Turn: " + curTurn);
                    System.out.println("Prev Uid: " + prevUid + " Cur Uid: " + curUid);
                    System.out.println("Cur Node Uid: " +curNode.uid);
                    // Determine whose move it is and send it
                    if (prevTurn == curTurn && prevUid != curUid && (curNode.uid == 1 || curNode.uid == 2)) {
                        /* 
                        // Receive JSON data
                        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
                        String json = in.readLine();

                        // Deserialize JSON to complex data object
                        Gson gson = new Gson();
                        Move curMove = gson.fromJson(json, Move.class);
                        */
                        System.out.println("Passed the user check");


                        //Alternative to using Gson
                        try {

                            //int[] coordinates = (int[]) in.readObject(); 
                            ObjectInputStream in = new ObjectInputStream(inputStream);

                            Move curMove = (Move) in.readObject();
                            System.out.println("Received data from client " + curUid + ":" + curMove.getRow() + "," + curMove.getCol());
 

                            // try move
                            Tuple result = controller.userPressed(curMove.getRow(), curMove.getCol());
                            curTurn = result.getCurrentPlayerColor();

                            // convert result
                            //String moveResult = gson.toJson(result);

                            // send result
                            sendMove(result);

                            if (curTurn != prevTurn) {
                                prevTurn = curTurn;
                                if (curNode.uid == 1) {
                                    prevUid = 1;
                                } else if (curNode.uid == 2) {
                                    prevUid = 2;
                                }
                            }

                        
                        } catch(ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            curNode = curNode.next;
            System.out.println("Check next node");
        }
    }
 
    public static void main(String[] args) {
        /*/
        if (args.length != 1) {
            System.out.println("Usage: java Game Server <port number>");
            System.exit(-1);
        }
        */
        int PORT_NUM = 21001; //Integer.parseInt(args[0]);

        Node curNode;
        Node nextNode;
        int users = 0;
        Controller controller = new Controller(null);

        try {
            ServerSocket serverSocket = new ServerSocket(PORT_NUM);
           // serverSocket.setSoTimeout(100);

            System.out.println("Establishing connections");
            while (true) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    //clientSocket.setSoTimeout(100);

                    users++;
                    String message;
                    if (head == null) {
                        head = new Node(clientSocket, users, "Player " + users);
                        head.next = null;
                        sender = head;
                        prevTurn = ChessPieceColor.W;
                        curTurn = ChessPieceColor.W;
                        curUid = head.uid;
                        prevUid = 0;
                    } else {
                        curNode = findLastNode();
                        nextNode = new Node(clientSocket, users, "Player " + users);
                        curNode.next = nextNode;
                        nextNode.next = null;
                        sender = nextNode;
                    }

                    message = "User " + users + " has connected\n";
                    System.out.println(message);
                } catch (SocketException se) {
                    // Ignore timeout exceptions
                }
                playGame(controller);
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
 

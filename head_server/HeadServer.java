package head_server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

import head_server.GameThread;
import head_server.utility.*;
import server.GameServer;


public class HeadServer implements Runnable
{
    private ArrayList<ConnectionHandler> connections;
    private ServerSocket server;
    private boolean done;
    private ExecutorService pool;
    private int headServerPort;
    private HeadServerNode headServerNode = new HeadServerNode();
    
    public HeadServer(int headServerPort)
    {
        connections = new ArrayList<>();
        done = false;
        this.headServerPort = headServerPort;
    }

    public void run()
    {
        try
        {
            server = new ServerSocket(headServerPort);
            pool = Executors.newCachedThreadPool();

            // Thread here is used to continuously accept new connections
            while (!done) {
                Socket client = server.accept();
                ConnectionHandler handler = new ConnectionHandler(client); // Starts new thread for handling Client and Server Connections
                connections.add(handler);
                pool.execute(handler);
            }
        }
        catch (IOException e)
        {
            //shutdown();
        }
    }

/* This function shuts down the head server */
    public void shutdown() 
    {
        try
        {
            done = true;
            pool.shutdown();
            if (!server.isClosed()) 
            {
                server.close();
            }

            for (ConnectionHandler ch : connections)
            {
                ch.shutdown();
            }
        }
        catch (IOException e)
        {

        }
    }



    class ConnectionHandler implements Runnable
    {
        private Socket client;
        private String whichClient;
        private BufferedReader in;
        private PrintWriter out;
        private String playerID;
        private PlayerNode playerNode;
        private boolean findPort;

        public ConnectionHandler (Socket client)
        {
            this.client = client;
            findPort = false;

        }

        @Override
        public void run()
        {
            try
            {
                out = new PrintWriter(client.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                whichClient = in.readLine(); /* After connection, client or server will immediately send string of "server" or "client"
                                                so headServer knows which one to handle */

                System.out.println(whichClient + " connected"); // Prints to HeadServer terminal if server or client connected

                /***** HANDLES SERVER *****/
                if (whichClient.startsWith("server")) 
                {
                    int gameServerPort = headServerNode.findPortNum(); // Finds open port
                    GameServerNode gameServerNode = new GameServerNode(gameServerPort);
                    headServerNode.addGameServer(gameServerNode); // Adds gameServerNode to headServerNode gameServers array list

                    while (!done)
                    {
                        // Need to add logic to end the game server when the game ends
                    }
                }

                /***** HANDLES CLIENT *****/
                else if (whichClient.startsWith("client")) 
                {
                    out.println("Please enter a nickname: "); // Player will be prompted to enter their nickname (playerID)
                    String playerID = in.readLine();

                    // While loop checks if playerID is already in use and accepts another ID
                    while (IDManagement.allClientIDs.contains(playerID)) {
                        out.println("Nickname is already in use. Enter another nickname: ");
                        playerID = in.readLine();
                    }
                    out.println("done");

                    int playerPortNum = headServerNode.findPortNum(); // Finds Port number for client (Used in P2P)
                    PlayerNode playerNode = new PlayerNode(playerID, playerPortNum);
                    headServerNode.addPlayer(playerNode); // Player Node is added to headServerNode players array list

                    System.out.println(playerID + " connected"); // Prints to HeadServer terminal the playerID which connected

                    // For loop prints out the port numbers of all available game servers so player can see what are available
                    // This will need to be changed when GUI is created
                    for (GameServerNode gameServer : headServerNode.getAllGameServers()) {
                        out.println(gameServer.getPortNumber());
                    }
                    out.println("over");

                    /* While loop accepts terminal input from player. If player enters "New Server", a game server is created and 
                       the port number of the new server is sent to the client. The head server then immediately ends connection 
                       with the client, and this thread ends. 

                       If player enters one of the given port numbers, the player is sent the port number (so the client knows it
                       can go to that port) and the head server ends connection with the client, and this thread ends. 

                       This needs to be changed when GUI is created.
                    */

                    String message;
                    boolean findPort = false;



                    while (!findPort)
                    {
                        message = in.readLine();
                        int clientToPortNum = 0;
                        boolean isInt = false;
                        try {
                            clientToPortNum = Integer.parseInt(message);
                            isInt = true;
                        } catch (NumberFormatException e) {}

                        System.out.println(message);
                        if (!isInt) {
                            int newPortNum = headServerNode.findPortNum();
                            out.println(newPortNum);
                            shutdown();
                            GameServer gameServer = new GameServer(newPortNum);
                        }

                        else {
                            if (IDManagement.allPortNumber.contains(clientToPortNum)) {
                                out.println(message);
                                System.out.println("Connecting client to server with port number: " + message);
                                shutdown();
                            }
                            else {
                                out.println("no");
                            }
                        }
                        
                    }

                }


                
            }
            catch (IOException e)
            {
                shutdown();
            }
        }

        public void sendMessage(String message)
        {
            out.println(message);
        }

        public void shutdown() 
        {
            try
            {
                this.findPort = true;
                in.close();
                out.close();
                if (!client.isClosed())
                {
                    client.close();
                }
            }
            catch (IOException e)
            {

            }
        }
    }

    public static void main(String[] args)
    {
        HeadServer server = new HeadServer(32156);
        server.run();
    }
}

package head_server;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class HeadServer implements Runnable
{
    private ArrayList<ConnectionHandler> connections;
    private ServerSocket server;
    private boolean done;
    private ExecutorService pool;
    private ArrayList<String> serverSocketNums;
    
    public HeadServer()
    {
        connections = new ArrayList<>();
        serverSocketNums = new ArrayList<>();
        done = false;
    }

    public void run()
    {
        try
        {
            server = new ServerSocket(9999);
            pool = Executors.newCachedThreadPool();
            while (!done) {
                Socket client = server.accept();
                ConnectionHandler handler = new ConnectionHandler(client);
                connections.add(handler);
                pool.execute(handler);
            }
        }
        catch (IOException e)
        {
            //shutdown();
        }
    }

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
        private String nickname;

        public ConnectionHandler (Socket client)
        {
            this.client = client;

        }

        @Override
        public void run()
        {
            try
            {
                out = new PrintWriter(client.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                whichClient = in.readLine();
                System.out.println(whichClient + " connected");

                if (whichClient.startsWith("server"))
                {
                    while (!done)
                    {
                    }
                }
                else if (whichClient.startsWith("client"))
                {
                    out.println("Please enter a nickname: ");
                    nickname = in.readLine();
                    //out.println(nickname);
                    System.out.println(nickname + " connected");
                    String message;
                    while ((message = in.readLine()) != null)
                    {
                        System.out.println(message);
                        if (message.startsWith("server1"))
                        {
                            System.out.println("Connecting client to server 1");
                            shutdown();
                        }

                        else if (message.startsWith("quit"))
                        {
                            shutdown();
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
                done = true;
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
        HeadServer server = new HeadServer();
        server.run();
    }
}

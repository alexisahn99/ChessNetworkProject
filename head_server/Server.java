import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class Server implements Runnable {

    private ArrayList<ConnectionHandler> connections;
    private ServerSocket server;
    private Socket client;
    private boolean done;
    private ExecutorService pool;
    private PrintWriter out;
    private int portNum;
    
    public Server(int portNum)
    {
        this.portNum = portNum;
        connections = new ArrayList<>();
        done = false;
    }


    @Override
    public void run()
    {
        try
        {
            // handles connection with head server
            client = new Socket("127.0.0.1", 9999);
            out = new PrintWriter(client.getOutputStream(), true);
            out.println("server");

            server = new ServerSocket(portNum);
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
            shutdown();
        }
    }

    public void broadcast(String message)
    {
        for (ConnectionHandler ch : connections)
        {
            if (ch != null) 
            {
                ch.sendMessage(message);
            }
        }
    }

    public void shutdown()
    {
        try
        {
            done = true;
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
                out.println("Please enter a nickname: ");
                nickname = in.readLine();
                System.out.println(nickname + " connected");
                broadcast(nickname + " joined the chat!");
                String message;
                while ((message = in.readLine()) != null)
                {
                    if (message.startsWith("/nick "))
                    {
                        String[] messageSplit = message.split(" ", 2);
                        if (messageSplit.length == 2)
                        {
                            broadcast(nickname + " renamed themselves to " + messageSplit[1]);
                            System.out.println(nickname + " renamed themselves to " + messageSplit[1]);
                            nickname = messageSplit[1];
                            out.println("Succesfully changed nickname to " + nickname);
                        }
                        else
                        {
                            out.println("No nickname provided");
                        }
                    }
                    else if (message.startsWith("/quit"))
                    {
                        broadcast(nickname + " left the chat.");
                        shutdown();
                    }
                    else
                    {
                        broadcast(nickname + ": " + message); 
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
        int portNum = Integer.parseInt(args[0]);
        Server server = new Server(portNum);
        server.run();
    }
}
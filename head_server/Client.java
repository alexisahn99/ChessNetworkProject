import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client implements Runnable
{

    private Socket client;
    private BufferedReader in;
    private PrintWriter out;
    private boolean done;
    private boolean newConnection;
    private int portNum;
    private boolean notConnected;

    public Client(int portNum)
    {
        done = false;
        newConnection = false;
        notConnected = false;
        this.portNum = portNum;
    }

    @Override
    public void run()
    {
        try
        {
            done = false;
            client = new Socket("127.0.0.1", portNum);
            out = new PrintWriter(client.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        
            InputHandler inHandler = new InputHandler();
            Thread t = new Thread(inHandler);
            t.start();

            String inMessage;
            while ((inMessage = in.readLine()) != null)
            {
                System.out.println(inMessage);
            }
            System.out.println("Thread ended");
        }
        catch (IOException e)
        {
            shutdown();
        }
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

    public void transferToPort(int newPortNum)
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
            this.portNum = newPortNum;
            run();
        }
        catch (IOException e)
        {
            shutdown();
        }
    }

    class InputHandler implements Runnable
    {

        @Override
        public void run()
        {
            System.out.println("New thread created");
            try
            {
                BufferedReader inReader = new BufferedReader(new InputStreamReader(System.in));
                out.println("client");

                while (!done)
                {
                    String message = inReader.readLine();
                    
                    if (message.equals("server1") && !notConnected)
                    {
                        out.println(message);
                        transferToPort(8080);

                    }
                    else if (message.startsWith("/quit"))
                    {
                        inReader.close();
                        shutdown();
                    }
                    else if (!notConnected)
                    {
                        out.println(message);
                    }
                    else if (message.equals("back"))
                    {
                        out.println(message);
                        //inReader.close();
                        //shutdown();
                        Client client = new Client(9999);
                        client.run();
                    }
                }
            }
            catch (IOException e)
            {
                shutdown();
            }
            System.out.println("Thread at handler ended");
        }
    }

    public static void main(String[] args)
    {
        Client client = new Client(9999);
        client.run();
    }
}
package client;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

import utility.FunctionFlag;
import utility.Tuple;


public class GameClient {
    private static ObjectOutputStream out;
    private static ObjectInputStream in;
    private static ChatGUI chatGUI;
    private static GameView gameView;
    private static int clientID;

    private Socket client;
    private BufferedReader headIn;
    private PrintWriter headOut;
    private boolean done;
    private boolean newConnection;
    private int portNum;
    private String hostname;
    private boolean notConnected;

    public GameClient(String hostname, int portNum) {
        done = false;
        newConnection = false;
        notConnected = false;
        this.portNum = portNum;
        this.hostname = hostname;
    }

    public void run()
    {
        try
        {
            client = new Socket(hostname, portNum);
            headOut = new PrintWriter(client.getOutputStream(), true);
            headIn = new BufferedReader(new InputStreamReader(client.getInputStream()));

            while(!done) {
                try
            {
                BufferedReader inReader = new BufferedReader(new InputStreamReader(System.in));
                headOut.println("client");

                String serverMessage = headIn.readLine();
                while(!serverMessage.equals("done")) {
                    System.out.println(serverMessage);
                    headOut.println(inReader.readLine());
                    serverMessage = headIn.readLine();
                }

                serverMessage = headIn.readLine();
                while (!serverMessage.equals("over")) {
                    System.out.println(serverMessage);
                    serverMessage = headIn.readLine();
                }

                String message;

                while (!done)
                {
                    message = inReader.readLine();
                    headOut.println(message);
                    serverMessage = headIn.readLine();
                    if (!serverMessage.equals("no")) {
                        int portNum = Integer.parseInt(serverMessage);
                        System.out.println("Moving to game server");
                        transferToPort(portNum);
                    }
                    else {
                        System.out.println("Incorrect entry. Enter port number or any letter for new server");
                    }
                }
            }
            catch (IOException e)
            {
                shutdown();
            }

            }
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
            headIn.close();
            headOut.close();
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
            headIn.close();
            headOut.close();
            if (!client.isClosed())
            {
                client.close();
            }
            this.portNum = newPortNum;
            runGame();
        }
        catch (IOException e)
        {
            shutdown();
        }
    }

    public void runGame() {
        try {
            Socket clientSocket = new Socket(hostname, portNum);
            System.out.println("Connected to server.");

            out = new ObjectOutputStream(clientSocket.getOutputStream());
            in = new ObjectInputStream(clientSocket.getInputStream());
            clientID = 10; //TODO: Hardcoded for now    
            gameView = new GameView(clientID, out);

            while(true){
                try{
                    Object input = in.readObject();
                    handleInput(input);
                } catch(ClassNotFoundException err){
                    System.out.println("Error: Couldn't read from action input stream");
                }
            }
        } catch (IOException err) {
            System.out.println("I/O error creating socket: " + err.getMessage());
        }
    }
    
    private static void handleInput(Object input){
        if(input instanceof Tuple){
            
            Tuple tuple = (Tuple) input;
            FunctionFlag flag = tuple.getFunctionFlag();
            ArrayList<int[]> pieceLocations = tuple.getChessPieces();
            if(!tuple.getGameOver()){
                switch(flag){
                    case DESTINATION:
                        //Outline all possible moves for player
                        gameView.drawPossibleMoves(pieceLocations);
                        break;
                    case SOURCE:
                        //Enable the squares for player to move
                        gameView.enableSquares(pieceLocations);
                        break;
                    case REPAINT:
                        //Update the screen for the next players turn
                        ArrayList<String> unicodes = tuple.getChessPieceUnicode();
                        gameView.update(pieceLocations, unicodes);
                        break;
                    default:
                        //incorrect flag recieved ?
                        break;
                }
            }
            else{
                //TODO: Handle Game Over Here
            }
        }
    }

    public static void main(String[] args) {
        /* 
        if (args.length < 2) {
            System.out.println("Usage: java TestClient <server IP> <port number>");
            return;
        }
        */
        String hostname = "127.0.0.1"; //args[0];
        int port = 32156; // Integer.parseInt(args[1]);

        GameClient gameClient = new GameClient(hostname, port);
        gameClient.run();
    }
}

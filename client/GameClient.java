package client;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

import server.model.ChessPieces.ChessPieceColor;
import utility.FunctionFlag;
import utility.Tuple;


public class GameClient {
    private static ObjectOutputStream out;
    private static ObjectInputStream in;
    private BufferedReader headIn;
    private PrintWriter headOut;
    private static GameView gameView;
    private Socket client;
    private boolean done;
    private int headPortNum;
    private int gamePortNum;
    private int selfPortNum;
    private static String headServerIP;
    private String userName;

    public GameClient(String headServerIP, int headPortNum) {
        done = false;
        this.headPortNum = headPortNum;
        this.headServerIP = headServerIP;
    }

    public void run()
    {
        try
        {
            client = new Socket(this.headServerIP, this.headPortNum);
            headOut = new PrintWriter(client.getOutputStream(), true);
            headIn = new BufferedReader(new InputStreamReader(client.getInputStream()));

            while(!done) {
                try
            {
                BufferedReader inReader = new BufferedReader(new InputStreamReader(System.in));
                headOut.println("client");

                // Setting username that the client types in
                // Checks the username with the HeadServer to makes sure it's available
                // Is good when "done" message received
                String serverMessage = headIn.readLine();
                while(!serverMessage.equals("done")) {
                    System.out.println(serverMessage);
                    this.userName = inReader.readLine();
                    headOut.println(this.userName);
                    serverMessage = headIn.readLine();
                }

                 // Printing available ports
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
                        this.gamePortNum = Integer.parseInt(serverMessage);
                        System.out.println("Moving to game server");
                        transferToPort(this.gamePortNum);
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

    public void transferToPort(int gamePortNum)
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
            this.gamePortNum = gamePortNum;
            runGame();
        }
        catch (IOException e)
        {
            shutdown();
        }
    }

    public void runGame() {
        try {
            Socket clientSocket = new Socket(this.headServerIP, this.gamePortNum);
            // System.out.println("Connected to server.");

            out = new ObjectOutputStream(clientSocket.getOutputStream());
            in = new ObjectInputStream(clientSocket.getInputStream());
            gameView = new GameView(selfPortNum, userName, out);

            gameView.initializeDisplay();

            while(true){
                try{
                    Object input = in.readObject();
                    handleInput(input);
                } catch(ClassNotFoundException err){
                    System.out.println("ERROR in GameClient: Couldn't read from action input stream");
                }
            }
        } catch (IOException err) {
            System.out.println("ERROR in GameClient: I/O error creating socket - " + err.getMessage());
        }
    }

    private void handleInput(Object input){
        if(input instanceof Tuple){
            Tuple tuple = (Tuple) input;
            FunctionFlag flag = tuple.getFunctionFlag();
            ArrayList<int[]> pieceLocations = tuple.getChessPieces();
            ChessPieceColor currPlayer = tuple.getCurrentPlayerColor();
            ArrayList<String> unicodes = tuple.getChessPieceUnicode();

            gameView.displayCurrentPlayer(currPlayer);
            
            if(tuple.isCheck()){
                gameView.displayCheckStatus();
            }
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
                    gameView.update(pieceLocations, unicodes);
                    break;
                case DISABLE:
                    gameView.disableBoard();
                    break;
                case CHECKMATE:
                    gameView.disableBoard();
                    gameView.update(pieceLocations, unicodes);
                    gameView.displayCheckMateStatus(currPlayer);
                    break;
                case PORT:
                    // set port number
                    gameView.initPeerToPeer(tuple.getCentralPortNum(), this.headServerIP);
                    break;
                default:
                    //incorrect flag recieved ?
                    break;
            }
        }
    }

    public static void main(String[] args) {
        
        if (args.length < 1) {
            System.out.println("Usage: java GameClient <head server IP>");
            return;
        }
        
        int headPortNum = 32156; 

        GameClient gameClient = new GameClient(args[0], headPortNum);
        gameClient.run();
    }
}

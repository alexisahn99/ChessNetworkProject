package client;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import javax.swing.*;

import client.ChessPieces;
import server.controller.FunctionFlag;
import server.model.Move;
import java.awt.*;
import java.awt.event.*;

public class Client {
    private static ObjectOutputStream out;
    private static ObjectInputStream in;
    private static ChatGUI chatGUI;
    private static JPanel boardPanel;
    private static JButton[][] boardSegment;
    private static JLabel statusLabel;

    public static void main(String[] args) {
<<<<<<< HEAD
        /*
         * if (args.length < 2) {
         * System.out.println("Usage: java TestClient <server IP> <port number>");
         * return;
         * }
         */
        String hostname = "127.0.0.1"; // args[0];
=======
        /* 
        if (args.length < 2) {
            System.out.println("Usage: java TestClient <server IP> <port number>");
            return;
        }
        */
        String hostname = "127.0.0.1"; //args[0];
>>>>>>> 84bf2724c5560e0ba7980da82823211c5ebfb151
        int port = 21001; // Integer.parseInt(args[1]);

        try {
            Socket clientSocket = new Socket(hostname, port);
            System.out.println("Connected to server.");

            out = new ObjectOutputStream(clientSocket.getOutputStream());
            in = new ObjectInputStream(clientSocket.getInputStream());

            SwingUtilities.invokeLater(Client::chessGUI);

            chatGUI = new ChatGUI();

            while(true){
                try{
                    Object action = in.readObject();
                    if(action instanceof FunctionFlag){
                        FunctionFlag flag = (FunctionFlag) action;
                        handleAction(flag);
                    }
                } catch(ClassNotFoundException err){
                    System.out.println("Error: Couldnt read from action input stream");
                }
            }
        } catch (IOException err) {
            System.out.println("I/O error creating socket: " + err.getMessage());
        }
    }

    private static void handleAction(FunctionFlag flag){
        switch(flag){
            case DESTINATION:
                //drawPossibleMoves()
                break;
            case SOURCE:
                //enableSquares
                break;
            case REPAINT:
                //update
                break;
            default:
                //incorrect flag recieved ?
                break;
        }
    }

    private static void chessGUI() {
        JFrame frame = new JFrame("Chess Client");
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                // TODO
            }
        });

        JPanel mainPanel = new JPanel();
        mainPanel.setPreferredSize(new Dimension(750, 650));
        mainPanel.setBackground(new Color(192, 192, 192));

        JPanel displayPanel = new JPanel(new GridLayout(1, 1));
        displayPanel.setBackground(new Color(192, 192, 192));
        displayPanel.setPreferredSize(new Dimension(400, 50));

        boardPanel = new JPanel(new GridLayout(10, 10));
        boardPanel.setBackground(new Color(192, 192, 192));
        generateChessBoard();
        mainPanel.add(boardPanel);

        ChessPieces chessPieces = new ChessPieces(boardSegment);

        frame.getContentPane().add(mainPanel);
        frame.pack();
        frame.setVisible(true);
    }

    private static void generateChessBoard() {
        boardSegment = new JButton[8][8];
        for (int row = 0; row < 8; row++) {
            JLabel rowLabel = new JLabel(String.valueOf(Math.abs(row - 8)), JLabel.CENTER);
            rowLabel.setPreferredSize(new Dimension(20, 70));
            boardPanel.add(rowLabel);
            for (int col = 0; col < 8; col++) {
                boardSegment[row][col] = new JButton();
                boardSegment[row][col].setOpaque(true);
                boardSegment[row][col].setBorder(null);
                if ((row + col) % 2 == 0) {
                    boardSegment[row][col].setBackground(new Color(235, 235, 208));
                } else {
                    boardSegment[row][col].setBackground(new Color(119, 148, 86));
                }
                boardSegment[row][col].setPreferredSize(new Dimension(70, 70));
                boardSegment[row][col].addActionListener(new ButtonClickListener(new int[] {row, col}));
                boardPanel.add(boardSegment[row][col]);
            }
        }
        generateColumns(boardPanel);
    }

    public static void generateColumns(JPanel panel) {
        panel.add(new JLabel(""));
        for (int i = 0; i < 8; i++) {
            JLabel colLabel = new JLabel(String.valueOf((char) ('A' + i)), JLabel.CENTER);
            colLabel.setPreferredSize(new Dimension(20, 70));
            panel.add(colLabel);
        }
        panel.add(new JLabel(""));
    }

    private void disableBoard() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                boardSegment[row][col].setEnabled(false);
                boardSegment[row][col].setOpaque(true);
                boardSegment[row][col].setBorder(null);
            }
        }
    }

    public String getCurrentLabel(int row, int col) {
        return boardSegment[row][col].getText();
    }

    public void drawPossibleMoves(ArrayList<int[]> enabledSquares) {
        this.disableBoard(); // Disable all buttons

        for (int[] getRowCol : enabledSquares) // Get every legal square and enable it
        {
            int row = getRowCol[0];
            int col = getRowCol[1];
            boardSegment[row][col].setEnabled(true);
            boardSegment[row][col].setBorder(BorderFactory.createLineBorder(Color.BLACK, 4, true));
            boardSegment[row][col].setOpaque(true);
        }
    }

    public void enableSquares(ArrayList<int[]> allCurrentPieces) {
        this.disableBoard();
        for (int[] pieceLoc : allCurrentPieces) {
            int row = pieceLoc[0];
            int col = pieceLoc[1];
            boardSegment[row][col].setEnabled(true);
            boardSegment[row][col].setOpaque(true);
            boardSegment[row][col].setBorder(null);
        }
    }

    public void removeDots() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if ((row + col) % 2 == 0) {
                    boardSegment[row][col].setBackground(new Color(235, 235, 208));
                    boardSegment[row][col].setOpaque(true);
                    boardSegment[row][col].setBorder(null);
                } else {
                    boardSegment[row][col].setBackground(new Color(119, 148, 86));
                    boardSegment[row][col].setOpaque(true);
                    boardSegment[row][col].setBorder(null);
                }
            }
        }
    }
/*
    public void update() {
        // Erase board
        removeDots();
        for (JButton[] buttons : this.boardSegment) {
            for (JButton button : buttons) {
                button.setText(null);
            }
        }

        // Draw pieces on board
        ArrayList<int[]> pieces = this.board.getPiecesLocation();
        for (int[] piece : pieces) {
            int row = piece[0];
            int col = piece[1];
            String label = this.board.getChessPiece(row, col).getLabel();
            this.boardSegment[row][col].setText(label);
            this.boardSegment[row][col].setFont(new Font("Dialog", Font.PLAIN, 45));
        }
    }

    public void displayWinner(String winner) {
        this.disableBoard();
        this.statusLabel.setText("The Winner Is: " + winner);
        this.statusLabel.setFont(new Font("Dialog", Font.BOLD, 20));
    }

    public void promptNewPiece() {
        ConvertPawn convertPanel = new ConvertPawn(this.controller.getCurrentPlayer(), this.controller);
    }
    */

    static class ButtonClickListener implements ActionListener {
        private int[] buttonValue;

        public ButtonClickListener(int[] buttonValue) {
            this.buttonValue = buttonValue;
        }

        @Override
        public void actionPerformed(ActionEvent event) {
            try {

                Move move = new Move(buttonValue[0], buttonValue[1]);
                System.out.println("Sending data to server: " + buttonValue[0] + buttonValue[1]);

                // Serialize complex data to bytes
                out.writeObject(move);

                System.out.println("Data sent");

            } catch (IOException err) {
                System.out.println("I/O error: " + err.getMessage());
            }
        }
    }
}
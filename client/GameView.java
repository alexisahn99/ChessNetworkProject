package client;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import javax.swing.*;

import client.ChessPieces;
import client.Chat;
import client.UnicodeMap;
import peer_to_peer.Connection;
import peer_to_peer.Peer;
import server.controller.FunctionFlag;
import server.controller.Tuple;
import server.model.Move;
import java.awt.*;
import java.awt.event.*;

public class GameView {
    private static JPanel boardPanel;
    private static JButton[][] boardSegment;
    private ObjectOutputStream out;
    private int clientID;

    public GameView(int clientID, ObjectOutputStream out){
        this.clientID = clientID;
        this.out = out;
        JFrame frame = new JFrame("Chess Client");
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        mainPanel.setPreferredSize(new Dimension(750, 650));
        mainPanel.setBackground(new Color(192, 192, 192));

        boardPanel = new JPanel(new GridLayout(10, 10));
        boardPanel.setBackground(new Color(192, 192, 192));
        generateChessBoard();
        mainPanel.add(boardPanel);

        frame.getContentPane().add(mainPanel);
        frame.pack();
        frame.setVisible(true);
    }

    private void generateChessBoard() {
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
                boardSegment[row][col].addActionListener(new ButtonClickListener(row, col, this, out));
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

    public int getClientID() {
        return clientID;
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

    public void update(ArrayList<int[]> pieces, ArrayList<String> unicodes) {
        // Erase board
        removeDots();
        for (JButton[] buttons : this.boardSegment) {
            for (JButton button : buttons) {
                button.setText(null);
            }
        }

        // Draw pieces on board
        for (int i = 0; i < pieces.size(); i++) {
            int[] piece = pieces.get(i);
            int row = piece[0];
            int col = piece[1];
            String pieceUnicode = unicodes.get(i);
            this.boardSegment[row][col].setText(pieceUnicode);
            this.boardSegment[row][col].setFont(new Font("Dialog", Font.PLAIN, 45));
        }
    }
    /*
    public void displayWinner(String winner) {
        this.disableBoard();
        this.statusLabel.setText("The Winner Is: " + winner);
        this.statusLabel.setFont(new Font("Dialog", Font.BOLD, 20));
    }
    
    public void promptNewPiece() {
        ConvertPawn convertPanel = new ConvertPawn(this.controller.getCurrentPlayer(), this.controller);
    }
    */
}   class ButtonClickListener implements ActionListener {
    private int row;
    private int col;
    private GameView gameView;
    private ObjectOutputStream out;

    public ButtonClickListener(int row, int col, GameView gameView, ObjectOutputStream out) {
        this.gameView = gameView;
        this.row = row;
        this.col = col;
        this.out = out;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try{
            // Delegate handling of user input to the client
            int clientID = gameView.getClientID();
            System.out.println("Row: " + row + " Col: " + col + " Client ID Num: " + clientID);
            Move move = new Move(row, col, clientID);
            // Serialize complex data to bytes
            out.writeObject(move);
            System.out.println("Data sent");
        } catch (IOException err) {
            System.out.println("I/O error: " + err.getMessage());
        }
    }
}

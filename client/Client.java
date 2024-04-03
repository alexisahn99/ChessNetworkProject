package client;

import java.io.*;
import java.net.*;
import javax.swing.*;
import server.model.Move;
import java.awt.*;
import java.awt.event.*;

public class Client {
    private static ObjectOutputStream out;
    private static ObjectInputStream in;

    public static void main(String[] args) {
        /* 
        if (args.length < 2) {
            System.out.println("Usage: java TestClient <server IP> <port number>");
            return;
        }
        */
        String hostname = "127.0.0.1"; //args[0];
        int port = 21001; // Integer.parseInt(args[1]);

        try  {
            Socket clientSocket = new Socket(hostname, port);
            System.out.println("Connected to server.");

             out = new ObjectOutputStream(clientSocket.getOutputStream());

             // TODO implement how to handle response from server.
             in = new ObjectInputStream(clientSocket.getInputStream());


            SwingUtilities.invokeLater(Client::tempGUI);
        } catch (IOException err) {
            System.out.println("I/O error creating socket: " + err.getMessage());
        }
    }

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

    private static void tempGUI() {
        JFrame frame = new JFrame("Chess Client");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(2, 2));

        JButton button1 = new JButton("1");
        JButton button2 = new JButton("2");
        JButton button3 = new JButton("3");
        JButton button4 = new JButton("4");

        button1.addActionListener(new ButtonClickListener(new int[] {1, 1}));
        button2.addActionListener(new ButtonClickListener(new int[] {1, 2}));
        button3.addActionListener(new ButtonClickListener(new int[] {2, 1}));
        button4.addActionListener(new ButtonClickListener(new int[] {2, 2}));

        panel.add(button1);
        panel.add(button2);
        panel.add(button3);
        panel.add(button4);

        frame.getContentPane().add(panel);
        frame.pack();
        frame.setVisible(true);
    }
}
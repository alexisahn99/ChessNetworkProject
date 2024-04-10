package client;

import peer_to_peer.Peer;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class P2PGUI extends JFrame {
    private Peer peer;
    private JTextArea textArea;
    private JTextField inputField;
    private JButton sendButton;
    private static int portNum;

    public P2PGUI(int port) {
        portNum = port;

        setTitle("P2P GUI");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        textArea = new JTextArea();
        textArea.setEditable(false);

        inputField = new JTextField(20);

        sendButton = new JButton("Send");
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendData();
            }
        });

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(new JScrollPane(textArea), BorderLayout.CENTER);

        JPanel inputPanel = new JPanel();
        inputPanel.add(inputField);
        inputPanel.add(sendButton);
        getContentPane().add(inputPanel, BorderLayout.SOUTH);

        peer = new Peer(this.portNum);
        peer.start();
    }

    private void sendData() {
        String inputText = inputField.getText();
        if (!inputText.isEmpty()) {
            appendToTextArea("You: " + inputText + "\n");
            inputField.setText(""); // Clear the input field

            System.out.println("Sending: " + inputText);
            peer.sendMessage(inputText);
        }
    }

    public void displayData() {
        // Start a separate thread to continuously read data from the terminal
        Thread readerThread = new Thread(() -> {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                String line;
                while ((line = reader.readLine()) != null) {
                    // Append the read line to the JTextArea
                    appendToTextArea(line + "\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        readerThread.start();
    }

    // Method to append text to the JTextArea from any thread
    public void appendToTextArea(String text) {
        SwingUtilities.invokeLater(() -> textArea.append(text));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            P2PGUI gui = new P2PGUI(portNum);
            gui.setVisible(true);
            // Start displaying data from the terminal
            gui.displayData();
        });
    }
}

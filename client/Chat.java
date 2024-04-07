package client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import peer_to_peer.Connection;
import peer_to_peer.Peer;

public class Chat extends JFrame {
    private JTextArea messageArea;
    private JTextField messageField;
    private JButton sendButton;

    private Peer peer;

    public Chat(Peer peer) {
        this.peer = peer;

        setTitle("P2P Chat Client");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        messageArea = new JTextArea();
        messageArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(messageArea);
        add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout());
        messageField = new JTextField();
        bottomPanel.add(messageField, BorderLayout.CENTER);
        sendButton = new JButton("Send");
        sendButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });
        bottomPanel.add(sendButton, BorderLayout.EAST);
        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void sendMessage() {
        String message = messageField.getText().trim();
        if (!message.isEmpty()) {
            peer.sendMessage(message);
            messageField.setText("");
        }
    }

    public void displayMessage(String message) {
        messageArea.append(message + "\n");
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: java PeerChatGUI <port>");
            return;
        }

        int port = Integer.parseInt(args[0]);
        Peer peer = new Peer(port);
        peer.start();

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Chat(peer);
            }
        });
    }
}

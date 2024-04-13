package client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GameClientGUI extends JFrame {
    private JTextField inputField;
    private JTextArea outputArea;
    private JButton sendButton;
    private GameClient gameClient;

    public GameClientGUI() {
        setTitle("Game Client");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        inputField = new JTextField();
        outputArea = new JTextArea();
        sendButton = new JButton("Send");

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());
        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);

        add(outputArea, BorderLayout.CENTER);
        add(inputPanel, BorderLayout.SOUTH);

        gameClient = new GameClient("localhost", 12345); // Provide hostname and port
        //gameClient.setGUI(this);

        sendButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String message = inputField.getText();
                gameClient.sendMessage(message);
                inputField.setText("");
            }
        });
    }

    public void appendMessage(String message) {
        outputArea.append(message + "\n");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new GameClientGUI().setVisible(true);
            }
        });
    }
}

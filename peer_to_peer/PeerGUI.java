package peer_to_peer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class PeerGUI {
    private JTextArea chat;
    private JTextField messageField;
    private Peer peer;
    private String userName;

    public PeerGUI(Peer peer, String userName){
        this.peer = peer;
        this.userName = userName;
        displayGUI();
    }

    private void displayGUI(){
        JFrame mainFrame = new JFrame("Peer to Peer Chat");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(400, 300);
        mainFrame.setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new BorderLayout());
        messageField = new JTextField();
        messageField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event){
                sendMessage();
            }
        });
        JButton sendButton = new JButton("Send");
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event){
                sendMessage();
            }
        });

        chat = new JTextArea();
        chat.setEditable(false);
        JScrollPane scroller = new JScrollPane(chat);

        //Add all GUI elements to panel and panel to frame
        inputPanel.add(messageField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);
        mainFrame.add(scroller, BorderLayout.CENTER);
        mainFrame.add(inputPanel, BorderLayout.SOUTH);

        mainFrame.setVisible(true);
    }

    private void sendMessage(){
        String message = messageField.getText();
        if(message.isEmpty() == false){
            peer.sendMessage(message);
            messageField.setText("");
        }
    }

    public void appendMessage(String message){
        chat.append(message + "\n");
    }
}

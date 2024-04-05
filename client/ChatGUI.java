package client;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ChatGUI extends JFrame{
    private JButton sendButton;
    private JTextField textField;
    private JTextArea textArea;

    public ChatGUI(){
        setTitle("Chat");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());
        textArea = new JTextArea();
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.X_AXIS));
        
        textField = new JTextField();
        sendButton = new JButton("Send");
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent action){
                sendMessage();
            }
        });
        inputPanel.add(textField);
        inputPanel.add(sendButton);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(inputPanel, BorderLayout.SOUTH);

        getContentPane().add(mainPanel);
        setVisible(true);
    }

    private void sendMessage(){
        String message = textField.getText();
        if(!message.isEmpty()){
            addMessage("Me: " + message);
            textField.setText("");
        }
    }

    public void addMessage(String message){
        textArea.append(message + "\n");
    }
}

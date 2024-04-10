import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class P2PChatClient extends JFrame {
    private JTextArea textArea;
    private JTextField inputField;
    private JButton sendButton;
    private PrintWriter out;

    public P2PChatClient() {
        setTitle("P2P Chat Client");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Create a JTextArea to display the chat messages
        textArea = new JTextArea();
        textArea.setEditable(false);

        // Create a JTextField for user input
        inputField = new JTextField(20);

        // Create a JButton for sending messages
        sendButton = new JButton("Send");
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });

        // Add components to the JFrame
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(new JScrollPane(textArea), BorderLayout.CENTER);

        JPanel inputPanel = new JPanel();
        inputPanel.add(inputField);
        inputPanel.add(sendButton);
        getContentPane().add(inputPanel, BorderLayout.SOUTH);
    }

    private void sendMessage() {
        String message = inputField.getText().trim();
        if (!message.isEmpty()) {
            out.println(message);
            appendToTextArea("You: " + message + "\n");
            inputField.setText(""); // Clear the input field
        }
    }

    private void appendToTextArea(String text) {
        SwingUtilities.invokeLater(() -> textArea.append(text));
    }

    public void startListening(int port) {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            appendToTextArea("Listening for incoming connections on port " + port + "\n");

            // Accept incoming connections
            while (true) {
                Socket clientSocket = serverSocket.accept();
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                out = new PrintWriter(clientSocket.getOutputStream(), true);

                // Start a thread to handle incoming messages from the connected peer
                Thread thread = new Thread(new IncomingMessageHandler(in));
                thread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class IncomingMessageHandler implements Runnable {
        private BufferedReader in;

        public IncomingMessageHandler(BufferedReader in) {
            this.in = in;
        }

        @Override
        public void run() {
            try {
                String message;
                while ((message = in.readLine()) != null) {
                    appendToTextArea("Peer: " + message + "\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        // Usage: java P2PChatClient <port>
        if (args.length != 1) {
            System.out.println("Usage: java P2PChatClient <port>");
            return;
        }

        int port = Integer.parseInt(args[0]);

        SwingUtilities.invokeLater(() -> {
            P2PChatClient client = new P2PChatClient();
            client.setVisible(true);
            client.startListening(port);
        });
    }
}


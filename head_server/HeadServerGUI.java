import javax.swing.*;
import java.awt.event.*;

public class HeadServerGUI extends JFrame {
    public HeadServerGUI() {
        setTitle("Head Server");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        JButton joinGameButton = new JButton("Join Game");
        joinGameButton.setAlignmentX(JButton.CENTER_ALIGNMENT);
        joinGameButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Add action here when the button is clicked
                // For example: open a new window for the game
                System.out.println("Join Game button clicked");
            }
        });
        mainPanel.add(joinGameButton);

        getContentPane().add(mainPanel);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new HeadServerGUI();
            }
        });
    }
}
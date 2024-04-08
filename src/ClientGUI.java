import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class ClientGUI extends JFrame {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int PORT = 12345;
    private String username;
    private PrintWriter out;

    private JTextField messageField;
    private JTextArea chatArea;

    public ClientGUI() {
        super("Chat Client");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        messageField = new JTextField();
        JButton sendButton = new JButton("Send");
        sendButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });

        chatArea = new JTextArea();
        chatArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(chatArea);

        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout());
        bottomPanel.add(messageField, BorderLayout.CENTER);
        bottomPanel.add(sendButton, BorderLayout.EAST);

        panel.add(bottomPanel, BorderLayout.SOUTH);

        add(panel);

        try {
            Socket socket = new Socket(SERVER_ADDRESS, PORT);
            out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            username = JOptionPane.showInputDialog("Please enter your username:");
            out.println(username);

            Thread readThread = new Thread(() -> {
                try {
                    String serverResponse;
                    while ((serverResponse = in.readLine()) != null) {
                        chatArea.append(serverResponse + "\n");
                    }
                } catch (IOException e) {
                    System.err.println("Error reading server response: " + e.getMessage());
                }
            });
            readThread.start();

        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private void sendMessage() {
        String message = messageField.getText();
        if (!message.isEmpty()) {
            out.println(username + ": " + message);
            messageField.setText("");
            // Karakter uzunluğunu hesapla ve göster
            int characterCount = message.length();
            chatArea.append("Message Length: " + characterCount + "\n");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new ClientGUI().setVisible(true);
            }
        });
    }
}

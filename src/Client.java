import java.io.*;
import java.net.*;

public class Client {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int PORT = 12345;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_ADDRESS, PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))) {

            String username;
            System.out.println("Lütfen kullanıcı adınızı giriniz:");
            username = stdIn.readLine();
            out.println(username);

            Thread readThread = new Thread(() -> {
                try {
                    String serverResponse;
                    while ((serverResponse = in.readLine()) != null) {
                        System.out.println(serverResponse);
                    }
                } catch (IOException e) {
                    System.err.println("Error reading server response: " + e.getMessage());
                }
            });
            readThread.start();

            while (true) {
                System.out.println("Lütfen mesajınızı giriniz:");
                String userInput = stdIn.readLine();
                if (userInput != null) {
                    out.println(username + ": " + userInput);
                    System.out.println("Girilen mesajın uzunluğu: " + userInput.length());
                    System.out.println("Gönderilen mesaj: " + username + ": " + userInput);
                }
            }
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}

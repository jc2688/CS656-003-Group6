import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Client {
    public static void main(String[] args) {
        try {
            // Connect to the server
            Socket socket = new Socket("localhost", 5000); // Assuming server is running on the same machine

            // Set up input and output streams
            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Start a thread to handle incoming messages from the server
            new Thread(() -> {
                try {
                    String receivedMessage;
                    while ((receivedMessage = reader.readLine()) != null) {
                        System.out.println("Received from server: " + receivedMessage);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

            // Read user input and send messages to the server
            String userInputLine;
            while ((userInputLine = userInput.readLine()) != null) {
                String timeStamp = new SimpleDateFormat("HH:mm:ss").format(new Date());
                writer.println("[" + timeStamp + "] Client Message: " + userInputLine);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
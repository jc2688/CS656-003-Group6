import java.io.*;
import java.net.*;

public class Client {
    private static final String SERVER_IP = "127.0.0.1";
    private static final int SERVER_PORT = 5555;

    public static void main(String[] args) {
        try {
            Socket socket = new Socket(SERVER_IP, SERVER_PORT);
            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            System.out.print("Enter your name: ");
            String name = userInput.readLine();
            out.println(name);

            while (true) {
                System.out.print("Enter message (format: source:destination:message): ");
                String message = userInput.readLine();

                if (message.equalsIgnoreCase("exit")) {
                    out.println("exit");
                    break;
                }

                out.println(message);

                String serverResponse = in.readLine();
                System.out.println("Server: " + serverResponse);
            }

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
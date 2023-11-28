import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MultiClientServer {
    public static void main(String[] args) {
        try {
            // Server setup
            ServerSocket serverSocket = new ServerSocket(5000); // Server listens on port 5000
            System.out.println("Server started. Waiting for clients to connect...");

            // Accept three client connections
            Socket client1Socket = serverSocket.accept();
            System.out.println("Client 1 connected.");

            Socket client2Socket = serverSocket.accept();
            System.out.println("Client 2 connected.");

            Socket client3Socket = serverSocket.accept();
            System.out.println("Client 3 connected.");

            // Start threads for each pair of clients to handle communication
            new Thread(new ClientHandler(client1Socket, client2Socket)).start();
            new Thread(new ClientHandler(client2Socket, client1Socket)).start();

            new Thread(new ClientHandler(client2Socket, client3Socket)).start();
            new Thread(new ClientHandler(client3Socket, client2Socket)).start();

            new Thread(new ClientHandler(client1Socket, client3Socket)).start();
            new Thread(new ClientHandler(client3Socket, client1Socket)).start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class ClientHandler implements Runnable {
    private final Socket senderSocket;
    private final Socket receiverSocket;

    public ClientHandler(Socket senderSocket, Socket receiverSocket) {
        this.senderSocket = senderSocket;
        this.receiverSocket = receiverSocket;
    }

    public void run() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(senderSocket.getInputStream()));
            PrintWriter writer = new PrintWriter(receiverSocket.getOutputStream(), true);

            String inputLine;
            while ((inputLine = reader.readLine()) != null) {
                String timeStamp = new SimpleDateFormat("HH:mm:ss").format(new Date());
                writer.println("[" + timeStamp + "] Message from Client: " + inputLine);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
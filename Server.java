import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class Server {
    private static final int PORT = 5555;
    private static HashMap<String, Socket> clients = new HashMap<>();

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Server is running...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                new ClientHandler(clientSocket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ClientHandler extends Thread {
        private Socket socket;
        private BufferedReader in;
        private PrintWriter out;
        private String clientName;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                clientName = in.readLine();
                System.out.println(clientName + " connected.");
                clients.put(clientName, socket);
                displayConnectedClients();

                while (true) {
                    String message = in.readLine();
                    if (message.equals("exit")) {
                        break;
                    }
                    sendMessage(message);
                    logClientMessage(clientName, message);
                }

                clients.remove(clientName);
                socket.close();
                System.out.println(clientName + " disconnected.");
                displayConnectedClients();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void sendMessage(String message) {
            String timestamp = new SimpleDateFormat("HH:mm:ss.SSS").format(new Date());
            StringTokenizer st = new StringTokenizer(message, ":");
            String source = st.nextToken();
            String destination = st.nextToken();
            String msg = st.nextToken();

            if (clients.containsKey(destination)) {
                Socket destSocket = clients.get(destination);
                try {
                    PrintWriter destOut = new PrintWriter(destSocket.getOutputStream(), true);
                    destOut.println("[" + timestamp + "] " + source + ": " + msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                out.println("Client \"" + destination + "\" not found!");
            }
        }

        private void displayConnectedClients() {
            System.out.println("Connected Clients:");
            for (String client : clients.keySet()) {
                System.out.println("- " + client);
            }
            System.out.println();
        }

        private void logClientMessage(String clientName, String message) {
            String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());
            System.out.println("[" + timestamp + "] " + clientName + " sent: " + message);
        }
    }
}
package dev.zwazel.connection;

import java.net.Socket;

public class ConnectionManager {
    private static ConnectionManager instance;
    private Socket socket;
    private Thread listenerThread;

    private ConnectionManager() {
        // Private constructor to prevent instantiation
    }

    public static ConnectionManager getInstance() {
        if (instance == null) {
            instance = new ConnectionManager();
        }
        return instance;
    }

    public boolean connect(String host, int port) {
        try {
            socket = new Socket(host, port);
            System.out.println("Connected to " + host + ":" + port);
            System.out.println("Starting listener thread...");
            listenerThread = new Thread(new ListenerThread(instance), "Socket-Listener");
            listenerThread.start();
            return true;
        } catch (Exception e) {
            System.err.println("Error connecting to " + host + ":" + port);
            e.printStackTrace();
        }
        return false;
    }

    public void send() {
        // Send any outgoing messages
        try {
            String message = "Hello from Java!";
            System.out.println("Sending message: " + message);
            socket.getOutputStream().write(message.getBytes());
        } catch (Exception e) {
            System.err.println("Error sending message");
            e.printStackTrace();
        }
    }

    protected Socket getSocket() {
        return socket;
    }

    public boolean isConnected() {
        return socket != null && socket.isConnected();
    }
}

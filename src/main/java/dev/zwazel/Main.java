package dev.zwazel;

import dev.zwazel.connection.ConnectionManager;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Main {
    // Shared queues
    private static final ConnectionManager connectionManager = ConnectionManager.getInstance();
    private static final BlockingQueue<String> incomingMessages = new LinkedBlockingQueue<>();
    private static final BlockingQueue<String> outgoingMessages = new LinkedBlockingQueue<>();

    public static void main(String[] args) {
        String host = "127.0.0.1";
        int port = 9999;

        if (!connectionManager.connect(host, port)) {
            System.out.println("Failed to connect to " + host + ":" + port);
        }


    }
}

package dev.zwazel.connection;

import lombok.RequiredArgsConstructor;

import java.net.Socket;

@RequiredArgsConstructor
public class ListenerThread implements Runnable {
    private final ConnectionManager manager;

    @Override
    public void run() {
        System.out.println("Listener thread started");
        Socket socket = manager.getSocket();

        try {
            while (!Thread.currentThread().isInterrupted()) {
                manager.send();

                byte[] buffer = new byte[1024];
                int bytesRead = socket.getInputStream().read(buffer);
                if (bytesRead == -1) {
                    System.out.println("Connection closed by remote host");
                    break;
                }
                String message = new String(buffer, 0, bytesRead);
                System.out.println("Received message: " + message);
            }
        } catch (Exception e) {
            System.err.println("Error reading from socket");
            e.printStackTrace();
        }
    }
}

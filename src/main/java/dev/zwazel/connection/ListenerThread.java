package dev.zwazel.connection;

import dev.zwazel.messages.MessageContainer;
import dev.zwazel.messages.MessageParser;
import lombok.RequiredArgsConstructor;

import java.io.DataInputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

@RequiredArgsConstructor
public class ListenerThread implements Runnable {
    private final ConnectionManager manager;

    @Override
    public void run() {
        System.out.println("Listener thread started");
        Socket socket = manager.getSocket();

        try {
            DataInputStream input = new DataInputStream(socket.getInputStream());
            while (!Thread.currentThread().isInterrupted()) {
                byte[] lengthBytes = new byte[4];
                input.readFully(lengthBytes);
                int length = ByteBuffer.wrap(lengthBytes).order(ByteOrder.LITTLE_ENDIAN).getInt();
                System.out.println("Received message length: " + length);

                if (length == 0) {
                    System.out.println("Received message length is zero.");
                    return;
                }

                byte[] data = new byte[length];
                input.readFully(data);
                System.out.println("Received message data: " + new String(data));

                MessageContainer message = MessageParser.parseMessage(data);
                System.out.println("Received message:\n" + message);
            }
        } catch (Exception e) {
            System.err.println("Error reading from socket");
            e.printStackTrace();
        }
    }
}

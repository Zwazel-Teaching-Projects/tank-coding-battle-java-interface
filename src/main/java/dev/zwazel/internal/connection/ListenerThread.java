package dev.zwazel.internal.connection;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.zwazel.internal.InternalGameWorld;
import dev.zwazel.internal.messages.MessageContainer;
import lombok.RequiredArgsConstructor;

import java.io.DataInputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

@RequiredArgsConstructor
public class ListenerThread implements Runnable {
    private final ConnectionManager manager;
    private final InternalGameWorld world;
    private final DataInputStream input;

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void run() {
        System.out.println("Listener thread started");

        try {
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

                MessageContainer message = mapper.readValue(data, MessageContainer.class);
                System.out.println("Received message:\n" + message);

                world.pushIncomingMessage(message);
            }
        } catch (Exception e) {
            System.err.println("Error reading from socket");
            e.printStackTrace();
        }
    }
}

package dev.zwazel.internal.connection;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.zwazel.internal.InternalGameWorld;
import dev.zwazel.internal.message.MessageContainer;
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
            while (manager.isConnected()) {
                byte[] lengthBytes = new byte[4];
                input.readFully(lengthBytes);
                int length = ByteBuffer.wrap(lengthBytes).order(ByteOrder.LITTLE_ENDIAN).getInt();

                if (length == 0) {
                    return;
                }

                byte[] data = new byte[length];
                input.readFully(data);

                if (world.isInternalDebug()) {
                    JsonNode json = mapper.readTree(data);

                    System.out.println("Received message (JSON):\n\t" + json.toPrettyString());
                }

                MessageContainer[] messages = mapper.readValue(data, MessageContainer[].class);

                for (MessageContainer message : messages) {
                    message.applyOnReceive(world);
                }
            }
        } catch (Exception e) {
            System.err.println("Error reading from socket");
            world.stop();
            e.printStackTrace();
        }
    }
}

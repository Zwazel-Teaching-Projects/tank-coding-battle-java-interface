package dev.zwazel.internal.connection;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.zwazel.internal.InternalGameWorld;
import dev.zwazel.internal.messages.MessageContainer;
import lombok.RequiredArgsConstructor;

import java.io.DataOutputStream;
import java.nio.charset.StandardCharsets;

@RequiredArgsConstructor
public class WritingThread implements Runnable {
    private final ConnectionManager manager;
    private final InternalGameWorld world;
    private final DataOutputStream output;

    @Override
    public void run() {
        System.out.println("Writing thread started");

        while (!Thread.currentThread().isInterrupted()) {
            world.pollOutgoingMessage().ifPresent(this::send);
        }
    }

    public void send(MessageContainer message) {
        System.out.println("Trying to send message:\n" + message);

        if (!manager.isConnected()) {
            System.err.println("Socket is not connected");
            return;
        }

        try {
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(message);
            byte[] jsonBytes = json.getBytes(StandardCharsets.UTF_8);

            // Send the length prefix
            output.writeInt(jsonBytes.length);

            // Write the message
            output.write(jsonBytes);

            // Flush the dos
            output.flush();

            System.out.println("Sent message with " + jsonBytes.length + " bytes.\n" + json);
        } catch (Exception e) {
            System.err.println("Error sending message");
            e.printStackTrace();
        }
    }
}

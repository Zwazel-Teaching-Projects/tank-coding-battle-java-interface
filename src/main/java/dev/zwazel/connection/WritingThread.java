package dev.zwazel.connection;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.zwazel.messages.MessageContainer;
import lombok.RequiredArgsConstructor;

import java.io.DataOutputStream;
import java.nio.charset.StandardCharsets;

@RequiredArgsConstructor
public class WritingThread implements Runnable {
    private final ConnectionManager manager;
    private final DataOutputStream output;

    @Override
    public void run() {
        System.out.println("Writing thread started");

        try {

            while (!Thread.currentThread().isInterrupted()) {
                System.out.println("Waiting for message to send...");
                MessageContainer message = manager.getOutgoingMessages().take();
                System.out.println("Sending message:\n" + message);
                send(message);
            }
        } catch (Exception e) {
            System.err.println("Error writing to socket");
            e.printStackTrace();
        }
    }

    public void send(MessageContainer message) {
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

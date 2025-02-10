package dev.zwazel.internal.connection;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.zwazel.internal.InternalGameWorld;
import dev.zwazel.internal.message.MessageContainer;
import lombok.RequiredArgsConstructor;

import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@RequiredArgsConstructor
public class WritingThread implements Runnable {
    private final ConnectionManager manager;
    private final InternalGameWorld world;
    private final DataOutputStream output;

    @Override
    public void run() {
        System.out.println("Writing thread started");

        while (world.getPublicGameWorld().isRunning()) {
            world.pollOutgoingMessage().ifPresent(message -> {
                try {
                    send(message);
                } catch (Exception e) {
                    e.printStackTrace();
                    world.stop();
                }
            });
        }
    }

    public void send(MessageContainer message) throws IOException {
        if (!manager.isConnected()) {
            System.err.println("Socket is not connected");
            return;
        }

        message.applyBeforeSend(world);
        
        if (world.isInternalDebug()) {
            System.out.println("Sending message:\n\t" + message);
        }

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);

        String json = mapper.writeValueAsString(message);

        byte[] jsonBytes = json.getBytes(StandardCharsets.UTF_8);

        // Send the length prefix
        output.writeInt(jsonBytes.length);

        // Write the message
        output.write(jsonBytes);

        output.flush();
    }
}

package dev.zwazel.internal;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.zwazel.internal.message.MessageContainer;
import dev.zwazel.internal.message.data.GameConfig;
import dev.zwazel.internal.message.data.GameState;
import lombok.RequiredArgsConstructor;

import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

@RequiredArgsConstructor
public class GameSimulationThread implements Runnable {
    private final InternalGameWorld internalWorld;
    private final DataOutputStream output;
    private final ObjectMapper objectMapper = new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
    private boolean ranSetup = false;

    @Override
    public void run() {
        System.out.println("Game simulation thread started");

        PublicGameWorld publicWorld = internalWorld.getPublicGameWorld();
        long currentTickToProcess = 0;

        try {
            while (publicWorld.isRunning()) {
                GameState state = publicWorld.getGameState();

                if (!ranSetup) {
                    GameConfig config = publicWorld.getGameConfig();
                    if (config != null) {
                        internalWorld.getBot().setup(publicWorld, config);
                        ranSetup = true;
                    }
                }

                if (state != null && state.tick() > currentTickToProcess) {
                    // Process the next tick
                    currentTickToProcess = state.tick();

                    // Remove any message from the queue that is older than the current tick
                    long finalCurrentTickToProcess = currentTickToProcess;
                    internalWorld.getIncomingMessageQueue().removeIf(message
                            -> message.getTickSent() < finalCurrentTickToProcess);

                    // Calling Bot
                    internalWorld.getBot().processTick(publicWorld, publicWorld.getTank());
                }

                sendTickMessages();
            }
        } catch (Exception e) {
            System.err.println("Error processing game simulation");
            internalWorld.stop();
            e.printStackTrace();
        }
    }

    private void sendTickMessages() throws IOException {
        BlockingQueue<MessageContainer> messages = internalWorld.getOutgoingMessageQueue();
        List<MessageContainer> messageList = new ArrayList<>();
        messages.drainTo(messageList);

        if (messageList.isEmpty()) {
            return;
        }

        messageList.forEach(message -> message.applyBeforeSend(internalWorld));

        String json = objectMapper.writeValueAsString(messageList);

        if (internalWorld.isInternalDebug()) {
            System.out.println("Sending message:\n\t" + json);
        }

        byte[] jsonBytes = json.getBytes(StandardCharsets.UTF_8);

        // Send the length prefix
        output.writeInt(jsonBytes.length);

        // Write the message
        output.write(jsonBytes);

        output.flush();
    }
}

package dev.zwazel.internal;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.zwazel.internal.message.MessageContainer;
import dev.zwazel.internal.message.data.GameConfig;
import dev.zwazel.internal.message.data.GameState;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.swing.*;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.BlockingQueue;

@RequiredArgsConstructor
public class GameSimulationThread implements Runnable {
    private final InternalGameWorld internalWorld;
    private final DataOutputStream output;
    private final ObjectMapper objectMapper = new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
    private boolean ranSetup = false;
    @Setter
    private JPanel mapVisualiser;

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
                        internalWorld.getBot().setup(publicWorld);
                        ranSetup = true;
                    }
                }

                if (state != null && state.tick() > currentTickToProcess) {
                    // Process the next tick
                    currentTickToProcess = state.tick();
                    // Update the map visualiser
                    if (mapVisualiser != null) {
                        mapVisualiser.repaint();
                    }

                    // Remove any message from the queue that is older than the current tick
                    long finalCurrentTickToProcess = currentTickToProcess;
                    internalWorld.getIncomingMessageQueue().removeIf(message
                            -> message.getTickSent() < finalCurrentTickToProcess);

                    // Calling Bot
                    internalWorld.getBot().processTick(publicWorld);
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

        // Use a set to track which unique message types have been encountered (from the end)
        Set<Class<?>> seenUniqueTypes = new HashSet<>();
        // Iterate backward to keep the most recent message and remove older duplicates
        ListIterator<MessageContainer> iterator = messageList.listIterator(messageList.size());
        while (iterator.hasPrevious()) {
            MessageContainer current = iterator.previous();
            if (current.getMessage().isUnique()) {
                if (seenUniqueTypes.contains(current.getMessage().getClass())) {
                    // Remove this outdated message of the same unique type
                    iterator.remove();

                    if (internalWorld.isInternalDebug()) {
                        System.out.println("Duplicate message type removed: " + current.getMessage().getClass().getSimpleName());
                    }
                } else {
                    // Mark this unique type as encountered
                    seenUniqueTypes.add(current.getMessage().getClass());

                    if (internalWorld.isInternalDebug()) {
                        System.out.println("Unique message type: " + current.getMessage().getClass().getSimpleName());
                    }
                }
            }
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

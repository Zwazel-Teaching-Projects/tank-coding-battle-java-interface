package dev.zwazel.internal;

import dev.zwazel.internal.message.data.GameConfig;
import dev.zwazel.internal.message.data.GameState;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class GameSimulationThread implements Runnable {
    private final InternalGameWorld internalWorld;
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
            }
        } catch (Exception e) {
            System.err.println("Error processing game simulation");
            internalWorld.stop();
            e.printStackTrace();
        }
    }
}

package dev.zwazel.internal;

import dev.zwazel.internal.message.data.GameState;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GameSimulationThread implements Runnable {
    private final InternalGameWorld internalWorld;

    @Override
    public void run() {
        System.out.println("Game simulation thread started");

        PublicGameWorld publicWorld = internalWorld.getPublicGameWorld();
        long currentTickToProcess = 0;

        try {
            while (publicWorld.isRunning()) {
                GameState state = publicWorld.getGameState();

                if (state != null && state.tick() > currentTickToProcess) {
                    currentTickToProcess = state.tick();
                    internalWorld.getBot().processTick(publicWorld);

                    // TODO: Process new Tick
                }
            }
        } catch (Exception e) {
            System.err.println("Error processing game simulation");
            internalWorld.stop();
            e.printStackTrace();
        }
    }
}

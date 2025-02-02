package dev.zwazel.internal;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GameSimulationThread implements Runnable {
    private final InternalGameWorld world;

    @Override
    public void run() {
        PublicGameWorld publicWorld = world.getPublicGameWorld();
        System.out.println("Game simulation thread started!");

        while (publicWorld.isRunning()) {

        }
    }
}

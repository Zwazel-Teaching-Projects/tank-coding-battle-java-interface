package dev.zwazel.bot;

import dev.zwazel.internal.PublicGameWorld;

public class TestBot implements BotInterface {
    @Override
    public void processTick(PublicGameWorld world) {
        System.out.println("TestBot tick!");
        System.out.println(world.getGameState().tick());
    }
}

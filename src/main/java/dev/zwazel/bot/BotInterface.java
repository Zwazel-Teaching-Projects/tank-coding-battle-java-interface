package dev.zwazel.bot;

import dev.zwazel.internal.PublicGameWorld;

public interface BotInterface {
    /**
     * Set up the bot.
     * Gets called once at the start of the game.
     *
     * @param world The game world.
     */
    void setup(PublicGameWorld world);

    /**
     * Process a tick.
     * Gets called every tick.
     * Should be used to schedule commands, like moving or shooting.
     *
     * @param world The game world.
     */
    void processTick(PublicGameWorld world);
}

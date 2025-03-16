package dev.zwazel.bot;

import dev.zwazel.internal.PublicGameWorld;
import dev.zwazel.internal.config.LocalBotConfig;

public interface BotInterface {
    /**
     * Get the local bot configuration.
     * The local bot configuration is used to determine the bot's name and tank type when connecting to the game. It can not be changed after the game has started.
     *
     * @return the local bot configuration
     */
    LocalBotConfig getLocalBotConfig();

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

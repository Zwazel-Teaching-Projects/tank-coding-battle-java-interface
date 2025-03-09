package dev.zwazel.internal.message.data;

import dev.zwazel.internal.InternalGameWorld;
import dev.zwazel.internal.message.MessageData;
import lombok.Builder;

/**
 * Message data for when a player successfully joins a lobby.
 *
 * @param message The message.
 */
@Builder
public record SuccessfullyJoinedLobby(String message) implements MessageData {
    /**
     * Apply the message data on receive. if the bot is configured to start the game, the game will start.
     *
     * @param world the world
     * @return true so the bot can also handle the message if needed.
     */
    @Override
    public boolean applyOnReceive(InternalGameWorld world) {
        world.startGame();

        return true;
    }
}

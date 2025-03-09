package dev.zwazel.internal.message.data;

import com.fasterxml.jackson.annotation.JsonTypeName;
import dev.zwazel.internal.InternalGameWorld;
import dev.zwazel.internal.message.MessageData;
import lombok.Builder;

/**
 * Start game configuration.
 * Sent to the server to start the game.
 *
 * @param fillEmptySlotsWithDummies Whether to fill empty slots with dummies or not.
 */
@Builder
@JsonTypeName("StartGame")
public record StartGameConfig(boolean fillEmptySlotsWithDummies) implements MessageData {
    @Override
    public boolean applyOnReceive(InternalGameWorld world) {
        return false;
    }
}

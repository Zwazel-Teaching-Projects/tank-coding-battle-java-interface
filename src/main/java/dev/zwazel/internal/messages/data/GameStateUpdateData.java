package dev.zwazel.internal.messages.data;

import com.fasterxml.jackson.annotation.JsonTypeName;
import dev.zwazel.GameWorld;
import dev.zwazel.internal.messages.MessageData;

@JsonTypeName("GAME_STATE_UPDATE")
public record GameStateUpdateData(int tick) implements MessageData {
    @Override
    public void applyOnReceive(GameWorld world) {

    }
}

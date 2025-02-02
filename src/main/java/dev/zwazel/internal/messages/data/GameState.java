package dev.zwazel.internal.messages.data;

import com.fasterxml.jackson.annotation.JsonTypeName;
import dev.zwazel.internal.InternalGameWorld;
import dev.zwazel.internal.messages.MessageData;

@JsonTypeName("GAME_STATE_UPDATE")
public record GameState(Long tick) implements MessageData {

    @Override
    public void applyOnReceive(InternalGameWorld internalWorld) {
        internalWorld.updateState(this);
    }
}

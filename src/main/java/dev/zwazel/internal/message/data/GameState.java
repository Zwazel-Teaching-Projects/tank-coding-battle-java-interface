package dev.zwazel.internal.message.data;

import dev.zwazel.internal.InternalGameWorld;
import dev.zwazel.internal.message.MessageData;

public record GameState(Long tick) implements MessageData {

    @Override
    public boolean applyOnReceive(InternalGameWorld internalWorld) {
        internalWorld.updateState(this);
        return false;
    }
}

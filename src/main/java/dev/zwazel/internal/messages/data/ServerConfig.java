package dev.zwazel.internal.messages.data;

import dev.zwazel.internal.InternalGameWorld;
import dev.zwazel.internal.messages.MessageData;

public record ServerConfig(long clientId, long tickRate) implements MessageData {
    @Override
    public void applyOnReceive(InternalGameWorld internalWorld) {
        internalWorld.setServerConfig(this);
    }
}

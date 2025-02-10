package dev.zwazel.internal.message.data;

import dev.zwazel.internal.InternalGameWorld;
import dev.zwazel.internal.message.MessageData;

public record ServerConfig(long clientId, long tickRate) implements MessageData {
    @Override
    public void applyOnReceive(InternalGameWorld internalWorld) {
        internalWorld.setServerConfig(this);
    }
}

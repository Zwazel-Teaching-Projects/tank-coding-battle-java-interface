package dev.zwazel.internal.message.data;

import dev.zwazel.internal.InternalGameWorld;
import dev.zwazel.internal.message.MessageData;
import lombok.Builder;

@Builder
public record StartGameConfig(boolean fillEmptySlotsWithDummies) implements MessageData {
    @Override
    public boolean applyOnReceive(InternalGameWorld world) {
        return false;
    }
}

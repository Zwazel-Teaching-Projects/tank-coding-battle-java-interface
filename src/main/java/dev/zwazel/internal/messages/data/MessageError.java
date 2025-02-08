package dev.zwazel.internal.messages.data;

import dev.zwazel.internal.InternalGameWorld;
import dev.zwazel.internal.messages.MessageData;

public record MessageError(ErrorMessageTypes error) implements MessageData {
    @Override
    public void applyOnReceive(InternalGameWorld internalWorld) {

    }
}

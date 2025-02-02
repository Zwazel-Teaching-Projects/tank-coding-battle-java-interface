package dev.zwazel.internal;

import dev.zwazel.internal.messages.MessageContainer;

public interface PublicGameWorld {
    boolean isRunning();

    void send(MessageContainer message);
}

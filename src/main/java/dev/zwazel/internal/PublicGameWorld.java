package dev.zwazel.internal;

import dev.zwazel.internal.messages.MessageContainer;
import dev.zwazel.internal.messages.data.GameState;

public interface PublicGameWorld {
    boolean isRunning();

    void send(MessageContainer message);

    GameState getGameState();
}

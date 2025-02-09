package dev.zwazel.internal;

import dev.zwazel.internal.messages.MessageContainer;
import dev.zwazel.internal.messages.data.GameState;

public interface PublicGameWorld {
    boolean isRunning();

    boolean isDebug();

    void send(MessageContainer message);

    GameState getGameState();
}

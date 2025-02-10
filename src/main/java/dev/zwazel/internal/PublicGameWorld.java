package dev.zwazel.internal;

import dev.zwazel.internal.client.ConnectedClientConfig;
import dev.zwazel.internal.message.MessageContainer;
import dev.zwazel.internal.message.data.GameConfig;
import dev.zwazel.internal.message.data.GameState;

import java.util.Optional;

public interface PublicGameWorld {
    boolean isRunning();

    boolean isDebug();

    void send(MessageContainer message);

    GameState getGameState();

    Long getMyClientId();

    Optional<ConnectedClientConfig> getConnectedClient(String name);

    ConnectedClientConfig[] getConnectedClients();

    GameConfig getGameConfig();
}

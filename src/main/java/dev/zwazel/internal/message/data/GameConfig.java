package dev.zwazel.internal.message.data;

import dev.zwazel.internal.InternalGameWorld;
import dev.zwazel.internal.connection.client.ConnectedClientConfig;
import dev.zwazel.internal.game.map.MapDefinition;
import dev.zwazel.internal.message.MessageData;

import java.util.Arrays;
import java.util.Optional;

public record GameConfig(long tickRate, long clientId, MapDefinition mapDefinition,
                         ConnectedClientConfig[] connectedClients) implements MessageData {
    @Override
    public boolean applyOnReceive(InternalGameWorld internalWorld) {
        internalWorld.setGameConfig(this);

        System.out.println("Received Game Config from Server:\n\t" + this);

        return false;
    }

    public Optional<ConnectedClientConfig> getClientConfig(String clientName) {
        return Arrays.stream(connectedClients)
                .filter(clientConfig -> clientConfig.clientName().equals(clientName))
                .findFirst();
    }

    public Optional<ConnectedClientConfig> getClientConfig(long clientId) {
        return Arrays.stream(connectedClients)
                .filter(clientConfig -> clientConfig.clientId() == clientId)
                .findFirst();
    }
}

package dev.zwazel.internal.messages.data;

import dev.zwazel.internal.InternalGameWorld;
import dev.zwazel.internal.messages.MessageData;
import lombok.Builder;

@Builder
public record FirstContact(String botName, String lobbyName, String mapName, String teamName,
                           ClientType clientType) implements MessageData {
    @Override
    public void applyOnReceive(InternalGameWorld internalWorld) {
        // Do nothing, we should never receive this message, only send it to the server
    }

    public enum ClientType {
        PLAYER,
        SPECTATOR
    }
}
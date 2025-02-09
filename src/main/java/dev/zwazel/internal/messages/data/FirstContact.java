package dev.zwazel.internal.messages.data;

import dev.zwazel.internal.messages.MessageData;
import lombok.Builder;

@Builder
public record FirstContact(String botName, String lobbyName, String mapName, String teamName,
                           ClientType clientType) implements MessageData {
    public enum ClientType {
        PLAYER,
        SPECTATOR
    }
}
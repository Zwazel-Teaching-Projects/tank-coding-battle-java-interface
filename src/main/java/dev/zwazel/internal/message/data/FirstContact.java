package dev.zwazel.internal.message.data;

import dev.zwazel.internal.message.MessageData;
import lombok.Builder;

@Builder
public record FirstContact(String botName, String lobbyName, String mapName, String teamName,
                           ClientType clientType, Long botAssignedSpawnPoint) implements MessageData {
    public enum ClientType {
        PLAYER,
        SPECTATOR
    }
}
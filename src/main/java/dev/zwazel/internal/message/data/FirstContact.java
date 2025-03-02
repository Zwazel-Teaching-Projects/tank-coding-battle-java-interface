package dev.zwazel.internal.message.data;

import dev.zwazel.internal.game.tank.TankType;
import dev.zwazel.internal.message.MessageData;
import lombok.Builder;

@Builder
public record FirstContact(String botName, String lobbyName, String mapName, String teamName,
                           ClientType clientType, Long botAssignedSpawnPoint, TankType tankType) implements MessageData {
    public enum ClientType {
        PLAYER,
        SPECTATOR
    }
}
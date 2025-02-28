package dev.zwazel.internal.connection.client;

import dev.zwazel.internal.game.tank.TankType;

public record ConnectedClientConfig(long clientId, String clientName, String clientTeam, long assignedSpawnPoint,
                                    TankType clientTankType) {
}

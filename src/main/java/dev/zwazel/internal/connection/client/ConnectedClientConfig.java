package dev.zwazel.internal.connection.client;

public record ConnectedClientConfig(long clientId, String clientName, String clientTeam, long assignedSpawnPoint,
                                    String clientTankType) {
}

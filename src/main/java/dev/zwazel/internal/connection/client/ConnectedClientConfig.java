package dev.zwazel.internal.connection.client;

import dev.zwazel.internal.PublicGameWorld;
import dev.zwazel.internal.game.state.ClientState;
import dev.zwazel.internal.game.tank.TankConfig;
import dev.zwazel.internal.game.tank.TankType;

public record ConnectedClientConfig(long clientId, String clientName, String clientTeam, long assignedSpawnPoint,
                                    TankType clientTankType) {
    public ClientState getClientState(PublicGameWorld world) {
        return world.getClientState(this);
    }

    public TankConfig getTankConfig(PublicGameWorld world) {
        return world.getTankConfig(clientTankType).orElse(null);
    }
}

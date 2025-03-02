package dev.zwazel.internal.message.data.tank;

import dev.zwazel.internal.InternalGameWorld;
import dev.zwazel.internal.game.state.ClientState;
import dev.zwazel.internal.game.tank.TankConfig;
import dev.zwazel.internal.game.tank.TankType;
import dev.zwazel.internal.message.MessageData;

public record ShootCommand() implements MessageData {
    @Override
    public void applyOnAddingToQueue(InternalGameWorld world) {
        ClientState predictedState = world.getPublicGameWorld().getMyPredictedState();

        TankConfig tankConfig = world.getPublicGameWorld().getConnectedClientConfig(predictedState)
                .map(clientConfig -> {
                    TankType tankType = clientConfig.clientTankType();
                    return world.getPublicGameWorld().getTankConfig(tankType).orElse(null);
                }).orElse(null);

        if (tankConfig == null) {
            return;
        }

        world.updatePredictedState(new ClientState(predictedState.id(), predictedState.transformBody(),
                predictedState.transformTurret(), predictedState.state(), tankConfig.shootCooldown(), predictedState.currentHealth()));
    }
}

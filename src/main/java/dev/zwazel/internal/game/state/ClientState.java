package dev.zwazel.internal.game.state;

import dev.zwazel.internal.game.transform.Transform;

public record ClientState(long id, Transform transformBody, Transform transformTurret, PlayerState state,
                          long shootCooldown) {
    enum PlayerState {
        ALIVE,
        DEAD
    }
}

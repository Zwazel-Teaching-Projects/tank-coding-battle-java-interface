package dev.zwazel.internal.game.state;

import dev.zwazel.internal.game.transform.Transform;

/**
 * Represents the state of a client in the game.
 *
 * @param id              The id of the client
 * @param transformBody   The transform of the body of the tank
 * @param transformTurret The transform of the turret of the tank (relative to the body)
 * @param state           The state of the player (e.g. alive, dead)
 * @param shootCooldown   The amount of ticks until the tank can shoot again
 */
public record ClientState(long id, Transform transformBody, Transform transformTurret, PlayerState state,
                          long shootCooldown) {
    enum PlayerState {
        ALIVE,
        DEAD
    }
}

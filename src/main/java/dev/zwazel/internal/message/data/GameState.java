package dev.zwazel.internal.message.data;

import dev.zwazel.internal.InternalGameWorld;
import dev.zwazel.internal.game.state.ClientState;
import dev.zwazel.internal.game.state.FlagBaseState;
import dev.zwazel.internal.game.state.FlagGameState;
import dev.zwazel.internal.game.state.ProjectileState;
import dev.zwazel.internal.message.MessageData;
import lombok.Builder;

import java.util.HashMap;

/**
 * Represents the state of the game at a given tick.
 *
 * @param tick             The tick at which this state was recorded.
 * @param score            The score of each team.
 * @param clientStates     The state of each client.
 * @param projectileStates The state of each projectile.
 * @param flagStates       The state of each flag.
 * @param flagBaseStates   The state of each flag base.
 */
@Builder
public record GameState(Long tick, HashMap<String, Long> score, HashMap<Long, ClientState> clientStates,
                        HashMap<Long, ProjectileState> projectileStates,
                        HashMap<Long, FlagGameState> flagStates,
                        HashMap<Long, FlagBaseState> flagBaseStates) implements MessageData {
    @Override
    public boolean applyOnReceive(InternalGameWorld internalWorld) {
        internalWorld.updateState(this);
        return false;
    }
}

package dev.zwazel.internal.message.data;

import dev.zwazel.internal.InternalGameWorld;
import dev.zwazel.internal.game.state.ClientState;
import dev.zwazel.internal.game.state.FlagGameState;
import dev.zwazel.internal.game.state.ProjectileState;
import dev.zwazel.internal.message.MessageData;
import lombok.Builder;

import java.util.HashMap;

@Builder
public record GameState(Long tick, HashMap<Long, ClientState> clientStates,
                        HashMap<Long, ProjectileState> projectileStates,
                        HashMap<Long, FlagGameState> flagStates) implements MessageData {
    @Override
    public boolean applyOnReceive(InternalGameWorld internalWorld) {
        internalWorld.updateState(this);
        return false;
    }
}

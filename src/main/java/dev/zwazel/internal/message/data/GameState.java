package dev.zwazel.internal.message.data;

import dev.zwazel.internal.InternalGameWorld;
import dev.zwazel.internal.game.state.ClientState;
import dev.zwazel.internal.message.MessageData;

import java.util.HashMap;

public record GameState(Long tick, HashMap<Long, ClientState> clientStates) implements MessageData {
    @Override
    public boolean applyOnReceive(InternalGameWorld internalWorld) {
        internalWorld.updateState(this);
        return false;
    }
}

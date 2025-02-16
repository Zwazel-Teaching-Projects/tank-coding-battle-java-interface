package dev.zwazel.internal.game.state;

import dev.zwazel.internal.game.map.Transform;

public record ClientState(long id, Transform transform) {
}

package dev.zwazel.internal.game.state;

import dev.zwazel.internal.game.transform.Transform;

public record ProjectileState(Long projectileId, Long ownerId, Transform transform) {
}

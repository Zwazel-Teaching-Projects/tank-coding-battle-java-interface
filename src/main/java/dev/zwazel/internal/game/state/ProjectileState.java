package dev.zwazel.internal.game.state;

import dev.zwazel.internal.game.transform.Transform;

/**
 * Represents the state of a projectile.
 *
 * @param projectileId The ID of the projectile.
 * @param ownerId      The ID of the client that owns the projectile (client that fired it).
 * @param transform    The transform of the projectile.
 */
public record ProjectileState(Long projectileId, Long ownerId, Transform transform) {
}

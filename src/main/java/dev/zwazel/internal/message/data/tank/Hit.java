package dev.zwazel.internal.message.data.tank;

import dev.zwazel.internal.game.misc.Side;
import dev.zwazel.internal.message.MessageData;

/**
 * Represents the data of a hit message.
 * We receive this message when a projectile, that this client has shot, hits another entity.
 *
 * @param hitEntity        The entity that was hit
 * @param projectileEntity The entity of the projectile that hit the entity
 * @param hitSide          The side of the entity that was hit
 * @param damageDealt      The amount of damage that was dealt
 */
public record Hit(long hitEntity, long projectileEntity, Side hitSide,
                  float damageDealt) implements MessageData {
}

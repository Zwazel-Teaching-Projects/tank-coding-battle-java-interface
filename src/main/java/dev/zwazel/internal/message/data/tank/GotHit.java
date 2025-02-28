package dev.zwazel.internal.message.data.tank;

import dev.zwazel.internal.game.misc.Side;
import dev.zwazel.internal.message.MessageData;

/**
 * Represents the data of a hit message.
 * We receive this message when this client gets hit by a projectile.
 *
 * @param shooterEntity    The entity that shot the projectile
 * @param projectileEntity The entity of the projectile that hit this client
 * @param hitSide          The side of this client that was hit
 * @param damageReceived   The amount of damage that was received
 */
public record GotHit(long shooterEntity, long projectileEntity, Side hitSide,
                     float damageReceived) implements MessageData {
}

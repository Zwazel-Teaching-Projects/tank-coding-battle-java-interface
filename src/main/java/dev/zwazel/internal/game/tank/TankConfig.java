package dev.zwazel.internal.game.tank;

import dev.zwazel.internal.game.misc.Side;
import dev.zwazel.internal.game.transform.Vec3;

import java.util.HashMap;

/**
 * @param moveSpeed                the speed at which the tank can move forward and backward per tick
 * @param bodyRotationSpeed        the speed at which the tank can rotate left and right per tick
 * @param turretYawRotationSpeed   the speed at which the turret can rotate left and right per tick
 * @param turretPitchRotationSpeed the speed at which the turret can rotate up and down per tick
 * @param turretMaxPitch           the maximum pitch the turret can have
 * @param turretMinPitch           the minimum pitch the turret can have
 * @param maxSlope                 the maximum slope the tank can drive on ("climb" up)
 * @param size                     the size of the tank (full extents)
 * @param shootCooldown            how many ticks the tank has to wait between shots
 * @param projectileDamage         how much damage the projectile does
 * @param projectileSpeed          how fast the projectile moves per tick
 * @param projectileLifetime       how many ticks the projectile lives
 * @param projectileSize           the size of the projectile (half extents)
 * @param maxHealth                the maximum health the tank can have
 * @param armor                    the armor of the tank on each side (0-1, 0 = no armor, 1 = full armor)
 *                                 Damage is reduced by the armor value on the side that was hit
 *                                 calculated as: damage = damage * (1 - armor)
 * @param respawnTimer             how many ticks the tank has to wait before respawning
 * @param projectileGravity        how much the projectile is affected by gravity (if 0, no gravity)
 */
public record TankConfig(float moveSpeed, float bodyRotationSpeed, float turretYawRotationSpeed,
                         float turretPitchRotationSpeed, float turretMaxPitch, float turretMinPitch, float maxSlope,
                         Vec3 size, Long shootCooldown, float projectileDamage, float projectileSpeed,
                         long projectileLifetime, Vec3 projectileSize, float maxHealth, HashMap<Side, Float> armor,
                         long respawnTimer, float projectileGravity) {
}

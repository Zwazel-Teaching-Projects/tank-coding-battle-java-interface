package dev.zwazel.internal.game.tank;

import dev.zwazel.internal.game.transform.Vec3;

/**
 * @param moveSpeed                the speed at which the tank can move forward and backward per tick
 * @param bodyRotationSpeed        the speed at which the tank can rotate left and right per tick
 * @param turretYawRotationSpeed   the speed at which the turret can rotate left and right per tick
 * @param turretPitchRotationSpeed the speed at which the turret can rotate up and down per tick
 * @param turretMaxPitch           the maximum pitch the turret can have
 * @param turretMinPitch           the minimum pitch the turret can have
 * @param maxSlope                 the maximum slope the tank can drive on ("climb" up)
 * @param size                     the size of the tank (half extents)
 * @param shootCooldown            how many ticks the tank has to wait between shots
 * @param projectileDamage         how much damage the projectile does
 * @param projectileSpeed          how fast the projectile moves per tick
 * @param projectileLifetime       how many ticks the projectile lives
 * @param projectileSize           the size of the projectile (half extents)
 * @param maxHealth                the maximum health the tank can have
 */
public record TankConfig(double moveSpeed, double bodyRotationSpeed, double turretYawRotationSpeed,
                         double turretPitchRotationSpeed, double turretMaxPitch, double turretMinPitch, double maxSlope,
                         Vec3 size, Long shootCooldown, double projectileDamage, double projectileSpeed,
                         long projectileLifetime, Vec3 projectileSize, double maxHealth) {
}

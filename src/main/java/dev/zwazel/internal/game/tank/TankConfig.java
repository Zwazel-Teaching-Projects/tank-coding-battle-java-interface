package dev.zwazel.internal.game.tank;

import dev.zwazel.internal.game.transform.Vec3;

public record TankConfig(float moveSpeed, double bodyRotationSpeed, double turretYawRotationSpeed,
                         double turretPitchRotationSpeed, double turretMaxPitch, double turretMinPitch, float maxSlope,
                         Vec3 size) {
}

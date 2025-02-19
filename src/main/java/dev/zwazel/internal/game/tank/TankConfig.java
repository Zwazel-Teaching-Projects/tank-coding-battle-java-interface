package dev.zwazel.internal.game.tank;

import dev.zwazel.internal.game.transform.Vec3;

public record TankConfig(float moveSpeed, float bodyRotationSpeed, float turretRotationSpeed, float maxSlope,
                         Vec3 size) {
}

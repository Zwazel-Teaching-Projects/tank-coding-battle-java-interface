package dev.zwazel.internal.message.data.tank;

import dev.zwazel.internal.game.tank.Tank;
import dev.zwazel.internal.message.MessageData;
import lombok.Builder;

@Builder
public record RotateTankTurretCommand(Tank.RotationDirection direction, float angle) implements MessageData {
}

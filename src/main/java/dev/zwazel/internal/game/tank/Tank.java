package dev.zwazel.internal.game.tank;

import dev.zwazel.internal.PublicGameWorld;
import dev.zwazel.internal.message.MessageContainer;
import dev.zwazel.internal.message.MessageTarget;
import dev.zwazel.internal.message.data.GameConfig;
import dev.zwazel.internal.message.data.tank.MoveTankCommand;
import dev.zwazel.internal.message.data.tank.RotateTankBodyCommand;
import dev.zwazel.internal.message.data.tank.RotateTankTurretCommand;

import java.util.HashMap;

public interface Tank {
    String getTankType();

    default TankConfig getConfig(PublicGameWorld world) {
        GameConfig config = world.getGameConfig();
        HashMap<String, TankConfig> configs = config.tankConfigs();
        return configs.get(getTankType());
    }

    default void move(PublicGameWorld world, MoveDirection direction) {
        TankConfig config = getConfig(world);
        move(world, direction, config.moveSpeed());
    }

    default void move(PublicGameWorld world, MoveDirection direction, float distance) {
        TankConfig config = getConfig(world);
        // If distance > config.moveSpeed, move only config.moveSpeed
        distance = Math.min(distance, config.moveSpeed());

        MessageContainer message = new MessageContainer(
                MessageTarget.Type.TO_SELF.get(),
                MoveTankCommand.builder().direction(direction).distance(distance).build()
        );
        world.send(message);
    }

    default void rotateBody(PublicGameWorld world, RotationDirection direction) {
        TankConfig config = getConfig(world);
        rotateBody(world, direction, config.bodyRotationSpeed());
    }

    default void rotateBody(PublicGameWorld world, RotationDirection direction, float angle) {
        TankConfig config = getConfig(world);
        // If angle > config.rotationSpeed, rotate only config.rotationSpeed
        angle = Math.min(angle, config.bodyRotationSpeed());

        MessageContainer message = new MessageContainer(
                MessageTarget.Type.TO_SELF.get(),
                RotateTankBodyCommand.builder().direction(direction).angle(angle).build()
        );
        world.send(message);
    }

    default void rotateTurret(PublicGameWorld world, RotationDirection direction) {
        TankConfig config = getConfig(world);
        rotateTurret(world, direction, config.turretRotationSpeed());
    }

    default void rotateTurret(PublicGameWorld world, RotationDirection direction, float angle) {
        TankConfig config = getConfig(world);
        // If angle > config.rotationSpeed, rotate only config.rotationSpeed
        angle = Math.min(angle, config.turretRotationSpeed());

        MessageContainer message = new MessageContainer(
                MessageTarget.Type.TO_SELF.get(),
                RotateTankTurretCommand.builder().direction(direction).angle(angle).build()
        );
        world.send(message);
    }

    enum MoveDirection {
        FORWARD,
        BACKWARD,
    }

    enum RotationDirection {
        CLOCKWISE,
        COUNTER_CLOCKWISE,
    }
}

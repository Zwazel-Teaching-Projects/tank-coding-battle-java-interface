package dev.zwazel.internal.game.tank;

import dev.zwazel.internal.PublicGameWorld;
import dev.zwazel.internal.game.transform.Quaternion;
import dev.zwazel.internal.game.transform.Transform;
import dev.zwazel.internal.game.transform.Vec3;
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

    default Transform getTransform(PublicGameWorld world) {
        return world.getMyState().transform();
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

    default void rotateBody(PublicGameWorld world, RotationDirection direction, double angle) {
        TankConfig config = getConfig(world);
        // If angle > config.rotationSpeed, rotate only config.rotationSpeed
        angle = Math.min(angle, config.bodyRotationSpeed());

        MessageContainer message = new MessageContainer(
                MessageTarget.Type.TO_SELF.get(),
                RotateTankBodyCommand.builder().direction(direction).angle(angle).build()
        );

        world.send(message);
    }

    default void rotateBodyTowards(PublicGameWorld world, Vec3 targetPosition) {
        // Get the current tank transform.
        Transform currentTransform = world.getMyState().transform();
        Vec3 currentPosition = currentTransform.getPosition();
        Quaternion currentRotation = currentTransform.getRotation();

        // Get the current forward vector in game space.
        Vec3 currentForward = currentRotation.getForward();
        double currentYaw = currentForward.getAngle();

        // Compute the vector from the tank's position to the target,
        // and project it onto the XZ-plane (ignore Y).
        Vec3 toTarget = targetPosition.subtract(currentPosition);
        Vec3 desiredDirection = new Vec3(toTarget.getX(), 0, toTarget.getZ());

        desiredDirection = desiredDirection.normalize();
        double desiredYaw = desiredDirection.getAngle();

        // Compute the smallest signed angle difference.
        double deltaYaw = Math.atan2(Math.sin(desiredYaw - currentYaw), Math.cos(desiredYaw - currentYaw));

        double angleToRotate = Math.abs(deltaYaw);
        RotationDirection direction = (deltaYaw >= 0)
                ? RotationDirection.CLOCKWISE
                : RotationDirection.COUNTER_CLOCKWISE;

        // Delegate to the existing rotateBody method (which caps the rotation speed).
        rotateBody(world, direction, angleToRotate);
    }

    default void rotateTurret(PublicGameWorld world, RotationDirection direction) {
        TankConfig config = getConfig(world);
        rotateTurret(world, direction, config.turretRotationSpeed());
    }

    default void rotateTurret(PublicGameWorld world, RotationDirection direction, double angle) {
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

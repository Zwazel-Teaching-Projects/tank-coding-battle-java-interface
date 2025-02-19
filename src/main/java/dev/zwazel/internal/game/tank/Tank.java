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

    /**
     * Move the tank in the specified direction with the specified distance.
     *
     * @param world     The game world.
     * @param direction The direction to move the tank in (forward or backward).
     * @param distance  The distance to move the tank by. If the distance is greater than the tank's move speed,
     *                  the tank will only move by the move speed.
     */
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

    /**
     * Move the tank towards the target position in the specified move direction.
     * If moving forward, the tank's forward vector will be aligned towards the target.
     * If moving backwards, the tank will be rotated such that its rear faces the target.
     * The boolean parameter 'simultaneous' controls whether the tank rotates and moves at the same time.
     * When false, the tank will only move if its alignment to the target is within a set threshold.
     *
     * @param world          The game world.
     * @param moveDirection  The direction to move (FORWARD or BACKWARDS).
     * @param targetPosition The target position to approach.
     * @param simultaneous   If true, rotate and move concurrently; if false, only move when aligned.
     */
    default void moveTowards(PublicGameWorld world, MoveDirection moveDirection, Vec3 targetPosition, boolean simultaneous) {
        // Fetch the tank's current state and position.
        Transform currentTransform = world.getMyState().transform();
        Vec3 currentPosition = currentTransform.getPosition();
        Quaternion currentRotation = currentTransform.getRotation();
        Vec3 currentForward = currentRotation.getForward();
        double currentYaw = currentForward.getAngle();

        // Compute the desired yaw based on move direction.
        Vec3 toTarget = targetPosition.subtract(currentPosition);
        Vec3 desiredDirection = new Vec3(toTarget.getX(), 0, toTarget.getZ()).normalize_or_zero();
        double desiredYaw;
        if (moveDirection == MoveDirection.FORWARD) {
            desiredYaw = desiredDirection.getAngle();
        } else if (moveDirection == MoveDirection.BACKWARD) {
            // For backwards, the tank's rear must face the target.
            desiredYaw = normalizeAngle(desiredDirection.getAngle() + Math.PI);
        } else {
            throw new IllegalArgumentException("Invalid move direction: " + moveDirection);
        }

        // Calculate the smallest signed angle difference.
        double deltaYaw = Math.atan2(Math.sin(desiredYaw - currentYaw), Math.cos(desiredYaw - currentYaw));
        double angleToRotate = Math.abs(deltaYaw);
        RotationDirection rotationDirection = (deltaYaw >= 0) ? RotationDirection.CLOCKWISE : RotationDirection.COUNTER_CLOCKWISE;

        // Calculate the distance to the target.
        float distance = (float) currentPosition.distance(targetPosition);

        if (simultaneous) {
            // Unleash chaos: rotate and move at the same time.
            rotateBody(world, rotationDirection, angleToRotate);
            move(world, moveDirection, distance);
        } else {
            // Only advance when the tank's murderous focus is nearly aligned.
            final double ALIGNMENT_THRESHOLD = 0.1; // roughly 5.7 degrees of tolerance
            if (angleToRotate <= ALIGNMENT_THRESHOLD) {
                move(world, moveDirection, distance);
            } else {
                rotateBody(world, rotationDirection, angleToRotate);
            }
        }
    }

    /**
     * Normalize an angle to be within [0, 2*PI).
     *
     * @param angle the angle in radians.
     * @return the normalized angle.
     */
    default double normalizeAngle(double angle) {
        double twoPi = 2 * Math.PI;
        angle = angle % twoPi;
        if (angle < 0) {
            angle += twoPi;
        }
        return angle;
    }

    /**
     * Rotate the tank body in the specified direction using the tank's body rotation speed.
     *
     * @param world     The game world.
     * @param direction The direction to rotate the tank body in (clockwise or counter-clockwise).
     * @return The predicted angle of rotation in radians. Could be different from the actual angle rotated,
     * as the rotation might not have been fully completed because of possible collisions.
     */
    default double rotateBody(PublicGameWorld world, RotationDirection direction) {
        TankConfig config = getConfig(world);
        return rotateBody(world, direction, config.bodyRotationSpeed());
    }

    /**
     * @param world     The game world.
     * @param direction The direction to rotate the tank body in (clockwise or counter-clockwise).
     * @param angle     The angle to rotate in radians. (might get capped by the tank's rotation speed)
     * @return The predicted angle of rotation in radians. Could be different from the actual angle rotated,
     * as the rotation might not have been fully completed because of possible collisions.
     */
    default double rotateBody(PublicGameWorld world, RotationDirection direction, double angle) {
        TankConfig config = getConfig(world);
        // If angle > config.rotationSpeed, rotate only config.rotationSpeed
        angle = Math.min(angle, config.bodyRotationSpeed());

        MessageContainer message = new MessageContainer(
                MessageTarget.Type.TO_SELF.get(),
                RotateTankBodyCommand.builder().direction(direction).angle(angle).build()
        );

        world.send(message);

        return angle;
    }

    /**
     * @param world          The game world.
     * @param targetPosition The position to rotate the tank body towards.
     * @return The predicted angle of rotation in radians. Could be different from the actual angle rotated,
     * as the rotation might not have been fully completed because of possible collisions.
     */
    default double rotateBodyTowards(PublicGameWorld world, Vec3 targetPosition) {
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

        desiredDirection = desiredDirection.normalize_or_zero();
        double desiredYaw = desiredDirection.getAngle();

        // Compute the smallest signed angle difference.
        double deltaYaw = Math.atan2(Math.sin(desiredYaw - currentYaw), Math.cos(desiredYaw - currentYaw));

        double angleToRotate = Math.abs(deltaYaw);
        RotationDirection direction = (deltaYaw >= 0)
                ? RotationDirection.CLOCKWISE
                : RotationDirection.COUNTER_CLOCKWISE;

        // Delegate to the existing rotateBody method (which caps the rotation speed).
        return rotateBody(world, direction, angleToRotate);
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

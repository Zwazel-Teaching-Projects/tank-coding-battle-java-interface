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
    default void move(PublicGameWorld world, MoveDirection direction, double distance) {
        TankConfig config = getConfig(world);
        // If distance > config.moveSpeed, move only config.moveSpeed
        distance = Math.min(distance, config.moveSpeed());
        distance *= direction.multiplier;

        MessageContainer message = new MessageContainer(
                MessageTarget.Type.TO_SELF.get(),
                MoveTankCommand.builder().distance(distance).build()
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
        Transform currentTransform = world.getMyPredictedState().transformBody();
        Vec3 currentPosition = currentTransform.getTranslation();
        Vec3 currentForward = currentTransform.forward();
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

        // Calculate the distance to the target.
        double distance = currentPosition.distance(targetPosition);

        if (simultaneous) {
            // Unleash chaos: rotate and move at the same time.
            rotateBody(world, deltaYaw);
            move(world, moveDirection, distance);
        } else {
            // Only advance when the tank's murderous focus is nearly aligned.
            final double ALIGNMENT_THRESHOLD = 0.1; // roughly 5.7 degrees of tolerance
            if (Math.abs(deltaYaw) < ALIGNMENT_THRESHOLD) {
                move(world, moveDirection, distance);
            } else {
                rotateBody(world, deltaYaw);
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
     * @param world The game world.
     * @param angle The angle to rotate in radians. (might get capped by the tank's rotation speed)
     * @return The predicted angle of rotation in radians. Could be different from the actual angle rotated,
     * as the rotation might not have been fully completed because of possible collisions.
     */
    default double rotateBody(PublicGameWorld world, double angle) {
        TankConfig config = getConfig(world);
        // If angle > config.rotationSpeed, rotate only config.rotationSpeed
        angle = Math.max(-config.bodyRotationSpeed(), Math.min(angle, config.bodyRotationSpeed()));

        MessageContainer message = new MessageContainer(
                MessageTarget.Type.TO_SELF.get(),
                RotateTankBodyCommand.builder().angle(angle).build()
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
        Transform currentTransform = world.getMyPredictedState().transformBody();
        Vec3 currentPosition = currentTransform.getTranslation();
        Quaternion currentRotation = currentTransform.getRotation();

        // Get the current forward vector in game space.
        Vec3 currentForward = currentTransform.forward();
        double currentYaw = currentForward.getAngle();

        // Compute the vector from the tank's position to the target,
        // and project it onto the XZ-plane (ignore Y).
        Vec3 toTarget = targetPosition.subtract(currentPosition);
        Vec3 desiredDirection = new Vec3(toTarget.getX(), 0, toTarget.getZ());

        desiredDirection = desiredDirection.normalize_or_zero();
        double desiredYaw = desiredDirection.getAngle();

        // Compute the smallest signed angle difference.
        double deltaYaw = Math.atan2(Math.sin(desiredYaw - currentYaw), Math.cos(desiredYaw - currentYaw));

        // Delegate to the existing rotateBody method (which caps the rotation speed).
        return rotateBody(world, deltaYaw);
    }

    default void rotateTurretYaw(PublicGameWorld world, double yawAngle) {
        rotateTurret(world, yawAngle, 0);
    }

    default void rotateTurretPitch(PublicGameWorld world, double pitchAngle) {
        rotateTurret(world, 0, pitchAngle);
    }

    default void rotateTurret(PublicGameWorld world, double yawAngle, double pitchAngle) {
        TankConfig config = getConfig(world);
        // If yawAngle > config.turretYawRotationSpeed, rotate only config.turretYawRotationSpeed
        yawAngle = Math.max(-config.turretYawRotationSpeed(),
                Math.min(yawAngle, config.turretYawRotationSpeed()));
        // If pitchAngle > config.turretPitchSpeed, rotate only config.turretPitchSpeed
        pitchAngle = Math.max(-config.turretPitchRotationSpeed(),
                Math.min(pitchAngle, config.turretPitchRotationSpeed()));

        MessageContainer message = new MessageContainer(
                MessageTarget.Type.TO_SELF.get(),
                RotateTankTurretCommand.builder()
                        .yawAngle(yawAngle)
                        .pitchAngle(pitchAngle)
                        .build()
        );
        world.send(message);
    }

    default void rotateTurretTowards(PublicGameWorld world, Vec3 targetPosition) {
        Transform bodyTransform = world.getMyPredictedState().transformBody();
        Transform turretLocalTransform = world.getMyPredictedState().transformTurret();

        Transform calculatedPredictedTurretGlobalTransform = Transform.combineTransforms(bodyTransform, turretLocalTransform);
        Transform actualPredictedTurretGlobalTransform = world.getMyPredictedState().globalTransformTurret();
        Transform actualTurretGlobalTransform = world.getMyState().globalTransformTurret();

        System.out.println(
                "Calculated PREDICTED turret global transform:\n\t" + calculatedPredictedTurretGlobalTransform
                        + "\nActual PREDICTED turret global transform:\n\t" + actualPredictedTurretGlobalTransform
                        + "\nActual turret global transform:\n\t" + actualTurretGlobalTransform
                        + "\n---"
        );

        Vec3 turretGlobalPosition = calculatedPredictedTurretGlobalTransform.getTranslation();

        Vec3 currentForward = calculatedPredictedTurretGlobalTransform.forward();

        double currentYaw = Math.atan2(currentForward.getZ(), currentForward.getX());

        double horizontalMag = Math.sqrt(currentForward.getX() * currentForward.getX()
                + currentForward.getZ() * currentForward.getZ());
        double currentPitch = Math.atan2(currentForward.getY(), horizontalMag);

        Vec3 toTarget = targetPosition.subtract(turretGlobalPosition);

        Vec3 desiredDirection = new Vec3(toTarget.getX(), 0, toTarget.getZ()).normalize_or_zero();
        double desiredYaw = Math.atan2(desiredDirection.getZ(), desiredDirection.getX());

        // 8. Determine desired pitch by considering vertical difference vs. horizontal distance
        double targetHorizontal = Math.sqrt(toTarget.getX() * toTarget.getX()
                + toTarget.getZ() * toTarget.getZ());
        double desiredPitch = Math.atan2(toTarget.getY(), targetHorizontal);

        double rawDeltaYaw = desiredYaw - currentYaw;
        double deltaYaw = Math.atan2(Math.sin(rawDeltaYaw), Math.cos(rawDeltaYaw));

        double pitchDelta = currentPitch - desiredPitch;

        rotateTurret(world, deltaYaw, pitchDelta);
    }

    enum YawRotationDirection {
        CLOCKWISE(-1.0),
        COUNTER_CLOCKWISE(1.0);

        final double multiplier;

        YawRotationDirection(double multiplier) {
            this.multiplier = multiplier;
        }
    }

    enum MoveDirection {
        FORWARD(1.0),
        BACKWARD(-1.0);

        final double multiplier;

        MoveDirection(double multiplier) {
            this.multiplier = multiplier;
        }
    }
}

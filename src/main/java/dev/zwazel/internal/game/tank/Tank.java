package dev.zwazel.internal.game.tank;

import dev.zwazel.internal.PublicGameWorld;
import dev.zwazel.internal.game.transform.Transform;
import dev.zwazel.internal.game.transform.Vec3;
import dev.zwazel.internal.message.MessageContainer;
import dev.zwazel.internal.message.MessageTarget;
import dev.zwazel.internal.message.data.GameConfig;
import dev.zwazel.internal.message.data.tank.MoveTankCommand;
import dev.zwazel.internal.message.data.tank.RotateTankBodyCommand;
import dev.zwazel.internal.message.data.tank.RotateTankTurretCommand;
import dev.zwazel.internal.message.data.tank.ShootCommand;

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
        // Obtain current tank transform
        Transform currentTransform = world.getMyPredictedState().transformBody();
        Vec3 currentPos = currentTransform.getTranslation();
        Vec3 currentForward = currentTransform.forward();

        // Direction to the target, ignoring vertical component
        Vec3 toTarget = targetPosition.subtract(currentPos);
        Vec3 horizontalToTarget = new Vec3(toTarget.getX(), 0, toTarget.getZ()).normalize_or_zero();

        // Calculate the signed angle difference between currentForward and horizontalToTarget
        double currentYaw = currentForward.getAngle();
        double desiredYaw = horizontalToTarget.getAngle();
        double deltaYaw = Math.atan2(Math.sin(desiredYaw - currentYaw), Math.cos(desiredYaw - currentYaw));

        // If we move simultaneously, rotate and move in this call
        if (simultaneous) {
            rotateBody(world, deltaYaw);
            move(world, moveDirection);
        } else {
            // Only move if we're facing mostly toward the target
            double angleThreshold = 0.1; // adjust as needed
            if (Math.abs(deltaYaw) > angleThreshold) {
                rotateBody(world, deltaYaw);
            } else {
                move(world, moveDirection);
            }
        }
    }


    default void moveTowards(PublicGameWorld world, Vec3 targetPosition, boolean simultaneous) {
        Transform currentTransform = world.getMyPredictedState().transformBody();
        Vec3 currentForward = currentTransform.forward();

        double currentYaw = currentForward.getAngle();

        // Calculate desired yaw for forward
        Vec3 toTarget = new Vec3(targetPosition.getX() - currentTransform.getTranslation().getX(), 0,
                targetPosition.getZ() - currentTransform.getTranslation().getZ()).normalize_or_zero();
        double desiredYawForward = toTarget.getAngle();
        double deltaYawForward = Math.atan2(Math.sin(desiredYawForward - currentYaw),
                Math.cos(desiredYawForward - currentYaw));

        // Calculate desired yaw for backward
        double desiredYawBackward = desiredYawForward + Math.PI;
        double deltaYawBackward = Math.atan2(Math.sin(desiredYawBackward - currentYaw),
                Math.cos(desiredYawBackward - currentYaw));

        // Determine which direction requires less rotation
        if (Math.abs(deltaYawForward) <= Math.abs(deltaYawBackward)) {
            moveTowards(world, MoveDirection.FORWARD, targetPosition, simultaneous);
        } else {
            moveTowards(world, MoveDirection.BACKWARD, targetPosition, simultaneous);
        }
    }

    /**
     * @param world The game world.
     * @param angle The angle to rotate in radians. (might get capped by the tank's rotation speed)
     */
    default void rotateBody(PublicGameWorld world, double angle) {
        TankConfig config = getConfig(world);
        angle = Math.clamp(angle, -config.bodyRotationSpeed(), config.bodyRotationSpeed());

        MessageContainer message = new MessageContainer(
                MessageTarget.Type.TO_SELF.get(),
                RotateTankBodyCommand.builder().angle(angle).build()
        );

        world.send(message);
    }

    /**
     * @param world          The game world.
     * @param targetPosition The position to rotate the tank body towards.
     */
    default void rotateBodyTowards(PublicGameWorld world, Vec3 targetPosition) {
        Transform currentTransform = world.getMyPredictedState().transformBody();
        Vec3 currentPos = currentTransform.getTranslation();
        Vec3 currentForward = currentTransform.forward();

        // Project the direction to the target onto the horizontal plane
        Vec3 toTarget = targetPosition.subtract(currentPos);
        Vec3 horizontalToTarget = new Vec3(toTarget.getX(), 0, toTarget.getZ()).normalize_or_zero();

        // Current yaw and desired yaw
        double currentYaw = currentForward.getAngle();
        double desiredYaw = horizontalToTarget.getAngle();
        // Smallest signed angle difference
        double deltaYaw = Math.atan2(Math.sin(desiredYaw - currentYaw), Math.cos(desiredYaw - currentYaw));

        // Rotate by deltaYaw
        rotateBody(world, deltaYaw);
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

    default void rotateTurretTowards(PublicGameWorld world, Vec3 globalTarget) {
        // Obtain body and turret transforms
        Transform bodyTransform = world.getMyPredictedState().transformBody();
        Transform turretLocalTransform = world.getMyPredictedState().transformTurret();

        // Combine transforms to get turret's world transform
        Transform turretWorldTransform = Transform.combineTransforms(bodyTransform, turretLocalTransform);

        // Compute turret's global position
        Vec3 turretGlobalPos = turretWorldTransform.getTranslation();

        // Convert global target into turret-local space using the turret's world rotation
        // By applying the conjugate to the difference vector
        Vec3 localTarget = turretWorldTransform.getRotation().conjugate()
                .apply(globalTarget.subtract(turretGlobalPos));

        // In turret-local space the default forward is (0, 0, 1)
        // Compute desired yaw and pitch relative to this frame.
        double desiredYaw = localTarget.getAngle(); // atan2(x, z)
        double horizontalDist = Math.sqrt(localTarget.getX() * localTarget.getX() + localTarget.getZ() * localTarget.getZ());
        double desiredPitch = Math.atan2(localTarget.getY(), horizontalDist);

        // Since the turret's local forward is (0,0,1) (yaw==0, pitch==0), the angles to rotate are:
        double pitchDelta = -desiredPitch;

        rotateTurret(world, desiredYaw, pitchDelta);
    }

    default void shoot(PublicGameWorld world) {
        // TODO: Check for cooldown. maybe return a boolean if the shot was successful?
        world.send(new MessageContainer(MessageTarget.Type.TO_SELF.get(), new ShootCommand()));
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

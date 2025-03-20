package dev.zwazel.internal.message.data.tank;

import dev.zwazel.internal.InternalGameWorld;
import dev.zwazel.internal.game.state.ClientState;
import dev.zwazel.internal.game.transform.Quaternion;
import dev.zwazel.internal.game.transform.Transform;
import dev.zwazel.internal.message.MessageData;
import lombok.Builder;

@Builder
public record RotateTankTurretCommand(double yawAngle, double pitchAngle) implements MessageData {
    @Override
    public void applyOnAddingToQueue(InternalGameWorld world) {
        // Apply predicted rotation to the predicted state
        ClientState predictedState = world.getPublicGameWorld().getMyPredictedState();
        Transform currentBodyTransform = predictedState.transformBody();
        Transform currentTurretTransform = predictedState.transformTurret();

        // Calculate the new rotation
        Quaternion yawRotation = Quaternion.fromAxisAngle(0, 1, 0, yawAngle);
        Quaternion pitchRotation = Quaternion.fromAxisAngle(1, 0, 0, pitchAngle);
        Quaternion newRotation = currentTurretTransform.getRotation().multiply(yawRotation).multiply(pitchRotation);

        // Update the local transform of the turret
        Transform newTurretTransform = new Transform(currentTurretTransform.getTranslation(), newRotation);

        // Update the predicted state
        predictedState = new ClientState(predictedState.id(), currentBodyTransform, newTurretTransform, predictedState.state(), predictedState.shootCooldown(), predictedState.currentHealth());
        world.updatePredictedState(predictedState);
    }

    @Override
    public boolean isUnique() {
        return true;
    }
}
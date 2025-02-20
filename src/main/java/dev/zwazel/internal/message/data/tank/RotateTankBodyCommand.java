package dev.zwazel.internal.message.data.tank;

import dev.zwazel.internal.InternalGameWorld;
import dev.zwazel.internal.game.state.ClientState;
import dev.zwazel.internal.game.transform.Quaternion;
import dev.zwazel.internal.game.transform.Transform;
import dev.zwazel.internal.message.MessageData;
import lombok.Builder;

@Builder
public record RotateTankBodyCommand(double angle) implements MessageData {
    @Override
    public void applyOnAddingToQueue(InternalGameWorld world) {
        // Apply predicted rotation to the predicted state
        ClientState predictedState = world.getPublicGameWorld().getMyPredictedState();
        Transform currentTransform = predictedState.transformBody();

        // Calculate the new rotation
        Quaternion newRotation = currentTransform.getRotation().multiply(Quaternion.fromAxisAngle(0, 1, 0, angle));

        // Update the predicted state
        predictedState = new ClientState(predictedState.id(), new Transform(currentTransform.getTranslation(), newRotation),
                predictedState.transformTurret(), predictedState.globalTransformTurret());
        world.updatePredictedState(predictedState);
    }
}
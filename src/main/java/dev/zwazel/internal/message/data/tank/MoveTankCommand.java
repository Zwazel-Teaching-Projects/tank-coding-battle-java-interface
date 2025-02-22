package dev.zwazel.internal.message.data.tank;

import dev.zwazel.internal.InternalGameWorld;
import dev.zwazel.internal.game.state.ClientState;
import dev.zwazel.internal.game.transform.Transform;
import dev.zwazel.internal.game.transform.Vec3;
import dev.zwazel.internal.message.MessageData;
import lombok.Builder;

@Builder
public record MoveTankCommand(double distance) implements MessageData {
    @Override
    public void applyOnAddingToQueue(InternalGameWorld world) {
        // Apply predicted movement to the predicted state
        ClientState predictedState = world.getPublicGameWorld().getMyPredictedState();
        Transform currentTransform = predictedState.transformBody();
        Vec3 myForward = currentTransform.forward();

        // Calculate the new position
        Vec3 newPosition = currentTransform.getTranslation().add(myForward.multiply(distance));

        // Update the predicted state
        predictedState = new ClientState(predictedState.id(), new Transform(newPosition, currentTransform.getRotation()),
                predictedState.transformTurret(), predictedState.state(), predictedState.shootCooldown());
        world.updatePredictedState(predictedState);
    }
}

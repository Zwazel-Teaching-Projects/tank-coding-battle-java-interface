package dev.zwazel.internal.message;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import dev.zwazel.internal.InternalGameWorld;
import dev.zwazel.internal.message.data.*;
import dev.zwazel.internal.message.data.tank.MoveTankCommand;
import dev.zwazel.internal.message.data.tank.RotateTankBodyCommand;
import dev.zwazel.internal.message.data.tank.RotateTankTurretCommand;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "message_type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = GameState.class),
        @JsonSubTypes.Type(value = FirstContact.class),
        @JsonSubTypes.Type(value = SimpleTextMessage.class),
        @JsonSubTypes.Type(value = MessageError.class),
        @JsonSubTypes.Type(value = GameConfig.class),
        @JsonSubTypes.Type(value = SuccessfullyJoinedLobby.class),
        @JsonSubTypes.Type(value = StartGameConfig.class),
        @JsonSubTypes.Type(value = MoveTankCommand.class),
        @JsonSubTypes.Type(value = RotateTankBodyCommand.class),
        @JsonSubTypes.Type(value = RotateTankTurretCommand.class),
})
public interface MessageData {
    /**
     * Applies to the world when received
     *
     * @param world the world
     * @return if true, we add it to the Input Queue, so Bots can read them. If false, we don't.
     */
    default boolean applyOnReceive(InternalGameWorld world) {
        return true;
    }

    /**
     * Applies to the world before sending
     *
     * @param world the world
     */
    default void applyBeforeSend(InternalGameWorld world) {
    }

    /**
     * Applies to the world when added to the queue
     * @param world
     */
    default void applyOnAddingToQueue(InternalGameWorld world) {
    }
}

package dev.zwazel.internal.message;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import dev.zwazel.internal.InternalGameWorld;
import dev.zwazel.internal.message.data.*;
import dev.zwazel.internal.message.data.flag.FlagGotDropped;
import dev.zwazel.internal.message.data.flag.FlagGotPickedUp;
import dev.zwazel.internal.message.data.flag.FlagReturnedInBase;
import dev.zwazel.internal.message.data.tank.*;

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
        @JsonSubTypes.Type(value = ShootCommand.class),
        @JsonSubTypes.Type(value = GotHit.class),
        @JsonSubTypes.Type(value = Hit.class),
        @JsonSubTypes.Type(value = PlayerRespawned.class),
        @JsonSubTypes.Type(value = PlayerDied.class),
        @JsonSubTypes.Type(value = FlagGotDropped.class),
        @JsonSubTypes.Type(value = FlagGotPickedUp.class),
        @JsonSubTypes.Type(value = FlagReturnedInBase.class),
        @JsonSubTypes.Type(value = TeamScored.class),
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
     *
     * @param world the world
     */
    default void applyOnAddingToQueue(InternalGameWorld world) {
    }

    /**
     * Determines if the message is unique.
     * If it is unique, before sending all messages to the server at the end of the tick, we remove all other messages of the same type.
     * So only the latest message of this type is sent.
     *
     * @return if the message is unique
     */
    @JsonIgnore
    default boolean isUnique() {
        return false;
    }
}

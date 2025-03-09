package dev.zwazel.internal.message;

import dev.zwazel.internal.InternalGameWorld;
import dev.zwazel.internal.message.data.GameState;
import lombok.*;

/**
 * A container for a message.
 * Contains the target, the message, and the tick it was sent and received.
 * <p>
 * Can be used to send messages to the server, and represent messages received from the server.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public final class MessageContainer {
    /**
     * The target of the message.
     */
    @Setter(AccessLevel.NONE)
    private MessageTarget target;

    /**
     * The Message itself.
     */
    @Setter(AccessLevel.NONE)
    private MessageData message;

    /**
     * If we are the sender of the message, this will be the tick we sent the message.
     * If we are the receiver of the message, this will be the tick on which the message was sent by the server.
     */
    @Setter(AccessLevel.NONE)
    private long tickSent;

    /**
     * If we are the receiver of the message, this will be the tick on which the message was received.
     * If we are the sender of the message, this won't be set.
     */
    @Setter(AccessLevel.NONE)
    private long tickReceived;

    /**
     * The sender of the message.
     * If we are the sender of the message, this will be null.
     * If we receive the message, this will be the ID of the sender.
     * if the sender is null when we receive the message, it means the message was sent by the server.
     */
    @Setter(AccessLevel.NONE)
    private Long sender;

    public MessageContainer(MessageTarget messageTarget, MessageData message) {
        this.target = messageTarget;
        this.message = message;
    }

    /**
     * Apply the message on receive.
     * This will apply the message to the world, and push it to the incoming message queue, if needed.
     * <p>
     * Messages that are not pushed to the incoming message queue can not be handled by the bot.
     * This is useful for messages that are only used for internal state changes.
     *
     * @param internalWorld the world
     */
    public void applyOnReceive(InternalGameWorld internalWorld) {
        GameState state = internalWorld.getPublicGameWorld().getGameState();
        if (state != null) {
            this.tickReceived = state.tick();
        }

        if (internalWorld.isInternalDebug()) {
            System.out.println("Received message (Serialized):\n\t " + this);
        }

        if (message.applyOnReceive(internalWorld)) {
            internalWorld.pushIncomingMessage(this);
        }
    }

    /**
     * Apply the message before sending it.
     * This will apply the message to the world, and push it to the outgoing message queue.
     *
     * @param internalWorld the world
     */
    public void applyBeforeSend(InternalGameWorld internalWorld) {
        GameState state = internalWorld.getPublicGameWorld().getGameState();
        if (state != null) {
            this.tickSent = state.tick();
        }

        message.applyBeforeSend(internalWorld);
    }

    /**
     * Apply the message when added to the queue.
     *
     * @param internalWorld the world
     */
    public void applyOnAddingToQueue(InternalGameWorld internalWorld) {
        message.applyOnAddingToQueue(internalWorld);
    }
}

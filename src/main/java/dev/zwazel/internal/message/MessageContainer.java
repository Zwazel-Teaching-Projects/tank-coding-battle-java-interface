package dev.zwazel.internal.message;

import dev.zwazel.internal.InternalGameWorld;
import dev.zwazel.internal.message.data.GameState;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public final class MessageContainer {
    @Setter(AccessLevel.NONE)
    private MessageTarget target;
    @Setter(AccessLevel.NONE)
    private MessageData message;
    @Setter(AccessLevel.NONE)
    private long tickSent;
    @Setter(AccessLevel.NONE)
    private long tickReceived;
    @Setter(AccessLevel.NONE)
    private Long sender;

    public MessageContainer(MessageTarget messageTarget, MessageData message) {
        this.target = messageTarget;
        this.message = message;
    }

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

    public void applyBeforeSend(InternalGameWorld internalWorld) {
        GameState state = internalWorld.getPublicGameWorld().getGameState();
        if (state != null) {
            this.tickSent = state.tick();
        }
    }
}

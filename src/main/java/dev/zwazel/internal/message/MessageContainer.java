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
    @Setter(AccessLevel.PRIVATE)
    private long tickSent;
    @Setter(AccessLevel.PRIVATE)
    private long tickReceived;

    public MessageContainer(MessageTarget messageTarget, MessageData message) {
        this.target = messageTarget;
        this.message = message;
    }

    public void applyOnReceive(InternalGameWorld internalWorld) {
        GameState state = internalWorld.getPublicGameWorld().getGameState();
        if (state != null) {
            this.setTickReceived(state.tick());
        }

        if (internalWorld.isInternalDebug()) {
            System.out.println("Received message (Serialized):\n\t " + this);
        }
        message.applyOnReceive(internalWorld);
    }

    public void applyBeforeSend(InternalGameWorld internalWorld) {
        GameState state = internalWorld.getPublicGameWorld().getGameState();
        if (state != null) {
            this.setTickSent(state.tick());
        }
    }
}

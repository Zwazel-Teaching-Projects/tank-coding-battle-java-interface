package dev.zwazel.internal.messages;

import dev.zwazel.internal.InternalGameWorld;
import dev.zwazel.internal.messages.data.GameState;
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

        message.applyOnReceive(internalWorld);
    }
}

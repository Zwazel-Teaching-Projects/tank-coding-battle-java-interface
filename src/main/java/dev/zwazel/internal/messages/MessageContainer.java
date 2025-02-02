package dev.zwazel.internal.messages;

import dev.zwazel.internal.messages.data.FirstContact;

public record MessageContainer(MessageTarget target, MessageData message, Long tick_sent, Long tick_received) {
    public MessageContainer(MessageTarget messageTarget, FirstContact build) {
        this(messageTarget, build, 0L, 0L);
    }
}

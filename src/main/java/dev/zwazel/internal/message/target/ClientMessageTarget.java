package dev.zwazel.internal.message.target;

import dev.zwazel.internal.message.MessageTarget;

public record ClientMessageTarget(long clientId) implements MessageTarget {
}

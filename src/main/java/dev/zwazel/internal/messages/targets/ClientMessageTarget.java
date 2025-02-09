package dev.zwazel.internal.messages.targets;

import dev.zwazel.internal.messages.MessageTarget;

public record ClientMessageTarget(long clientId) implements MessageTarget {
}

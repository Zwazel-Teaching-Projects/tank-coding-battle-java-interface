package dev.zwazel.internal;

import dev.zwazel.internal.connection.ConnectionManager;
import dev.zwazel.internal.messages.MessageContainer;

import java.util.Optional;

public interface InternalGameWorld {
    Optional<MessageContainer> pollOutgoingMessage();

    void pushIncomingMessage(MessageContainer message);

    PublicGameWorld getPublicGameWorld();

    ConnectionManager getConnectionManager();
}

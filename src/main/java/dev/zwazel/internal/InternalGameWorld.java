package dev.zwazel.internal;

import dev.zwazel.bot.BotInterface;
import dev.zwazel.internal.connection.ConnectionManager;
import dev.zwazel.internal.messages.MessageContainer;
import dev.zwazel.internal.messages.data.GameState;
import dev.zwazel.internal.messages.data.ServerConfig;

import java.util.Optional;

public interface InternalGameWorld {
    void stop();

    boolean isInternalDebug();

    Long getMyClientId();

    void setServerConfig(ServerConfig serverConfig);

    PublicGameWorld getPublicGameWorld();

    ConnectionManager getConnectionManager();

    BotInterface getBot();

    Optional<MessageContainer> pollOutgoingMessage();

    void pushIncomingMessage(MessageContainer message);

    void updateState(GameState newState);
}

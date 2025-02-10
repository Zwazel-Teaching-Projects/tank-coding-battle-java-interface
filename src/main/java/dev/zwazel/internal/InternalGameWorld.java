package dev.zwazel.internal;

import dev.zwazel.bot.BotInterface;
import dev.zwazel.internal.connection.ConnectionManager;
import dev.zwazel.internal.message.MessageContainer;
import dev.zwazel.internal.message.data.GameState;
import dev.zwazel.internal.message.data.GameConfig;

import java.util.Optional;
import java.util.concurrent.BlockingQueue;

public interface InternalGameWorld {
    void stop();

    boolean isInternalDebug();

    Long getMyClientId();

    void setGameConfig(GameConfig serverConfig);

    PublicGameWorld getPublicGameWorld();

    ConnectionManager getConnectionManager();

    BotInterface getBot();

    Optional<MessageContainer> pollOutgoingMessage();

    void pushIncomingMessage(MessageContainer message);

    void updateState(GameState newState);

    BlockingQueue<MessageContainer> getIncomingMessageQueue();
}

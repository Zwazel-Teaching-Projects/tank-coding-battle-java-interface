package dev.zwazel.internal;

import dev.zwazel.bot.BotInterface;
import dev.zwazel.internal.connection.ConnectionManager;
import dev.zwazel.internal.message.MessageContainer;
import dev.zwazel.internal.message.data.GameConfig;
import dev.zwazel.internal.message.data.GameState;

import java.util.concurrent.BlockingQueue;

public interface InternalGameWorld {
    void stop();

    boolean isInternalDebug();

    Long getMyClientId();

    void setGameConfig(GameConfig serverConfig);

    PublicGameWorld getPublicGameWorld();

    ConnectionManager getConnectionManager();

    BotInterface getBot();

    void pushIncomingMessage(MessageContainer message);

    void updateState(GameState newState);

    BlockingQueue<MessageContainer> getIncomingMessageQueue();

    BlockingQueue<MessageContainer> getOutgoingMessageQueue();
}

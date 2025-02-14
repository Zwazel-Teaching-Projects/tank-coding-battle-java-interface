package dev.zwazel;

import dev.zwazel.bot.BotInterface;
import dev.zwazel.internal.GameSimulationThread;
import dev.zwazel.internal.InternalGameWorld;
import dev.zwazel.internal.PublicGameWorld;
import dev.zwazel.internal.connection.ConnectionManager;
import dev.zwazel.internal.connection.client.ConnectedClientConfig;
import dev.zwazel.internal.game.tank.Tank;
import dev.zwazel.internal.message.MessageContainer;
import dev.zwazel.internal.message.data.GameConfig;
import dev.zwazel.internal.message.data.GameState;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class GameWorld implements InternalGameWorld, PublicGameWorld {
    private static GameWorld instance;

    private final PropertyHandler properties = PropertyHandler.getInstance();
    private final BlockingQueue<MessageContainer> incomingMessages = new LinkedBlockingQueue<>();
    private final BlockingQueue<MessageContainer> outgoingMessages = new LinkedBlockingQueue<>();
    private ConnectionManager connection;
    private GameState gameState;
    private BotInterface bot;
    private DebugMode debug = DebugMode.NONE;
    private GameConfig gameConfig;

    private volatile boolean running = false;

    private GameWorld() {
        // Private constructor to prevent instantiation
    }

    public static void startGame(Tank bot) {
        GameWorld gameWorld = GameWorld.getInstance();
        gameWorld.bot = bot;
        gameWorld.debug = DebugMode.valueOf(gameWorld.properties.getProperty("debug.mode").toUpperCase());
        gameWorld.start();
    }

    private static GameWorld getInstance() {
        if (instance == null) {
            instance = new GameWorld();
            instance.connection = ConnectionManager.getInstance(instance);
        }
        return instance;
    }


    private void start() {
        if (running) {
            System.err.println("Game world is already running!");
            return;
        }

        System.out.println("Starting game world...");

        String serverIp = properties.getProperty("server.ip");
        int serverPort = Integer.parseInt(properties.getProperty("server.port"));

        running = true;
        if (!connection.connect(serverIp, serverPort)) {
            System.err.println("Failed to connect to " + serverIp + ":" + serverPort);
            running = false;
        } else {
            Thread simulationThread = new Thread(new GameSimulationThread(instance), "Game-Simulation");
            simulationThread.start();
        }
    }

    @Override
    public Optional<MessageContainer> pollOutgoingMessage() {
        return Optional.ofNullable(outgoingMessages.poll());
    }

    @Override
    public void pushIncomingMessage(MessageContainer message) {
        incomingMessages.add(message);
    }

    @Override
    public void updateState(GameState newState) {
        gameState = newState;
    }

    @Override
    public BlockingQueue<MessageContainer> getIncomingMessageQueue() {
        return incomingMessages;
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    @Override
    public boolean isDebug() {
        return debug == DebugMode.PUBLIC || debug == DebugMode.INTERNAL;
    }

    @Override
    public void send(MessageContainer message) {
        /*
         TODO:
          - we need to validate the messages and check for duplicates and other stuff
          - Rather use a set?
        */
        outgoingMessages.add(message);
    }

    @Override
    public GameState getGameState() {
        return gameState;
    }

    @Override
    public Optional<ConnectedClientConfig> getConnectedClient(String name) {
        return gameConfig != null ? gameConfig.getClientConfig(name) : Optional.empty();
    }

    @Override
    public ConnectedClientConfig[] getConnectedClients() {
        return gameConfig != null ? gameConfig.connectedClients() : new ConnectedClientConfig[0];
    }

    @Override
    public GameConfig getGameConfig() {
        return gameConfig;
    }

    @Override
    public void setGameConfig(GameConfig gameConfig) {
        this.gameConfig = gameConfig;
    }

    @Override
    public List<MessageContainer> getMessages() {
        return List.copyOf(incomingMessages);
    }

    @Override
    public void stop() {
        running = false;
        connection.disconnect();
    }

    @Override
    public boolean isInternalDebug() {
        return debug == DebugMode.INTERNAL;
    }

    @Override
    public Long getMyClientId() {
        return gameConfig != null ? gameConfig.clientId() : null;
    }

    @Override
    public PublicGameWorld getPublicGameWorld() {
        return instance;
    }

    @Override
    public ConnectionManager getConnectionManager() {
        return connection;
    }

    @Override
    public BotInterface getBot() {
        return bot;
    }

    private enum DebugMode {
        NONE,
        INTERNAL,
        PUBLIC,
    }
}

package dev.zwazel;

import dev.zwazel.bot.BotInterface;
import dev.zwazel.internal.GameSimulationThread;
import dev.zwazel.internal.InternalGameWorld;
import dev.zwazel.internal.PublicGameWorld;
import dev.zwazel.internal.connection.ConnectionManager;
import dev.zwazel.internal.game.state.ClientState;
import dev.zwazel.internal.game.tank.Tank;
import dev.zwazel.internal.game.tank.TankFactory;
import dev.zwazel.internal.message.MessageContainer;
import dev.zwazel.internal.message.data.GameConfig;
import dev.zwazel.internal.message.data.GameState;
import dev.zwazel.internal.message.data.StartGameConfig;

import javax.swing.*;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static dev.zwazel.internal.message.MessageTarget.Type.TO_LOBBY_DIRECTLY;

public class GameWorld implements InternalGameWorld, PublicGameWorld {
    private static GameWorld instance;

    private final BlockingQueue<MessageContainer> incomingMessages = new LinkedBlockingQueue<>();
    private final BlockingQueue<MessageContainer> outgoingMessages = new LinkedBlockingQueue<>();
    private DebugMode debug = DebugMode.NONE;
    private boolean immediatelyStartGame;
    private ConnectionManager connection;
    private GameState gameState;
    private BotInterface bot;
    private Tank tank;
    private GameConfig gameConfig;

    /**
     * The predicted state of the client.
     * Resets every tick.
     * Every move command locally executed applies its predicted effect to this state.
     */
    private ClientState myPredictedState;
    private GameSimulationThread simulationThread;

    private volatile boolean running = false;

    private GameWorld() {
        // Private constructor to prevent instantiation
    }

    public static boolean startGame(BotInterface bot) {
        GameWorld gameWorld = GameWorld.getInstance();
        gameWorld.setup(bot, true);
        if (gameWorld.connect()) {
            return true;
        } else {
            System.err.println("Failed to start the game world due to connection issues.");
            return false;
        }
    }

    public static boolean connectToServer(BotInterface bot) {
        GameWorld gameWorld = GameWorld.getInstance();
        gameWorld.setup(bot, false);
        return gameWorld.connect();
    }

    private static GameWorld getInstance() {
        if (instance == null) {
            instance = new GameWorld();
            instance.connection = ConnectionManager.getInstance(instance);
        }
        return instance;
    }

    private void setup(BotInterface bot, boolean immediatelyStartGame) {
        this.bot = bot;
        this.debug = bot.getLocalBotConfig().debugMode();
        this.tank = TankFactory.createTank(bot.getLocalBotConfig().tankType());
        this.immediatelyStartGame = immediatelyStartGame;
    }

    private boolean connect() {
        if (connection.isConnected()) {
            return true;
        }

        String serverIp = bot.getLocalBotConfig().serverIp();
        int serverPort = bot.getLocalBotConfig().serverPort();

        if (!connection.connect(serverIp, serverPort)) {
            System.err.println("Failed to connect to " + serverIp + ":" + serverPort);
            return false;
        }

        this.running = true;
        try {
            this.simulationThread = new GameSimulationThread(instance, new DataOutputStream(this.connection.getSocket().getOutputStream()));
            new Thread(this.simulationThread, "Game-Simulation").start();
        } catch (IOException e) {
            this.running = false;
            e.printStackTrace();

            throw new RuntimeException(e);
        }

        return true;
    }

    @Override
    public void pushIncomingMessage(MessageContainer message) {
        incomingMessages.add(message);
    }

    @Override
    public void updateState(GameState newState) {
        gameState = newState;

        updatePredictedState(getMyState());
    }

    @Override
    public void updatePredictedState(ClientState newState) {
        myPredictedState = newState;
    }

    @Override
    public BlockingQueue<MessageContainer> getIncomingMessageQueue() {
        return incomingMessages;
    }

    @Override
    public BlockingQueue<MessageContainer> getOutgoingMessageQueue() {
        return outgoingMessages;
    }

    @Override
    public void startGame() {
        if (!immediatelyStartGame) {
            return;
        }

        if (!running) {
            System.err.println("Game world is not running!");
        }

        boolean fillEmptySlotsWithDummies = bot.getLocalBotConfig().lobbyConfig().fillEmptySlots();
        this.send(new MessageContainer(
                TO_LOBBY_DIRECTLY.get(),
                StartGameConfig.builder().fillEmptySlotsWithDummies(fillEmptySlotsWithDummies).build()
        ));
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
        message.applyOnAddingToQueue(this);
        outgoingMessages.add(message);
    }

    @Override
    public GameState getGameState() {
        return gameState;
    }

    @Override
    public ClientState getMyPredictedState() {
        return myPredictedState;
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
    public List<MessageContainer> getIncomingMessages() {
        return List.copyOf(incomingMessages);
    }

    @Override
    public void registerVisualiser(JPanel panel) {
        this.simulationThread.setMapVisualiser(panel);
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
    public Tank getTank() {
        return tank;
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

    public enum DebugMode {
        NONE,
        INTERNAL,
        PUBLIC,
    }
}

package dev.zwazel;

import dev.zwazel.internal.GameSimulationThread;
import dev.zwazel.internal.GameState;
import dev.zwazel.internal.InternalGameWorld;
import dev.zwazel.internal.PublicGameWorld;
import dev.zwazel.internal.connection.ConnectionManager;
import dev.zwazel.internal.messages.MessageContainer;

import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class GameWorld implements InternalGameWorld, PublicGameWorld {
    private static GameWorld instance;

    private final PropertyHandler properties = PropertyHandler.getInstance();
    private ConnectionManager connection;
    private final GameState gameState = GameState.getInstance();

    private final BlockingQueue<MessageContainer> incomingMessages = new LinkedBlockingQueue<>();
    private final BlockingQueue<MessageContainer> outgoingMessages = new LinkedBlockingQueue<>();

    private boolean running = false;

    private GameWorld() {
        // Private constructor to prevent instantiation
    }

    public static void main(String[] args) {
        GameWorld.getInstance().start();
    }

    private static GameWorld getInstance() {
        if (instance == null) {
            instance = new GameWorld();
            instance.connection = ConnectionManager.getInstance(instance);
        }
        return instance;
    }


    public void start() {
        if (running) {
            System.err.println("Game world is already running!");
            return;
        }

        System.out.println("Starting game world...");

        String serverIp = properties.getProperty("server.ip");
        int serverPort = Integer.parseInt(properties.getProperty("server.port"));

        if (!connection.connect(serverIp, serverPort)) {
            System.err.println("Failed to connect to " + serverIp + ":" + serverPort);
        } else {
            running = true;

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
    public boolean isRunning() {
        return running;
    }

    @Override
    public void send(MessageContainer message) {
        // TODO: we need to validate the messages and check for duplicates and other stuff
        outgoingMessages.add(message);
    }

    @Override
    public PublicGameWorld getPublicGameWorld() {
        return instance;
    }

    @Override
    public ConnectionManager getConnectionManager() {
        return connection;
    }
}

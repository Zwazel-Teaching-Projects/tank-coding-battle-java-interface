package dev.zwazel.internal.connection;

import dev.zwazel.PropertyHandler;
import dev.zwazel.internal.InternalGameWorld;
import dev.zwazel.internal.message.MessageContainer;
import dev.zwazel.internal.message.data.FirstContact;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

import static dev.zwazel.internal.message.MessageTarget.Type.SERVER_ONLY;

public class ConnectionManager {
    private static ConnectionManager instance;
    private final PropertyHandler properties = PropertyHandler.getInstance();

    private InternalGameWorld world;
    private Socket socket;

    private ConnectionManager() {
        // Private constructor to prevent instantiation
    }

    public static ConnectionManager getInstance(InternalGameWorld world) {
        if (world == null) {
            throw new IllegalStateException("World is not set");
        }

        if (instance == null) {
            instance = new ConnectionManager();
            instance.world = world;
        }
        return instance;
    }

    public boolean connect(String host, int port) throws IllegalStateException {
        if (world == null) {
            throw new IllegalStateException("World is not set");
        }

        try {
            socket = new Socket(host, port);
            System.out.println("Connected to " + host + ":" + port);

            Thread listenerThread = new Thread(new ListenerThread(this, world, new DataInputStream(socket.getInputStream())), "Socket-Listener");
            listenerThread.start();

            Thread writingThread = new Thread(new WritingThread(this, world, new DataOutputStream(socket.getOutputStream())), "Socket-Writer");
            writingThread.start();

            // Sending first contact message
            MessageContainer message = new MessageContainer(
                    SERVER_ONLY.get(),
                    FirstContact.builder()
                            .lobbyName(properties.getProperty("lobby.name"))
                            .botName(properties.getProperty("bot.name"))
                            .mapName(properties.getProperty("lobby.map.name"))
                            .teamName(properties.getProperty("lobby.team.name"))
                            .botAssignedSpawnPoint(properties.getProperty("lobby.spawnPoint") != null ?
                                    Long.parseLong(properties.getProperty("lobby.spawnPoint")) : null)
                            .clientType(FirstContact.ClientType.PLAYER)
                            .build()
            );
            world.getPublicGameWorld().send(message);

            return true;
        } catch (Exception e) {
            System.err.println("Error connecting to " + host + ":" + port);
            world.stop();
            e.printStackTrace();
        }
        return false;
    }

    public void disconnect() {
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (Exception e) {
            System.err.println("Error disconnecting from socket");
            e.printStackTrace();
        }
    }

    public boolean isConnected() {
        return socket != null && socket.isConnected();
    }

}

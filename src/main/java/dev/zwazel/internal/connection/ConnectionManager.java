package dev.zwazel.internal.connection;

import dev.zwazel.internal.InternalGameWorld;
import dev.zwazel.internal.PublicGameWorld;
import dev.zwazel.internal.config.LocalBotConfig;
import dev.zwazel.internal.message.MessageContainer;
import dev.zwazel.internal.message.data.FirstContact;
import lombok.Getter;

import java.io.DataInputStream;
import java.net.Socket;

import static dev.zwazel.internal.message.MessageTarget.Type.SERVER_ONLY;

public class ConnectionManager {
    private static ConnectionManager instance;

    private InternalGameWorld world;
    @Getter
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
        System.out.println("Connecting to " + host + ":" + port + "...");

        if (world == null) {
            throw new IllegalStateException("World is not set");
        }

        try {
            socket = new Socket(host, port);
            System.out.println("Connected to " + host + ":" + port);

            Thread listenerThread = new Thread(new ListenerThread(this, world, new DataInputStream(socket.getInputStream())), "Socket-Listener");
            listenerThread.start();

            PublicGameWorld publicGameWorld = this.world.getPublicGameWorld();
            LocalBotConfig botConfig = this.world.getBot().getLocalBotConfig();
            // Sending first contact message
            MessageContainer message = new MessageContainer(
                    SERVER_ONLY.get(),
                    FirstContact.builder()
                            .lobbyName(botConfig.lobbyConfig().lobbyName())
                            .botName(botConfig.botName())
                            .mapName(botConfig.lobbyConfig().mapName())
                            .teamName(botConfig.lobbyConfig().teamName())
                            .botAssignedSpawnPoint(
                                    botConfig.lobbyConfig().spawnPoint().isPresent() ?
                                            Long.valueOf(botConfig.lobbyConfig().spawnPoint().get())
                                            : null
                            )
                            .clientType(FirstContact.ClientType.PLAYER)
                            .tankType(publicGameWorld.getTank().getTankType())
                            .build()
            );
            publicGameWorld.send(message);

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

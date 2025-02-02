package dev.zwazel.internal.connection;

import dev.zwazel.PropertyHandler;
import dev.zwazel.internal.InternalGameWorld;
import dev.zwazel.internal.messages.MessageContainer;
import dev.zwazel.internal.messages.MessageTarget;
import dev.zwazel.internal.messages.data.FirstContact;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

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

            System.out.println("Starting listener thread...");
            Thread listenerThread = new Thread(new ListenerThread(this, world, new DataInputStream(socket.getInputStream())), "Socket-Listener");
            listenerThread.start();

            System.out.println("Starting writing thread...");
            Thread writingThread = new Thread(new WritingThread(this, world, new DataOutputStream(socket.getOutputStream())), "Socket-Writer");
            writingThread.start();

            // Sending first contact message
            System.out.println("Sending first contact message...");
            MessageContainer message = new MessageContainer(
                    MessageTarget.SERVER_ONLY,
                    FirstContact.builder()
                            .lobby_id(properties.getProperty("lobby.id"))
                            .name(properties.getProperty("bot.name"))
                            .build()
            );
            world.getPublicGameWorld().send(message);

            return true;
        } catch (Exception e) {
            System.err.println("Error connecting to " + host + ":" + port);
            e.printStackTrace();
        }
        return false;
    }

    public boolean isConnected() {
        return socket != null && socket.isConnected();
    }

}

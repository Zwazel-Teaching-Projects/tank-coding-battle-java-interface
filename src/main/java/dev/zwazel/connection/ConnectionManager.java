package dev.zwazel.connection;

import dev.zwazel.ServerApplication;
import dev.zwazel.messages.MessageContainer;
import dev.zwazel.messages.MessageTarget;
import dev.zwazel.messages.data.FirstContact;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ConnectionManager {
    private static ConnectionManager instance;
    private final BlockingQueue<MessageContainer> incomingMessages = new LinkedBlockingQueue<>();
    private final BlockingQueue<MessageContainer> outgoingMessages = new LinkedBlockingQueue<>();
    private Socket socket;
    private Thread listenerThread;
    private Thread writingThread;

    private ConnectionManager() {
        // Private constructor to prevent instantiation
    }

    public static ConnectionManager getInstance() {
        if (instance == null) {
            instance = new ConnectionManager();
        }
        return instance;
    }

    public boolean connect(String host, int port) {
        try {
            socket = new Socket(host, port);
            System.out.println("Connected to " + host + ":" + port);

            System.out.println("Starting listener thread...");
            listenerThread = new Thread(new ListenerThread(instance, new DataInputStream(socket.getInputStream())), "Socket-Listener");
            listenerThread.start();

            System.out.println("Starting writing thread...");
            writingThread = new Thread(new WritingThread(instance, new DataOutputStream(socket.getOutputStream())), "Socket-Writer");
            writingThread.start();

            // Sending first contact message
            System.out.println("Sending first contact message...");
            MessageContainer message = new MessageContainer(
                    MessageTarget.SERVER_ONLY,
                    FirstContact.builder()
                            .lobby_id(ServerApplication.getProperty("lobby.id"))
                            .name(ServerApplication.getProperty("bot.name"))
                            .build()
            );
            outgoingMessages.add(message);

            return true;
        } catch (Exception e) {
            System.err.println("Error connecting to " + host + ":" + port);
            e.printStackTrace();
        }
        return false;
    }

    protected BlockingQueue<MessageContainer> getOutgoingMessages() {
        return outgoingMessages;
    }

    public boolean isConnected() {
        return socket != null && socket.isConnected();
    }

    public boolean allThreadsAlive() {
        return listenerThread.isAlive() && writingThread.isAlive();
    }

}

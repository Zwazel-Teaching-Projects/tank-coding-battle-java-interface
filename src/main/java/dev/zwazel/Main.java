package dev.zwazel;

import dev.zwazel.connection.ConnectionManager;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

public class Main {
    // Shared queues
    private static final ConnectionManager connectionManager = ConnectionManager.getInstance();

    public static void main(String[] args) {
        String rootPath = Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("")).getPath();
        String appConfigPath = rootPath + "app.properties";

        try {
            Properties appProps = new Properties();
            appProps.load(new FileInputStream(appConfigPath));

            String serverIp = appProps.getProperty("server.ip");
            int serverPort = Integer.parseInt(appProps.getProperty("server.port"));

            if (!connectionManager.connect(serverIp, serverPort)) {
                System.err.println("Failed to connect to " + serverIp + ":" + serverPort);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

package dev.zwazel;

import java.net.Socket;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class Main {
    public static void main(String[] args) {
        String serverAddress = "127.0.0.1";
        int serverPort = 9999;

        try (Socket socket = new Socket(serverAddress, serverPort)) {
            System.out.println("Java Client: connected to server " + serverAddress + ":" + serverPort);

            // Send something
            OutputStream out = socket.getOutputStream();
            out.write("Hello from Java!\n".getBytes(StandardCharsets.UTF_8));
            out.flush();

            // Read response
            InputStream in = socket.getInputStream();
            byte[] buf = new byte[1024];
            int n = in.read(buf);
            if (n > 0) {
                String response = new String(buf, 0, n, StandardCharsets.UTF_8);
                System.out.println("Got response: " + response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Java Client: done");
    }
}

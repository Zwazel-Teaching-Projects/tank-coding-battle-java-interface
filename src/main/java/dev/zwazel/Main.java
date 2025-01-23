package dev.zwazel;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Main {
    public static void main(String[] args) {
        String serverAddress = "127.0.0.1";  // Must match Rust server
        int port = 9999;                    // Must match Rust server

        try (Socket socket = new Socket(serverAddress, port);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            System.out.println("Java Client: connected to server " + serverAddress + ":" + port);

            // Read the greeting from the Rust server
            String serverMessage = in.readLine();
            System.out.println("Java Client: received from server -> " + serverMessage);

            // Send our own greeting back
            String message = "Hello from Java!";
            out.println(message);
            System.out.println("Java Client: sent -> " + message);

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Java Client: done");
    }
}

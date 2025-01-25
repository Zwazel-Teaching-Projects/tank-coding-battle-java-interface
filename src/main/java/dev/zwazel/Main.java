package dev.zwazel;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class Main {
    // Shared queues
    private static final BlockingQueue<String> incomingMessages = new LinkedBlockingQueue<>();
    private static final BlockingQueue<String> outgoingMessages = new LinkedBlockingQueue<>();

    public static void main(String[] args) {
        String host = "127.0.0.1";
        int port = 9999;

        try {
            Socket socket = new Socket(host, port);
            System.out.println("Connected to " + host + ":" + port);

            // Start a thread for reading
            Thread readerThread = new Thread(() -> runReader(socket), "Socket-Reader");
            readerThread.start();

            // Start a thread for writing
            Thread writerThread = new Thread(() -> runWriter(socket), "Socket-Writer");
            writerThread.start();

            // Now the main thread can do other work
            // For example, we can periodically send a message:
            for (int i = 0; i < 5; i++) {
                String messageToSend = "Hello from main thread! Count=" + i;
                System.out.println("Enqueuing message to send: " + messageToSend);
                outgoingMessages.put(messageToSend);

                // Sleep a bit, pretend we are busy with other logic
                Thread.sleep(1000);
            }

            // Meanwhile, let's also read any incoming messages from the queue
            // We'll do a small loop to poll them
            for (int i = 0; i < 10; i++) {
                // Poll for any incoming messages with a small timeout
                String incoming = incomingMessages.poll(500, TimeUnit.MILLISECONDS);
                if (incoming != null) {
                    System.out.println("Main thread got message: " + incoming);
                }
            }

            // Closing all threads
            System.out.println("Main thread: interrupting reader and writer threads...");
            readerThread.interrupt();
            writerThread.interrupt();

            // Demonstration complete: close the socket to shut down threads
            System.out.println("Main thread: closing the socket...");
            socket.close();

            System.out.println("All threads stopped. Goodbye!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Continuously reads lines from the socket and puts them into the incomingMessages queue.
     */
    private static void runReader(Socket socket) {
        System.out.println("Reader thread started.");
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                socket.getInputStream(), StandardCharsets.UTF_8))) {

            String line;
            while ((line = br.readLine()) != null) {
                // If we successfully read a line from the server, enqueue it
                incomingMessages.put(line);
            }
            // If readLine() returns null, the connection was closed by the server

        } catch (IOException e) {
            // Could be "socket closed" or real IO error
            System.out.println("Reader thread: IO error: " + e);
        } catch (InterruptedException e) {
            System.out.println("Reader thread interrupted.");
        }
        System.out.println("Reader thread stopped.");
    }

    /**
     * Continuously takes messages from outgoingMessages queue and writes them to the socket.
     */
    private static void runWriter(Socket socket) {
        System.out.println("Writer thread started.");
        try (PrintWriter pw = new PrintWriter(new OutputStreamWriter(
                socket.getOutputStream(), StandardCharsets.UTF_8), true)) {

            while (!socket.isClosed() && !Thread.currentThread().isInterrupted()) {
                // This will block until there's a message to send
                String toSend = outgoingMessages.take();
                pw.println(toSend); // println(...) + flush automatically
            }

        } catch (IOException e) {
            System.out.println("Writer thread: IO error: " + e);
        } catch (InterruptedException e) {
            System.out.println("Writer thread interrupted.");
        }
        System.out.println("Writer thread stopped.");
    }
}

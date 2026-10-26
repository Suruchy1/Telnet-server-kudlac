
package utb.fai;

import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class App {

    public static void main(String[] args) {
        int port = 12345;
        int max_threads = 10;

        try {
            if (args.length > 0) {
                port = Integer.parseInt(args[0]);
            }

            if (args.length > 1) {
                max_threads = Integer.parseInt(args[1]);
            }

            if (port < 1 || port > 65535 || max_threads < 0) {
                System.err.println("Invalid parameters");
                return;
            }

        } catch (NumberFormatException e) {
            System.err.println("Invalid parameters");
            return;
        }

        ExecutorService executor;

        if (max_threads == 0) {
            executor = Executors.newCachedThreadPool();
        } else {
            executor = new ThreadPoolExecutor(
                max_threads,
                max_threads,
                0L,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>()
            );
        }

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            serverSocket.setReuseAddress(true);
            System.out.println("Server listening on port " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                clientSocket.setKeepAlive(true);
                clientSocket.setTcpNoDelay(true);

                executor.execute(new ClientThread(clientSocket));
            }

        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        } finally {
            executor.shutdown();
        }
    }
}

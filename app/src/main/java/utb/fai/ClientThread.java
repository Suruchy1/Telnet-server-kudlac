
package utb.fai;

import java.io.*;
import java.net.*;

public class ClientThread extends Thread {

    private final Socket clientSocket;

    public ClientThread(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try (
            Socket socket = clientSocket;
            InputStream input = socket.getInputStream();
            OutputStream output = socket.getOutputStream()
        ) {
            byte[] buffer = new byte[4096];
            int length;

            while ((length = input.read(buffer)) != -1) {
                output.write(buffer, 0, length);
                output.flush();
            }

        } catch (IOException e) {
            System.err.println("Client error: " + e.getMessage());
        }
    }
}

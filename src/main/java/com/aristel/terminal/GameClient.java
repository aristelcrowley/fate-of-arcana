package com.aristel.terminal;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class GameClient {
    private static final String SERVER_IP = "localhost";
    private static final int SERVER_PORT = 12345;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_IP, SERVER_PORT);
             Scanner scanner = new Scanner(System.in)) {
            
            System.out.println("Connected to Memory Game Server.");
            System.out.println("1. CREATE:RoomName");
            System.out.println("2. JOIN:RoomName");
            System.out.println("3. START");
            System.out.println("4. CLICK:Index");

            ServerListener listener = new ServerListener(socket);
            listener.start();

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            while (scanner.hasNextLine()) {
                String input = scanner.nextLine();
                out.println(input);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class ServerListener extends Thread {
        private Socket socket;
        public ServerListener(Socket socket) { this.socket = socket; }
        @Override
        public void run() {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                String response;
                while ((response = in.readLine()) != null) {
                    System.out.println("[SERVER] " + response);
                }
            } catch (IOException e) {
                System.out.println("Connection Closed");
            }
        }
    }
}
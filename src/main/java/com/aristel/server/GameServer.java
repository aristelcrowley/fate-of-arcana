package com.aristel.server;

import java.io.*;
import java.net.*;
import java.util.*;
import java.net.Socket;

public class GameServer {
    private static final int PORT = 12345;
    private static Map<String, GameRoom> rooms = new HashMap<>();

    public static void main(String[] args) {
        System.out.println("Memory Game Server running on port " + PORT);
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                ClientHandler handler = new ClientHandler(clientSocket);
                handler.start(); 
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static synchronized boolean createRoom(String roomId) {
        if (rooms.containsKey(roomId)) {
            return false;
        }
        rooms.put(roomId, new GameRoom(roomId));
        System.out.println("Created new room: " + roomId);
        return true;
    }

    public static synchronized GameRoom findRoom(String roomId) {
        return rooms.get(roomId);
    }

    public static synchronized void removeRoom(String roomId) {
        if (rooms.containsKey(roomId)) {
            rooms.remove(roomId);
            System.out.println("Room '" + roomId + "' is empty and has been deleted.");
        }
    }
}

class ClientHandler extends Thread {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private GameRoom currentRoom;
    public int playerID;
    public int score = 0;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                String[] parts = inputLine.split(":");
                String command = parts[0];

                if (command.equals("CREATE")) {
                    String roomId = parts[1];
                    boolean created = GameServer.createRoom(roomId);
                    if (created) {
                        sendMessage("MSG:Room '" + roomId + "' created.");
                        
                        GameRoom room = GameServer.findRoom(roomId);
                        if (room.addPlayer(this)) {
                            this.currentRoom = room;
                            sendMessage("JOINED:" + this.playerID);
                            sendMessage("MSG:You are the Room Master!");
                        }
                    } else {
                        sendMessage("ERROR:Room '" + roomId + "' already exists.");
                    }
                } else if (command.equals("JOIN")) {
                    String roomId = parts[1];
                    GameRoom room = GameServer.findRoom(roomId);
                    
                    if (room == null) {
                        sendMessage("ERROR:Room does not exist.");
                    } else {
                        if (room.addPlayer(this)) {
                            this.currentRoom = room;
                            sendMessage("JOINED:" + this.playerID);
                        } else {
                            sendMessage("ERROR:Room Full");
                        }
                    }
                } else if (command.equals("START")) {
                    if (currentRoom != null) {
                        currentRoom.startGame(this); 
                    }
                } else if (command.equals("CLICK")) {
                    if (currentRoom != null) {
                        int cardIndex = Integer.parseInt(parts[1]);
                        currentRoom.processTurn(this.playerID, cardIndex);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Player disconnected");
        } finally {
            try { 
                if (currentRoom != null) currentRoom.removePlayer(this);
                socket.close(); 
            } catch (IOException e) {}
        }
    }

    public void sendMessage(String msg) {
        if (out != null) out.println(msg);
    }
}
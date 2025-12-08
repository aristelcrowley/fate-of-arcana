# Fate of Arcana: A Socket-based Multiplayer Memory Match

A retro-fantasy themed multiplayer card game built with Java Socket Programming and JavaFX.

## About The Project
**Fate of Arcana** is a networked multiplayer game where up to 4 players compete to match pairs of tarot cards. It demonstrates core concepts of distributed systems, including client-server architecture, thread synchronization, and custom communication protocols.

**Tech Stack:** Java (JDK 21), JavaFX, Socket API (TCP)

---

## Key Features

### The Lobby
- **Dynamic Room List:** Real-time updates of available game rooms.
- **Private Rooms:** Lock your destiny with a secret key (password protection).
- **Search & Filter:** Find rooms instantly by name.
- **Auto-Refresh:** UI updates automatically on errors or status changes.

### The Room
- **Room Management:** Create rooms, kick players, and transfer ownership.
- **Log System:** Broadcast messages to all players in the waiting room.

### The Gameplay
- **Turn-Based Logic:** Synchronized turns enforced by the server.
- **Competitive Scoring:** Match cards to steal points and keep your turn.
- **Disconnect Handling:** "Ghost" system handles dropouts without stopping the game.

---

## Technical Architecture

### Socket & Concurrency
The system relies on a **Multithreaded Server** architecture to handle real-time interaction efficiently.

* **Prefix-Based Protocol:** User interaction is handled via a string-based message protocol. Every packet sent over the socket follows a `PREFIX:PAYLOAD` format (e.g., `FLIP:5` or `JOIN:RoomA`). The server parses the prefix to identify the user's intent (Action) and uses the payload to execute logic, ensuring lightweight and readable communication.
* **Multithreading & Synchronization:**  The `GameServer` spawns a dedicated `ClientHandler` thread for every connected user. This allows multiple clients to communicate simultaneously without blocking the main server loop. To maintain data integrity, critical game states are protected using `synchronized` blocks within the `GameRoom` class, preventing race conditions when multiple players attempt to modify the board state at the exact same moment.

### Project Structure
```text
src/main/java/com/aristel
├── App.java            # Main Entry Point (Client)
├── controller/         # JavaFX Controllers (Lobby, Room, GameBoard)
├── network/            # Socket Connection & Message Listeners
├── server/             # GameServer, ClientHandler, GameRoom Logic
└── util/               # SoundManager & Utilities
```

---

## Installation & Setup

### Prerequisites
* **Java JDK 21** or higher.
* **JavaFX SDK 21** (if not using Maven).
* **Maven** (Optional, for dependency management).

### Running the Project (Windows)

This project includes automated batch scripts for easy building and running.

**1. Build the Project**

Compile the server, client, and assets.
```cmd
.\scripts\build.bat
```

**2. Start the Server**

Run the central game server first.
```cmd
.\scripts\run_server.bat
```

**3. Start Clients**

Run as many clients as you want (up to 4 per room).
```cmd
.\scripts\run_client.bat
```

---

## How to Play

1.  **Launch:** Open the Server and at least 2 Client windows.
2.  **Lobby:**
    * Click **"+ CONJURE ROOM"** to create a new game.
    * Select **"Private"** and enter a key to password-protect it.
    * Or, click **"JOIN FATE"** on an existing room.
3.  **Waiting Room:**
    * Wait for other players (Min: 2, Max: 4).
    * The **Room Master** (creator) clicks **"START"**.
4.  **Game Board:**
    * Watch the **Countdown** and **Shuffle**.
    * When it is **Your Turn** (Green Text), click two cards to flip them.
    * **Match:** You get +1 Score and go again.
    * **Mismatch:** Turn passes to the next player.
5.  **Victory:** The game ends when all pairs are found. The player with the highest score wins.

---
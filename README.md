
# ChessNetworkProject

## Overview
ChessNetworkProject is a robust, networked multiplayer chess game designed to be played over a distributed system. The project is structured into several key directories, each containing specific components that handle different aspects of the game, from server management and client interfaces to utility functions and peer-to-peer communication.

## Project Structure
The project is organized into the following main directories:
- **client**: Contains the client-side application code, handling the graphical user interface and client-server communication.
- **server**: Includes the server-side logic necessary for managing game sessions and client interactions.
- **head_server**: Houses the main server files responsible for managing multiple game threads and providing a server GUI for administration.
- **peer_to_peer**: Implements the functionality for peer-to-peer communications between clients.
- **utility**: Provides utility and model classes that are used across various parts of the application for data handling and representation.

Each directory is equipped with specific Java files that contribute to the overall functionality and operation of the ChessNetworkProject.

## Key Components

### Client
- **GameView.java**: Manages the GUI and user interactions on the client side.
- **GameClient.java**: Handles communication with the server, processing and sending game-related messages.
- **ChessPieces.java**: Defines the properties and behavior of the chess pieces used in the game.

### Server
- **UserThread.java**: Manages server-client interactions for individual users.
- **GameServer.java**: Oversees the main server operations and initializes the game environment.
- **GameLogic.java**: Contains the core game logic and decision-making algorithms.
- **Controller.java**: Manages the game state and processes game commands and moves.

### Head Server
- **GameThread.java**: Dedicated to handling a single game or client connection in a multi-threaded server environment.
- **HeadServer.java**: Coordinates all game-related activities and manages `GameThread` instances.
- **HeadServerGUI.java**: Provides a GUI for the server, allowing for real-time server management and monitoring.

### Peer-to-Peer
- **Peer.java**: Manages the logic related to each peer in the network.
- **Connection.java**: Handles the network connections between peers.
- **PeerGUI.java**: Provides the graphical interface for the peer-to-peer networking features.

### Utility
- **FunctionFlag.java**: Defines enumeration flags used throughout the application to represent different game states and commands.
- **Tuple.java**: Utility class for holding and managing paired data.
- **UnicodeMap.java**: Contains mappings for Unicode representations, used primarily for displaying chess pieces.
- **Move.java**: Represents a move within the chess game, encapsulating necessary details such as start and end positions.

## Setup and Running Instructions

### Compilation
To compile the entire project, you can use the provided `compile.sh` script:
```bash
./compile.sh
```

### Execution
Run the head server and client applications using the following commands:
```bash
java head_server/HeadServer
java client/GameClient <HEAD SERVER IP ADDRESS>
```

## Additional Scripts
- **cleanup.sh**: A script to clean up any temporary files or compile artifacts to maintain a clean project environment.

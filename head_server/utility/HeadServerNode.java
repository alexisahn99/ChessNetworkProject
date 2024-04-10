package head_server.utility;

import java.util.ArrayList;
import java.util.Iterator;

public class HeadServerNode {
    private int portNumber;
    private ArrayList<GameServerNode> gameServers;
    private ArrayList<PlayerNode> players;
    private int serverPortNum;

    public HeadServerNode() {
        this.portNumber = 32156;
        IDManagement.addPortNumber(this.portNumber);
        this.gameServers = new ArrayList<>();
        this.players = new ArrayList<>();
        this.serverPortNum = 32157; // Used for finding unused port number
    }

    public int getPortNumber() {
        return this.portNumber;
    }

    ////////////////////* GAME SERVER FUNCTIONS *////////////////////
    
    public ArrayList<GameServerNode> getAllGameServers() {
        return gameServers;
    }

/* This function finds and returns an unused port number */
    public int findPortNum() {
        int serverPort = this.serverPortNum;
        while(IDManagement.allPortNumber.contains(serverPort))
        {
            serverPort ++;
        }
        return serverPort;
    }
/*
    public boolean addGameServer(GameServerNode server) {
        if (IDManagement.addPortNumber(server.getPortNumber())) {
            this.gameServers.add(server);
            return true; // Server added successfully
        } else {
            return false; // Server Port Number is a duplicate and was not added
        }
    } */
    public void addGameServer(GameServerNode server) {
        this.gameServers.add(server);
    }

    public GameServerNode findGameServerByPort(int portNumber) {
        for (GameServerNode gameServer : this.gameServers) {
            if (gameServer.getPortNumber() == portNumber) {
                return gameServer; // Found the server with the specified port number
            }
        }
        return null; // No server with the specified port number was found
    }

    public boolean removeGameServer(int portNumber) {
        Iterator<GameServerNode> iterator = this.gameServers.iterator();
        while (iterator.hasNext()) {
            GameServerNode server = iterator.next();
            if (server.getPortNumber() == portNumber) {
                iterator.remove(); // Remove the server
                IDManagement.removePortNumber(portNumber); // Assume this method exists
                return true; // Server removed successfully
            }
        }
        return false; // No server with the specified port number was found
    }

    ////////////////////* PLAYER FUNCTIONS *////////////////////

    public ArrayList<PlayerNode> getAllPlayers() {
        return players;
    }

    public boolean addPlayer(PlayerNode client) {
        if (IDManagement.addClientId(client.getClientId())) {
            this.players.add(client);
            return true; // Client added successfully
        } else {
            return false; // Client ID is a duplicate and was not added
        }
    }

    public PlayerNode findPlayer(int portNumber) {
        for (PlayerNode player : this.players) {
            if (player.getPortNumber() == portNumber) {
                return player; 
            }
        }
        return null; 
    }

    public boolean removePlayer(int serverPortNumber) {
        Iterator<PlayerNode> iterator = this.players.iterator();
        while (iterator.hasNext()) {
            PlayerNode player = iterator.next();
            if (player.getPortNumber() == portNumber) {
                iterator.remove(); 
                IDManagement.removePortNumber(portNumber); 
                IDManagement.removeClientId(player.getClientId());
                return true; 
            }
        }
        return false;
    }

}

package head_server.utility;

import java.util.ArrayList;
import java.util.Iterator;

public class HeadServerNode {
    private int portNumber;
    private int serverPortNum;
    private ArrayList<GameServerNode> gameServers;
    private ArrayList<PlayerNode> players;

    public HeadServerNode() {
        this.portNumber = 32156;
        this.serverPortNum = 32157; // Used for finding unused port number
        IDManagement.addPortNumber(this.portNumber);
        this.gameServers = new ArrayList<>();
        this.players = new ArrayList<>();
    }

    public int getPortNumber() {
        return this.portNumber;
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

    ////////////////////* GAME SERVER FUNCTIONS *////////////////////
    
    public ArrayList<GameServerNode> getAllGameServers() {
        return gameServers;
    }

    public int getNumGameServers() {
        return gameServers.size();
    }
  
    public void addGameServer(GameServerNode server) {
        this.gameServers.add(server);
        IDManagement.allPortNumber.add(server.getPortNumber());
    }

    public int findGameServerByPort(int portNumber) {
        for (GameServerNode gameServer : this.gameServers) {
            if (gameServer.getPortNumber() == portNumber) {
                return 1; // Found the server with the specified port number
            }
        }
        return 0; // No server with the specified port number was found
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


    public void addPlayer(PlayerNode client) {
        IDManagement.allUserNames.add(client.getUserName());
        IDManagement.allPortNumber.add(client.getPortNumber());
        this.players.add(client);
    }

    public PlayerNode findPlayer(int portNumber) {
        for (PlayerNode player : this.players) {
            if (player.getPortNumber() == portNumber) {
                return player; 
            }
        }
        return null; 
    }

    /*

    public boolean removePlayer(int serverPortNumber) {
        Iterator<PlayerNode> iterator = this.players.iterator();
        while (iterator.hasNext()) {
            PlayerNode player = iterator.next();
            if (player.getPortNumber() == portNumber) {
                iterator.remove(); 
                IDManagement.removePortNumber(portNumber); 
                IDManagement.removeClientId(player.getSelfPortNum());
                return true; 
            }
        }
        return false;
    }
    */

}

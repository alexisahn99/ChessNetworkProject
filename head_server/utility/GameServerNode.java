package head_server.utility;

import java.util.ArrayList;
import java.util.Iterator;

public class GameServerNode {
    private int portNumber; // used for Players to connect to this game
    private ArrayList<PlayerNode> allPlayers;

    public GameServerNode(int portNumber) {
        this.portNumber = portNumber;
        this.allPlayers = new ArrayList<>();
    }

    public int getPortNumber() {
        return this.portNumber;
    }

    public ArrayList<PlayerNode> getAllPlayers() {
        return this.allPlayers;
    }

    public void addPlayer(PlayerNode player) {
        this.allPlayers.add(player);
    }

    public PlayerNode findPlayer(int portNumber) {
        for (PlayerNode player : this.allPlayers) {
            if (player.getPortNumber() == portNumber) {
                return player; 
            }
        }
        return null; 
    }

    public boolean removePlayer(int serverPortNumber) {
        Iterator<PlayerNode> iterator = this.allPlayers.iterator();
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
}


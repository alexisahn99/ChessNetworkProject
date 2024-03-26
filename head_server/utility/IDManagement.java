package head_server.utility;

import java.util.HashSet;
import java.util.Set;

public class IDManagement {
    private static Set<String> allClientIDs = new HashSet<>();
    private static Set<Integer> allServerPorts = new HashSet<>();

    // Adds a client ID to the set if not already present, ensuring uniqueness
    public static boolean addClientId(String clientId) {
        return allClientIDs.add(clientId);
    }

    // Removes a client ID from the set
    public static boolean removeClientId(String clientId) {
        return allClientIDs.remove(clientId);
    }

    // Adds a server port to the set if not already present, ensuring uniqueness
    public static boolean addServerPort(int portNumber) {
        return allServerPorts.add(portNumber);
    }

    // Removes a server port from the set
    public static boolean removeServerPort(int portNumber) {
        return allServerPorts.remove(portNumber);
    }

    public static boolean updateClientId(ClientNode client, String newId) {
        if (!allClientIDs.contains(newId)) {
            removeClientId(client.getClientId());
            client.setClientId(newId);
            addClientId(newId);
            return true;
        }
        return false; // New ID is already in use.
    }
}

package head_server.utility;

import java.util.HashSet;
import java.util.Set;

public class IDManagement {
    private static Set<String> allClientIDs = new HashSet<>();
    private static Set<Integer> allPortNumber = new HashSet<>();

    // Adds a client ID to the set if not already present, ensuring uniqueness
    public static boolean addClientId(String clientId) {
        return allClientIDs.add(clientId);
    }

    // Removes a client ID from the set
    public static boolean removeClientId(String clientId) {
        return allClientIDs.remove(clientId);
    }

    // Adds a server port to the set if not already present, ensuring uniqueness
    public static boolean addPortNumber(int portNumber) {
        return allPortNumber.add(portNumber);
    }

    // Removes a server port from the set
    public static boolean removePortNumber(int portNumber) {
        return allPortNumber.remove(portNumber);
    }

}

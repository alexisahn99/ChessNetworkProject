package head_server.utility;

/*
To make Head Server Node object: ServerNode(portNumber, true)
To make Game Server Node object: ServerNode(portNumber)
*/ 

import java.util.ArrayList;
import java.util.List;

public class HeadServerNode {
    private int portNumber;
    private List<ServerNode> childServers;
    private List<ClientNode> childClients;

    public HeadServerNode(int portNumber) {
        this.portNumber = portNumber;
        this.childServers = new ArrayList<>();
        this.childClients = new ArrayList<>();
    }

    public boolean addChildServer(ServerNode server) {
        if (IDManagement.addServerPort(server.getPortNumber())) {
            this.childServers.add(server);
            return true; // Server added successfully
        } else {
            return false; // Server Port Number is a duplicate and was not added
        }
    }

    public boolean addChildClient(ClientNode client) {
        if (IDManagement.addClientId(client.getClientId())) {
            this.childClients.add(client);
            return true; // Client added successfully
        } else {
            return false; // Client ID is a duplicate and was not added
        }
    }

    public List<ServerNode> getChildServers() {
        return childServers;
    }

    public List<ClientNode> getChildClients() {
        return childClients;
    }

    public int getPortNumber() {
        return portNumber;
    }
}

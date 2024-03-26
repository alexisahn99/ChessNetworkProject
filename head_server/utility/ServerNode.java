package head_server.utility;

import java.util.ArrayList;
import java.util.List;

public class ServerNode {
    private int portNumber;
    private List<ClientNode> childClients;

    public ServerNode(int portNumber) {
        this.portNumber = portNumber;
        this.childClients = new ArrayList<>();
    }

    public void addChildClient(ClientNode client) {
        this.childClients.add(client);
    }

    public List<ClientNode> getChildClients() {
        return childClients;
    }

    public int getPortNumber() {
        return portNumber;
    }
}


package head_server.utility;

public class PlayerNode {
    private String clientId;
    private int portNumber; // used for P2P connections

    public PlayerNode(String clientId, int portNumber) {
        this.clientId = clientId;
        this.portNumber = portNumber;
    }

    public int getPortNumber() {
        return this.portNumber;
    }

    public String getSelfPortNum() {
        return this.clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
}

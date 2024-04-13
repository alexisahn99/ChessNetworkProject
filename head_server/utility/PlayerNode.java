package head_server.utility;

public class PlayerNode {
    private String userName;
    private int portNumber; // used for P2P connections

    public PlayerNode(String userName, int portNumber) {
        this.userName = userName;
        this.portNumber = portNumber;
    }

    public int getPortNumber() {
        return this.portNumber;
    }

    public int getSelfPortNum() {
        return this.portNumber;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setClientId(String clientId) {
        this.userName = userName;
    }
}

package head_server;

import java.io.ObjectOutputStream;
import java.net.Socket;

import head_server.HeadServer;


public class GameThread extends Thread {
    private Socket socket;
    private HeadServer server;
    private ObjectOutputStream out;

        public GameThread(Socket socket, HeadServer server) {
        this.socket = socket;
        this.server = server;
    }
}

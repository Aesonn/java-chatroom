package chatsystem_server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class server_Accept extends Thread {

    private ServerSocket serverSocket;
    private Socket socket;
    private clientListStore clientListStore;
    private clientList clientList;

    public server_Accept(ServerSocket ss, clientListStore cls, clientList cl) {
        this.serverSocket = ss;
        this.clientListStore = cls;
        this.clientList = cl;
    }

//*****************************************accept all user************************************************************************************************
    @Override
    public void run() {
        while (true) {
            try {
                socket = serverSocket.accept();
                new server_Process(socket, clientListStore, clientList);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}

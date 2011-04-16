package chatsystem_server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class server_Accept extends Thread {

    private ServerSocket serverSocket;
    private Socket socket;
    private clientListStore clientListStore;
    private clientList clientList;
    private clientList noRoomClientList;

    public server_Accept(ServerSocket ss, clientListStore cls, clientList cl, clientList cl2) {
        this.serverSocket = ss;
        this.clientListStore = cls;
        this.clientList = cl;
        this.noRoomClientList = cl2;
    }

//*****************************************accept all user************************************************************************************************
    @Override
    public void run() {
        while (true) {
            try {
                socket = serverSocket.accept();
                //new server_Process(socket, clientListStore, clientList, noRoomClientList);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}

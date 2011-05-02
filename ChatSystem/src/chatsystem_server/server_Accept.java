package chatsystem_server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;

public class server_Accept extends Thread {

    private ServerSocket serverSocket;
    private Socket socket;
    private static Connection mySqlConnection;
    private roomList roomList;
    private room clientList;
    private room noRoomClientList;

    public server_Accept(ServerSocket ss, roomList rList, room r1, room r2, Connection mysqlCon) {
        this.serverSocket = ss;
        this.mySqlConnection = mysqlCon;
        this.roomList = rList;
        this.clientList = r1;
        this.noRoomClientList = r2;
    }

//*****************************************accept all user************************************************************************************************
    @Override
    public void run() {
        while (true) {
            try {
                socket = serverSocket.accept();
                new server_Process(socket, roomList, clientList, noRoomClientList, mySqlConnection).start();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
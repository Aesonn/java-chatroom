package chatsystem_server;

import java.io.IOException;
import java.net.ServerSocket;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class server_Main {

    private ServerSocket serverSocket;
    //store all the room
    private clientListStore clientListStore;
    //store all the online user
    private clientList clientList;
    //time
    String DATE_FORMAT = " kk:mm, dd/MM/yyyy...";
    SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);


    public server_Main() throws IOException{

        Calendar c1 = Calendar.getInstance(); // today
        String nowtime = sdf.format(c1.getTime());
        System.out.println("Start time: "+nowtime);
        serverSocket=new ServerSocket(5999);
        System.out.println("Messenger Server Started...");
        clientListStore = new clientListStore();
        System.out.println("Users List Store Created...");
        clientList = new clientList("/Main",nowtime);
        System.out.println("User List Created...");
        new server_Accept(serverSocket, clientListStore, clientList);
        System.out.println("Ready to accept user...");
        clientList.insertClientToTheFirst("LELELE");
        clientList.insertClientToTheFirst("LALALA");
        clientList.insertClientToTheFirst("LOLOLOL");
        System.out.println(clientList.sendAvailableRoom());

    }

    public static void main(String[] args) {
        try {
            new server_Main();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
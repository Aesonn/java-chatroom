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
    //store all the user who no join the room
    private clientList noRoomClientList;
    //time
    String DATE_FORMAT = "kk:mm:ss, dd/MM/yyyy";
    SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);


    public server_Main() throws IOException{

        Calendar c1 = Calendar.getInstance(); // today
        String nowtime = sdf.format(c1.getTime());
        System.out.println("Start time: "+nowtime);
        serverSocket=new ServerSocket(6769);
        System.out.println("Messenger Server Started...");
        clientListStore = new clientListStore();
        System.out.println("Users List Store Created...");
        clientList = new clientList("/allClient", null);
        System.out.println("User List Created...");
        noRoomClientList = new clientList("/noRoomClient", null);
        System.out.println("no room user List Created...");
        new server_Accept(serverSocket, clientListStore, clientList, noRoomClientList).start();
        
        System.out.println("Ready to accept user...");
        for(int i=1; i<131; i++){
            clientListStore.insertClientListToTheFirst("LOL"+i, "welcome join room "+i, null, null);
            System.out.println("LOL"+i);
        }
    }

    public static void main(String[] args) {
        try {
            new server_Main();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
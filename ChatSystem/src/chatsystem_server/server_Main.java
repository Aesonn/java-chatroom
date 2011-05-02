package chatsystem_server;

import java.io.IOException;
import java.net.ServerSocket;
import java.text.SimpleDateFormat;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;

public class server_Main {

    private ServerSocket serverSocket;
    private static Connection mySqlConnection;
    //store all the room
    private roomList roomList;
    //store all the online user
    private room clientList;
    //store all the user who no join the room
    private room noRoomClientList;



    public server_Main() throws IOException, SQLException{

        serverSocket=new ServerSocket(6769);
        System.out.println(getDateAndTime()+"Messenger Server Started... ");
        
        initializeDatabases();
        System.out.println(getDateAndTime()+"Database initialized...");
        
        roomList = new roomList();
        clientList = new room("/allClient", null);
        noRoomClientList = new room("/noRoomClient", null);
        
        new server_Accept(serverSocket, roomList, clientList, noRoomClientList, mySqlConnection).start();
        System.out.println(getDateAndTime()+"Ready to accept user...");
        
        for(int i=1; i<131; i++){
            roomList.insertClientListToTheFirst("LOL"+i, "welcome join room "+i, null, null);
        }
        
    }

    public static void main(String[] args) {
        try {
            new server_Main();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public static void initializeDatabases() throws SQLException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            mySqlConnection = DriverManager.getConnection("jdbc:mysql://localhost/chatroom", "root", "aaa");
            Statement sql = mySqlConnection.createStatement();
            String query = "UPDATE client SET online = 0"; 
            sql.executeUpdate(query);
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }
    
    public String getDateAndTime(){
        String DATE_FORMAT = "[dd/MM/yyyy|kk:mm:ss]";
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        Calendar c1 = Calendar.getInstance(); // today
        String nowtime = sdf.format(c1.getTime());
        return nowtime;
    }
}
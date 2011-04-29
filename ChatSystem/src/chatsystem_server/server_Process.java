package chatsystem_server;

import java.util.logging.Level;
import java.util.logging.Logger;
import packets.Opcode;
import packets.packet_clientMessage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import packets.packet_newRoom;

public class server_Process extends Thread implements Opcode{

    private String clientName;
    private boolean run;
    //connection
    private Socket socket;
    private ObjectInputStream clientInput;
    private ObjectOutputStream clientOutput;
    //storage
    private clientListStore clientListStore;
    private clientList clientList;
    private clientList noRoomClientList;;
    //packets
    private Byte opcode;
    private packet_clientMessage clientMSG;
    private packet_newRoom newRoom;
    //sql
    private static Connection connection;
    private Statement sql;

    public server_Process(Socket s, clientListStore cls, clientList cl, clientList cl2) throws IOException {
        this.socket = s;
        this.clientListStore = cls;
        this.clientList = cl;
        this.noRoomClientList = cl2;
        this.clientInput = new ObjectInputStream(socket.getInputStream());
        this.clientOutput = new ObjectOutputStream(socket.getOutputStream());
        this.clientName = "GUEST";
        this.run = true;
    }

//*****************************************mysql database driver******************************************************************************************
    public static Connection dataAccess() throws SQLException {
        try {
            Class.forName("com.mysql.jdbc.Driver");

            connection = DriverManager.getConnection("jdbc:mysql://localhost/chatroom", "root", "aaa");

        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return connection;
    }

//*****************************************check username and password from database**********************************************************************
    public int checkLogin(String name, String password) throws SQLException {

        int login = 0;
        Connection con = dataAccess();
        sql = con.createStatement();
        String query = "select * from client where name='" + name + "'and password = '" + password + "'";
        ResultSet result = sql.executeQuery(query);
        if (result.first())
        {
            if(result.getInt(5)==0)
                login = 1;
            else
                login = 2;
        }
        else
        {
            login = 0;
        }
        return login;
    }
    
//**********************************************************************************************************************************************************
    public void loginSucess(String name, ObjectOutputStream os) throws SQLException{
        this.clientName = name;
        clientList.insertClientToTheFirst(name, os);
        noRoomClientList.insertClientToTheFirst(name, os);
        
        Connection con = dataAccess();
        sql = con.createStatement();
        String query = "UPDATE client SET online = 1 WHERE name ='"+name+"'"; 
        sql.executeUpdate(query);
    }
    
    public void logout(String name){
        try {
            if(clientListStore.checkClient(name)==null)
            {
                noRoomClientList.removeClient(name);
                clientList.removeClient(name);
            }else
            {
                clientListStore.checkClient(name).removeClient(name);
                clientList.removeClient(name);
            }
            Connection con = dataAccess();
            sql = con.createStatement();
            String query = "UPDATE client SET online = 0 WHERE name ='"+name+"'"; 
            sql.executeUpdate(query);
        } catch (SQLException ex) {
            Logger.getLogger(server_Process.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void closeThread(){
        try{
            this.run = false;
            this.clientOutput.close();
            this.clientInput.close();
            this.socket.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public String getTime(){
        String DATE_FORMAT = "kk:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        Calendar c1 = Calendar.getInstance();
        String time = sdf.format(c1.getTime());
        return time;
    }

//*****************************************process all packet_request*************************************************************************************
    @Override
    public void run(){
       while(run) {
           try{
               opcode = clientInput.readByte();
             switch(opcode)
             {
                 case CMSG_LOGIN:
                     String username = (String) clientInput.readObject();
                     String password = (String) clientInput.readObject();
                     
                     int result = checkLogin(username, password);
                     if(result == 1)
                     {                        
                         this.clientOutput.writeByte(SMSG_LOGIN_SUCCESS);
                         this.clientOutput.flush();
                         loginSucess(username, this.clientOutput);
                         refreshRoomList();
                     }
                     else if(result == 2)
                     {
                         this.clientOutput.writeByte(SMSG_MULTI_LOGIN);
                         this.clientOutput.flush();
                         closeThread();
                     }
                     else
                     {
                         this.clientOutput.writeByte(SMSG_LOGIN_FAILED);
                         this.clientOutput.flush();
                         closeThread();
                     }
                     break;

                 case CMSG_LOGOUT:
                     logout(this.clientName);
                     closeThread();
                     break;

                 case MSG_SENDGROUPMESSAGE:
                     clientMSG = (packet_clientMessage) clientInput.readObject();
                     clientMSG.setTime(getTime());
                     clientListStore.checkClient(clientMSG.getName()).sendGroupMessage(MSG_SENDGROUPMESSAGE, clientMSG);
                     break;

                 case CMSG_JOINROOM:
                     String roomName = (String) clientInput.readObject();
                     if(clientListStore.findClientList(roomName).getQuestion()==null){
                         noRoomClientList.removeClient(this.clientName);
                         clientListStore.findClientList(roomName).insertClientToTheFirst(this.clientName, this.clientOutput);
                         this.clientOutput.writeByte(SMSG_JOINROOM_SUCCESS);
                         this.clientOutput.flush();
                     }
                     else
                     {
                         
                     }
                     break;

                 case CMSG_LEAVEROOM:
                     if(clientListStore.checkClient(this.clientName)!=null)
                         clientListStore.checkClient(this.clientName).removeClient(this.clientName);
                     noRoomClientList.insertClientToTheFirst(this.clientName, this.clientOutput);
                     this.clientOutput.writeByte(SMSG_LEAVEROOM_SUCCESS);
                     this.clientOutput.flush();
                     refreshRoomList();
                     break;

                 case CMSG_CREATEROOM:
                     newRoom = (packet_newRoom) clientInput.readObject();
                     if(clientListStore.checkClient(newRoom.getName())==null)
                     {
                         clientListStore.insertClientListToTheFirst(newRoom);
                         
                         this.clientOutput.writeByte(SMSG_CREATEROOM_SUCCESS);
                         this.clientOutput.flush();
                          
                         noRoomClientList.removeClient(this.clientName);
                         clientListStore.findClientList(newRoom.getName()).insertClientToTheFirst(this.clientName, this.clientOutput);
                         this.clientOutput.writeByte(SMSG_JOINROOM_SUCCESS);
                         this.clientOutput.flush();
                         
                         refreshRoomList();
                     }
                     else
                     {
                         this.clientOutput.writeByte(SMSG_MULTI_CREATEROOM);
                         this.clientOutput.flush();
                     }
                     break;
                     
                 default:
                     System.out.println("Unknown Opcode Receive...");
                     break;

             }
           }catch(Exception ex){
               logout(this.clientName);
               closeThread();
               ex.printStackTrace();
           }
       }
    }

    public void refreshRoomList() throws IOException, InterruptedException{
        noRoomClientList.sendRoomList(clientListStore, this.currentThread());
    }
}
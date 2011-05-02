package chatsystem_server;

import packets.Opcode;
import packets.packet_clientMessage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.sql.Connection;
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
    private roomList roomList;
    private room clientList;
    private room noRoomClientList;
    //sql
    private static Connection mysqlConnection;
    private Statement sql;

    public server_Process(Socket s, roomList rList, room r1, room r2, Connection mysqlCon) throws IOException {
        this.socket = s;
        this.mysqlConnection = mysqlCon;
        this.roomList = rList;
        this.clientList = r1;
        this.noRoomClientList = r2;
        this.clientInput = new ObjectInputStream(socket.getInputStream());
        this.clientOutput = new ObjectOutputStream(socket.getOutputStream());
        this.clientName = "GUEST";
        this.run = true;
    }

//*****************************************check username and password from database**********************************************************************
    public int checkLogin(String name, String password) throws SQLException {

        int login = 0;
        Connection con = mysqlConnection;
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
        
        Connection con = mysqlConnection;
        sql = con.createStatement();
        String query = "UPDATE client SET online = 1 WHERE name ='"+name+"'"; 
        sql.executeUpdate(query);
    }
    
    public void logout(String name){
        try {
            if(roomList.checkClient(name)==null)
            {
                noRoomClientList.removeClient(name);
                clientList.removeClient(name);
            }else
            {
                roomList.checkClient(name).removeClient(name);
                clientList.removeClient(name);
            }
            Connection con = mysqlConnection;
            sql = con.createStatement();
            String query = "UPDATE client SET online = 0 WHERE name ='"+name+"'"; 
            sql.executeUpdate(query);
        } catch (SQLException ex) {
            ex.printStackTrace();
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
        Byte opcode;
        String username;
        String password;
        String roomName;
        String answer;
        String sameRoomClient;
        String whisperTargetName;
        client whisperTarget;
        packet_clientMessage clientMSG;
        packet_clientMessage whisperMSG;
        packet_newRoom newRoom;
        
       while(run) {
           try{
               opcode = this.clientInput.readByte();
             switch(opcode)
             {
                 case CMSG_LOGIN:
                     username = (String) this.clientInput.readObject();
                     password = (String) this.clientInput.readObject();
                     
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
                     clientMSG = (packet_clientMessage) this.clientInput.readObject();
                     clientMSG.setTime(getTime());
                     roomList.checkClient(clientMSG.getName()).sendGroupMessage(MSG_SENDGROUPMESSAGE, clientMSG);
                     break;

                 case CMSG_JOINROOM:
                     roomName = (String) this.clientInput.readObject();
                     if(roomList.findClientList(roomName).getQuestion()==null){
                         noRoomClientList.removeClient(this.clientName);
                         roomList.findClientList(roomName).insertClientToTheFirst(this.clientName, this.clientOutput);
                         this.clientOutput.writeByte(SMSG_JOINROOM_SUCCESS);
                         this.clientOutput.flush();
                     }
                     else
                     {
                         this.clientOutput.writeByte(SMSG_SEND_QUESTION);
                         this.clientOutput.flush();
                         this.clientOutput.writeObject(roomList.findClientList(roomName).getQuestion());
                         this.clientOutput.flush();
                         
                         opcode = this.clientInput.readByte();
                         if(opcode == CMSG_SEND_ANSWER)
                         {
                             answer = (String) this.clientInput.readObject();
                             if(roomList.findClientList(roomName).getAnswer().compareTo(answer)==0)
                             {
                                 noRoomClientList.removeClient(this.clientName);
                                 roomList.findClientList(roomName).insertClientToTheFirst(this.clientName, this.clientOutput);
                                 this.clientOutput.writeByte(SMSG_JOINROOM_SUCCESS);
                                 this.clientOutput.flush();
                             }
                             else
                             {
                                 this.clientOutput.writeByte(SMSG_JOINROOM_FAILED);
                                 this.clientOutput.flush();
                             }
                             
                         }
                         else if(opcode == CMSG_CANCEL_ANSWER)
                         {
                         } 
                     }
                     break;

                 case CMSG_LEAVEROOM:
                     if(roomList.checkClient(this.clientName)!=null)
                         roomList.checkClient(this.clientName).removeClient(this.clientName);
                     noRoomClientList.insertClientToTheFirst(this.clientName, this.clientOutput);
                     this.clientOutput.writeByte(SMSG_LEAVEROOM_SUCCESS);
                     this.clientOutput.flush();
                     refreshRoomList();
                     break;

                 case CMSG_CREATEROOM:
                     newRoom = (packet_newRoom) this.clientInput.readObject();
                     if(roomList.checkClient(newRoom.getName())==null)
                     {
                         roomList.insertClientListToTheFirst(newRoom);
                         
                         this.clientOutput.writeByte(SMSG_CREATEROOM_SUCCESS);
                         this.clientOutput.flush();
                          
                         noRoomClientList.removeClient(this.clientName);
                         roomList.findClientList(newRoom.getName()).insertClientToTheFirst(this.clientName, this.clientOutput);
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
                     
                 case CMSG_REQUEST_ROOMCLIENT:
                     sameRoomClient = roomList.checkClient(this.clientName).getSameRoomClient();
                     this.clientOutput.writeByte(SMSG_SEND_ROOMCLIENT);
                     this.clientOutput.flush();
                     this.clientOutput.writeObject(sameRoomClient);
                     this.clientOutput.flush();
                     break;
                     
                 case MSG_WHISPERMESSAGE:
                     whisperTargetName = (String) this.clientInput.readObject();
                     whisperMSG = (packet_clientMessage) this.clientInput.readObject();
                     whisperMSG.setTime(getTime());
                     
                     whisperTarget = roomList.checkClient(this.clientName).findClient(whisperTargetName);
                     if(whisperTarget != null)
                     {
                     whisperTarget.getClientOutput().writeByte(MSG_WHISPERMESSAGE);
                     whisperTarget.getClientOutput().flush();
                     whisperTarget.getClientOutput().writeObject(whisperMSG);
                     whisperTarget.getClientOutput().flush();
                     }
                     else
                     {
                         this.clientOutput.writeByte(MSG_WHISPERMESSAGE_FAILED);
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
        noRoomClientList.sendRoomList(roomList, this.currentThread());
    }
}
package chatsystem_server;

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

public class server_Process extends Thread implements Opcode{

    private String clientName;
    //connection
    private Socket socket;
    private ObjectInputStream clientInput;
    private ObjectOutputStream clientOutput;
    //storage
    private clientListStore clientListStore;
    private clientList clientList;
    private clientList noRoomClientList;
    //packets
    private packet_request request;
    private packet_loginData loginDATA;
    private packet_clientMessage clientMSG;
    private packet_systemMessage systemMSG;
    private packet_roomData roomDATA;
    //timer
    String DATE_FORMAT = " hh:mm,dd/MM/yyyy;";
    SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
    Calendar c1;
    String time;;
    //sql
    private static Connection connection;
    private Statement sql;

    public server_Process(Socket s, clientListStore cls, clientList cl, clientList cl2) throws IOException {
        this.socket = s;
        this.clientListStore = cls;
        this.clientList = cl;
        this.clientList = cl2;
        this.clientInput = new ObjectInputStream(socket.getInputStream());
        this.clientOutput = new ObjectOutputStream(socket.getOutputStream());

    }

//*****************************************mysql database driver******************************************************************************************
    public static Connection dataAccess() throws SQLException {
        try {
            Class.forName("com.mysql.jdbc.Driver");

            connection = DriverManager.getConnection("jdbc:mysql://localhost/my_messenger_db", "", "");

        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return connection;
    }

//*****************************************check username and password from database**********************************************************************
    public boolean checkLogin(String name, String password) throws SQLException {

        Connection con = dataAccess();
        sql = con.createStatement();
        String query = "select * from user where id='" + name + "'and pass = '" + password + "' and status = 'available'";
        ResultSet result = sql.executeQuery(query);
        if (result.next())
            return true;
        else {
            return false;
        }
    }

//*****************************************process all packet_request********************************************************************************************
    @Override
    public void run(){
        clientList current;
       while(true) {
           try{
             //reading request
             request = (packet_request) clientInput.readObject();
             this.clientName = request.getName();
             //read the opcode from the request packet
             switch(request.getOpcode())
             {
                 //opcode is login
                 case CMSG_LOGIN:
                     //read again and convert to another object format
                     loginDATA = (packet_loginData) clientInput.readObject();
                     //check name and password from the databases
                     if(checkLogin(loginDATA.getUsername(), loginDATA.getPassword()))
                     {
                         //if findclient() return null mean no multilogin
                         if(clientList.findClient(loginDATA.getUsername())==null)
                         {
                             c1 = Calendar.getInstance();
                             time = sdf.format(c1.getTime());
                             //if true create client and add client into clientList, this clientList is store all online client
                             clientList.insertClientToTheFirst(loginDATA.getUsername(), time, clientOutput);
                             noRoomClientList.insertClientToTheFirst(loginDATA.getUsername(), time, clientOutput);
                             //send success message to the client
                             this.clientOutput.writeObject(new packet_systemMessage(SMSG_LOGIN_SUCCESS));

                         }
                         else
                         {
                             //if got same acc inside clientList we send multi login warning msg to the client
                             this.clientOutput.writeObject(new packet_systemMessage(SMSG_MULTI_LOGIN));
                         }
                     }
                     else
                         //if false send fail message to the client
                         this.clientOutput.writeObject(new packet_systemMessage(SMSG_LOGIN_FAILED));

                 //opcode is logout
                 case CMSG_LOGOUT:
                     String name = request.getName();
                     //remove client from the clientList
                     clientList.removeClient(name);
                     //check the client have join any room or not
                     current = clientListStore.checkClient(name);
                     //if the room no return null mean he havent leave room so need remove him from that room
                     if(current.removeClient(name)!=null)
                     {
                         current.removeClient(name);
                     }
                     else
                     {
                         noRoomClientList.removeClient(name);
                     }
                     //close all connection
                     this.clientOutput.close();
                     this.clientInput.close();
                     this.socket.close();

                 //opcode is send group msg
                 case CMSG_SENDGROUPMESSAGE:
                     //read again and convert to another object format
                     clientMSG = (packet_clientMessage) clientInput.readObject();
                     //check client at which room and then send to all ppl inside that room
                     clientListStore.checkClient(clientMSG.getName()).sendGroupMessage(new packet_systemMessage(CMSG_SENDGROUPMESSAGE), clientMSG);

                 //opcode is create room
                 case CMSG_CREATEROOM:
                     //read again and convert to another object format
                     roomDATA = (packet_roomData) clientInput.readObject();
                     //if the room name u wish to create already exist
                     if(clientListStore.isExist(roomDATA.getName()))
                         //send multi room warning message to the client
                         this.clientOutput.writeObject(new packet_systemMessage(SMSG_MULTI_CROOM));
                     //no same room name
                     else
                     {
                         //check u want to set password for this room or not, then use different constructer to create room
                         if(roomDATA.getPassword()==null)
                             clientListStore.insertClientListToTheFirst(roomDATA.getName(),roomDATA.getTime());
                         else
                             clientListStore.insertClientListToTheFirst(roomDATA.getName(), roomDATA.getPassword(), roomDATA.getTime());
                         //send create room success msg to client
                         this.clientOutput.writeObject(new packet_systemMessage(SMSG_CROOM_SUCCESS));
                     }

                  //opcode is join room
                 case CMSG_JOINROOM:
                     //read again and convert to another object format
                     roomDATA = (packet_roomData) clientInput.readObject();
                     //if the room name u wish to join already exist
                     if(clientListStore.isExist(roomDATA.getName()))
                     {
                         c1 = Calendar.getInstance();
                         time = sdf.format(c1.getTime());
                         //add client into room
                         clientListStore.findClientList(roomDATA.getName()).insertClientToTheFirst(this.clientName, time, this.clientOutput);
                         //send multi room warning message to the client
                         this.clientOutput.writeObject(new packet_systemMessage(SMSG_JOINROOM_SUCCESS));
                     }
                     else
                     {

                     }

                 case CMSG_LEAVEROOM:
                     //search client at which room and then remove that client from that room.
                     clientListStore.removeClientList(clientListStore.checkClient(request.getName()).getRoomName());
                     //send leave room success message to the client
                     this.clientOutput.writeObject(new packet_systemMessage(SMSG_LEAVEROOM_SUCCESS));

             }
           }catch(Exception ex){
               ex.printStackTrace();
           }
       }
    }

    public void refreshRoomList(ObjectOutputStream os) throws IOException{
        os.writeObject(new packet_systemMessage(SMSG_SEND_ROOMLIST));
        clientListStore.sendAvailableRoom(os);
        os.writeObject(new packet_systemMessage(SMSG_END_SEND_ROOMLIST));
    }
}

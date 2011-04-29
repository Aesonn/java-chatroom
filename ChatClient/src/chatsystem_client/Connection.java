/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chatsystem_client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.swing.JFrame;
import packets.*;

/**
 *
 * @author User
 */
public class Connection extends Thread implements Opcode {

    private Socket socket;
    private ObjectInputStream in;
    private static ObjectOutputStream out;
    private JFrame loginFrame;
    private packet_clientMessage clientMSG;
    private FriendListUI friendList;
    private packet_roomData roomData;
    private chatUI chatUI;
    private static String clientName;
    private static String roomName;
    private static boolean run;
    private Byte opcode;
    static roomList roomList;
    static CreateRoomUI createRoomFrame;

    public Connection(ObjectInputStream in, FriendListUI friendList, String name, JFrame jf) {
        this.in = in;
        this.friendList = friendList;
        this.clientName = name;
        this.loginFrame = jf;
        run = true;
    }

    public Connection() {
    }

    public int connect(String name, String pass, JFrame loginFrame, roomList roomList) throws UnknownHostException, IOException, ClassNotFoundException {
        socket = new Socket("127.0.0.1", 6769);
        out = new ObjectOutputStream(socket.getOutputStream());
        out.writeByte(CMSG_LOGIN);
        out.flush();
        out.writeObject(name);
        out.flush();
        out.writeObject(pass);
        out.flush();

        in = new ObjectInputStream(socket.getInputStream());
        opcode = in.readByte();
        if (opcode == SMSG_LOGIN_SUCCESS) {
            clientName = name;
            this.friendList = new FriendListUI(clientName, loginFrame, out, roomList);
            this.loginFrame = loginFrame;
            new Connection(in, friendList, name, friendList).start();
            this.roomList = roomList;
            return 1;
        }else if (opcode == SMSG_MULTI_LOGIN) {
            return 2;
        }else{
            return 0;
        }

    }

    @Override
    public void run() {
        while (run) {
            try {
                
                opcode = in.readByte();
                switch (opcode) {
                    case SMSG_SEND_ROOMLIST:
                        roomList.clearRoomList();
                        boolean run2 = true;
                        while (run2) {
                            roomData = (packet_roomData) in.readObject();
                            if(roomData.getName()==null)
                            {
                                run2 = false;
                                this.friendList.updateList();
                            }else
                            {
                              room room = new room(roomData.getName(), roomData.getDescription(), roomData.isIsPrivate());
                              roomList.insertRoomToTheFirst(room);
                            }
                        }
                        break;

                    case SMSG_JOINROOM_SUCCESS:
                        chatUI = new chatUI(this.roomName, friendList, out, clientName);
                        break;

                    case SMSG_LEAVEROOM_SUCCESS:
                        friendList = new FriendListUI(clientName, loginFrame, out, roomList);
                        break;

                    case MSG_SENDGROUPMESSAGE:
                        clientMSG = (packet_clientMessage) in.readObject();
                        display(clientMSG.getTime(),clientMSG.getName(),clientMSG.getMessage());
                        System.out.println(clientMSG.getName() + ": " + clientMSG.getMessage());
                        break;
                        
                    case SMSG_CREATEROOM_SUCCESS:
                        createRoomFrame.respondBox(1);
                        break;
                        
                    case SMSG_MULTI_CREATEROOM:
                        createRoomFrame.respondBox(0);
                        break;

                    default:
                        System.out.print("unknown opcode");
                        break;

                }
            } catch (Exception e) {
                e.printStackTrace();
                LoginUI frame = new LoginUI(clientName, roomList);
                frame.setSize(300, 500);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setLocationRelativeTo(null);
                frame.setResizable(false);
                frame.setVisible(true);
                run = false;
            }
        }
    }

    public void leaveRoom() throws IOException {
        out.writeByte(CMSG_LEAVEROOM);
        out.flush();
    }

    public void logout() throws IOException {
        out.writeByte(CMSG_LOGOUT);
        out.flush();
    }

    public void display(String aaa, String bbb, String ccc) {
        ccc = ccc.replaceAll("\n", "\n     ");
        chatUI.txtOutput.append(String.format("[%s]%s says:\n", aaa,bbb));
        chatUI.txtOutput.append(String.format("     %s\n", ccc));
    }

    public boolean check(packet_roomData rd) {
        System.out.println(rd.getName());
        if (rd==null) {
            roomData = null;
        } else {
            roomData = rd;
            roomList.insertRoomToTheFirst(rd.getName(), rd.getDescription(), rd.isIsPrivate());
        }
        return (rd!=null);
    }

    public void joinRoom(ObjectOutputStream os, String roomname) throws IOException {
        os.writeByte(CMSG_JOINROOM);
        os.flush();
        os.writeObject(roomname);
        os.flush();
    }
    
    public void joinSuccess(String name) throws IOException {
        this.roomName = name;
    }
    
    public void createroom(){
        createRoomFrame = new CreateRoomUI(this.out);
        createRoomFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        createRoomFrame.setLocationRelativeTo(null);
        createRoomFrame.setResizable(false);
        createRoomFrame.setVisible(true);
    }
}

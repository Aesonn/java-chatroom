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
import javax.swing.JList;
import packets.*;

/**
 *
 * @author User
 */
public class Connection extends Thread implements Opcode {

    private Socket socket;
    private ObjectInputStream in;
    private static ObjectOutputStream out;
    packet_systemMessage systemMSG;
    JFrame loginFrame;
    private packet_systemMessage request;
    private packet_clientMessage clientMSG;
    private packet_roomData rd;
    private FriendListUI lol;
    private String aa;
    private chatUI chatUI;
    static String clientName;
    static boolean run;

    public Connection(ObjectInputStream in, FriendListUI lol, String name, JFrame jf) {
        this.in = in;
        this.lol = lol;
        this.clientName = name;
        this.loginFrame = jf;
        run = true;
    }

    public Connection() {
    }

    public boolean connect(String name, String pass, JFrame loginFrame) throws UnknownHostException, IOException, ClassNotFoundException {
        socket = new Socket("127.0.0.1", 6769);
        out = new ObjectOutputStream(socket.getOutputStream());
        out.writeObject(new packets.packet_request(CMSG_LOGIN, name));
        out.flush();
        out.writeObject(new packets.packet_loginData(name, pass));
        out.flush();

        in = new ObjectInputStream(socket.getInputStream());
        systemMSG = (packet_systemMessage) in.readObject();
        if (systemMSG.getOpcode() == SMSG_LOGIN_SUCCESS) {
            System.out.println(systemMSG.getOpcode());
            System.out.println(SMSG_LOGIN_SUCCESS);
            lol = new FriendListUI("yaya", "yoyo", loginFrame, out);
            this.loginFrame = loginFrame;
            new Connection(in, lol, name, lol).start();
            return true;
        } else {
            System.out.println(systemMSG.getOpcode());
            System.out.println(SMSG_LOGIN_SUCCESS);
            return false;
        }

    }

    @Override
    public void run() {
        while (run) {
            try {
                System.out.println("here0");
                request = (packet_systemMessage) in.readObject();
                System.out.println(request.getOpcode());
                switch (request.getOpcode()) {
                    case SMSG_SEND_ROOMLIST:
                        System.out.println("here1");
                        while (!check((packet_roomData) in.readObject())) {
                            System.out.println("here2");
                            System.out.println(aa);
                            lol.updateList(aa);
                        }
                        break;

                    case SMSG_JOINROOM_SUCCESS:
                        System.out.println("herro0o");
                        chatUI = new chatUI("o0o", lol, out, clientName);
                        break;

                    case SMSG_LEAVEROOM_SUCCESS:
                        System.out.println("leave room sucess");
                        lol = new FriendListUI("yaya", "yoyo", loginFrame, out);
                        break;

                    case CMSG_SENDGROUPMESSAGE:
                        clientMSG = (packet_clientMessage) in.readObject();
                        display(clientMSG.getName() + ": " + clientMSG.getMessage());
                        System.out.println(clientMSG.getName() + ": " + clientMSG.getMessage());
                        break;

                    default:
                        System.out.print("unknown opcode");
                        break;

                }
            } catch (Exception e) {
                e.printStackTrace();
                LoginUI frame = new LoginUI(clientName);
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
        out.writeObject(new packet_request(CMSG_LEAVEROOM, clientName));
    }

    public void logout() throws IOException {
        out.writeObject(new packet_request(CMSG_LOGOUT, clientName));
    }

    public void display(String aaa) {
        chatUI.txtOutput.append(aaa);
    }

    public boolean check(packet_roomData a) {
        if (a.isEmpty()) {
            aa = null;
        } else {
            aa = a.getName();
        }
        return a.isEmpty();
    }

    public void joinRoom(ObjectOutputStream os, String roomname) throws IOException {
        os.writeObject(new packet_request(CMSG_JOINROOM, clientName));
        os.writeObject(new packet_roomData(roomname, null));
    }
}

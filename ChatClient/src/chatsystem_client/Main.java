package chatsystem_client;

import javax.swing.JFrame;

public class Main {

    static String clientName;
    static roomList roomList;

    public static void main(String[] args) {

        roomList = new roomList();
        LoginUI frame = new LoginUI(clientName, roomList);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);
    }
}

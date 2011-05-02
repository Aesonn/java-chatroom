package chatsystem_client;

import javax.swing.JFrame;
import javax.swing.UIManager;

public class Main {

    static String clientName;
    static roomList roomList;

    public static void main(String[] args) {

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
        }
            roomList = new roomList();
            LoginUI frame = new LoginUI(clientName, roomList);
            //answerUI frame = new answerUI("yoyo", null);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLocationRelativeTo(null);
            frame.setResizable(false);
            frame.setVisible(true);
    }
}

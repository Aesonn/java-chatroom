/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chatsystem_client;

import javax.swing.JFrame;

/**
 *
 * @author User
 */
public class Main {

    static String clientName;

    public static void main(String[] args) {

        LoginUI frame = new LoginUI(clientName);
        frame.setSize(300, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);
    }
}

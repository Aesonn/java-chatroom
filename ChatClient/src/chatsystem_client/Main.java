/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chatsystem_client;

import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import javax.swing.JFrame;

/**
 *
 * @author User
 */
public class Main {

    static String clientName;

    public static void main(String[] args) {

        LoginUI frame = new LoginUI(clientName);
        frame.setUndecorated(true);        
        frame.setSize(300, 500);
        frame.setBackground(new Color (0, 0, 0, 100));
        frame.setShape(new Ellipse2D.Float (0, 0, 300,500));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);
        
    }
}

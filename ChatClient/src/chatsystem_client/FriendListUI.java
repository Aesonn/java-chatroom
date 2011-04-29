package chatsystem_client;

import java.awt.Color;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.io.IOException;
import java.io.ObjectOutputStream;
import packets.Opcode;

public class FriendListUI extends JFrame implements Opcode, ActionListener
{
    JList friendList;
    JScrollPane friendListPane;
    JButton buttonLogout, buttonCreate;
    
    JLabel lblName;
    private ObjectOutputStream out;
    JFrame loginFrame;
    static roomList roomList;
    DefaultListModel model;
    
    public FriendListUI(String name, JFrame loginFrame, ObjectOutputStream out, roomList roomList)
    {
        this.roomList = roomList;
        this.out = out;
        setTitle("Room List");
        setLayout(null);

        this.loginFrame = loginFrame;

        buttonLogout = new JButton("Logout");
        buttonCreate= new JButton("Create");
        lblName = new JLabel(name);
        
        friendList = new JList();
        friendListPane = new JScrollPane(friendList);
        
        add(lblName);
        add(friendListPane);
        add(buttonLogout);
        add(buttonCreate);
        
        buttonLogout.setBounds(25,430,90,25);
        buttonCreate.setBounds(155,430,90,25);
        lblName.setBounds(15, 30, 245, 25);
        lblName.setForeground(new Color (250, 0, 0, 255));
        friendListPane.setBounds(10, 80, 245, 340);
        

        
        setUndecorated(true);        
        setBackground(new Color (10, 10, 10, 230));
        setShape(new Ellipse2D.Float (0, 0, 300,500));
        
        setSize(270, 500);
        setResizable(false);
        setLocationRelativeTo(loginFrame);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
        loginFrame.dispose();
        friendList.addMouseListener(mouseListener);
        buttonLogout.addActionListener(this);
        buttonCreate.addActionListener(this);
    }

    public void updateList(){
        model = new  DefaultListModel();
        roomList.showList(model);
        friendList.setModel(model);
        System.out.println(friendList.isVisible()+" -- "+friendList.isShowing());
        //friendList.repaint();
    }

    MouseListener mouseListener = new MouseAdapter()
    {
        @Override
        public void mouseClicked(MouseEvent e)
        {
            System.out.println(e.getClickCount());
            if (e.getClickCount() == 2)
            {
                int index = friendList.locationToIndex(e.getPoint());
                room r =(room)model.getElementAt(index);
                System.out.println(r);
                try {
                    new Connection().joinRoom(out,r.getName());
                    new Connection().joinSuccess(r.getName());
                } catch (IOException ex) {
                    Logger.getLogger(FriendListUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    };

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(buttonLogout))
        {
            try {
                new Connection().logout();
                dispose();
                return;
            } catch (Exception ex) {
                Logger.getLogger(FriendListUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (e.getSource().equals(buttonCreate))
        {
            try {
                new Connection().createroom();
                return;
            } catch (Exception ex) {
                Logger.getLogger(FriendListUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}

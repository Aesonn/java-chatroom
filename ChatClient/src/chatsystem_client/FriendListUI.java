package chatsystem_client;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import java.awt.event.*;
import java.io.ObjectOutputStream;
import packets.Opcode;

public class FriendListUI extends JFrame implements Opcode
{
    JComboBox cStatus;
    JList friendList;
    JScrollPane friendListPane;
    
    JLabel lblName;
    JLabel lblPSM;
    JLabel lblFList;
    private ObjectOutputStream out;
    JFrame loginFrame;
    DefaultListModel model;
    
    String[] status = {"Online", "Away", "Busy", "Appear Offline", "Logout"};
    
    public FriendListUI(String name, String psm, JFrame loginFrame, ObjectOutputStream out)
    {
        this.out = out;
        setTitle(String.format("%s - %s", name, psm));
        setLayout(null);

        this.loginFrame = loginFrame;

        lblName = new JLabel(name);
        lblPSM = new JLabel(psm);
        
        cStatus = new JComboBox(status);
        
        model = new DefaultListModel();
        friendList = new JList(model);
        friendListPane = new JScrollPane(friendList);
        
        add(lblName);
        add(lblPSM);
        add(cStatus);
        add(friendListPane);
        
        lblName.setBounds(15, 10, 245, 25);
        lblPSM.setBounds(15, 35, 245, 25);
        cStatus.setBounds(10, 65, 245, 25);
        friendListPane.setBounds(10, 100, 245, 360);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(loginFrame);
        setSize(270, 500);
        setResizable(false);
        setVisible(true);
        loginFrame.dispose();
        friendList.addMouseListener(mouseListener);
        addWindowListener(winListener);
    }

    public void updateList(String name){
        model.addElement(name);
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
                String c =(String)model.getElementAt(index);
                System.out.println(c);
                try {
                    new Connection().joinRoom(out,c);
                } catch (IOException ex) {
                    Logger.getLogger(FriendListUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    };

    WindowListener winListener = new WindowAdapter()
    {
        @Override
        public void windowClosing(WindowEvent e)
        {
            try {
                System.out.println("logout!!!!!!!!!!!!");
                new Connection().logout();
                //out.writeObject(new packet_request(CMSG_LEAVEROOM, "D073003012B"));
                //ContactListUI.chatWindow.removeElement((ChatUI)e.getSource());
            } catch (Exception ex) {
                Logger.getLogger(chatUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    };
}

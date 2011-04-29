package chatsystem_client;

import java.awt.Color;
import javax.swing.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

public class LoginUI extends JFrame implements ActionListener
{
    static String clientName;
    static roomList roomList;
    Connection connection;
    
    //UI components
    JButton buttonLogin, buttonExit, buttonReg;
    JLabel labelUsername, labelPassword;
    JTextField textUsername;
    JPasswordField textPassword;

    public LoginUI(String name, roomList roomList)
    {

        clientName = name;
        this.roomList   = roomList;
        connection = new Connection();
        
        setTitle("Login Form");
        setLayout(null);
        setUndecorated(true);        
        setSize(300, 500);
        setBackground(new Color (10, 10, 10, 230));

        buttonLogin   = new JButton("Login");
        buttonReg     = new JButton("Register");
        buttonExit    = new JButton("Exit");
        labelUsername = new JLabel("Username");
        labelPassword = new JLabel("Password");
        textUsername  = new JTextField();
        textPassword  = new JPasswordField();

        add(buttonLogin);
        add(buttonReg);
        add(buttonExit);
        add(labelUsername);
        add(labelPassword);
        add(textUsername);
        add(textPassword);

        buttonLogin.setBounds(100,320,90,25);
        buttonReg.setBounds(30,370,90,25);
        buttonExit.setBounds(160, 370, 90, 25);
        labelUsername.setBounds(40,150,100,25);
        labelPassword.setBounds(40,220,100,25);
        textUsername.setBounds(40,170,220,25);
        textPassword.setBounds(40,240,220,25);
        
        Border b = new LineBorder(new Color (250, 100, 100, 0));
        
        labelUsername.setForeground(new Color (250, 0, 0, 255));
        labelPassword.setForeground(new Color (250, 0, 0, 255));

        
        
        buttonLogin.addActionListener(this);
        buttonReg.addActionListener(this);
        buttonExit.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource().equals(buttonLogin))
        {
            if (textUsername.getText().equals(""))
            {
                JOptionPane.showMessageDialog(this, "Please enter your username.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (textPassword.getPassword().length == 0)
            {
                JOptionPane.showMessageDialog(this, "Please enter your password.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try
            {
                int respondCode = connection.connect(textUsername.getText(), new String(textPassword.getPassword()), this, roomList);
                if(respondCode==0)
                {
                    JOptionPane.showMessageDialog(this, "Login failed.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                else if(respondCode==2)
                {
                    JOptionPane.showMessageDialog(this, "Your already logon at another computer.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;   
                }
            }catch(Exception ex){}
        }

        if (e.getSource().equals(buttonExit))
        {
            System.exit(0);
        }

        if (e.getSource().equals(buttonReg))
        {
                    //new chatUI("o0o1");
        }
    }

    public void loginSuccess(){
        new FriendListUI(clientName, this, null, roomList);
    }
}

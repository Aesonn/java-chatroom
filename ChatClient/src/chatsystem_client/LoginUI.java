package chatsystem_client;

import java.awt.Color;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

public class LoginUI extends JFrame implements ActionListener
{
    JButton btnLogin, btnExit, btnReg;
    JTextField txtUsername;
    JPasswordField txtPassword;
    JLabel lblUsername,lblPassword;
    Connection connection = new Connection();
    static String clientName;

    public LoginUI(String name)
    {
        this.clientName = name;
        setTitle("Login Form");
        setLayout(null);

        lblUsername = new JLabel("Username");
        lblPassword = new JLabel("Password");
        txtUsername = new JTextField();
        txtPassword = new JPasswordField();
        btnLogin = new JButton("Login");
        btnReg = new JButton("Register");
        btnExit = new JButton("Exit");

        add(lblPassword);
        add(lblUsername);
        add(txtPassword);
        add(txtUsername);
        add(btnLogin);
        add(btnReg);
        add(btnExit);

        lblPassword.setBounds(110,220,100,25);
        lblUsername.setBounds(110,150,100,25);
        txtPassword.setBounds(40,240,220,25);
        txtUsername.setBounds(40,170,220,25);
        btnLogin.setBounds(120,320,60,25);
        btnReg.setBounds(50,370,60,25);
        btnExit.setBounds(180, 370, 60, 25);
        
        Border thickBorder = new LineBorder(new Color (250, 100, 100, 0));
        
        btnLogin.setBackground(new Color (250, 100, 100, 200));
        btnLogin.setBorder(thickBorder);
        btnReg.setBackground(new Color (250, 100, 100, 200));
        btnReg.setBorder(thickBorder);
        btnExit.setBackground(new Color (250, 100, 100, 200));
        btnExit.setBorder(thickBorder);

        
        
        btnLogin.addActionListener(this);
        btnReg.addActionListener(this);
        btnExit.addActionListener(this);
    }

    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource().equals(btnLogin))
        {
            if (txtUsername.getText().equals(""))
            {
                JOptionPane.showMessageDialog(this, "Please enter your username.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (txtPassword.getPassword().length == 0)
            {
                JOptionPane.showMessageDialog(this, "Please enter your password.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try
            {
                connection.connect(txtUsername.getText(), new String(txtPassword.getPassword()), this);
            }catch(Exception ex){}
        }

        if (e.getSource().equals(btnExit))
        {
            System.exit(0);
        }

        if (e.getSource().equals(btnReg))
        {
                    //new chatUI("o0o1");
        }
    }

    public void loginSuccess(){
        new FriendListUI("yaya", "yoyo", this, null);
    }
}

package chatsystem_client;

import java.awt.Color;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import java.awt.event.*;
import java.io.ObjectOutputStream;
import packets.Opcode;
import packets.packet_newRoom;

//import Core.*;
//import UI.ChatUI;

public class CreateRoomUI extends JFrame implements Opcode, ActionListener
{
    JLabel labelRoomname, labelDescription, labelQuestion, labelAnswer;
    JTextField textRoomname, textDescription, textQuestion, textAnswer;
    JButton buttonCreate, buttonBack;
    
    private ObjectOutputStream out;

    public CreateRoomUI(ObjectOutputStream out)
    {
        this.out = out;
        
        setTitle("Create Room");
        setLayout(null);
        setUndecorated(true);        
        setSize(300, 500);
        setBackground(new Color (10, 10, 10, 230));
        
        labelRoomname    = new JLabel("Room Name");
        labelDescription = new JLabel("Description");
        labelQuestion    = new JLabel("Question");
        labelAnswer      = new JLabel("Answer");
        textRoomname     = new JTextField();
        textDescription  = new JTextField();
        textQuestion     = new JTextField();
        textAnswer       = new JTextField();
        buttonCreate     = new JButton("Create");
        buttonBack       = new JButton("Back");
        
        add(labelRoomname);
        add(labelDescription);
        add(labelQuestion);
        add(labelAnswer);
        add(textRoomname);
        add(textDescription);
        add(textQuestion);
        add(textAnswer);
        add(buttonCreate);
        add(buttonBack);
        
        labelRoomname.setBounds(40,80,100,25);
        labelDescription.setBounds(40,150,100,25);
        labelQuestion.setBounds(40,220,100,25);
        labelAnswer.setBounds(40,290,100,25);
        textRoomname.setBounds(40,100,220,25);
        textDescription.setBounds(40,170,220,25);
        textQuestion.setBounds(40,240,220,25);
        textAnswer.setBounds(40,310,220,25);
        buttonCreate.setBounds(50, 400, 80, 25);
        buttonBack.setBounds(170, 400, 80, 25);
        
        labelRoomname.setForeground(new Color (250, 0, 0, 255));
        labelDescription.setForeground(new Color (250, 0, 0, 255));
        labelQuestion.setForeground(new Color (250, 0, 0, 255));
        labelAnswer.setForeground(new Color (250, 0, 0, 255));

        buttonCreate.addActionListener(this);
        buttonBack.addActionListener(this);
        addWindowListener(winListener);
    }

        WindowListener winListener = new WindowAdapter()
    {
        @Override
        public void windowDeactivated(WindowEvent e)
        {
            try {
                dispose();
            } catch (Exception ex) {
                Logger.getLogger(chatUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    };
    
    @Override
    public void actionPerformed(ActionEvent e) {        
        if (e.getSource().equals(buttonBack))
        {
            dispose();
            return;
        }
        
        if(e.getSource().equals(buttonCreate))
        {
            try {
                out.writeByte(CMSG_CREATEROOM);
                out.flush();
                if(!textRoomname.getText().trim().equals("")&&!textDescription.getText().trim().equals("")&&!textQuestion.getText().trim().equals("")&&!textAnswer.getText().trim().equals(""))
                {
                    out.writeObject(new packet_newRoom(textRoomname.getText().trim(), textDescription.getText().trim(), textQuestion.getText().trim(), textAnswer.getText().trim()));
                    out.flush();     
                }
                else if((!textRoomname.getText().trim().equals("")&&!textDescription.getText().trim().equals("")))
                {
                    out.writeObject(new packet_newRoom(textRoomname.getText().trim(), textDescription.getText().trim()));
                    out.flush();     
                }
                else
                {
                    if(textRoomname.getText().trim().equals(""))
                    {
                        JOptionPane.showMessageDialog(this, "Please enter roomname.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    else if(textDescription.getText().trim().equals(""))
                    {
                        JOptionPane.showMessageDialog(this, "Please enter description.", "Error", JOptionPane.ERROR_MESSAGE);   
                    }
                    else if(textQuestion.getText().trim().equals(""))
                    {
                        JOptionPane.showMessageDialog(this, "Please enter question", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    else if(textAnswer.getText().trim().equals(""))
                    {
                        JOptionPane.showMessageDialog(this, "Please enter answer.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } catch (Exception ex) {
                Logger.getLogger(CreateRoomUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void respondBox(int i){
        if(i==1)
        {
             JOptionPane.showMessageDialog(this, "sucess create room.", "", JOptionPane.INFORMATION_MESSAGE);
        }
        else
        {
            JOptionPane.showMessageDialog(this, "The room u wish to create already exist.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
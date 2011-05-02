package chatsystem_client;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import java.awt.event.*;
import java.io.ObjectOutputStream;
import packets.Opcode;
import packets.packet_clientMessage;
import packets.packet_newRoom;


public class chatUI extends JFrame implements Opcode
{
    //Contact c;

    JScrollPane paneOutput;
    JScrollPane paneInput;

    JTextArea txtOutput;
    JTextArea txtInput;

    JFrame loginFrame;
    ObjectOutputStream out;
    static String clientName;

    public chatUI(String name, JFrame lf, ObjectOutputStream ot, String cN)
    {
        //this.c = c;
        this.clientName = cN;
        this.loginFrame = lf;
        this.out = ot;

        setTitle(name);
        setLayout(null);

        txtOutput = new JTextArea();
        txtInput = new JTextArea();

        paneOutput = new JScrollPane(txtOutput, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS ,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        paneInput = new JScrollPane(txtInput, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED ,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        add(paneOutput);
        add(paneInput);

        paneOutput.setBounds(10, 10, 350, 300);
        paneInput.setBounds(10, 315, 350, 100);

        txtOutput.setEditable(false);

        setSize(375, 450);
        setResizable(false);
        setLocationRelativeTo(loginFrame);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
        lf.dispose();



        addWindowListener(winListener);
        txtInput.addKeyListener(keyListener);
    }

    WindowListener winListener = new WindowAdapter()
    {
        @Override
        public void windowClosing(WindowEvent e)
        {
            try {
                System.out.println("ChatUI");
                new Connection().leaveRoom();
                //out.writeObject(new packet_newRoom(CMSG_LEAVEROOM, "D073003012B"));
                //ContactListUI.chatWindow.removeElement((ChatUI)e.getSource());
            } catch (Exception ex) {
                Logger.getLogger(chatUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    };

    KeyListener keyListener = new KeyAdapter()
    {
        public void keyReleased(KeyEvent e)
        {
            if (e.getKeyCode() == e.VK_ENTER)
            {
                if (e.isShiftDown())
                {
                    txtInput.append("\n");
                    return;
                }

                if (txtInput.getText().trim().equals(""))
                {
                    txtInput.setText("");
                    return;
                }
                
                if (txtInput.getText().trim().equals("/who"))
                {
                    try {
                        new Connection().sendRequestsameRoomClient(out);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    txtInput.setText("");
                }
                else if(txtInput.getText().startsWith("/w "))
                {
                    try
                    {
                    String[] whisper = txtInput.getText().substring(3).split(" " , 2);
                    if(!whisper[0].isEmpty())
                    {

                        System.out.println(whisper[0]);   
                    }
                    if(!whisper[1].isEmpty())
                    {
                        System.out.println(whisper[1]);
                    }
                    if(whisper[0].equals(clientName))
                    {
                            cannotWhisperSelf();
                    }
                    else
                    {
                    out.writeByte(MSG_WHISPERMESSAGE);
                    out.flush();
                    out.writeObject(whisper[0]);
                    out.flush();
                    out.writeObject(new packet_clientMessage(clientName,whisper[0].trim()));
                    out.flush();
                    }
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }catch(ArrayIndexOutOfBoundsException ex)
                    {
                        wrongCommand();
                    }
                    txtInput.setText("");
                }
                else
                {
                try {
                    out.writeByte(MSG_SENDGROUPMESSAGE);
                    out.flush();
                    out.writeObject(new packet_clientMessage(clientName,txtInput.getText().trim()));
                    out.flush();
                } catch (IOException ex) {
                    Logger.getLogger(chatUI.class.getName()).log(Level.SEVERE, null, ex);
                }
                txtInput.setText("");
            }
            }
                
        }
    };
    
    public void wrongCommand(){
        JOptionPane.showMessageDialog(this, "Wrong command...", "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    public void wrongTarget(){
        JOptionPane.showMessageDialog(this, "This room don't have this people.", "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    public void cannotWhisperSelf(){
        JOptionPane.showMessageDialog(this, "You cannot whisper your self.", "Error", JOptionPane.ERROR_MESSAGE);
    }
}
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chatsystem_client;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import packets.Opcode;

/**
 *
 * @author User
 */
public class answerUI extends JFrame implements Opcode, ActionListener{
    
    JLabel labelQuestion;
    JTextField textAnswer;
    JButton buttonOK, buttonBack;
    
    private ObjectOutputStream out;
    
    public answerUI(String ques, ObjectOutputStream out){
        
        this.out = out;
        
        setTitle("Answer the question first.");
        setLayout(null);
        setUndecorated(true);        
        setSize(500, 300);
        setBackground(new Color (10, 10, 10, 230));
        
        labelQuestion = new JLabel(ques);
        textAnswer    = new JTextField("Your Answer here");
        buttonOK      = new JButton("OK");
        buttonBack      = new JButton("Back");
        
        add(labelQuestion);
        add(textAnswer);
        add(buttonOK);
        add(buttonBack);
        
        labelQuestion.setBounds(50,80,460,25);
        textAnswer.setBounds(50,140,400,25);
        buttonOK.setBounds(100,200,100,25);
        buttonBack.setBounds(280,200,100,25);
        
        labelQuestion.setForeground(new Color (250, 0, 0, 255));
        buttonOK.addActionListener(this);
        buttonBack.addActionListener(this);
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
        
        addWindowListener(winListener);
    }

    WindowListener winListener = new WindowAdapter()
    {
        @Override
        public void windowDeactivated(WindowEvent e)
        {
            try {
                new Connection().cancelQuestion(out);
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
            try {
                new Connection().cancelQuestion(out);
                dispose();
                return;
            } catch (IOException ex) {
                Logger.getLogger(answerUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        if (e.getSource().equals(buttonOK))
        {
            if(textAnswer.getText().equals("Your Answer here")||textAnswer.getText().equals(""))
            {
                this.removeWindowListener(winListener);
                JOptionPane.showMessageDialog(this, "Please enter answer first before submit.", "Error", JOptionPane.ERROR_MESSAGE);
                this.addWindowListener(winListener);
                return;
            }
            else
            {
            try {
                new Connection().sendAnswer(out, textAnswer.getText());
                dispose();
                return;
            } catch (IOException ex) {
                Logger.getLogger(answerUI.class.getName()).log(Level.SEVERE, null, ex);
            }
            }
        }
    }
    
    public void failToJoinRoom(){
        JOptionPane.showMessageDialog(this, "wrong answer", "Error", JOptionPane.ERROR_MESSAGE);
    }
}
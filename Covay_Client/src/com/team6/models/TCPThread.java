/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.team6.models;

import com.team6.common.Message;
import com.team6.common.User;
import com.team6.views.HomeForm;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Quoc Hung
 */
public class TCPThread extends Thread{
    private HomeForm homeForm;
    private User user;
    private String enemyName;
    private Socket socket;

    public TCPThread(HomeForm homeForm, User user, Socket socket) {
        this.homeForm = homeForm;
        this.user = user;
        this.socket = socket;
        
        this.homeForm.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e){
                try {
                    socket.close();
                } catch (IOException ex) {
                    Logger.getLogger(TCPThread.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
    
    @Override
    public void run(){
        
        ObjectOutputStream oos = null;
        ObjectInputStream ois = null;
        try {
            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());
            
            while (true){
                Object o = ois.readObject();
                
                if (o instanceof Message){
                    Message message = (Message) o;
                    
                    if (message.getTitle().equals("CheckOnline")) {
                        oos.writeObject(new Message("ON", null));
                        continue;
                    }
                    
                    if (message.getTitle().equals("Invite")){
                        if (JOptionPane.showConfirmDialog(homeForm, 
                                message.getContent().toString()+
                                " muốn so tài với cậu, cậu có đồng ý không?")>0){
                            oos.writeObject(new Message("AC", null));
                            System.out.println("Playing...");
                        } else oos.writeObject(new Message("DE", null));
                    }
                }
            }      
            
        } catch (IOException ex) {
            Logger.getLogger(TCPThread.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(TCPThread.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                oos.close();
                ois.close();
            } catch (IOException ex) {
                Logger.getLogger(TCPThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        try {
            socket.close();
        } catch (IOException ex) {
            Logger.getLogger(TCPThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

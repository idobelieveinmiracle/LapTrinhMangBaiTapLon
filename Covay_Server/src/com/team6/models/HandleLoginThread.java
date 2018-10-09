/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.team6.models;

import com.team6.common.Message;
import com.team6.common.User;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Quoc Hung
 */
public class HandleLoginThread extends Thread{
    private HashMap<User, Socket> mapOnlineUsers;
    private ServerSocket tcpServerSocket;

    public HandleLoginThread(HashMap<User, Socket> mapOnlineUsers, ServerSocket tcpServerSocket) {
        this.mapOnlineUsers = mapOnlineUsers;
        this.tcpServerSocket = tcpServerSocket;
    }

    @Override
    public void run() {
        
        System.out.println("Started handling TCP login");
        
        while (true) {            
            try {
                Socket clientSocket = tcpServerSocket.accept();
                
                ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());
                
                Object o = ois.readObject();
                if (!(o instanceof Message))continue;
                
                Message message = (Message) o;
                
                System.out.println(message.getTitle());
                
                if (message.getTitle().equals("Login")){
                    mapOnlineUsers.put((User) message.getContent(), clientSocket);
                }
                
            } catch (IOException ex) {
                Logger.getLogger(HandleLoginThread.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(HandleLoginThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}

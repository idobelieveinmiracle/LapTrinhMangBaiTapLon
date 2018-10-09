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
    private HashMap<User, IODataCollection> mapOnlineUsers;
    private ServerSocket tcpServerSocket;

    public HandleLoginThread(HashMap<User, IODataCollection> mapOnlineUsers, ServerSocket tcpServerSocket) {
        this.mapOnlineUsers = mapOnlineUsers;
        this.tcpServerSocket = tcpServerSocket;
    }

    @Override
    public void run() {
        
        System.out.println("Started handling TCP login");
        
        while (true) {   
            ObjectInputStream ois = null;
            ObjectOutputStream oos = null;
            try {
                Socket clientSocket = tcpServerSocket.accept();
                System.out.println("Client connected");
                
                oos = new ObjectOutputStream(clientSocket.getOutputStream());
                ois = new ObjectInputStream(clientSocket.getInputStream());
                System.out.println("Got input stream");
                
                Object o = ois.readObject();
                
                if (!(o instanceof Message))continue;
                
                Message message = (Message) o;
                
                System.out.println(message.getTitle());
                
                if (message.getTitle().equals("Login")){
                    mapOnlineUsers.put((User) message.getContent(), new IODataCollection(clientSocket, oos, ois));
                }
                
            } catch (IOException ex) {
                System.out.println("Client disconnected");
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(HandleLoginThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}

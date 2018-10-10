/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.team6.controllers;

import com.team6.common.Message;
import com.team6.common.RMIInterface;
import com.team6.common.User;
import com.team6.models.TCPThread;
import com.team6.views.HomeForm;
import com.team6.views.LoginForm;
import com.team6.views.SignUpForm;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


/**
 *
 * @author Quoc Hung
 */
public class ClientMainController {
    private LoginForm loginForm;
    private SignUpForm signupForm;
    private HomeForm homeForm;
    
    private User user;
    
    private RMIInterface rmiServer;
    private Registry registry;
    
    private String rmiHostName;
    private int rmiPort;
    private String rmiService;
    
    private int tcpPort;
    private Socket tcpSocket;
    
    private TCPThread listeningThread;
    
    public ClientMainController() {
        initVariables();
        initRMI();
        initForms();       
    }
    
    private void initForms(){
        loginForm = new LoginForm();
        signupForm = new SignUpForm();
        homeForm = new HomeForm();
        
        signupForm.addBtnSignUpFormActionListener(new MainControllerActionListener());
        loginForm.addBtnsActionListener(new MainControllerActionListener());
        homeForm.addHomeFormActionListener(new MainControllerActionListener());
        
        loginForm.setVisible(true);
        
        homeForm.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                try {
                    rmiServer.logOut(user.getUsername());
                    tcpSocket.close();
                } catch (RemoteException ex) {
                    Logger.getLogger(ClientMainController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(ClientMainController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
    
    private void initRMI(){
        try{
            registry = LocateRegistry.getRegistry(rmiHostName, rmiPort);
            rmiServer = (RMIInterface) registry.lookup(rmiService);
        } catch (RemoteException ex) {
            Logger.getLogger(ClientMainController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NotBoundException ex) {
            Logger.getLogger(ClientMainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void initVariables(){
        String fileName = "data.json";
        
        JSONParser parser = new JSONParser();
        
        try {
            Object obj = parser.parse(new FileReader(fileName));
            
            JSONObject jsonObject = (JSONObject) obj;
            
            tcpPort = Integer.parseInt((String) jsonObject.get("tcpPort"));
            rmiPort =Integer.valueOf((String)jsonObject.get("rmiPort"));
            rmiHostName = (String) jsonObject.get("rmiAddress");
            rmiService = (String) jsonObject.get("rmiService");
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
    }
    
    public void setUser(User user){
        this.user = user;
    }
    
    public User getUser(){
        return this.user;
    }
    
    public void startListeningThread(){
        try {
            tcpSocket = new Socket("localhost", tcpPort);
            
            ObjectOutputStream oos = new ObjectOutputStream(tcpSocket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(tcpSocket.getInputStream());
            
            oos.writeObject(new Message("Login", user));
            
        } catch (IOException ex) {
            Logger.getLogger(ClientMainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private class MainControllerActionListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e){
            if(e.getSource().equals(loginForm.getBtnLogin())){
                String username = loginForm.getUsername();
                String password = loginForm.getPassword();
                if (!username.equals("") && !password.equals("")){
                    try {
                        user = rmiServer.checkLogin(username, password);
                        
                        if (user != null){
                            loginForm.setVisible(false);
                            homeForm.setUser(user);
                            homeForm.setListUsers(rmiServer.getAllOnlineUsers(), user.getUsername());
                            homeForm.setVisible(true);
                            
                            tcpSocket = new Socket("localhost", tcpPort);
                            
                            System.out.println("Socket: "+tcpSocket.getLocalAddress());
                            new TCPThread(homeForm, user, tcpSocket).start();
                        } else JOptionPane.showMessageDialog(loginForm, "Cậu đã nhập sai tên đăng nhập hoặc mật khẩu");
                    } catch (RemoteException ex) {
                        JOptionPane.showMessageDialog(loginForm, "Tớ cảm thấy có gì đó sai sai");
                        Logger.getLogger(ClientMainController.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(ClientMainController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                        
                } else {
                    JOptionPane.showMessageDialog(loginForm, "Cậu phải nhập đủ tên đăng nhập và mật khẩu");
                }
            }
            
            if(e.getSource().equals(loginForm.getBtnSignup())){
                loginForm.setVisible(false);
                signupForm.setVisible(true);
            }
            
            if(e.getSource().equals(signupForm.getBtnBack())){
                signupForm.setVisible(false);
                loginForm.setVisible(true);
            }
            
            if(e.getSource().equals(signupForm.getBtnSignup())){
                if (signupForm.validInformation()){
                    User user = signupForm.getUser();
                    
                    try {
                        if (rmiServer.insertUser(user)){
                            JOptionPane.showMessageDialog(signupForm, "Cậu đã đăng ký thành công. Giờ hãy đăng nhập");
                            signupForm.setVisible(false);
                            loginForm.setVisible(true);
                        } else JOptionPane.showMessageDialog(signupForm, "Trùng tên đăng nhập rồi cậu thử tên đăng nhập khách xem");
                    } catch (RemoteException ex) {
                        JOptionPane.showMessageDialog(loginForm, "Cậu đã nhập đúng tên đăng nhập hoặc mật khẩu");                        
                        Logger.getLogger(ClientMainController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }else {
                    JOptionPane.showMessageDialog(signupForm, "Cậu nhập sai mật khẩu hoặc cậu nhập thiếu thông tin nào đó");
                }
            }
            
            if (e.getSource().equals(homeForm.getBtnResetList())){
                try {
                    homeForm.setListUsers(rmiServer.getAllOnlineUsers(), getUser().getUsername());
                } catch (RemoteException ex) {
                    JOptionPane.showMessageDialog(homeForm, "Có gì đó sai sai -.-");
                    Logger.getLogger(ClientMainController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            if (e.getSource().equals(homeForm.getBtnInvite())){
                String username = homeForm.getUsernameSelected();
                
                if(username == null) JOptionPane.showMessageDialog(homeForm, "Cậu chưa chọn đối thủ, hãy chọn một người");
                else {
                    try {
                        if(rmiServer.invite(user.getUsername(),username)){
                            
                        } else JOptionPane.showMessageDialog(homeForm, "Bạn ý không đồng ý chơi với cậu :((");
                    } catch (RemoteException ex) {
                        Logger.getLogger(ClientMainController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
        
    }
    
}

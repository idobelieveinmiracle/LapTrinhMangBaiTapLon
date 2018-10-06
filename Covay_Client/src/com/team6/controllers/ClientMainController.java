/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.team6.controllers;

import com.team6.common.RMIInterface;
import com.team6.common.User;
import com.team6.views.LoginForm;
import com.team6.views.SignUpForm;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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
    
    private RMIInterface rmiServer;
    private Registry registry;
    
    private String rmiHostName;
    private int rmiPort;
    private String rmiService;
    
    public ClientMainController() {
        initVariables();
        initRMI();
        initForms();       
    }
    
    private void initForms(){
        loginForm = new LoginForm();
        signupForm = new SignUpForm();
        
        signupForm.addBtnSignUpFormActionListener(new MainControllerActionListener());
        loginForm.addBtnsActionListener(new MainControllerActionListener());
        
        loginForm.setVisible(true);
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
    
    private class MainControllerActionListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e){
            if(e.getSource().equals(loginForm.getBtnLogin())){
                String username = loginForm.getUsername();
                String password = loginForm.getPassword();
                if (!username.equals("") &&
                        !password.equals(""))
                {
                    try {
                        User user = rmiServer.checkLogin(username, password);
                        
                        if (user != null){
                            JOptionPane.showMessageDialog(loginForm, "Cậu đã nhập đúng tên đăng nhập hoặc mật khẩu");
                        } else JOptionPane.showMessageDialog(loginForm, "Cậu đã nhập sai tên đăng nhập hoặc mật khẩu");
                    } catch (RemoteException ex) {
                        JOptionPane.showMessageDialog(loginForm, "Tớ cảm thấy có gì đó sai sai");
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
        }
        
    }
    
}

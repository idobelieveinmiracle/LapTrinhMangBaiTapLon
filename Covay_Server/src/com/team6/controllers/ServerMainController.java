/*
   mo change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.team6.controllers;

import com.team6.common.RMIInterface;
import com.team6.common.User;
import com.team6.models.UserData;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


/**
 *
 * @author Quoc Hung
 */
public class ServerMainController extends UnicastRemoteObject implements RMIInterface{
    
    private int rmiPort;
    private String rmiAddress;
    private Registry registry;
    private String rmiService;
    private Connection conn;
    
    private int udpPort;    
    
    private int tcpPort;
    private ServerSocket tcpServerSocket;
    
    private HashSet<UserData> setOnlineUsers;
    
    public ServerMainController() throws RemoteException{ 
        setOnlineUsers = new HashSet<>();
        initVariables();
        createRegistry();
        initConnection();
    }
    
    private void initVariables(){
        String fileName = "data.json";
        
        JSONParser parser = new JSONParser();
        
        try {
            Object obj = parser.parse(new FileReader(fileName));
            
            JSONObject jsonObject = (JSONObject) obj;
            
            tcpPort = Integer.parseInt((String) jsonObject.get("tcpPort"));
            rmiPort =Integer.valueOf((String)jsonObject.get("rmiPort"));
            rmiAddress = (String) jsonObject.get("rmiAddress");
            rmiService = (String) jsonObject.get("rmiService");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ServerMainController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ServerMainController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(ServerMainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void createRegistry() throws RemoteException{
        registry = LocateRegistry.createRegistry(rmiPort);
        registry.rebind(rmiService, this);
    }
    
    private void initConnection(){
        String dbUrl = "jdbc:mysql://localhost:3306/covaydb";
        String dbClass = "com.mysql.jdbc.Driver";
        
        try {
            Class.forName(dbClass);
            
            conn = DriverManager.getConnection(dbUrl, "root", "");
            System.out.println("Database connected");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ServerMainController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ServerMainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public User getUser(String string) throws RemoteException {
        User user = null;
        String sql = "SELECT * FROM tbl_user WHERE username=?";
        
        try {
            PreparedStatement stm = conn.prepareStatement(sql);
            stm.setString(1, string);
            ResultSet rs = stm.executeQuery();
            
            if (rs.next()){
                user = new User();
                
                user.setUsername(rs.getString("username"));
                user.setName(rs.getString("name"));
                user.setScore(rs.getInt("score"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServerMainController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return user;
    }

    @Override
    public boolean setUser(User user) throws RemoteException {
        String sql = "UPDATE tbl_user SET password=?, name=?, score=? WHERE username=?";
        
        try {
            PreparedStatement stm = conn.prepareStatement(sql);
            
            stm.setString(1, user.getPassword());
            stm.setString(2, user.getName());
            stm.setInt(3, user.getScore());
            stm.setString(4, user.getUsername());
            
            return stm.executeUpdate() > 0;
        } catch (SQLException ex) {
            Logger.getLogger(ServerMainController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return false;
    }

    @Override
    public boolean insertUser(User user) throws RemoteException {
        System.out.println(user);
        System.out.println(user.getUsername()+" "+user.getPassword()+" "+user.getName()+" "+user.getScore());
        String sql = "INSERT INTO tbl_user(username, password, name, score) "+
                "VALUES(?, ?, ?, ?)";
        
        try {
            PreparedStatement stm = conn.prepareStatement(sql);
            
            stm.setString(1, user.getUsername());
            stm.setString(2, user.getPassword());
            stm.setString(3, user.getName());
            stm.setInt(4, user.getScore());
            
            
            return stm.executeUpdate() > 0;
        } catch (SQLException ex) {
            Logger.getLogger(ServerMainController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    @Override
    public User checkLogin(String username, String password) throws RemoteException {
        User user = null;
        String sql = "SELECT * FROM tbl_user WHERE username=? AND password=?";
        
        try {
            PreparedStatement stm = conn.prepareStatement(sql);
            
            stm.setString(1, username);
            stm.setString(2, password);
            
            ResultSet rs = stm.executeQuery();
            
            if (rs.next()){
                user = new User();
                user.setUsername(username);
                user.setPassword(password);
                user.setName(rs.getString(3));
                user.setScore(rs.getInt(4));
                if (setOnlineUsers.add(new UserData(user, null))) return user;
                else return null;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServerMainController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return user;
    }

    @Override
    public void logOut(String username) throws RemoteException {
        setOnlineUsers.remove(new UserData(new User(username, "", "", 0, 0),null));
    }

    @Override
    public ArrayList<User> getAllOnlineUsers() throws RemoteException {
        ArrayList<User> list = new ArrayList<>();
        
        Iterator<UserData> iter = setOnlineUsers.iterator();
        
        while (iter.hasNext()){
            list.add(iter.next().getUserInfo());
        }
        
        return list;
    }
    
}

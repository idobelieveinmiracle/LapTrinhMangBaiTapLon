/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.team6.common;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 *
 * @author Quoc Hung
 */
public interface RMIInterface extends Remote{
    public User getUser(String username) throws RemoteException;
    public boolean setUser(User user) throws RemoteException;
    public boolean insertUser(User user) throws RemoteException;
    public User checkLogin(String username, String password) throws RemoteException; 
    public void logOut(String username) throws RemoteException;
    public ArrayList<User> getAllOnlineUsers() throws RemoteException;
    public boolean invite(String inviter, String username) throws RemoteException;
    public boolean changePassword(String username, String password, String oldPassword) throws RemoteException;
    public void changeDisplayName(String username, String name) throws RemoteException;
    public boolean rematch(String username1, String username2) throws RemoteException;
}

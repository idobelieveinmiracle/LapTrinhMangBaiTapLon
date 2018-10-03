/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.team6.views;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

/**
 *
 * @author Quoc Hung
 */
public class HomeForm extends JFrame{
    private JPanel userInfoContent;
    private JLabel txtName;
    private JLabel txtScore;
    private JLabel txtUsername;
    
    private JPanel listUsersContent;
    private JScrollPane scrListUsers;
    private JTable tblListUsers;
    
    private GridBagConstraints c;

    public HomeForm() {
        super("Home");
        
        
    }
    
    private void setUserInfoContent(){
        userInfoContent = new JPanel(new GridBagLayout());
        txtName = new JLabel();
        txtScore = new JLabel();
        txtUsername = new JLabel();
    }   
    
}

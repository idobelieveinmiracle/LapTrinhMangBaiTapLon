/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.team6.views;

import com.team6.common.User;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Quoc Hung
 */
public class HomeForm extends JFrame{
    private JPanel userInfoContent;
    private JLabel txtName;
    private JLabel txtScore;
    private JLabel txtUsername;
    
    private JScrollPane scrListUsers;
    private JTable tblListUsers;
    private DefaultTableModel mdlListUsers;
    
    private JPanel buttonContent;
    private JButton btnInvite;
    
    private GridBagConstraints c;

    public HomeForm() {
        super("Home");
        
        setUserInfoContent();
        setListUsersContent();
        setButtonContent();
        
        JPanel content = (JPanel) this.getContentPane();
        
        content.setLayout(new GridBagLayout());
        
        c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(3, 3, 3, 3);
        c.gridx = 0;
        c.gridy = 0;
        content.add(userInfoContent, c);
        
        c.gridy = 1;
        content.add(scrListUsers, c);
        
        c.gridy = 2;
        content.add(buttonContent, c);
        
        this.pack();
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(3);
    }
    
    private void setUserInfoContent(){
        userInfoContent = new JPanel(new GridBagLayout());
        txtName = new JLabel();
        txtScore = new JLabel();
        txtUsername = new JLabel();
                
        c = new GridBagConstraints();
        
        c.insets = new Insets(3, 3, 3, 3);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.CENTER;
        c.ipadx = 70;
        c.ipady = 10;
        
        c.gridx = 0;
        c.gridy = 0;
        userInfoContent.add(new JLabel("Username:"),c);
        
        c.gridx = 0;
        c.gridy = 1;
        userInfoContent.add(new JLabel("Player name:"),c);
        
        c.gridx = 0;
        c.gridy = 2;
        userInfoContent.add(new JLabel("Player score:"),c);
        
        c.ipadx = 200;
        c.gridx = 1;
        c.gridy = 0;
        userInfoContent.add(txtUsername,c);
        
        c.gridx = 1;
        c.gridy = 1;
        userInfoContent.add(txtName,c);
        
        c.gridx = 1;
        c.gridy = 2;
        userInfoContent.add(txtScore,c);        
        
    }   
    
    private void setListUsersContent(){
        String[] columnNames = {"Username", "Name", "Score"};
        Object[][] data = {};
        
        tblListUsers = new JTable(){
            public boolean isCellEditable(int nRow, int nCol) {
                return false;
            }
        };
//        tblRoomList.setPreferredScrollableViewportSize(new Dimension(500, 300));
        tblListUsers.setFillsViewportHeight(true);
        
        scrListUsers = new JScrollPane(tblListUsers); 
        mdlListUsers = (DefaultTableModel) tblListUsers.getModel();
        
        mdlListUsers.setColumnIdentifiers(columnNames);   
        tblListUsers.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }
    
    private void setButtonContent(){
        buttonContent = new JPanel(new GridBagLayout());
        
        btnInvite = new JButton("Invite");
        
        c = new GridBagConstraints();
        
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(3, 3, 3, 3);
        
        buttonContent.add(btnInvite, c);
    }
    
    public void setUser(User user){
        System.out.println(user.getName()+" "+user.getScore());
        txtName.setText(user.getName());
        txtUsername.setText(user.getUsername());
        txtScore.setText(String.valueOf(user.getScore()));
    }
    
    public void setListUsers(ArrayList<User> listUsers){
        int rowCount = tblListUsers.getRowCount();
        
        for (int i = 0; i < rowCount; i ++) mdlListUsers.removeRow(0);
        
        for(User r: listUsers){
            mdlListUsers.addRow(new Object[]{
                r.getUsername(), r.getName(), r.getScore()
            });
        }
    }
    
}

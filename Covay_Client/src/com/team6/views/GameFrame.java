/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.team6.views;

import com.team6.common.ChessBoard;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author Quoc Hung
 */
public class GameFrame extends JFrame{
    private GamePanel gamePanel;
    private ChessBoard chessBoard;
    
    public GameFrame() {
        chessBoard = new ChessBoard();
        gamePanel = new GamePanel(chessBoard);
        
        JPanel content = (JPanel) this.getContentPane();
        
        content.setLayout(new GridBagLayout());
        
        GridBagConstraints c = new GridBagConstraints();
        
        c.fill = GridBagConstraints.BOTH;
        
        c.ipadx = 390;
        c.ipady = 390;
        
        c.gridx = 0;
        c.gridy = 0;
        
        content.add(gamePanel, c);
        this.addMouseListener(gamePanel);
        this.pack();
        this.setDefaultCloseOperation(3);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
    }
    
    
}

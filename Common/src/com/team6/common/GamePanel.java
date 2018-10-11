/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.team6.common;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JPanel;

/**
 *
 * @author Quoc Hung
 */
public class GamePanel extends JPanel implements MouseListener{
    
    private ChessBoard chessBoard;
    private int z = 40;
    
    public GamePanel(ChessBoard chessBoard){
        this.setSize(400, 400);
        this.chessBoard = chessBoard;
        
    }

    public ChessBoard getChessBoard() {
        return chessBoard;
    }

    public void setChessBoard(ChessBoard chessBoard) {
        this.chessBoard = chessBoard;
        repaint();
    }
    
    @Override
    public void paint(Graphics g){
        Graphics2D g2 = (Graphics2D) g;
        
        
        
        
        g2.setColor(new Color(185, 196, 22));
        g2.fillRect(0, 0, 400, 400);
        
        g2.setColor(Color.BLACK);
        g2.drawString("Black: "+chessBoard.getBlackScore(), 10, 15);
        g2.drawString("White: "+chessBoard.getWhiteScore(), 70, 15);
        
        g2.setColor(Color.BLACK);
        for (int i = 1; i <= 9; i++){
            g2.drawLine(z*i, z,z*i , z*9);
            g2.drawLine(z, z*i, z*9, z*i);
        }
        
        for (int i = 0; i < 9; i ++){
            for (int j = 0; j < 9; j++){
                if (chessBoard.getBoard(i, j) == ChessBoard.BLACK){
                    g2.setColor(Color.BLACK);
                    g2.fillOval(z*j+25, z*i+25, 30, 30);
                }
                if (chessBoard.getBoard(i, j) == ChessBoard.WHITE){
                    g2.setColor(Color.WHITE);
                    g2.fillOval(z*j+25, z*i+25, 30, 30);
                }
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int x = e.getX()-10;
        int y = e.getY()-37;
        
        int a = -1, b = -1;
        
        if ( x < 30 || y < 30) return;
        
        for (int i = 50; i <375; i += z){
            if (i >= x){
                if (  x >= i-z/2) a = i/z -1;
                break;
            }
        }
        
        for (int i = 50; i <375; i += z){
            if (i >= y){
                if (  y >= i-z/2) b = i/z - 1;
                break;
            }
        }
        
        System.out.println("Clicked");
        if(chessBoard.move(b, a)) { 
            this.repaint();
            System.out.println(x+" "+ y+" "+a+" "+b);
//            chessBoard.printBoard();
        }
    }
    

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
    
    
    
}

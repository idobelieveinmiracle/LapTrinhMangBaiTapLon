/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.team6.models;

import com.team6.common.ChessBoard;
import com.team6.common.Message;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Quoc Hung
 */
public class MatchHandlingThread extends Thread{
    private IODataCollection player2IO;
    private IODataCollection player1IO;

    public MatchHandlingThread(IODataCollection player1, IODataCollection player2) {
        this.player2IO = player2;
        this.player1IO = player1;
    }

    @Override
    public void run() {
        ObjectOutputStream player1OutputStream = player1IO.getOos();
        ObjectOutputStream player2OutputStream = player2IO.getOos();
        ObjectInputStream player1InputStream = player1IO.getOis();
        ObjectInputStream player2InputStream = player2IO.getOis();
        
        try {
            player1OutputStream.writeObject(new Message("OpenMatch", "player1"));
            player2OutputStream.writeObject(new Message("OpenMatch", "player2"));
        } catch (IOException ex) {
            Logger.getLogger(MatchHandlingThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        while (true){
            try {
                Object o = player1InputStream.readObject();
                
                if (o instanceof Message){
                    Message mess1 = (Message) o;
                    
                    if (mess1.getTitle().equals("Move")){
                        System.out.println("Received Move Request of player 1");
                        ChessBoard chessBoard = (ChessBoard) mess1.getContent();
                        player2OutputStream.writeObject(new Message("Move", chessBoard));
                        System.out.println("Sent Move response to player 2");
                    }
                    else if (mess1.getTitle().equals("Result")){
                        
                    }
                }
                
                Object o2 = player2InputStream.readObject();
                
                if (o2 instanceof Message){
                    Message mess2 = (Message) o2;
                    
                    if (mess2.getTitle().equals("Move")){
                        System.out.println("Received Move Request of player 2");
                        ChessBoard chessBoard = (ChessBoard) mess2.getContent();
                        player1OutputStream.writeObject(new Message("Move", chessBoard));
                        System.out.println("Sent Move response to player 1");
                    }
                    else if (mess2.getTitle().equals("Result")){
                        
                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(MatchHandlingThread.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(MatchHandlingThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    
}

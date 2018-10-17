/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.team6.models;

import com.team6.common.ChessBoard;
import com.team6.common.Message;
import com.team6.common.User;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.SocketException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Quoc Hung
 */
public class MatchHandlingThread extends Thread{
    private HashMap<String, IODataCollection> mapOnlineUsers;
    private IODataCollection player2IO;
    private IODataCollection player1IO;
    private String username1;
    private String username2;
    private Connection conn;

    public MatchHandlingThread(IODataCollection player2IO, IODataCollection player1IO, String username1, String username2, Connection conn, HashMap<String, IODataCollection> mapOnlineUser) {
        this.mapOnlineUsers = mapOnlineUser;
        this.player2IO = player2IO;
        this.player1IO = player1IO;
        this.username1 = username1;
        this.username2 = username2;
        this.conn = conn;
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
                Object o = null;
                try{
                    o = player1InputStream.readObject();
                } catch (SocketException e){
                    updateResult(username2);
//                    player1OutputStream.writeObject(new Message("Result", "LOSE"));
                    player2OutputStream.writeObject(new Message("Result", "WIN"));
                    break;
                }
                
                if (o instanceof Message){
                    Message mess1 = (Message) o;
                    
                    if (mess1.getTitle().equals("Move")){
                        System.out.println("Received Move Request of player 1");
                        ChessBoard chessBoard = (ChessBoard) mess1.getContent();
                        
                        if (chessBoard.getCountTurn()== 81){
                            if (chessBoard.getBlackScore()>40){
                                updateResult(username2);
                                player1OutputStream.writeObject(new Message("Result", "LOSE"));
                                player2OutputStream.writeObject(new Message("Result", "WIN"));
                            } else {
                                updateResult(username1);                                
                                player2OutputStream.writeObject(new Message("Result", "LOSE"));
                                player1OutputStream.writeObject(new Message("Result", "WIN"));
                            }
                            break;
                        }
                        
                        player2OutputStream.writeObject(new Message("Move", chessBoard));
                        System.out.println("Sent Move response to player 2");
                    }
                    else if (mess1.getTitle().equals("Crash")){
                        player1OutputStream.writeObject(new Message("Result", "LOSE"));
                        player2OutputStream.writeObject(new Message("Result", "WIN"));
                        break;
                    }
                }
                
                try{
                    o = player2InputStream.readObject();
                } catch (SocketException e) {
                    updateResult(username1);                                
//                    player2OutputStream.writeObject(new Message("Result", "LOSE"));
                    player1OutputStream.writeObject(new Message("Result", "WIN"));
                    break;
                }
                
                if (o instanceof Message){
                    Message mess2 = (Message) o;
                    
                    if (mess2.getTitle().equals("Move")){
                        System.out.println("Received Move Request of player 2");
                        ChessBoard chessBoard = (ChessBoard) mess2.getContent();
                        
                        if (chessBoard.getCountTurn()== 81){
                            if (chessBoard.getBlackScore()>40){
                                updateResult(username2);
                                player1OutputStream.writeObject(new Message("Result", "LOSE"));
                                player2OutputStream.writeObject(new Message("Result", "WIN"));
                            } else {
                                updateResult(username1);                                
                                player2OutputStream.writeObject(new Message("Result", "LOSE"));
                                player1OutputStream.writeObject(new Message("Result", "WIN"));
                            }
                            break;
                        }
                        
                        player1OutputStream.writeObject(new Message("Move", chessBoard));
                        System.out.println("Sent Move response to player 1");
                    }
                    else if (mess2.getTitle().equals("Crash")){
                        player1OutputStream.writeObject(new Message("Result", "WIN"));
                        player2OutputStream.writeObject(new Message("Result", "LOSE"));      
                        break;
                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(MatchHandlingThread.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(MatchHandlingThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        mapOnlineUsers.get(username1).getUser().setStatus(User.FREE);
        mapOnlineUsers.get(username2).getUser().setStatus(User.FREE);
    }
    
    private void updateResult(String winnerUsername){
        String sqlSelect, sqlExec;
        PreparedStatement stm = null;
        ResultSet rs = null;
        int score = 0;
        sqlSelect = "SELECT score FROM tbl_user WHERE username=?";
        sqlExec = "UPDATE tbl_user SET score=? WHERE username=?";
        
        try {
            stm = conn.prepareStatement(sqlSelect);
            stm.setString(1, winnerUsername);
            rs = stm.executeQuery();
            rs.next();
            score = rs.getInt("score");
            
            stm = conn.prepareStatement(sqlExec);
            stm.setInt(1, score+1);
            stm.setString(2, winnerUsername);
            stm.executeUpdate();
            
        } catch (SQLException ex) {
            Logger.getLogger(MatchHandlingThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.team6.models;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

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
        ObjectOutputStream player1OutputSteam = player1IO.getOos();
        ObjectOutputStream player2OutputStram = player2IO.getOos();
        ObjectInputStream player1InputStream = player2IO.getOis();
        ObjectInputStream player2InputStream = player2IO.getOis();
        
        
    }
    
    
}

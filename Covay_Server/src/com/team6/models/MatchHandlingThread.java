/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.team6.models;

/**
 *
 * @author Quoc Hung
 */
public class MatchHandlingThread extends Thread{
    private IODataCollection player2;
    private IODataCollection player1;

    public MatchHandlingThread(IODataCollection player2, IODataCollection player1) {
        this.player2 = player2;
        this.player1 = player1;
    }

    @Override
    public void run() {
        
    }
    
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.team6.common;

import java.io.Serializable;

/**
 *
 * @author Quoc Hung
 */
public class User implements Serializable{
    
    public static final int ON = 1;
    public static final int OFF = 0;
    
    private String username;
    private String password;
    private String name;
    private int score;
    private int status;

   
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public User(String username, String password, String name, int score, int status) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.score = score;
        this.status = status;
    }

    public User() {
        this.username = "";
        this.password = "";
        this.name = "";
        this.score = 1500;
        this.status = 0;
    }

    @Override
    public boolean equals(Object obj) {
        
        User user = (User) obj;
        
        return this.username.equals(user.getUsername());
    }

    @Override
    public int hashCode() {
        return this.username.hashCode();
    }
    
    
    
}

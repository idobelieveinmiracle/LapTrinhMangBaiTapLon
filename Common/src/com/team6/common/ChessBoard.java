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
public class ChessBoard implements Serializable{
    
    public static final int BLACK = -1;
    public static final int WHITE = 1;
    
    //  So vi tri cac quan tren ban co, 1 la quan den, 2 la quan trang, 0 la chua co quan
    private int[][] board;
    private int blackScore;
    private int whiteScore;
    private int countTurn;
    private int side;
    
    //  Luot di = 1 thi ben trang di, = 2 thi ben den di
    private int turn;

    public int[][] getBoard() {
        return board;
    }

    public ChessBoard() {
        this.board = new int[9][9];
        this.turn = WHITE;
        this.countTurn = 0;
        this.blackScore = 0;
        this.whiteScore = 0;
        this.side = 0;
    }

    public int getSide() {
        return side;
    }

    public void setSide(int side) {
        this.side = side;
    }

    public int getBlackScore() {
        return blackScore;
    }

    public void setBlackScore(int blackScore) {
        this.blackScore = blackScore;
    }

    public int getWhiteScore() {
        return whiteScore;
    }

    public void setWhiteScore(int whiteScore) {
        this.whiteScore = whiteScore;
    }

    public void setBoard(int[][] board) {
        this.board = board;
    }
    
    public int getBoard(int x, int y){
        return this.board[x][y];
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public ChessBoard(int[][] board, int turn) {
        this.board = board;
        this.turn = turn;
        this.countTurn = 0;
    }
    
    public boolean move(int x, int y){
        if (! (x>=0 && x <= 8 && y >= 0 && y <= 8) ) return false;
        if ( this.board[x][y] != 0) return false;
        this.board[x][y] = this.turn;
        if (turn == BLACK) blackScore ++;
        else whiteScore ++;
        checkMove(new Position(x, y));
        getScore();
        this.turn = - this.turn;
        this.countTurn ++;
        System.out.println("Moved");
        return true;
    }
    
    public void checkMove(Position p){
       int x = p.x;
       int y = p.y;
       int side = turn;
       
       if (x-1 >= 0){
           if (board[x-1][y] == -side){
               for (int i = 2; x-i >= 0; i++ ){
                   if (board[x-i][y] == side){
                       for (int j = 1; j < i; j++){
                           board[x-j][y] = side;
                       }
                       break;
                   } 
                   if (board[x-i][y] == 0) break;
               }
           }
       }
       
       if (y-1 >= 0){
           if (board[x][y-1] == -side){
//               System.out.println("eat");
               for (int i = 2; y-i >= 0; i++ ){
                   if (board[x][y-i] == side){
                       for (int j = 1; j < i; j++){
                           board[x][y-j] = side;
                       }
                       break;
                   } 
                   if (board[x][y-i] == 0) break;
               }
           }
       }
       
       if (x+1 < 9){
           if (board[x+1][y] == -side){
//               System.out.println("eat");
               for (int i = 2; x+i < 9; i++ ){
                   if (board[x+i][y] == side){
                       for (int j = 1; j < i; j++){
                           board[x+j][y] = side;
                       }
                       break;
                   } 
                   if (board[x+i][y] == 0) break;
               }
           }
       }
       
       if (y+1 < 9){
           if (board[x][y+1] == -side){
               System.out.println("eat");
               for (int i = 2; y+i < 9; i++ ){
                   if (board[x][y+i] == side){
                       for (int j = 1; j < i; j++){
                           board[x][y+j] = side;
                       }
                       break;
                   } 
                   if (board[x][y+i] == 0) break;
               }
           }
       }
       
       if (x+1 < 9 && y+1 < 9){
           if (board[x+1][y+1] == -side){
//               System.out.println("eat");
               for (int i = 2; x+i < 9 && y+i < 9; i++){
                   if (board[x+i][y+i] == side){
//                       System.out.println("get: (x,y)=="+(x+i)+" "+(y+i));
                       for (int j = 1; j < i; j++){
                           board[x+j][y+j] = side;
                       }
                       break;
                   }
                   if (board[x+i][y+i] == 0){
                       break;
                   }
               }
           }
       }
       
       if (x+1 < 9 && y-1 >= 0){
           if (board[x+1][y-1] == -side){
//               System.out.println("eat");
               for (int i = 2; x+i < 9 && y-i >= 0; i++){
                   if (board[x+i][y-i] == side){
//                       System.out.println("get: (x,y)=="+(x+i)+" "+(y-i));
                       for (int j = 1; j < i; j++){
                           board[x+j][y-j] = side;
                       }
                       break;
                   }
                   if (board[x+i][y-i] == 0){
                       break;
                   }
               }
           }
       }
       
       if (x-1 >= 0 && y-1 >= 0){
           if (board[x-1][y-1] == -side){
//               System.out.println("eat");
               for (int i = 2; x-i >=0 && y-i >= 0; i++){
                   if (board[x-i][y-i] == side){
//                       System.out.println("get: (x,y)=="+(x-i)+" "+(y-i));
                       for (int j = 1; j < i; j++){
                           board[x-j][y-j] = side;
                       }
                       break;
                   }
                   if (board[x-i][y-i] == 0){
                       break;
                   }
               }
           }
       }
       
       if (x-1 >= 0 && y+1 < 9){
           if (board[x-1][y+1] == -side){
//               System.out.println("eat");
               for (int i = 2; x-i >=0 && y+i < 9; i++){
                   if (board[x-i][y+i] == side){
//                       System.out.println("get: (x,y)=="+(x-i)+" "+(y-i));
                       for (int j = 1; j < i; j++){
                           board[x-j][y+j] = side;
                       }
                       break;
                   }
                   if (board[x-i][y+i] == 0){
                       break;
                   }
               }
           }
       }
    }
    
    public void getScore(){
        blackScore = 0;
        whiteScore = 0;
        for (int i = 0; i < 9; i++){
            for (int j = 0; j < 9; j++){
                if (board[i][j] == BLACK) blackScore++;
                else if (board[i][j] == WHITE) whiteScore++;
            }
        }
    }
    
    public void printBoard(){
        for (int i = 0; i < 9; i++){
            for (int j = 0; j < 9; j++) System.out.print(board[i][j]+" ");
            System.out.println();
        }
    }
    
}

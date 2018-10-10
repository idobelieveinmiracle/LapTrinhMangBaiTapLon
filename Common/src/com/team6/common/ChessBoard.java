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
    
    public static final int BLACK = 1;
    public static final int WHITE = -1;
    
    //  So vi tri cac quan tren ban co, 1 la quan den, 2 la quan trang, 0 la chua co quan
    private int[][] board;
    private int blackScore;
    private int whiteScore;
    private int countTurn;
    
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
        this.turn = - this.turn;
        this.countTurn ++;
        return true;
    }
    
    public void checkMove(Position p){
        int side = this.board[p.x][p.y];
        if (p.y+1<9){
            if (this.board[p.x][p.y+1] == -side){
                for (int i = p.y+2; i < 9; i++){
                    if (board[p.x][i] == side) {
                        for (int j = p.y+1; j < i; j++){
                            board[p.x][j] = side;
                        }
                        if (side == BLACK){
                            blackScore += i - p.y;
                            whiteScore -= i - p.y;
                        } else {
                            blackScore -= i - p.y;
                            whiteScore += i - p.y;                            
                        }
                        break;
                    }
                    if (board[p.x][i] == 0){
                        break;
                    }                    
                }
            }
        }
        if (p.y-1>0){
            if (this.board[p.x][p.y-1] == -side){
                for (int i = p.y-2; i >= 0; i--){
                    if (board[p.x][i] == side) {
                        for (int j = p.y-1; j > i; j--){
                            board[p.x][j] = side;
                        }
                        if (side == BLACK){
                            blackScore += p.y-i;
                            whiteScore -= p.y-i;
                        } else {
                            blackScore -= p.y-i;
                            whiteScore += p.y-i;                            
                        }
                        break;
                    }
                    if (board[p.x][i] == 0){
                        break;
                    }                    
                }
            }
        }
        if (p.x+1<9){
            if (this.board[p.x+1][p.y] == -side){
                for (int i = p.x+2; i < 9; i++){
                    if (board[i][p.y] == side) {
                        for (int j = p.x+1; j < i; j++){
                            board[j][p.y] = side;
                        }
                        break;
                    }
                    if (board[i][p.y] == 0){
                        break;
                    }                    
                }
            }
        }
        if (p.x-1>0){
            if (this.board[p.x-1][p.y] == -side){
                for (int i = p.x-2; i > 0; i--){
                    if (board[i][p.y] == side) {
                        for (int j = p.x-1; j > i; j--){
                            board[j][p.y] = side;
                        }
                        break;
                    }
                    if (board[i][p.y] == 0){
                        break;
                    }                    
                }
            }
        }
        
        if (p.x+1<9 && p.y+1<9){
            if (board[p.x+1][p.y+1] == -side){
                for (int i = 2; i < 9 - p.x && i < 9 - p.y; i++){
                    if (board[p.x+i][p.y+i] == side) {
                        for (int j = 0; j < i; j++){
                            board[p.x+j][p.y+j] = side;
                        }
                        break;
                    }
                    if (board[p.x+i][p.y+i] == 0){
                        break;
                    }  
                }
            }
        }
        if (p.x-1>0 && p.y+1<9){
            if (board[p.x-1][p.y+1] == -side){
                for (int i = 2; p.x-i>0 && p.y+i<9; i++){
                    if (board[p.x-i][p.y+i] == side) {
                        for (int j = 0; j < i; j++){
                            board[p.x-j][p.y+j] = side;
                        }
                        break;
                    }
                    if (board[p.x-i][p.y+i] == 0){
                        break;
                    }  
                }
            }
        }
        if (p.x-1>0 && p.y-1>0){
            if (board[p.x-1][p.y-1] == -side){
                for (int i = 2; p.x-i>0 && p.y-i>0; i++){
                    if (board[p.x-i][p.y-i] == side) {
                        for (int j = 0; j < i; j++){
                            board[p.x-j][p.y-j] = side;
                        }
                        break;
                    }
                    if (board[p.x-i][p.y-i] == 0){
                        break;
                    }  
                }
            }
        }
        if (p.x+1<9 && p.y-1>0){
            if (board[p.x+1][p.y-1] == -side){
                for (int i = 2; p.x+i<9 && p.y-i>0; i++){
                    if (board[p.x+i][p.y-i] == side) {
                        for (int j = 0; j < i; j++){
                            board[p.x+j][p.y-j] = side;
                        }
                        break;
                    }
                    if (board[p.x+i][p.y-i] == 0){
                        break;
                    }  
                }
            }
        }
        System.out.println("count="+countTurn);
        
        if(countTurn == 80){
            getScore();
            System.out.println("Finished:");
            System.out.println("Black: "+blackScore);
            System.out.println("White: "+whiteScore);
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

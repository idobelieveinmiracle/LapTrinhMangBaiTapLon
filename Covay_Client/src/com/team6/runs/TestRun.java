/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.team6.runs;

import com.team6.common.User;
import com.team6.views.HomeForm;
import com.team6.views.SignUpForm;
import java.util.ArrayList;
import java.util.HashSet;

/**
 *
 * @author Quoc Hung
 */
public class TestRun {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        User user = new User("abc", "asasdc", "aasdsasd", 1, 1);
        User user1 = new User("abc", "asasdddddd", "assdasaaaad", 1, 1);
        
        HashSet<User> set = new HashSet<>();
        ArrayList<User> list = new ArrayList<>();
        
        set.add(user);
        set.add(user1);
        list.addAll(set);
        
        for (int i = 0; i < list.size(); i ++){
            
            System.out.println(list.get(i).getName());
        }
        
    }
    
}

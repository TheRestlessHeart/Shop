package com.example.demo.util;

import java.util.Random;

/**
 * Created by yancychan on 17-8-29.
 */
public class Token {

    public static String generateToken(String username){

        StringBuilder token = new StringBuilder();

        token.append(username);
        for (int i=0; i<15; i++){
            Random number = new Random();
            token.append(number.nextInt(9));
        }
        return token.toString();
    }
}

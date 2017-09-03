package com.example.demo.util;

/**
 * Created by yancychan on 17-9-2.
 */

//用来判断书本类别是否存在
public class Type {

    static String[] allType = {"Java", "Python", "算法", "人工智能", "JavaScript"};

    public static boolean isInclude(String type){
        Boolean flag = false;
        for (int i=0; i<allType.length; i++){
            if (allType[i].equals(type)){
                flag = true;
            }
        }
        return flag;
    }
}

package com.yyy.common.utils;


import java.util.ArrayList;
import java.util.List;


public class MyStringUtils extends org.springframework.util.StringUtils {

    public static int[] indicesOfSprcifiedChar(String origin, Character character){
        if (origin == null || character == null){
            return null;
        }

        char[] origin_chars = origin.toCharArray();
        List<Integer> indices = new ArrayList<Integer>();
        for (int i = 0; i <= origin_chars.length-1; i++){
            if (origin_chars[i] == character.charValue()){
                indices.add(i);
            }
        }
        int[] refactor = new int[indices.size()];
        for (int i = 0; i <= indices.size()-1; i++){
            refactor[i] = indices.get(i);
        }
        return refactor;
    }
}

package com.pzwm.netty.utils;

public class MyStringUtil {

    public static String addLenth(String str){
        str = String.format("%08d", str.length()) + str;
        return str;
    }
}

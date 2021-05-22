package com.mrshiehx.mschatroom.server.virtual_terminal.utils;

public class StringUtils {
    private StringUtils(){}
    public static boolean isEmpty(CharSequence charSequence){
        return charSequence==null||charSequence.length()==0;
    }

    public static boolean isNotEmpty(CharSequence charSequence){
        return !isEmpty(charSequence);
    }
}

package com.mrshiehx.mschatroom.server.virtual_terminal.utils;

public class CommandStringUtils {
    private CommandStringUtils(){}
    public static String format(String command){
        if(StringUtils.isNotEmpty(command)) {
            String newString = command.trim();
            char[] chars = newString.toCharArray();
            for (int i = 0; i < chars.length; i++) {
                if(chars[i]==' '){

                }
            }
        }
    }

    private static char[]exe(char[]chars){

    }
}

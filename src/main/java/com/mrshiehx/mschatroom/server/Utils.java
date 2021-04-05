package com.mrshiehx.mschatroom.server;

import java.io.*;

public class Utils {
    public static String getString(File target) throws IOException {
        return getString(new FileInputStream(target));
    }

    public static String getString(InputStream inputStream) throws IOException{
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
        BufferedReader reader = new BufferedReader(inputStreamReader);
        StringBuffer sb = new StringBuffer();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
            sb.append("\n");
        }
        inputStream.close();
        inputStreamReader.close();
        reader.close();
        return sb.toString();
    }

    public static boolean isEmpty(CharSequence charSequence){
        return charSequence==null||charSequence.length()==0;
    }
}

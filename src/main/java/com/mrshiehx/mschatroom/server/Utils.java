package com.mrshiehx.mschatroom.server;

import java.io.*;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

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

    public static byte[] toByteArray(File file) throws IOException {
        FileChannel fc = new RandomAccessFile(file, "r").getChannel();
        MappedByteBuffer byteBuffer = fc.map(FileChannel.MapMode.READ_ONLY, 0,
                fc.size()).load();
        //System.out.println(byteBuffer.isLoaded());
        byte[] result = new byte[(int) fc.size()];
        if (byteBuffer.remaining() > 0) {
            // System.out.println("remain");
            byteBuffer.get(result, 0, byteBuffer.remaining());
        }
        fc.close();
        return result;
    }
    public static String bytesToString(byte[] bytes) {
        StringBuilder builder = new StringBuilder();
        for (byte aByte : bytes) {
            builder.append(String.format("%02X", aByte));
        }
        return builder.toString().toUpperCase();
    }

    public static void bytes2File(byte[] bytes, File file) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        fileOutputStream.write(bytes, 0, bytes.length);
        fileOutputStream.flush();
        fileOutputStream.close();
    }
    public static void hexWrite(String bytes, File file) throws IOException {
        hexWrite(hexString2Bytes(bytes), file);
    }

    public static void hexWrite(byte[] bytes, File file) throws IOException {
        FileOutputStream fop = new FileOutputStream(file);
        if (!file.exists()) {
            file.createNewFile();
        }
        fop.write(bytes);
        fop.flush();
        fop.close();
        fop.close();
    }
    public static byte[] hexString2Bytes(String hex) {

        if ((hex == null) || (hex.equals(""))) {
            return null;
        } else if (hex.length() % 2 != 0) {
            return null;
        } else {
            hex = hex.toUpperCase();
            int len = hex.length() / 2;
            byte[] b = new byte[len];
            char[] hc = hex.toCharArray();
            for (int i = 0; i < len; i++) {
                int p = 2 * i;
                b[i] = (byte) (charToByte(hc[p]) << 4 | charToByte(hc[p + 1]));
            }
            return b;
        }

    }
    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }
}
